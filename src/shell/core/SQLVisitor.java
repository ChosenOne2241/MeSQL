package shell.core;

import java.util.LinkedList;
import java.util.List;

import shell.parser.ASTAndCondition;
import shell.parser.ASTAssignment;
import shell.parser.ASTAssignments;
import shell.parser.ASTChar;
import shell.parser.ASTColumns;
import shell.parser.ASTCondition;
import shell.parser.ASTCreateTable;
import shell.parser.ASTDelete;
import shell.parser.ASTDropTable;
import shell.parser.ASTField;
import shell.parser.ASTIdentifier;
import shell.parser.ASTInsert;
import shell.parser.ASTInt;
import shell.parser.ASTModifier;
import shell.parser.ASTNumber;
import shell.parser.ASTOrCondition;
import shell.parser.ASTQuit;
import shell.parser.ASTSelect;
import shell.parser.ASTSource;
import shell.parser.ASTStart;
import shell.parser.ASTString;
import shell.parser.ASTUnsignedNumber;
import shell.parser.ASTUpdate;
import shell.parser.ASTValues;
import shell.parser.ASTWildcard;
import shell.parser.MeSQLVisitor;
import shell.parser.SimpleNode;
import sql.command.Quit;
import sql.command.Sources;
import sql.condition.AndCondition;
import sql.condition.Equals;
import sql.condition.GreaterThan;
import sql.condition.GreaterThanOrEqual;
import sql.condition.LessThan;
import sql.condition.LessThanOrEqual;
import sql.condition.Like;
import sql.condition.NotEqual;
import sql.condition.OrCondition;
import sql.core.Condition;
import sql.core.Field;
import sql.core.SQLOperation;
import sql.field.CHARACTER;
import sql.field.INTEGER;
import sql.statement.CreateTable;
import sql.statement.Delete;
import sql.statement.DropTable;
import sql.statement.Insert;
import sql.statement.Select;
import sql.statement.Update;

public class SQLVisitor implements MeSQLVisitor
{
	private SimpleNode child(SimpleNode node, int index)
	{
		return (SimpleNode) node.jjtGetChild(index);
	}

	@Override
	public Object visit(ASTStart node, Object data)
	{
		List<SQLOperation> operations = new LinkedList<SQLOperation>();
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
		{
			operations.add((SQLOperation) child(node, i).jjtAccept(this, data));
		}
		return operations;
	}

	@Override
	public Object visit(ASTCreateTable node, Object data)
	{
		String name = child(node,0).jjtGetValue().toString();
		Field[] fields = new Field[node.jjtGetNumChildren() - 1];
		for (int i = 1; i< node.jjtGetNumChildren(); i++) {
			fields[i - 1] = (Field) child(node, i).jjtAccept(this, data);
		}
		return new CreateTable(name, fields);
	}

	@Override
	public Object visit(ASTSelect node, Object data)
	{
		if (node.jjtGetNumChildren() == 2)
		{
			return new Select(child(node, 1).jjtGetValue().toString(), (String[]) child(node, 0).jjtAccept(this, data));
		}
		return new Select(child(node, 1).jjtGetValue().toString(), (String[]) child(node, 0).jjtAccept(this, data), (Condition) child(node, 2).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTInsert node, Object data)
	{
		return new Insert(child(node, 0).jjtGetValue().toString(), (String[]) child(node, 1).jjtAccept(this, data), (String[]) child(node, 2).jjtAccept(this, data));
	}

	@Override
	public Object visit(ASTUpdate node, Object data)
	{
		Assignments assignments = (Assignments) child(node, 1).jjtAccept(this, data);
		if (node.jjtGetNumChildren() == 2)
		{
			return new Update(child(node,0).jjtGetValue().toString(), assignments.columns, assignments.values);
		}
		else
		{
			return new Update(child(node,0).jjtGetValue().toString(), assignments.columns, assignments.values, (Condition) child(node, 2).jjtAccept(this, data));
		}
	}

	@Override
	public Object visit(ASTDelete node, Object data)
	{
		if (node.jjtGetNumChildren() == 1)
		{
			return new Delete(child(node,0).jjtGetValue().toString());
		}
		else
		{
			return new Delete(child(node,0).jjtGetValue().toString(), (Condition) child(node, 1).jjtAccept(this, data));
		}
	}

	@Override
	public Object visit(ASTDropTable node, Object data)
	{
		return new DropTable(child(node, 0).jjtGetValue().toString());
	}

	@Override
	public Object visit(ASTSource node, Object data)
	{
		return new Sources(child(node,0).jjtAccept(this, data).toString());
	}

	@Override
	public Object visit(ASTQuit node, Object data)
	{
		return new Quit();
	}

	// This is used to return assignments for the SQL INSERT statement.
	private class Assignments
	{
		String[] columns;
		String[] values;

		public Assignments(int size)
		{
			columns = new String[size];
			values = new String[size];
		}
	}

	@Override
	public Object visit(ASTAssignments node, Object data)
	{
		Assignments assignments = new Assignments(node.jjtGetNumChildren());
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
		{
			SimpleNode n = child(node, i);
			if (!n.jjtGetValue().equals("=")) {
				throw new SQLParseException("Encountered " + n.jjtGetValue() + " but expected = at line " + n.jjtGetFirstToken().beginLine + ", column " + n.jjtGetFirstToken().beginColumn + ".");
			}
			assignments.columns[i] = child(n, 0).jjtGetValue().toString();
			assignments.values[i] = child(n, 1).jjtAccept(this, data).toString();
		}
		return assignments;
	}

	@Override
	public Object visit(ASTColumns node, Object data)
	{
		String[] columns = new String[node.jjtGetNumChildren()];
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
		{
			columns[i] = child(node, i).jjtGetValue().toString();
		}
		return columns;
	}

	@Override
	public Object visit(ASTValues node, Object data)
	{
		String[] values = new String[node.jjtGetNumChildren()];
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
		{
			values[i] = child(node, i).jjtAccept(this, data).toString();
		}
		return values;
	}

	@Override
	public Object visit(ASTField node, Object data)
	{
		return child(node, 1).jjtAccept(this, child(node, 0).jjtGetValue());
	}

	@Override
	public Object visit(ASTChar node, Object data)
	{
		int characters = Integer.parseInt(child(node, 0).jjtAccept(this, data).toString());
		boolean unique = false;
		boolean notnull = false;
		for (int i = 1; i < node.jjtGetNumChildren(); i++)
		{
			if (child(node, i).jjtGetValue().equals("UNIQUE"))
			{
				unique = true;
			}
			else
			{
				if (child(node, i).jjtGetValue().equals("NOT NULL"))
				{
					notnull = true;
				}
			}
		}
		return new CHARACTER(data.toString(), characters, unique, notnull);
	}

	@Override
	public Object visit(ASTInt node, Object data)
	{
		boolean unique = false;
		boolean autoincrement = false;
		boolean notnull = false;
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
		{
			if (child(node, i).jjtGetValue().equals("UNIQUE"))
			{
				unique = true;
			}
			else
			{
				if (child(node, i).jjtGetValue().equals("AUTO_INCREMENT"))
				{
					autoincrement = true;
				}
				else
				{
					if (child(node, i).jjtGetValue().equals("NOT NULL"))
					{
						notnull = true;
					}
				}
			}
		}
		return new INTEGER(data.toString(), unique, notnull, autoincrement);
	}

	@Override
	public Object visit(ASTNumber node, Object data)
	{
		String out = "";
		if (node.jjtGetValue() != null) out += node.jjtGetValue().toString();
		return out + child(node, 0).jjtGetValue().toString();
	}

	@Override
	public Object visit(ASTOrCondition node, Object data)
	{
		if (node.jjtGetNumChildren() == 1)
		{
			return child(node, 0).jjtAccept(this, null);
		}
		return new OrCondition((Condition) child(node, 0).jjtAccept(this, null), (Condition) child(node, 1).jjtAccept(this, null));
	}

	@Override
	public Object visit(ASTAndCondition node, Object data)
	{
		if (node.jjtGetNumChildren() == 1)
		{
			return child(node, 0).jjtAccept(this, null);
		}
		return new AndCondition((Condition) child(node, 0).jjtAccept(this, null), (Condition) child(node, 1).jjtAccept(this, null));
	}

	@Override
	public Object visit(ASTCondition node, Object data)
	{
		String operator = node.jjtGetValue().toString();

		switch (operator)
		{
			case "=":
				return new Equals(child(node, 0).jjtGetValue().toString(), child(node, 1).jjtAccept(this, data).toString());
			case "<":
				return new LessThan(child(node, 0).jjtGetValue().toString(), child(node, 1).jjtAccept(this, data).toString());
			case ">":
				return new GreaterThan(child(node, 0).jjtGetValue().toString(), child(node, 1).jjtAccept(this, data).toString());
			case "<=":
				return new LessThanOrEqual(child(node, 0).jjtGetValue().toString(), child(node, 1).jjtAccept(this, data).toString());
			case ">=":
				return new GreaterThanOrEqual(child(node, 0).jjtGetValue().toString(), child(node, 1).jjtAccept(this, data).toString());
			case "<>":
				return new NotEqual(child(node, 0).jjtGetValue().toString(), child(node, 1).jjtAccept(this, data).toString());
			case "LIKE":
				System.out.println(child(node, 1).jjtAccept(this, data));
				child(node, 1).dump("");
				return new Like(child(node, 0).jjtGetValue().toString(), child(node, 1).jjtAccept(this, data).toString());
			default:
				return null;
		}
	}

	@Override
	public Object visit(ASTString node, Object data)
	{
		String out = node.jjtGetValue().toString();
		if (out.charAt(0) == '\"' || out.charAt(0) == '\'')
		{
			out = out.substring(1, out.length() - 1);
		}
		return out;
	}

	// The methods below are not used but must remain to implement the meSQLVisitor interface.
	@Override
	public Object visit(SimpleNode node, Object data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTModifier node, Object data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTUnsignedNumber node, Object data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTWildcard node, Object data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTAssignment node, Object data)
	{
		// TODO Auto-generated method stub
		return null;
	}
}

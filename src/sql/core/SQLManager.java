package sql.core;

import java.util.HashMap;
import java.util.Map;

import sql.field.CHARACTER;

public class SQLManager
{
	private Map<String, Table> tables = new HashMap<String, Table>();

	public SQLManager()
	{
		tables.put("tables", new Table(new TableDescription("tables", new Field[] { new CHARACTER("table", 100, true, true) })));
		try
		{
			tables.get("tables").insert(new String[] {"table"}, new String[] {"tables"});
		}
		catch (SQLException e)
		{
			// It should never happen.
		}
	}
	
	// Execute an SQL Statement.
	public SQLResult execute(SQLStatement statement) throws SQLException
	{
		return statement.execute(tables);
	}

	// Execute an SQL command.
	public String execute(SQLCommand command) throws SQLException
	{
		return command.execute(this);
	}

	// This method is used where it is not clear whether the operation is a statement or a command.
	public String execute(SQLOperation operation) throws SQLException
	{
		if (operation instanceof SQLStatement)
		{
			return execute((SQLStatement) operation).toString();
		}
		else
		{
			return execute((SQLCommand) operation).toString();
		}
	}
}

package sql.statement;

import java.util.List;
import java.util.Map;

import sql.core.Condition;
import sql.core.Row;
import sql.core.SQLException;
import sql.core.SQLResult;
import sql.core.SQLStatement;
import sql.core.Table;
import sql.core.TableDescription;


public class Delete implements SQLStatement
{
	private String name;
	private Condition where;
	
	public Delete(String name, Condition where)
	{
		this.name = name;
		this.where = where;
	}
	
	public Delete(String name)
	{
		this(name, null);
	}
	
	@Override
	public SQLResult execute(Map<String, Table> tables) throws SQLException
	{
		if (name.equals("tables")) throw new SQLException("Table 'tables' cannot be modified.");

		final Table table = tables.get(name);
		if (table == null) throw new SQLException("Unknown table: " + name + ".");
		
		table.delete(where);

		return new SQLResult()
		{
			@Override
			public TableDescription description()
			{
				return table.description();
			}

			@Override
			public List<Row> rows()
			{
				return null;
			}
			
			public String toString()
			{
				return "Roger.";
			}
		};
	}
}

package sql.statement;

import java.util.List;
import java.util.Map;

import sql.core.Row;
import sql.core.SQLException;
import sql.core.SQLResult;
import sql.core.SQLStatement;
import sql.core.Table;
import sql.core.TableDescription;

public class Insert implements SQLStatement
{
	private String name;
	private String[] columns;
	private String[] values;
	
	public Insert(String name, String[] columns, String[] values)
	{
		this.name = name;
		this.columns = columns;
		this.values = values;
	}
	
	@Override
	public SQLResult execute(Map<String, Table> tables) throws SQLException
	{
		if (name.equals("tables")) throw new SQLException("Table 'tables' cannot be modified.");

		final Table table = tables.get(name);
		if (table == null) throw new SQLException("Unknown table: " + name + ".");

		table.description().checkForNotNulls(columns);
		
		String[] cols = table.description().resolveColumns(columns);
		
		table.insert(cols, values);
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

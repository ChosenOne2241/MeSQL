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

public class Update implements SQLStatement
{
	private String name;
	private String[] columns;
	private String[] values;
	private Condition where;
	
	public Update(String name, String[] columns, String[] values, Condition where)
	{
		this.name = name;
		this.columns = columns;
		this.values = values;
		this.where = where;
	}
	
	public Update(String name, String[] columns, String[] values)
	{
		this(name, columns, values, null);
	}
	
	@Override
	public SQLResult execute(Map<String, Table> tables) throws SQLException
	{
		if (name.equals("mesql.table")) throw new SQLException("Table 'mesql.table' cannot be modified.");
		final Table table = tables.get(name);
		if (table == null) throw new SQLException("Unknown table: " + name + ".");
		table.description().resolveColumns(columns);
		table.update(columns, values, where);

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

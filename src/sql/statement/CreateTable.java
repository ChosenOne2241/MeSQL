package sql.statement;

import java.util.List;
import java.util.Map;

import sql.core.Field;
import sql.core.Row;
import sql.core.SQLException;
import sql.core.SQLResult;
import sql.core.SQLStatement;
import sql.core.Table;
import sql.core.TableDescription;

public class CreateTable implements SQLStatement
{
	private String name;
	private Field[] fields;
	
	public CreateTable(String name, Field[] fields)
	{
		this.name = name;
		this.fields = fields;
	}

	@Override
	public SQLResult execute(Map<String, Table> tables) throws SQLException
	{
		if (tables.containsKey(name)) throw new SQLException("Duplicate Table Name: " + name + ".");
		
		final TableDescription description = new TableDescription(name, fields);
		
		tables.put(name, new Table(description));
		
		// Add a row recording the new table to the tables table.
		tables.get("tables").insert(new String[] {"table"}, new String[] {name});
		
		return new SQLResult()
		{
			public String toString()
			{
				return "Roger.";
			}

			@Override
			public TableDescription description()
			{
				return description;
			}

			@Override
			public List<Row> rows()
			{
				return null;
			}
		};
	}
}

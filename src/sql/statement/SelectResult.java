package sql.statement;

import java.util.List;

import sql.core.Field;
import sql.core.Row;
import sql.core.SQLResult;
import sql.core.TableDescription;

public class SelectResult implements SQLResult
{
	TableDescription description;
	List<Row> rows;
	String[] columns;

	public SelectResult(TableDescription description, List<Row> rows, String[] columns)
	{
		this.description = description;
		this.rows = rows;
		this.columns = columns;
	}

	public TableDescription description()
	{
		return description;
	}

	public List<Row> rows()
	{
		return rows;
	}

	public String toString()
	{
		String names = "|";
		for (int i = 0; i < columns.length; i++)
		{
			Field field = description.findField(columns[i]);
			names += " " + field.toFixedWidthString() + " |";
		}
		String line = "";
		for (int i = 0; i < names.length(); i++)
		{
			line += "-";
		}

		String out = line + "\n" + names + "\n" + line + "\n";
		for (Row row : rows)
		{
			out += "|";
			for (int i = 0; i < columns.length; i++)
			{
				Field field = description.findField(columns[i]);
				out += " " + field.toFixedWidthString(row.get(field.name())) + " |";
			}
			out += "\n";
		}
		out += line;
		return out;
	}
}

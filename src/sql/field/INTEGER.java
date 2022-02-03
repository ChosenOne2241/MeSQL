package sql.field;

import sql.core.Field;
import sql.core.SQLException;

public class INTEGER extends AbstractField
{
	private int counter = 1;
	private final static int MAX_LENGTH = 10;
	
	public INTEGER(String name, boolean unique, boolean autoIncrement, boolean notnull)
	{
		super(name, Field.INTEGER, unique, notnull, autoIncrement);
	}

	public String validate(String input) throws SQLException
	{
		try
		{
			Integer.parseInt(input);
		}
		catch (NumberFormatException e)
		{
			throw new SQLException("Invalid input for '" + name + "': Expected INTEGER but got: '" + input + "'.");
		}
		return input;
	}

	@Override
	public String toFixedWidthString()
	{
		String out = "";
		if (name.length() < MAX_LENGTH)
		{
			int padding = MAX_LENGTH - name.length();
			for (int i = 0; i < padding / 2; i++)
			{
				out += " ";
			}
			out += name;
			while (out.length() < MAX_LENGTH)
			{
				out += " ";
			}
		} else {
			out += name;
		}
		return out;
	}

	@Override
	public String toFixedWidthString(String data)
	{
		String out = "";
		int padding = columnFixedWidth() - data.length();
		for (int i = 0; i < padding / 2; i++) {
			out += " ";
		}
		out += data;
		while (out.length() < columnFixedWidth()) {
			out += " ";
		}
		return out;
	}

	@Override
	public int columnFixedWidth()
	{
		return Math.max(MAX_LENGTH, name.length());
	}

	@Override
	public String defaultValue()
	{
		if (isAutoIncrement()) {
			return "" + (counter++);
		}
		return "0";
	}

	@Override
	public Integer toValue(String value) throws SQLException
	{
		try
		{
			return Integer.parseInt(value);
		} catch (NumberFormatException e)
		{
			throw new SQLException("Invalid input for '" + name + "': Expected INTEGER but got: '" + value + "'.");
		}
	}
}

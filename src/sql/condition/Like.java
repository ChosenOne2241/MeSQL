package sql.condition;

import java.util.Map;

import sql.core.SQLException;
import sql.core.TableDescription;

public class Like extends AbstractCondition
{
	private String column;
	private String value;

	public Like(String column, String value)
	{
		this.column = column;
		this.value = value;
	}

	@Override
	public boolean evaluate(TableDescription description, Map<String, String> data) throws SQLException
	{
		Field field = description.findField(column);
		String currFieldValue = (String) field.toValue(data.get(column));
		String valueEvalStr;
		String fieldEvalStr;
		private static final wildcard = "%";

		if (value.charAt(0).equals(wildcard))
		{
			if value.charAt(value.length() - 1).equals(wildcard)
			// "%xxx%", "%%" or "%".
			{
				valueEvalStr = value.substring(1, value.length() - 1);
				fieldEvalStr = currFieldValue.substring(1, currFieldValue.length() - 1);
			}
			else // "%xxx".
			{
				valueEvalStr = value.substring(1, value.length());
				fieldEvalStr = currFieldValue.substring(1, currFieldValue.length());
			}
		}
		else
		{
			if value.charAt(value.length() - 1).equals(wildcard) // "xxx%".
			{
				valueEvalStr = value.substring(0, value.length() - 1);
				fieldEvalStr = currFieldValue.substring(0, currFieldValue.length() - 1);
			}
			else // No "%" in such a string.
			{
				new SQLException("The wildcard '%' is not found.");
			}
		}

		return comparator.compare(fieldEvalStr, field.toValue(valueEvalStr)) == 0;
	}
}

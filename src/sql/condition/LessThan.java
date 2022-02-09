package sql.condition;

import java.util.Map;

import sql.core.SQLException;
import sql.core.TableDescription;

public class LessThan extends AbstractCondition
{
	private String column;
	private String value;

	public LessThan(String column, String value)
	{
		this.column = column;
		this.value = value;
	}

	@Override
	public boolean evaluate(TableDescription description, Map<String, String> data) throws SQLException
	{
		Field field = description.findField(column);
		return comparator.compare(field.toValue(data.get(column)), field.toValue(value)) < 0;
	}
}

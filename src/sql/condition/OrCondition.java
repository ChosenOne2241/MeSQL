package sql.condition;

import java.util.Map;

import sql.core.Condition;
import sql.core.SQLException;
import sql.core.TableDescription;

public class OrCondition implements Condition
{
	private Condition left, right;
	
	public OrCondition(Condition left, Condition right)
	{
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean evaluate(TableDescription description, Map<String, String> data) throws SQLException
	{
		return left.evaluate(description, data) || right.evaluate(description, data);
	}
}

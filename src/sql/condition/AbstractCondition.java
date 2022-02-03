package sql.condition;

import java.util.Comparator;

import sql.core.Condition;
import util.IncomparableException;

public abstract class AbstractCondition implements Condition
{
	@SuppressWarnings("rawtypes")
	protected Comparator comparator = new Comparator()
	{
		@SuppressWarnings("unchecked")
		@Override
		public int compare(Object arg0, Object arg1)
		{
			if (arg0 instanceof Comparable && arg1 instanceof Comparable)
			{
				return ((Comparable) arg0).compareTo(arg1);
			}
			throw new IncomparableException();
		}
	};
}

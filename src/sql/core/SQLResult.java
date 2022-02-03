package sql.core;

import java.util.List;

public interface SQLResult
{
	public TableDescription description();
	public List<Row> rows();
}

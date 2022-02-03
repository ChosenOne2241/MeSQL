package sql.core;

import java.util.Map;

public interface SQLStatement extends SQLOperation
{
	public SQLResult execute(Map<String, Table> tables) throws SQLException;
}

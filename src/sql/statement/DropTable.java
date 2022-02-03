package sql.statement;

import java.util.List;
import java.util.Map;

import sql.core.Row;
import sql.core.SQLException;
import sql.core.SQLResult;
import sql.core.SQLStatement;
import sql.core.Table;
import sql.core.TableDescription;

public class DropTable implements SQLStatement
{
	private String name;

	public DropTable(String name)
	{
		this.name = name;
	}

	@Override
	public SQLResult execute(Map<String, Table> tables) throws SQLException
	{
		return new SQLResult()
		{
			public String toString()
			{
				return "Table has been dropped.";
			}

			@Override
			public TableDescription description()
			{
				return null;
			}

			@Override
			public List<Row> rows()
			{
				return null;
			}
		};
	}
}

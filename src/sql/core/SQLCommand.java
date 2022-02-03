package sql.core;

public interface SQLCommand extends SQLOperation
{
	public String execute(SQLManager manager) throws SQLException;
}

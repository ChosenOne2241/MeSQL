package sql.command;

import sql.core.SQLCommand;
import sql.core.SQLException;
import sql.core.SQLManager;

public class Quit implements SQLCommand
{
	@Override
	public String execute(SQLManager manager) throws SQLException
	{
		long pid = ProcessHandle.current().pid();
		System.out.println("The process (PID: " + pid + ") exits.");
		System.exit(0);
		return null;
	}
}

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import java.util.List;

import shell.core.SQLVisitor;
import shell.parser.MeSQL;
import shell.parser.ParseException;
import shell.parser.SimpleNode;
import sql.core.SQLException;
import sql.core.SQLManager;
import sql.core.SQLOperation;

public class Shell
{
	@SuppressWarnings({ "unchecked", "resource" })
	public static void main(String[] args)
	{
		SQLManager manager = new SQLManager();
		Scanner scanner = new Scanner(System.in);

		while (true)
		{
			System.out.print("MeSQL >>> ");
			String sql = scanner.nextLine();

			try
			{
				SimpleNode n = new MeSQL(new ByteArrayInputStream(sql.getBytes()) ).Start();
				List<SQLOperation> operations = (List<SQLOperation>) n.jjtAccept(new SQLVisitor(), null);
				for (SQLOperation operation : operations)
				{
					System.out.println(manager.execute(operation));
				}
			}
			catch (ParseException e)
			{
				System.out.println(e.getMessage());
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
		}
	}
}

package sql.command;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import shell.core.SQLVisitor;
import shell.parser.MeSQL;
import shell.parser.ParseException;
import shell.parser.SimpleNode;
import sql.core.SQLCommand;
import sql.core.SQLException;
import sql.core.SQLManager;
import sql.core.SQLOperation;

public class Sources implements SQLCommand
{
	private String filename;

	public Sources(String filename)
	{
		this.filename = filename;
	}

	@Override
	public String execute(SQLManager manager) throws SQLException
	{
		File file = new File(filename);
		Scanner input = null;
		try
		{
			input = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		while (input.hasNextLine())
		{
			String line = input.nextLine();
			try
			{
				SimpleNode n = new MeSQL(new ByteArrayInputStream(line.getBytes())).Start();

				@SuppressWarnings("unchecked")
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
		return file.toString() + " is sucessfully parsed.";
	}
}

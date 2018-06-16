import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class DBInteraction {
	static String file = GenerateCSV.outputFileName;

	private static Connection connectToDB() throws ClassNotFoundException, SQLException {
		String dbURL = "jdbc:postgresql://localhost:5432/asl";
		String user = "postgres";
		String password = "password";
		Class.forName("org.postgresql.Driver");

		Connection con = DriverManager.getConnection(dbURL, user, password);
		return con;
	}

	public static long writeObjsToTable() throws ClassNotFoundException, SQLException, IOException {
		long numberOfRowsAdded = 0;
		Connection con = connectToDB();

		CopyManager copyManager = new CopyManager((BaseConnection) con);
		String bulkInsertQuery = "COPY submeshes(geom,metadata) FROM STDIN WITH(DELIMITER '|')";

		File fileDir = new File(DBInteraction.file);
		Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF-8"));
		try {
			numberOfRowsAdded = copyManager.copyIn(bulkInsertQuery, in);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return numberOfRowsAdded;
	}

	public static int deleteObjsFromTable() throws ClassNotFoundException, SQLException, IOException {
		
		Connection con = connectToDB();
		Statement stmt = con.createStatement();
		String geom = "";
		int objsDeleted = 0;

		File fileDir = new File(DBInteraction.file);
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF-8"));

		while ((geom = in.readLine()) != null) {
			String deleteQuery = "DELETE FROM submeshes WHERE geom='" + geom + "'";
			objsDeleted += stmt.executeUpdate(deleteQuery);
			geom = "";
		}
		in.close();
		return objsDeleted;
	}

}

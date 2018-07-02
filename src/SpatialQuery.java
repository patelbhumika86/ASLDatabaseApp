import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.postgis.PGbox3d;

public class SpatialQuery {
	public static String queryOutputFile = "/Users/bhumi/Documents/Capstone/inputFiles/" + "outputObjects.txt";
	String dbURL = "jdbc:postgresql://localhost:5432/asl";
	String user = "postgres";
	String password = "password";

	public Connection connect() throws SQLException, ClassNotFoundException {
		Connection con = DriverManager.getConnection(dbURL, user, password);
		((org.postgresql.PGConnection) con).addDataType("geometry", Class.forName("org.postgis.PGgeometry"));
		((org.postgresql.PGConnection) con).addDataType("box3d", Class.forName("org.postgis.PGbox3d"));
		return con;
	}

	public static void deleteOldFile() {
		File file = new File(queryOutputFile);
		if (file.exists()) {
			file.delete();
		}
	}

	public long findIntersectingObjs(PGbox3d inputbox) throws ClassNotFoundException {
		deleteOldFile();
		String SQL = "SELECT id-1 id, metadata FROM tintable WHERE geom &&& ?";
		long startTime = System.nanoTime();
		long endTime=0;
//		Connection conn;
		try ( Connection conn= connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setObject(1, inputbox);
			ResultSet rs = pstmt.executeQuery();
			endTime = (System.nanoTime() - startTime) / 1000000;
			writeIntersectingObjects(rs);
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return endTime;
	}

	private void writeIntersectingObjects(ResultSet rs) throws SQLException {

		try (FileWriter fw = new FileWriter(queryOutputFile, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			
			while (rs.next()) {
				out.println(rs.getString("metadata"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

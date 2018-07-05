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

class ReturnResult{
	 int noOfRows;
	 long fetchTime;
}
public class SpatialQuery {
	public static String queryOutputFile = "/Users/bhumi/Documents/Capstone/inputFiles/" + "outputObjects.txt";
	String dbURL = "jdbc:postgresql://localhost:5432/asl";
	String user = "postgres";
	String password = "password";

	public Connection connect() throws SQLException, ClassNotFoundException {
		long startTime = System.nanoTime();	
		Connection con = DriverManager.getConnection(dbURL, user, password);
		System.out.println( (System.nanoTime() - startTime) / 1000000);
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

	public ReturnResult findIntersectingObjs(PGbox3d inputbox) throws ClassNotFoundException, SQLException {
		deleteOldFile();
		ReturnResult returnObj = new ReturnResult();
		String SQL = "SELECT id-1 id, metadata FROM tintable WHERE geom &&& ?";
		Connection conn = null;
		ResultSet rs = null;
		try{
			long startTime = System.nanoTime();	
			conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setObject(1, inputbox);
			rs = pstmt.executeQuery();
			returnObj.fetchTime = (System.nanoTime() - startTime) / 1000000;		
			returnObj.noOfRows= writeIntersectingObjects(rs);
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		
		rs.close();
		conn.close();
		return returnObj;
	}
	
	int writeIntersectingObjects(ResultSet rs) throws SQLException {
		int noOfObjectsReturned = 0;

		try (FileWriter fw = new FileWriter(queryOutputFile, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {			
			while (rs.next()) {
				out.println(rs.getString("metadata"));
				noOfObjectsReturned++;
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		return noOfObjectsReturned;
	}
}

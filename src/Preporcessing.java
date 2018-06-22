import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Preporcessing {

	ArrayList<String> coordinateList;

	static void generateTINFile(String filePath, boolean addTerminationChar) throws IOException {
		Preporcessing obj = new Preporcessing();
		File file = new File(filePath);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			System.out.println(e1.getMessage());
		}
		
		GenerateCSV.deleteOldFile();
		String st = new String();
		StringBuffer metadata = new StringBuffer();
		try {
			while ((st = br.readLine()) != null) {
				if (st.length() != 0 && st.charAt(0) == 'v') {
					obj.storeCoordinates(st);
				} else if (st.length() != 0 && st.charAt(0) == 'f') {
					obj.mapVertexToCoord(st);
				} else if (st.length() != 0 && st.charAt(0) == 'o') {
					GenerateCSV.writeFile(metadata);
					obj.coordinateList = new ArrayList<String>();
					metadata = new StringBuffer();
				}
				metadata.append(st + "\\n");
			}
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
		// write last record
		GenerateCSV.writeFile(metadata);
		if(addTerminationChar){
			GenerateCSV.addFileTermination("\\.");
		}
		
		br.close();
		return;
	}
	void mapVertexToCoord(String st) {

		String s = st.substring(2);// ignore f
		String[] verticesArray = s.split(" ");
		
		String[] arr0 = verticesArray[0].split("//");// take the first and
		// ignore 2nd
		String[] arr1 = verticesArray[1].split("//");// take the first and
		// ignore 2nd
		String[] arr2 = verticesArray[2].split("//");// take the first and
		// ignore 2nd

		String v1 = coordinateList.get(Integer.parseInt(arr0[0]));
		String v2 = coordinateList.get(Integer.parseInt(arr1[0]));
		String v3 = coordinateList.get(Integer.parseInt(arr2[0]));
		
		//make a triangle 
		String v4 = v1;//v1.substring(0, v1.length()-2);
		String triangle = "((" + v1 + ", " + v2 + ", "+v3  + ", "+v4 + ")),";			
		GenerateCSV.writeLine( triangle);
		
	}

	void storeCoordinates(String st) {
		String s = st.substring(2);// starting from 2nd index store entire
		// coordinates
		String s1 = s.trim();
		coordinateList.add(s1);
	}
}

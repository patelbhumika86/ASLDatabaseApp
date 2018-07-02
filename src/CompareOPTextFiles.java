import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;

public class CompareOPTextFiles {

	public static void main(String[] args) {
//		compareFiles();

	}

	public static String compareFiles() {
		String res = new String("");
		HashSet<String> meshNames = new HashSet<String>();
		BufferedReader reader1 = null;
		try {
			reader1 = new BufferedReader(
					new FileReader("/Users/bhumi/Documents/Capstone/inputFiles/" + "outputObjects.txt"));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

		BufferedReader reader2 = null;
		try {
			reader2 = new BufferedReader(new FileReader("/Users/bhumi/Documents/Capstone/inputFiles/" + "unityop.txt"));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

		String line1 = null;
		try {
			line1 = reader1.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		String line2 = null;
		try {
			line2 = reader2.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		while (line1 != null) {
			if (line1.length() > 0 && line1.charAt(0) == 'o') {
				String objName = line1.trim();
				meshNames.add(objName);
				// System.out.println("F1 " + objName);
			}
			try {
				line1 = reader1.readLine();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		res += "Number of Intersecting objects " + meshNames.size()+"\n";
//		System.out.println("Number of Intersecting objects " + meshNames.size());
		while (line2 != null) {
			if (line2.length() > 0 && line2.charAt(0) == 'o') {
				String objName = line2.trim();
				if (meshNames.contains(objName)) {
					meshNames.remove(objName);
					// System.out.println("F2 " + objName);
				} else {
					System.out.println("Two files have different content.");
					res +="Two files are different";
					System.out.println(line2 + " object is present in unity file but absent in database file");
				}
			}
			try {
				line2 = reader2.readLine();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		if (meshNames.isEmpty()) {
			System.out.println("Two files have same content.");
			res +="Two files are same";
		} else {
			System.out.println("Two files have different content.");
			res +="Two files are different";
			for (String s : meshNames) {
				System.out.println(s + " object is present in database file but absent in unity file");
			}
		}

		try {
			reader1.close();
			reader2.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return res;
	}
}

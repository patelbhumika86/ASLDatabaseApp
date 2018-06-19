import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;


import org.apache.commons.io.FileUtils;

public class CompareOPTextFiles {

	public static void main(String[] args) {
//		File file1 = new File("/Users/bhumi/Documents/Capstone/inputFiles/" + "unityop.txt");
//		File file2 = new File("/Users/bhumi/Documents/Capstone/inputFiles/" + "outputObjects.txt");
//		
//		boolean compare1and2 = false;
//		try {
//			compare1and2 = FileUtils.contentEquals(file1, file2);
//		} catch (IOException e) {		
//			System.out.println(e.getMessage());
//		}
//		
//		System.out.println("Are file1 and file2 the same? " + compare1and2);	
		
		   
		        BufferedReader reader1 = null;
				try {
					reader1 = new BufferedReader(new FileReader("/Users/bhumi/Documents/Capstone/inputFiles/" + "unityop.txt"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		         
		        BufferedReader reader2 = null;
				try {
					reader2 = new BufferedReader(new FileReader("/Users/bhumi/Documents/Capstone/inputFiles/" + "outputObjects.txt"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		         
		        String line1 = null;
				try {
					line1 = reader1.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		         
		        String line2 = null;
				try {
					line2 = reader2.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		         
		        boolean areEqual = true;
		         
		        int lineNum = 1;
		         
		        while (line1 != null || line2 != null)
		        {
		            if(line1 == null || line2 == null)
		            {
		                areEqual = false;
		                 
		                break;
		            }
		            else if(line1.length()>0 && line1.charAt(1) == 'n'){
		            	//ignore vn lines 
		            }
		            else if( ! line1.equalsIgnoreCase(line2))
		            {
		                areEqual = false;
		                 
		                break;
		            }
		             
		            try {
						line1 = reader1.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		             
		            try {
						line2 = reader2.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		             
		            lineNum++;
		        }
		         
		        if(areEqual)
		        {
		            System.out.println("Two files have same content.");
		        }
		        else
		        {
		            System.out.println("Two files have different content. They differ at line "+lineNum);
		             
		            System.out.println("File1 has "+line1+" and File2 has "+line2+" at line "+lineNum);
		        }
		         
		        try {
					reader1.close();
					 reader2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		         
		       
		    }
		
	}



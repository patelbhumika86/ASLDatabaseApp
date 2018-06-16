import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.postgis.PGbox3d;
import org.postgis.Point;

import javax.swing.JSeparator;

public class UserInterface implements ActionListener {
	long noOfObjsInserted = 0;
	String inputFilePath = "";

	private JLabel lblFilePath;
	private JLabel lblSaveDeleteOutput;
	private JLabel lblQueryLLB;
	private JLabel lblQueryURT;
	private JLabel lblQueryOutput;
	private JLabel lblQueryTime;

	private JButton btnSaveObjects;
	private JButton btnDeleteObjects;
	private JButton btnFindIntersectingObjects;

	private JTextField textfieldFilePath;

	private JTextField textfieldLLBX;// lower left bottom corner of the box as a
	// Point object
	private JTextField textfieldLLBY;
	private JTextField textfieldLLBZ;
	private JTextField textfieldURTX;
	private JTextField textfieldURTY;
	private JTextField textfieldURTZ;

	UserInterface() {
		JFrame f = new JFrame("Database Interface");
		// submit button
		btnSaveObjects = new JButton("Save Objects");
		btnSaveObjects.setBounds(297, 88, 140, 40);
		// enter name label
		lblFilePath = new JLabel();
		lblFilePath.setText("Enter file path :");
		lblFilePath.setBounds(20, 47, 100, 40);
		// empty label which will show event after button clicked
		lblSaveDeleteOutput = new JLabel();
		lblSaveDeleteOutput.setBounds(155, 131, 450, 40);
		// textfield to enter name
		textfieldFilePath = new JTextField();
		textfieldFilePath.setText("/Users/bhumi/Documents/Capstone/testFiles/MyMeshes0352.txt");
		textfieldFilePath.setBounds(155, 57, 450, 30);

		// for query
		btnFindIntersectingObjects = new JButton("Find Intersecting Objects");
		btnFindIntersectingObjects.setBounds(222, 387, 236, 40);
		// enter name label
		lblQueryLLB = new JLabel();
		lblQueryLLB.setText("Enter Lower Left Bottom of BB :");
		lblQueryLLB.setBounds(20, 273, 207, 48);

		lblQueryURT = new JLabel();
		lblQueryURT.setText("Enter Upper Right Top of BB :");
		lblQueryURT.setBounds(20, 326, 200, 40);

		// empty label which will show event after button clicked
		lblQueryOutput = new JLabel();
		lblQueryOutput.setBounds(108, 439, 727, 32);
		// textfield to enter name
		textfieldLLBX = new JTextField();
		textfieldLLBX.setBounds(222, 283, 71, 30);

		// add to frame
		f.getContentPane().add(lblSaveDeleteOutput);
		f.getContentPane().add(textfieldFilePath);
		f.getContentPane().add(lblFilePath);
		f.getContentPane().add(btnSaveObjects);
		f.getContentPane().add(lblQueryLLB);
		f.getContentPane().add(textfieldLLBX);
		f.getContentPane().add(lblQueryURT);
		f.getContentPane().add(btnFindIntersectingObjects);
		f.getContentPane().add(lblQueryOutput);

		f.setSize(787, 595);
		f.getContentPane().setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 183, 792, 12);
		f.getContentPane().add(separator);

		textfieldLLBY = new JTextField();
		textfieldLLBY.setBounds(305, 283, 71, 30);
		f.getContentPane().add(textfieldLLBY);

		textfieldLLBZ = new JTextField();
		textfieldLLBZ.setBounds(388, 285, 71, 30);
		f.getContentPane().add(textfieldLLBZ);

		JLabel lblX = new JLabel("X");
		lblX.setBounds(245, 255, 24, 16);
		f.getContentPane().add(lblX);

		JLabel lblY = new JLabel("Y");
		lblY.setBounds(333, 255, 32, 16);
		f.getContentPane().add(lblY);

		JLabel lblZ = new JLabel("Z");
		lblZ.setBounds(420, 257, 32, 16);
		f.getContentPane().add(lblZ);

		textfieldURTX = new JTextField();
		textfieldURTX.setBounds(225, 333, 71, 30);
		f.getContentPane().add(textfieldURTX);

		textfieldURTY = new JTextField();
		textfieldURTY.setBounds(308, 333, 71, 30);
		f.getContentPane().add(textfieldURTY);

		textfieldURTZ = new JTextField();
		textfieldURTZ.setBounds(391, 333, 71, 30);
		f.getContentPane().add(textfieldURTZ);

		JLabel lblNewLabel = new JLabel("Find Intersecting Objects");
		lblNewLabel.setBounds(20, 207, 261, 16);
		f.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Store Objects into the Database");
		lblNewLabel_1.setBounds(20, 6, 290, 16);
		f.getContentPane().add(lblNewLabel_1);

		btnDeleteObjects = new JButton("Delete Objects");
		btnDeleteObjects.setBounds(465, 88, 140, 40);
		f.getContentPane().add(btnDeleteObjects);

		lblQueryTime = new JLabel();
		lblQueryTime.setBounds(108, 484, 727, 32);
		f.getContentPane().add(lblQueryTime);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// action listener
		btnSaveObjects.addActionListener(this);
		btnDeleteObjects.addActionListener(this);
		btnFindIntersectingObjects.addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		inputFilePath = textfieldFilePath.getText();
		
		if (e.getSource().equals(btnSaveObjects)) {
			if (inputFilePath.isEmpty() || inputFilePath.length() == 0) {
				lblSaveDeleteOutput.setText("Please enter a file path for the input file of objects to be stored");
			} else {
				try {
					generateTempOPFile(inputFilePath,true);
				} catch (IOException e2) {
					lblSaveDeleteOutput.setText("Error: Problem occured during object conversion to TIN");
					System.err.println(e2.getMessage());
				}
				lblSaveDeleteOutput.setText("Copy Started");
				try {					
					noOfObjsInserted = DBInteraction.writeObjsToTable();
					lblSaveDeleteOutput.setText(noOfObjsInserted + " Objects inserted into the database");
				} catch (ClassNotFoundException | SQLException | IOException e1) {
					lblSaveDeleteOutput.setText("Error: Problem encountered during storing objects to database");
				}
				
			}

		} else if (e.getSource().equals(btnDeleteObjects)) {
			
			if (inputFilePath.isEmpty() || inputFilePath.length() == 0) {
				lblSaveDeleteOutput.setText("Please enter a file path for the input file of objects to be deleted");
			} else {
				try {
					generateTempOPFile(inputFilePath, false);
				} catch (IOException e2) {
					lblSaveDeleteOutput.setText("Error: Problem occured during object conversion to TIN");
					System.err.println(e2.getMessage());
				}
				lblSaveDeleteOutput.setText("Finding objects to be deleted");
				try {		
					int objsDeleted = DBInteraction.deleteObjsFromTable();
					lblSaveDeleteOutput.setText(objsDeleted + " Objects deleted");
				} catch (ClassNotFoundException | SQLException | IOException e1) {
					System.out.println(e1.getMessage());
					lblSaveDeleteOutput.setText("Path or File not valid");
				}
			}
		}

		else if (e.getSource().equals(btnFindIntersectingObjects)) {
			SpatialQuery q = new SpatialQuery();
			PGbox3d inputBoundingBox = null;
			try {

				double x = Double.parseDouble(textfieldLLBX.getText());
				double y = Double.parseDouble(textfieldLLBY.getText());
				double z = Double.parseDouble(textfieldLLBZ.getText());

				double x1 = Double.parseDouble(textfieldURTX.getText());
				double y1 = Double.parseDouble(textfieldURTY.getText());
				double z1 = Double.parseDouble(textfieldURTZ.getText());

				inputBoundingBox = new PGbox3d(new Point(x, y, z), new Point(x1, y1, z1));
				
				long startTime = System.nanoTime();
				q.findIntersectingObjs(inputBoundingBox);
				long endTime = (System.nanoTime() - startTime) / 1000000;
				lblQueryTime.setText("Time taken = " + endTime + " millisecs");
				lblQueryOutput.setText("Intersecting objects are stored in file-" + q.queryOutputFile);
			} catch (NumberFormatException ignore) {
				lblQueryOutput.setText("Invalid Input - please enter valid values for bounding box");
			} catch (ClassNotFoundException e1) {
				lblQueryOutput.setText("Error finding the intersection");
			}

		}
	}

	private void generateTempOPFile(String filePath, boolean addTerminationChar) throws IOException {
		Preporcessing obj = new Preporcessing();
		File file = new File(filePath);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			lblSaveDeleteOutput.setText("Path or File not valid");
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
			lblSaveDeleteOutput.setText("Path or File not valid");
		}
		// write last record
		GenerateCSV.writeFile(metadata);
		if(addTerminationChar){
			GenerateCSV.addFileTermination("\\.");
		}
		
		br.close();
		return;
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new UserInterface();
			}
		});
	}
}
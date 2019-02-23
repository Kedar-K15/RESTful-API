/**
* <h1>CSV File reader with added utilities</h1>
* This class acts as its own CSV class with many utilities to edit or update a particular CSV File
* @author  Kedar Kulkarni
* @version 3.2.3
* @since   2018-01-08
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class CSV {
	private File CSVFile;
	private ArrayList<ArrayList<String>> arr;
	private boolean automatic;
	
	/**
	 * Class constructor.
	 * <br>Creates a File object with the path and converts the file to an array</br>
	 * @param path of the CSV file
	 */
	public CSV(String path) {
		CSVFile = new File(path);
		arr = convertToArray();
		automatic = false;
	}
	
	/**
	 * Option to use the CSV object as a File object for added functions and utilities.
	 * @return CSVFile File object
	 */
	public File getCSV() {
		return CSVFile;
	}
	
	/**
	 * Moves the file location of the CSV File
	 * @param path of the CSV File
	 */
	public void moveFile(String path) {
		CSVFile.renameTo(new File(path));
	}
	
	/**
	 * If the private boolean field automatic is true, then it will dynamically convert the file to the array.
	 * @return arr- the CSV File in a multi-dimensional array format
	 */
	public ArrayList<ArrayList<String>> getArray() {
		if(automatic) {
			arr = convertToArray();
			return arr;
		}
		
		return arr;
	}
	
	/**
	 * The string version is separated by "|" to differentiate between this function and others.
	 * @return result - a String version of the CSV file
	 * @throws RuntimeException if there is an unexpected exception (most probably with the File object)
	 */
	public String convertToString() {
		try {
			Scanner reader = new Scanner(CSVFile);
			StringBuilder sb = new StringBuilder();
			reader.useDelimiter(",");
			while(reader.hasNext()) {
				sb.append(reader.next() + "|");
			}
			String result = sb.toString();
			result = result.substring(0, result.length()-1);
			reader.close();
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage() + "; " + e.getClass());
		}
	}
	
	/**
	 * Converts a multi-dimensional array into CSV File format.
	 * @param arr - a multi-dimensional array in the form of ArrayList<String>
	 * @return result - a string separated by commas (CSV format)
	 */
	public static String convertArrayToCSV(ArrayList<ArrayList<String>> arr) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < arr.size(); i++) {
			sb.append(arr.get(i));
			sb.append("\n");
		}
		
		String result = sb.toString();
		result = result.replaceAll("[\\[\\]]", "");
		return result;
	}
	
	/**
	 * Sets the private field automatic to the answer that is passed in the function.
	 * <br>If the private field automatic is set to true, then the CSV file will automatically update the file if the file is changed during runtime.</br>
	 * However, you must call one of the methods to achieve this action
	 * @param answer - the answer the user specifies whether true or false
	 */
	public void checkAutomatically(boolean answer) {
		automatic = answer;
	}
	
	/**
	 * Adds a particular string to the CSV File.
	 * <br>The CSV File will be automatically updated with the changes to the internal array made.</br>
	 * @param str - A particular string to add
	 * @param row - the row number of the array
	 * @param col - the column number of the array
	 */
	public void addItem(String str, int row, int col) {
		arr.get(row).add(col, str);
		updateFile();
	}
	
	/**
	 * Adds an entire row (or array) to the CSV File.
	 * <br>The CSV File will be automatically updated with the changes to the internal array made.</br>
	 * @param arr - a one-dimensional array that serves as the entire row to add
	 * @param row - the row number or location to add the array
	 */
	public void addRow(ArrayList<String> arr, int row) {
		this.arr.add(row, arr);
		updateFile();
	}
	/**
	 * Adds an entire row (or array) to the CSV File at the end.
	 * @param arr - a one-dimensional array that serves as the entire row to add
	 */
	public void addRow(ArrayList<String> arr) {
		this.arr.add(arr);
		updateFile();
	}
	
	/**
	 * Adds an entire column to the CSV File.
	 * <br>Each index of the array is added such that i + 1 is under the previous.</br>
	 * It is possible to start at any particular row desired.
	 * @param arr - a one-dimensional array that serves as the entire column to add
	 * @param col - the column number or location to add the array
	 * @param start - a number that specifies at which row the function should start adding the array into the column
	 * @throws RuntimeException if the array is greater than the number of rows in the CSV File
	 */
	public void addColumn(ArrayList<String> arr, int col, int start) {
		int index = 0;
		if(arr.size() > this.arr.size()) {
			throw new RuntimeException("Failed : Invalid bounds"); // create own class
		}
		for(int i = start; i < this.arr.size(); i++) {
			this.arr.get(i).add(col, arr.get(index));
			++index;
		}
		updateFile();
	}
	
	/**
	 * Adds an entire column to the CSV File at the end.
	 * <br>It is possible ot start at any particular row desired.</br>
	 * @param arr - a one-dimensional array that serves as the entire column to add
	 * @param start - a number that specifies at which row the function should start adding the array into the column
	 */
	public void addColumn(ArrayList<String> arr, int start) {
		int index = 0;
		if(arr.size() > this.arr.size()) {
			throw new RuntimeException("Failed : Invalid bounds"); // create own class
		}
		for(int i = start; i < this.arr.size(); i++) {
			this.arr.get(i).add(arr.get(index));
			++index;
		}
		updateFile();
	}
	
	/**
	 * Determines whether the particular file is of a .csv extension or not.
	 * @param path - path of the particular file to reference
	 * @return if extension equals csv, return true; otherwise return false
	 */
	public static boolean isCSV(String path) {
		String extension = "";

		int i = path.lastIndexOf('.');
		int p = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));

		if (i > p) {
		    extension = path.substring(i+1);
		}
		return extension.equals("csv") ? true : false;
	}
	
	
	@Override
	public String toString() {
		return this.getCSV().getAbsolutePath() + "; automatically update: " + this.automatic;
	}

	
	/**
	 * Private function that reads through each line of the CSV File and converts it into a multi-dimensional array to be set to the private data field.
	 * @return arr - the CSV File into a multi-dimensional array format
	 * @throws RuntimeException if there is an unexpected exception (most probably with the File object or index out of bounds in file)
	 */
	private ArrayList<ArrayList<String>> convertToArray() {
		ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(CSVFile));
			String line;
			
			while((line = reader.readLine()) != null) {
				StringBuilder sb = new StringBuilder();
				ArrayList<String> x = new ArrayList<String>();
					for(int i = 0; i < line.length(); i++) {
						if(line.indexOf(',') == -1) {
							line = line.trim();
							x.add(line);
							break;
						}
						else if(line.charAt(i) == ',') {
							String middle = sb.toString().trim();
							x.add(middle);
							sb = new StringBuilder();
						}
						else if(i == line.length()-1) {
							sb.append(line.charAt(i));
							String middle = sb.toString().trim();
							x.add(middle);
						}
						else {
							sb.append(line.charAt(i));
						}
					}
					arr.add(x);	
			}
			reader.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage() + "; " + e.getClass());
		}
		return arr;
	}

	/**
	 * Private function that updates the file by converting the internal array into CSV format (comma-delimited) and writes that to the CSV File.
	 * <br>The function must wipe the file and add all of the contents back, even though some contents may not have been updated.</br>
	 * @throws RuntimeException if there is an unexpected exception (most probably with the CSV File)
	 */
	private void updateFile() {
		try {
			PrintWriter pw = new PrintWriter(CSVFile);
			pw.write(convertArrayToCSV(arr));
			pw.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage() + "; " + e.getClass());
		}
	}
}
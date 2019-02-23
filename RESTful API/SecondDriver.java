/**
* <h1>Driver class</h1>
* Runs the program (second version)
* @author  Kedar Kulkarni
* @version 3.2.3
* @since   2018-01-08
*/
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import org.ini4j.Ini;

public class SecondDriver {
	/**
	 * Checks whether a multi-dimensional ArrayList has only whitespace (essentially empty).
	 * @param arr - a multi-dimensional ArrayList
	 * @return true if all the Strings in the ArrayList are whitespace, false otherwise
	 */
	private static boolean isEmpty(ArrayList<ArrayList<String>> arr) {
		for (int i = 0; i < arr.size(); i++) {
		    for (int j = 0; j < arr.get(i).size(); j++) {
		        if(!(arr.get(i).get(j).trim().isEmpty())) {
		        	return false;
		        }
		    } 
		}
		return true;
	}
	
	/**
	 * Moves a specified CSV data file to the Processed folder (indicating that the POST was a success).
	 * <br>The method handles both for the create and update Applicant</br>
	 * @param data - the CSV data object
	 * @param isUpdate - whether the file location at the CSV data object is an update Applicant (true) or create (false)
	 */
	private static void moveFileProcessed(CSV data, boolean isUpdate) {
		if(isUpdate && data != null) {
			String filePath = data.getCSV().getAbsolutePath();
			filePath = filePath.replace("\\Update\\" + data.getCSV().getName(), "\\Processed\\" + data.getCSV().getName());
			data.moveFile(filePath);
			System.out.println("New file path: " + filePath);
		}
		else if(!isUpdate && data != null) {
			String filePath = data.getCSV().getAbsolutePath();
			filePath = filePath.replace("\\Create\\" + data.getCSV().getName(), "\\Processed\\" + data.getCSV().getName());
			data.moveFile(filePath);
			System.out.println("New file path: " + filePath);
		}
	}
	
	/**
	 * Moves a specified CSV data file to the Error folder (indicating that the POST was a fail).
	 * <br>The method handles both for the create and update Applicant.</br>
	 * @param data - the CSV data object
	 * @param isUpdate - whether the file location at the CSV data object is an update Applicant (true) or create (false)
	 */
	private static void moveFileError(CSV data, boolean isUpdate) {
		if(isUpdate && data != null) {
			String filePath = data.getCSV().getAbsolutePath();
			filePath = filePath.replace("\\Update\\" + data.getCSV().getName(), "\\Error\\" + data.getCSV().getName());
			data.moveFile(filePath);
			System.out.println("New file path: " + filePath);
		}
		else if(!isUpdate && data != null) {
			String filePath = data.getCSV().getAbsolutePath();
			filePath = filePath.replace("\\Create\\" + data.getCSV().getName(), "\\Error\\" + data.getCSV().getName());
			data.moveFile(filePath);
			System.out.println("New file path: " + filePath);
		}
	}
	
	public static void main(String [] args) {
		System.out.println("The program started");
		for(;;) {
			try {
				Ini ini = new Ini(new File("C:\\Users\\KKulkarni\\Documents\\RESTFUL API\\data.ini"));
				WatchService watchService = FileSystems.getDefault().newWatchService();
				Path create = Paths.get("C:\\Users\\KKulkarni\\Documents\\RESTFUL API\\Create");
				Path update = Paths.get("C:\\Users\\KKulkarni\\Documents\\Restful API\\Update");
			    WatchKey createKey = create.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
			    WatchKey updateKey = update.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
			    boolean isUpdate;
			    CSV data = null;
			    while (true) {
				        for (WatchEvent<?> event : createKey.pollEvents()) {
				        	isUpdate = false;
					        try {
					        	int row = 1;
					        	String path = create.resolve((Path) event.context()).toString();
					        	System.out.println("Path to use: " + path);
					        	/*System.out.println(event.kind());
					        	System.out.println(CSV.isCSV(path));*/
					        	if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE &&
					        			CSV.isCSV(path)) {
							        Thread.sleep(2000); // this is needed to prevent undefined behavior with FileNotFoundException and Process still being used
					        		long startTime = System.nanoTime();
					        	    data = new CSV(path);
					        	    if(isEmpty(data.getArray())) {
					        	    	throw new RuntimeException("Error: CSV File is empty");
					        	    }
					        	    
					        		ArrayList<String> colArr = new ArrayList<String>();

									for(int i = 0; i < data.getArray().size()-1; i++) {										
										String response = Rest.postRequest(ini.get("Create", "URL"),
												Rest.configureJSON(data, row, false), 
												ini.get("Create", "apiKey"));
										
										colArr.add(Rest.parseID(response));
										++row;
									}									
									data.addColumn(colArr, 1);
									
									moveFileProcessed(data, isUpdate);
									long stopTime = System.nanoTime();
									System.out.println("Elapsed time: " + (double)(stopTime - startTime)/1000000000);
									System.out.println("Success!");
					        	}
					        } catch(Exception e) {
					        	moveFileError(data, isUpdate);
					        	e.printStackTrace();
					        }
				        }
				        for (WatchEvent<?> event : updateKey.pollEvents()) {
				        	isUpdate = true;
				        	try {
					        	int row = 1;
					        	String path = update.resolve((Path) event.context()).toString();
					        	System.out.println("Path to use: " + path);
					        	
					        	if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE &&
					        			CSV.isCSV(path)) {
					        		Thread.sleep(2000); // this is needed to prevent undefined behavior with FileNotFoundException and Process still being used
					        		long startTime = System.nanoTime();
					        		data = new CSV(path);
					        		if(isEmpty(data.getArray())) {
					        	    	throw new RuntimeException("Error: CSV File is empty");
					        	    }
					        		
					        		for(int i = 0; i < data.getArray().size()-1; i++) {
										Rest.postRequest(ini.get("Update", "URL"), 
												Rest.configureJSON(data, row, true), 
												ini.get("Update", "apiKey"));
										++row;
									}
									
					        		moveFileProcessed(data, isUpdate);
									long stopTime = System.nanoTime();
									System.out.println("Elapsed time: " + (double)(stopTime - startTime)/1000000000);
									System.out.println("Success!");
					        	}
				        	} catch(Exception e) {
				        		moveFileError(data, isUpdate);
				        		e.printStackTrace();
					        }
				        }
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}

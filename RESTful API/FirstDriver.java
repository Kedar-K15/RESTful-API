/**
* <h1>Driver class</h1>
* Runs the program (first version)
* @author  Kedar Kulkarni
* @version 1.2.0
* @since   2018-01-08
*/
import org.apache.commons.cli.Options;
import java.util.ArrayList;
import org.apache.commons.cli.*;

public class FirstDriver {
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
	
	public static void main(String [] args) {
		long startTime = System.nanoTime();
		Options options = new Options();
		
        Option path = new Option("p", "csvpath", true, "CSV File Path");
        path.setRequired(true);
        options.addOption(path);

        Option key = new Option("k", "apikey", true, "apiKey");
        key.setRequired(true);
        options.addOption(key);
        
        Option URL = new Option("ur", "url", true, "URL");
        URL.setRequired(true);
        options.addOption(URL);
        
        Option method = new Option("m", "method", true, "Update or Create");
        method.setRequired(true);
        
        options.addOption(method);
     

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
    	CSV data = null;
		String apiKey = null;
		String urlPath = null;
		boolean isUpdate = false;
		
        try {
            cmd = parser.parse(options, args);
            if(CSV.isCSV(cmd.getOptionValue("csvpath"))) {
            	data = new CSV(cmd.getOptionValue("csvpath"));
            	if(isEmpty(data.getArray())) {
        	    	throw new RuntimeException("Error: CSV File is empty");
        	    }
            }
            apiKey = cmd.getOptionValue("apikey");
            urlPath = cmd.getOptionValue("url");
            if(cmd.getOptionValue("m").toLowerCase().equals("update")) {
            	isUpdate = true;
            }
            else if(!(cmd.getOptionValue("m").toLowerCase().equals("create"))) {
            	System.out.println("Incorrect input");
            	System.exit(1);
            }
            
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        } catch(Exception e) {
        	System.out.println(e.getMessage() + "; " + e.getClass());
        	System.exit(1);
        }
       
		int row = 1;
		try {
			if(!isUpdate) {
				ArrayList<String> colArr = new ArrayList<String>();
				
				for(int i = 0; i < data.getArray().size()-1; i++) {
					String response = Rest.postRequest(urlPath, 
							Rest.configureJSON(data, row, isUpdate), 
											   apiKey);
					colArr.add(Rest.parseID(response));
					++row;
				
				}
				
				data.addColumn(colArr, 1);
				String filePath = data.getCSV().getAbsolutePath();
				filePath = filePath.replace("\\Create\\" + data.getCSV().getName(), "\\Processed\\" + data.getCSV().getName());
				data.moveFile(filePath);
				System.out.println("New file path: " + filePath);
			}
			else {
				for(int i = 0; i < data.getArray().size()-1; i++) {
					Rest.postRequest(urlPath, Rest.configureJSON(data, row, isUpdate), 
									apiKey);
					++row;
				}
				
				String filePath = data.getCSV().getAbsolutePath();
				filePath = filePath.replace("\\Update\\" + data.getCSV().getName(), "\\Processed\\" + data.getCSV().getName());
				data.moveFile(filePath);
				System.out.println("New file path: " + filePath);
			}
			long stopTime = System.nanoTime();
			System.out.println("Elapsed time: " + (double)(stopTime - startTime)/1000000000);
			System.out.println("Success!");
		} catch(Exception e) {
			System.out.println(e.getMessage() + "; " + e.getClass());
		}
	}
}

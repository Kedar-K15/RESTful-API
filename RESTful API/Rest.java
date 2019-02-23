/**
* <h1>RESTFUL request handler with added utilities</h1>
* This class can send POST requests and configure JSON bodies.
* @author  Kedar Kulkarni
* @version 3.2.3
* @since   2018-01-08
*/
import java.util.ArrayList;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Rest {
	
	/**
	 * Sends a POST request to the application server and receives its output.
	 * @param URL - the url that the function will send a POST request to
	 * @param input - the JSON body that will be used to POST
	 * @param apiKey - needed by the server as a header to function
	 * @return output - the response from the application server as a result of the request
	 * @throws RuntimeException if the request failed or any other unknown error
	 */
	public static String postRequest(String URL, String input, String apiKey) {
		try {
			Client client = Client.create();

			WebResource webResource = client
				.resource(URL);
					
			ClientResponse response = webResource
				.header("Content-Type", "application/json")
				.header("apiKey", apiKey)
				.post(ClientResponse.class, input);

			String output = response.getEntity(String.class);
			
			return output;
			
		  } catch (Exception e) {
			  throw new RuntimeException(e.getMessage() + "; " + e.getClass());
		 }
	}
	
	/**
	 * Configures the JSON body code specifically dependent on the server.
	 * <br>There are two possible bodies depending on which type of applicant is required.</br>
	 * @param file - a CSV File object that the information come from
	 * @param row - the particular row of that CSV File object which contains the information
	 * @param isUpdate - whether the applicant required is the updateApplicant (true) or else the createApplicant (false)
	 * @return String body of the JSON code configured
	 */
	public static String configureJSON(CSV file, int row, boolean isUpdate) {
		try {
			ArrayList<String> arr = file.getArray().get(row);
			System.out.println("READING: " + arr);
			if(isUpdate) {
				return "{ \"product\": \"server\", \"function\": \"ENROLL_EXT\", \"profileId\": \"123456789\", \"applicationType\": \"STUDENT\", \"applicationId\": " + addQuote(arr.get(9)) +", \r\n" + 
						"    \"person\": { \"biographics\": [{ \"name\": \"DateofBirth\", \"value\": "+addQuote(arr.get(1))+", \"datatype\": \"Date\" },\r\n" + 
						"    { \"name\": \"EmailID\", \"value\": "+addQuote(arr.get(2))+", \"datatype\": \"String\" },\r\n" + 
						"    { \"name\": \"ExpirationDate\", \"value\": "+addQuote(arr.get(3))+", \"datatype\": \"Date\" },\r\n" + 
						"    { \"name\": \"firstName\", \"value\": "+addQuote(arr.get(4))+", \"datatype\": \"String\" },\r\n" + 
						"    { \"name\": \"FormerFirstName\", \"value\": "+addQuote(arr.get(5))+", \"datatype\": \"String\" },\r\n" + 
						"    { \"name\": \"FormerLastName\", \"value\": "+addQuote(arr.get(6))+", \"datatype\": \"String\" },\r\n" + 
						"    { \"name\": \"lastName\", \"value\": "+addQuote(arr.get(7))+", \"datatype\": \"String\" }, \r\n" + 
						"    { \"name\": \"Nationality\", \"value\": "+addQuote(arr.get(8))+", \"datatype\": \"String\" },\r\n" + 
						"    { \"name\": \"OU\", \"value\": \"OSC\", \"datatype\": \"String\" }] } \r\n" + 
						"}";
	
			}
			
			return "{ \"product\": \"server\", \"function\": \"ENROLL_EXT\", \"profileId\": \"123456789\", \"applicationType\": \"STUDENT\", \r\n" + 
					"    \"person\": { \"biographics\": [{ \"name\": \"DateofBirth\", \"value\": "+addQuote(arr.get(1))+", \"datatype\": \"Date\" },\r\n" + 
					"    { \"name\": \"EmailID\", \"value\": "+addQuote(arr.get(2))+", \"datatype\": \"String\" },\r\n" + 
					"    { \"name\": \"ExpirationDate\", \"value\": "+addQuote(arr.get(3))+", \"datatype\": \"Date\" },\r\n" + 
					"    { \"name\": \"firstName\", \"value\": "+addQuote(arr.get(4))+", \"datatype\": \"String\" },\r\n" + 
					"    { \"name\": \"FormerFirstName\", \"value\": "+addQuote(arr.get(5))+", \"datatype\": \"String\" },\r\n" + 
					"    { \"name\": \"FormerLastName\", \"value\": "+addQuote(arr.get(6))+", \"datatype\": \"String\" },\r\n" + 
					"    { \"name\": \"lastName\", \"value\": "+addQuote(arr.get(7))+", \"datatype\": \"String\" }, \r\n" + 
					"    { \"name\": \"Nationality\", \"value\": "+addQuote(arr.get(8))+", \"datatype\": \"String\" },\r\n" + 
					"    { \"name\": \"OU\", \"value\": \"OSC\", \"datatype\": \"String\" }] } \r\n" + 
					"}";
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage() + "; " + e.getClass());
		}
	}
	
	/**
	 * Reads a particular response from the application server (specifically of manageID)
	 * @param jsonStr - the particular response from the server
	 * @return the applicationID or an error message (depending on if the information was valid)
	 */
	public static String parseID(String jsonStr) {
		return (jsonStr.indexOf("applicationid") == -1) ? jsonStr.substring(21,30) : jsonStr.substring(25,35);
	}
	
	/**
	 * Private function that adds quotation marks in the beginning and end of a particular String
	 * @param str - the String desired to have quotation marks surrounded
	 * @return String with quotation marks
	 */
	private static String addQuote(String str) {
		return "\""+str+"\"";
	}	

}

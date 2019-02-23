# RESTful API
# Utilizing version 1.0 (command line argument functionality of the program):
<ul>
  <li>In this project I utilize the create and update applicant's of the application server to send a POST request to the server to either enroll an applicant or update one.</li>
  <li>The project is an executable binary jar, which can be run through the command line</li>
  <li>Here is an example using the createApplicant:</li>
  <ul>
<li>The data shown above is taken from a .csv comma-delimited file. The program reads this data and exports the applicationID to the .csv file.</li>
<li>In a sample of 2000 records, the program does this process in approx. 141.5 seconds, which is 14.1 records per second (for createApplicant).</li>
  </ul>
<li>Once the success message is received, the program automatically moves the file into a "Processed" folder before exiting.</li>
<li>Here is an example using the updateApplicant:</li>
<ul>
<li>The data shown above is also taken from a .csv comma-delimited file, but it is a different csv than the one used for the createApplicant in a different folder. The program reads this data including the apiKey that was generated using the method above (createApplicant) in the same time.</li>
<li>In a sample of 2000 records, the program does this process in approx. 310 seconds, which is 6.45 records per second (for updateApplicant).</li>
<li>Once the success message is received, the program also automatically moves the file into a "Processed" folder before exiting.</li>
</ul>
</ul>

# Utilizing version 2.0 (Windows service functionality of the program):
<ul>
<li>Instead of having to go into the command line and enter the required arguments to use the program, version 2.0 has the program as a Windows service that runs automatically. Now, all one needs to do is drag or put in one (or multiple) files into either the Create folder or the Update folder (depending on the applicant type) and the program will automatically parse these files as needed and move them to either Error (the parse resulted in an error) or Processed (the parse was a success). The program will only parse files with a .csv extension.</li>
<li>In the data.ini file, you can put the correct information for the URL and apiKey that the program will use for both the update and create applicant.</li>
<li>This service folder will have files for the output of the program (same as seen in version 1.0 of the program) or any error messages.</li>
</ul>

# Java standalone class: CSV
<ul>
<li>This is a Java class that I created that can parse CSV files.</li>
<li>The class automatically converts a given CSV file object into a multi-dimensional array.</li>  
<li>The class can add rows and/or columns to this array and update the CSV file automatically (this is done in the program in order to add the applicationID to a createApplicant .csv file).</li>
<li>There are other utilities that the class provides, such as converting to a File object or changing the location of the CSV file, that the program utilizes.</li>
<li>More information will be in the source file with Javadoc headings.</li>
</ul>

# Java class: Rest
<ul>
<li>This is a Java class that I created whose public methods are all static.</li>
<li>The class can send a RESTFUL POST request call to a particular server given the URL, body, and API Key.</li>
<li>This is done by using the Jersey client and adding the appropriate headers to create a ClientResponse to receive from the server.</li>
<li>The class also configures a JSON body to post, depending on whether it is using application servers' update or create applicant.</li>
<li>The body is different depending on this boolean parameter, and it takes a CSV file and particular row of that file to parse and return the JSON string for that row.</li>
<li>More information will be in the source file with Javadoc headings.</li>
</ul>

# Libraries/API's utilized:
<ul>
<li>External libraries needed: 
<li>Jersey API client for RESTFUL requests</li>
<li>Apache Common's API for command line argument configuration</li>
<li>Java UTIL ArrayList</li>
<li>Java IO File</li>
<li>Java UTIL Scanner</li>
<li>Java IO BufferedReader</li>
<li>Java IO FileReader</li>
<li>Java IO PrintWriter</li>
<li>Java NIO Path/Paths</li>
<li>Java NIO WatchEvent/WatchKey/WatchService</li>
<li>Java NIO StandardWatchEventKinds</li>
<li>Ini4j for parsing .ini files</li>
</ul>

# Final notes:
<ul>
<li>Some parts of the code in the program is heavily dependent on certain aspects of the create and update Applicant</li>
<li>Examples:</li>
<li>The JSON function Rest.configureJSON(. . .) expects the CSV file row to have a certain amount of headers and produces a JSON body with the same headers (e.g., EmailID, DateOfBirth). Should there need to be additional headers or headers taken away, the logic of this function and the information in the CSV file must completely change in order to accept the new changes.</li>
<li>The applicationID function Rest.parseID(. . .) expects the response from the application server to stay the same each time with the same amount of spaces and such so that it can find the applicationID or error message. Should there be any changes to the response, the subsequent substring numbers must be changed.</li>
<li>The program expects the file hierarchy structure to match exactly; however, the name of the .csv extension files themselves can be any name. </li>
<li>All the methods of the program (with exception to the main methods) are commented with Javadoc headings for more information on the purpose of each method and exceptions.</li>
</ul>

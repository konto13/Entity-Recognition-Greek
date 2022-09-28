package CollectionToJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GetDBpediaAbstract {
	/**
    *
    * @param query
    * @param endpoint
    * @return
    * @throws UnsupportedEncodingException
    * @throws MalformedURLException
    * @throws IOException
    */
   public String dbpediaQuery(String query, String endpoint) throws UnsupportedEncodingException, MalformedURLException, IOException {

       String sparqlQueryURL = endpoint + "?query=" + URLEncoder.encode(query, "utf8");
       URL url = new URL(sparqlQueryURL);
       URLConnection con = url.openConnection();
       String xml_content = "text/csv";
       con.setRequestProperty("ACCEPT", xml_content);
       /* In case the Endpoint asks for username and password */
       InputStream is = con.getInputStream();
       InputStreamReader isr = new InputStreamReader(is, "utf8");
       BufferedReader in = new BufferedReader(isr);

       String input;
       String resultsString = "";
       int count = 0;
       while ((input = in.readLine()) != null) {
           if (count == 0) {
               count = 1;
               continue;
           }
           String[] data = input.split(",");
           if (data.length == 3) { //for finding the triples
               String subject = data[0].replace("\"", "");
               String predicate = data[1].replace("\"", "");
               String object = data[2].replace("\"", "");
               if(predicate.contains("abstract")) {
            	   resultsString = object.substring(object.lastIndexOf("/")+1);
               }
           }
           if(data.length==1 && data[0].contains("http://el.dbpedia.org/")){ //for finding the Greek URI
        	   return data[0].replace("\"", "");
           }
       }

       in.close();
       isr.close();
       is.close();

       return resultsString;

   }
}

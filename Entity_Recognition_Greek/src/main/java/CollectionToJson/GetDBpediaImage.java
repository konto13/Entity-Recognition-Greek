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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *
 * @author Michalis Mountantonakis
 * @co-author Nikos Kontonasios
 */
public class GetDBpediaImage {
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        GetDBpediaImage image=new GetDBpediaImage();
        System.out.println("Images Links for Nikos Kazantzakis");
        String entity = "http://dbpedia.org/resource/Nikos_Kazantzakis";
        String endpoint = "https://dbpedia.org/sparql";
        String query = "Select ?o where{<" + entity + "> <http://xmlns.com/foaf/0.1/depiction> ?o}";
        image.dbpediaQuery(query, endpoint);
    }
    
    
      /**
     *
     * @param query
     * @param endpoint
     * @return
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     */
    public JsonArray dbpediaQuery(String query, String endpoint) throws UnsupportedEncodingException, MalformedURLException, IOException {

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
        int count = 0;
        JsonArray ja = new JsonArray();
        while ((input = in.readLine()) != null) {
            if (count == 0) {
                count = 1;
                continue;
            }
                String imageLink=input.replace("\"","");
                JsonObject jo = new JsonObject();
        		jo.addProperty("img", imageLink);
        		ja.add(jo);
        }

        in.close();
        isr.close();
        is.close();

        return ja;
    }
}
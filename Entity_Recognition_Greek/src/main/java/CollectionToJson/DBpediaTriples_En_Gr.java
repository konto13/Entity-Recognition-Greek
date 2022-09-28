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
 * @author mountant
 */
public class DBpediaTriples_En_Gr {

    public static void main(String[] args) throws MalformedURLException, IOException {
        DBpediaTriples_En_Gr db = new DBpediaTriples_En_Gr();
        //For finding information in English
        System.out.println("Information for Nikos Kazantzakis in English");
        String entity = "http://dbpedia.org/resource/Aristotle";
        String endpoint = "https://dbpedia.org/sparql";
        String query = "Select <" + entity + "> ?p ?o where{<" + entity + "> ?p ?o}";
        db.dbpediaQuery(query, endpoint);
        //query = "select ?o where { <"+entity+"> <http://xmlns.com/foaf/0.1/depiction> ?o}";
        //db.dbpediaQuery(query, endpoint);
        System.out.println("\n===================\n");
        //For finding the Greek URI in English
        System.out.println("Retrieve Nikos Kazantzakis URL in greek DBpedia");
        endpoint = "http://el.dbpedia.org/sparql";
        query = "Select ?s where{?s owl:sameAs <" + entity + ">}";
        String greekURI=db.dbpediaQuery(query, endpoint);
        System.out.println(greekURI);
        
        
        System.out.println("\n===================\n");
        System.out.println("Information for Nikos Kazantzakis in Greek");
        //For finding the Greek information
        query = "Select <" + greekURI + "> ?p ?o where{<" + greekURI + "> ?p ?o}";
        String json = db.dbpediaQuery(query, endpoint);
        System.out.println(json);
     
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
		JsonArray ja = new JsonArray();
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
                //System.out.println(subject+ "\t" + predicate + "\t" + object);
                resultsString = subject+ "\t" + predicate + "\t" + object;
                JsonObject jo = new JsonObject();
        		jo.addProperty("predicate", predicate);
        		jo.addProperty("object", object);
        		ja.add(jo);
            }
            if(data.length==1 && data[0].contains("http://el.dbpedia.org/")){ //for finding the Greek URI
            	return data[0].replace("\"", "");
            }
        }

        in.close();
        isr.close();
        is.close();

        return ja.toString();

    }

}
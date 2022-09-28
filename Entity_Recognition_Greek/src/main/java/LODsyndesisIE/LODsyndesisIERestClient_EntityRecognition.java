/*  This code belongs to the Semantic Access and Retrieval (SAR) group of the
 *  Information Systems Laboratory (ISL) of the
 *  Institute of Computer Science (ICS) of the
 *  Foundation for Research and Technology - Hellas (FORTH)
 *  Nobody is allowed to use, copy, distribute, or modify this work.
 *  It is published for reasons of research results reproducibility.
 *  (c) 2020 Semantic Access and Retrieval group, All rights reserved
 */
package LODsyndesisIE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 *
 * @author Michalis Mountantonakis
 *
 * This Class is used to send requests to LODsyndesisIE rest API. 
 */
public class LODsyndesisIERestClient_EntityRecognition {

    private HttpClient client;
    private HttpGet getEntitiesOfText;
    private HttpGet exportAsRDFa;
    private HttpGet getTriplesOfEntities;
    private HttpGet findRelatedFacts;
    private HttpGet textEntitiesDatasetDiscovery;
    private static final String URL ="https://demos.isl.ics.forth.gr/LODsyndesisIE/rest-api";

    private String serviceName;

    private DecimalFormat df = new DecimalFormat(".##");

    /**
     * Used to open connection with client and LODsyndesis
     */
    public LODsyndesisIERestClient_EntityRecognition() {
        client = HttpClientBuilder.create().build();
        df.setRoundingMode(RoundingMode.DOWN);

    }
    
    /**
     * Used to execute the request, receive the response in n-quads or n-triples
     * format and produce an interpretable structure with it.
     *
     * @param request
     * @return An interpretable structure that contains current service
     * response.
     * @throws IOException
     */
    private ArrayList<ArrayList<String>> getContent(HttpGet request) throws IOException {

        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        String line = "";

        while ((line = rd.readLine()) != null) {
            String[] lineSplited = line.split("\\s+");
            ArrayList<String> lineSplitedClean = new ArrayList<>();
            for (String lineUnit : lineSplited) {
                if (lineUnit.equals(".")) {
                    continue;
                } else {
                    lineSplitedClean.add(lineUnit);
                }
            }
            result.add(lineSplitedClean);
        }
        return result;
    }


    /**
     * Retrieve the entities of a given text
     *
     * @param text the input text
     * @param ERTools the combination of entity extraction tools. Options [WAT, StanfordCoreNLP, DBpediaSpotlight, WAT_and_StanfordCoreNLP, WAT_and_DBpediaSpotlight, StanfordCoreNLP_and_DBpediaSpotlight, All]
     * @param equivalentURIs true for retrieving the equivalent URIs for each entity, fakse otherwise
     * @param provenance  true for retrieving the equivalent URIs for each entity, fakse otherwise
     * @return the entities of a text, and possibly their equivalent URIs and provenance
     */
    public String getEntitiesOfText(String text,String ERTools,String equivalentURIs,String provenance) throws IOException {
        try {
            serviceName = "getEntities";
            getEntitiesOfText = new HttpGet(URL + "/" + serviceName + "?text=" + text+"&ERtools="+ERTools+"&equivalentURIs="+equivalentURIs+"&provenance="+provenance);
            
            getEntitiesOfText.addHeader(ACCEPT, "text/tsv");
            getEntitiesOfText.addHeader(CONTENT_TYPE, "text/tsv");
           // System.out.println(getEntitiesOfText);
            ArrayList<ArrayList<String>> result = getContent(getEntitiesOfText);
            String output="";
              for (ArrayList<String> triple : result) {
                for(int i=0;i<triple.size();i++){
                    output+=triple.get(i);
                    if(i+1==triple.size()){
                        output+="\n";
                    }
                    else if(triple.get(i).startsWith("http")){
                        output+="\t";
                    }
                    else
                        output+=" ";
                }}
              output=output.replace(" http","\thttp");
            return output;
        } catch (Exception ex) {
            Logger.getLogger(LODsyndesisIERestClient_EntityRecognition.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void printEntities(String output, String tool){
        System.out.println("\nThe entities of the given text and their data by using the tool: "+tool+"\n");
        String[] split=output.split("\n");
        System.out.println("Number of Entities: "+split.length);
        for(int i=1;i<split.length;i++){
            String[] line=split[i].split("\t");
            String entity=line[0];
            String DBpediaURI=line[1];
            System.out.println("Entity: "+entity);
            System.out.println("URI: "+DBpediaURI);
            System.out.println("");
        }
        System.out.println("");
    }
    
    public JsonArray createEntitiesJSON(String output) {
        String[] split=output.split("\n");
        JsonArray ja = new JsonArray();

        for(int i=1;i<split.length;i++){
            String[] line=split[i].split("\t");
            String entity=line[0];
            String DBpediaURI=line[1];
            JsonObject jo = new JsonObject();
            jo.addProperty("fragment", entity);
	        jo.addProperty("url", DBpediaURI);
	        ja.add(jo);
        }
        return ja;
    }

   
   

    
    public static void main(String[] args) throws IOException {
        LODsyndesisIERestClient_EntityRecognition chanel = new LODsyndesisIERestClient_EntityRecognition();

        //ERTools [WAT, StanfordCoreNLP, DBpediaSpotlight, WAT_and_StanfordCoreNLP,
        //WAT_and_DBpediaSpotlight, StanfordCoreNLP_and_DBpediaSpotlight, All]
        
        String  text="Nikos Kazantzakis was born in Heraklion, Crete. Widely considered a giant of modern Greek "
                + "literature, he was nominated for the Nobel Prize in Literature in nine different"
                + " years. Kazantzakis' novels included Zorba the Greek "
                + "(published 1946 as Life and Times of Alexis Zorbas), Christ Recrucified (1948), "
                + "Captain Michalis (1950, translated Freedom and Death), and The "
                + "Last Temptation of Christ (1955). His fame spread in the "
                + "English-speaking world due to cinematic adaptations of Zorba the Greek (1964) "
                + "and The Last Temptation of Christ (1988). He translated also"
                + " a number of notable works into Modern Greek, such as the Divine Comedy,"
                + " Thus Spoke Zarathustra and the Iliad.  Late in 1957, even though suffering "
                + "from leukemia, he set out on one last trip to China and Japan. "
                + "Falling ill on his return flight, he was transferred to Freiburg, Germany, where he died.";
        text=text.replace(" ","%20");
        
        // Find the entities of the given text, their equivalent URIs and their provenance by using WAT
        //String ERTools="WAT";
        //String output1=chanel.getEntitiesOfText(text,ERTools,"false","false");
        //chanel.printEntities(output1,ERTools);
        
        
        //ERTools="DBS";
        //output1=chanel.getEntitiesOfText(text,ERTools,"false","false");
        //chanel.printEntities(output1,ERTools);
        
        String ERTools="DBSWAT";
        String output1=chanel.getEntitiesOfText(text,ERTools,"false","false");
        chanel.printEntities(output1,ERTools);

    }
}
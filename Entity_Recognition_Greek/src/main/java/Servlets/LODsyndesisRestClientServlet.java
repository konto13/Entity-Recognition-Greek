package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import CollectionToJson.GetDBpediaAbstract;
import CollectionToJson.GetDBpediaImage;
import LODsyndesisIE.LODsyndesisIERestClient_EntityRecognition;


/**
 * 
 * @author Nikos Kontonasios
 */
@WebServlet("/LODsyndesisRestClientServlet")
public class LODsyndesisRestClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8"); 
		request.setCharacterEncoding("utf-8");
		response.setStatus(200);
		String text = request.getReader().readLine();
		
		text=text.replace(" ","%20");
		LODsyndesisIERestClient_EntityRecognition chanel = new LODsyndesisIERestClient_EntityRecognition();
		String ERTools="DBSWAT";
        String output1=chanel.getEntitiesOfText(text,ERTools,"false","false");
        JsonArray ja = chanel.createEntitiesJSON(output1);
        
        GetDBpediaImage image = new GetDBpediaImage();
		GetDBpediaAbstract abstr = new GetDBpediaAbstract();
		for(int i = 0; i < ja.size(); i++) {
			JsonObject jo = (JsonObject) ja.get(i);
			String entity = jo.get("url").getAsString();
			String img_endpoint = "https://dbpedia.org/sparql";
			String abs_endpoint = "http://el.dbpedia.org/sparql";
	        String img_query = "Select ?o where{<" + entity + "> <http://xmlns.com/foaf/0.1/depiction> ?o}";
	        JsonArray img = image.dbpediaQuery(img_query, img_endpoint);
	        String greekURI_query = "Select ?s where{?s owl:sameAs <" + entity + ">}";
	        String greekURI = abstr.dbpediaQuery(greekURI_query, abs_endpoint);
	        String abs_query = "Select <" + greekURI + "> ?p ?o where{<" + greekURI + "> ?p ?o}";
	        String abs = abstr.dbpediaQuery(abs_query, abs_endpoint);
	        jo.add("images", img);
	        jo.addProperty("abstract", abs);
		}
		response.getWriter().write(ja.toString());
	}
}
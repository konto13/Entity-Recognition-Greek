package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import Evaluation.Evaluation;
import CollectionToJson.GetDBpediaAbstract;
import CollectionToJson.GetDBpediaImage;

/**
 * Servlet implementation class AddPropertiesToJsonServlet
 */
@WebServlet("/AddPropertiesToJsonServlet")
public class AddPropertiesToJsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddPropertiesToJsonServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8"); 
		request.setCharacterEncoding("utf-8");
		response.setStatus(200);
		String entities = request.getReader().readLine();

		JsonParser jp = new JsonParser();
		JsonArray ja = (JsonArray) jp.parse(entities);
		
		GetDBpediaImage image = new GetDBpediaImage();
		GetDBpediaAbstract abstr = new GetDBpediaAbstract();
		//Evaluation eval = new Evaluation();
		//eval.evaluate(ja);
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

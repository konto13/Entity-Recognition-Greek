package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import CollectionToJson.DBpediaTriples_En_Gr;

/**
 * 
 * @author Nikos Kontonasios
 */
@WebServlet("/ModalServlet")
public class ModalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8"); 
		request.setCharacterEncoding("utf-8");
		response.setStatus(200);
		String url = request.getReader().readLine();
		
		DBpediaTriples_En_Gr triples = new DBpediaTriples_En_Gr();
		String endpoint = "http://el.dbpedia.org/sparql";
        String query = "Select ?s where{?s owl:sameAs <" + url + ">}";
        String greekURI = triples.dbpediaQuery(query, endpoint);
        //For finding the Greek information
        query = "Select <" + greekURI + "> ?p ?o where{<" + greekURI + "> ?p ?o}";
        String jo = triples.dbpediaQuery(query, endpoint);
        response.getWriter().write(jo);
	}

}

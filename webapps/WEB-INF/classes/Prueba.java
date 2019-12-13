
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.servlet.annotation.*;

@WebServlet("/Prueba")
public class Prueba extends HttpServlet {
	private ArrayList<Canal> canalesList = new ArrayList<Canal>();
	private Canal canalAux = new Canal();
	private PrintWriter salida;

	@Override
	public void init() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db
					.parse(new URL("http://gssi.det.uvigo.es/users/agil/public_html/SINT/19-20/tvml-2004-12-01.xml")
							.openStream());

			NodeList canales = doc.getElementsByTagName("Canal");

			for (int i = 0; i < canales.getLength(); i++) {
			
				this.setCanalAux(obtenerCanales(canales.item(i)));
				this.getCanalesList().add(canalAux);

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {





		res.setCharacterEncoding("utf-8");
		res.setContentType("text/html");

		PrintWriter out = res.getWriter();
		salida = out;
		String urlRequest = req.getRequestURL().toString();
		String solicitante = req.getParameter("solicitante");
		String pfase = req.getParameter("pfase");

		out.println("<html>");
		out.println("<head>");

		out.println("<link rel='stylesheet' type='text/css' href='" + req.getContextPath() + "/css/prueba.css' >");
		out.println("<meta charset='utf-8'/><title>¡Hello World!</title>");
		out.println("</head>");
		out.println("<h1> Consulta de canales y programas. Solicitante: " + solicitante + "</h1>");
		out.println("<h2> -> Mostramos información del fichero .xml </h2>");
		out.println("<body>");
		if(pfase.isEmpty()){
			
			out.println("Query String: "+req.getQueryString() +"<br>");
			pfase = devuelveFase("pfase=01");
		}else{

			pfase = devuelveFase(req.getQueryString());
		}
		switch(pfase){
			case "01":
			//out.println("pfase = 01");
			mostrarFase01(out);
			break;
			case "02":
			//out.println("pfase = 02");
			break;
			case "11":
			//out.println("pfase = 11");
			break;
			case "12":
			//out.println("pfase = 12");
			break;
			case "13":
			//out.println("pfase = 13");
			break;
			default:
			//out.println("pfase = 01");
			break;
		}
		out.println("<br>");
		/*
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db
					.parse(new URL("http://gssi.det.uvigo.es/users/agil/public_html/SINT/19-20/tvml-2004-12-01.xml")
							.openStream());

			NodeList canales = doc.getElementsByTagName("Canal");

			Node padreCanal = canales.item(0).getParentNode();
			// out.println("Nodo padre " + padreCanal.getNodeName() + " Tiene " +
			// padreCanal.getChildNodes().getLength()
			// + " hijos" + "<br>");
			out.println("<br>");
			out.println("<div id='canales'> Canales </div>");
			out.println("<div id='canal' >");
			for (Canal channel : this.getCanalesList()) {
				out.println("# Canal " + channel.getIdCanal() + " --> "
						+ channel.getNombreCanal() + "- "
						+ channel.getGrupo());
				out.println("<div id='programa'>");
				out.println("    - Programas: ");
				for (Programa programa : channel.getProgramas()) {
					out.println(programa.getNombrePrograma() + " ("
							+ programa.getEdadMinima() + "), ");
				}
				out.println("</div>");

				out.println("<br>");
			}

			out.println("</div>");

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}*/
		System.out.println("mensaje de log con System.out.println()");

		this.log("mensaje de log con log()");
              	out.println("<br><button> <a href='"+urlRequest+"?solicitante=next'> nextPage </a> </button>");
		out.println("</body>");
		out.println("</html>");
	}
	
	private void mostrarFase01(PrintWriter out) {
	out.println("<a href=?pfase=01'> nextPage </a>");
	}

	private String devuelveFase(String queryString) {
	
		String aux = "";
		switch(queryString){
			case "pfase=01":
				aux = "01";
				return aux;
			case "pfase=02":
				aux = "02";
				return aux;

			case "pfase=11":
				aux = "11";
				return aux;
			case "pfase=12":
				aux = "12";
				return aux;
			case "pfase=13":
				aux = "12";
				return aux;
			default:
				aux = "01";
				return aux;
		}
	}
	private Canal obtenerCanales(Node canal) {
		// TODO Auto-generated method stub
		NamedNodeMap atributosC = canal.getAttributes();
		String lang = atributosC.item(1).getNodeValue();
		String idCanal = atributosC.item(0).getNodeValue();
		NodeList hijosCanal = canal.getChildNodes();
		String nombreCanal = "";
		String grupo = "";
		ArrayList<Programa> programas = new ArrayList<Programa>();
		Programa programa = null;
		// out.println("canal " + (i+1) + " --> ");
		for (int j = 0; j < hijosCanal.getLength(); j++) {
			if (hijosCanal.item(j).getNodeName().equals("NombreCanal")) {
				nombreCanal = hijosCanal.item(j).getTextContent();
			} else if (hijosCanal.item(j).getNodeName().equals("Grupo")) {
				grupo = hijosCanal.item(j).getTextContent();
			} else if (hijosCanal.item(j).getNodeName().equals("Programa")) {
				programa = obtenerPrograma(hijosCanal.item(j));
				programas.add(programa);
				// out.println( programa.getNombrePrograma().getFirstChild().getNodeValue()+ "
				// "+programa.getEdadMinima()+", ");

			}
		}

		Canal canalAux = new Canal(lang, idCanal, nombreCanal, grupo, programas);

		return canalAux;
	}

	private Programa obtenerPrograma(Node programa) {
		// TODO Auto-generated method stub
		NamedNodeMap atributosP = programa.getAttributes();

		String edadMinima = "";
		String langs = "";
		String horaInicio = "";
		String nombrePrograma = "";
		String categoria = "";
		String duracion = "";
		String horaFin = "";

		switch (atributosP.getLength()) {
		case 1:
			if (atributosP.item(0).getNodeName().equals("langs")) {
				langs = atributosP.item(0).getNodeValue();

			} else if (atributosP.item(0).getNodeName().equals("edadminima")) {
				edadMinima = atributosP.item(0).getNodeValue();

			}
			break;
		case 2:
			if (atributosP.item(0).getNodeName().equals("langs")) {
				langs = atributosP.item(0).getNodeValue();
				edadMinima = atributosP.item(1).getNodeValue();
			} else {
				langs = atributosP.item(1).getNodeValue();
				edadMinima = atributosP.item(0).getNodeValue();
			}
		}
		NodeList hijosP = programa.getChildNodes();
		for (int i = 0; i < hijosP.getLength(); i++) {
			if (hijosP.item(i).getNodeName().equals("NombrePrograma")) {
				nombrePrograma = hijosP.item(i).getTextContent();
			} else if (hijosP.item(i).getNodeName().equals("Categoria")) {
				categoria = hijosP.item(i).getTextContent();
			} else if (hijosP.item(i).getNodeName().equals("horaInicio")) {
				horaInicio = hijosP.item(i).getTextContent();
			} else if (hijosP.item(i).getNodeName().equals("duracion")) {
				duracion = hijosP.item(i).getTextContent();
			} else if (hijosP.item(i).getNodeName().equals("horaFin")) {
				horaFin =  hijosP.item(i).getTextContent();
			}
		}

		Programa programaClass = new Programa(edadMinima, langs, nombrePrograma, categoria, horaInicio, duracion,
				horaFin);

		return programaClass;
	}

	public ArrayList<Canal> getCanalesList() {
		return canalesList;
	}

	public void setCanalesList(ArrayList<Canal> canalesList) {
		this.canalesList = canalesList;
	}

	public Canal getCanalAux() {
		return canalAux;
	}

	public void setCanalAux(Canal canalAux) {
		this.canalAux = canalAux;
	}
}

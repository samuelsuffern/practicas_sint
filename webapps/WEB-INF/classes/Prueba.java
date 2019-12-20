
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
		if(pfase==null){
			pfase="01"; 		
		}

		switch(pfase){
			case "01": // pantalla principal
			pantallaPrincipal(out,req);
			mostrarFase01(out,req);
			break;
			case "02":	//fase de ficheros erróneos
			break;
			case "11":	//fase para escoger fecha de la programación. Consulta1
			pantallaPrincipal(out,req);
			mostrarFase11(out,req);
			
			break;
			case "12":
			pantallaPrincipal(out,req);
			mostrarFase12(out, req);
			break;
			case "13":
			pantallaPrincipal(out,req);
			mostrarFase13(out, req);
			//out.println("pfase = 13");
			break;
			default:
			pantallaPrincipal(out,req);
			mostrarFase01(out,req);
			//out.println("pfase = 01");
			break;
		}
		System.out.println("estoy cambiando");
      	//out.println("<br><button> <a href='"+urlRequest+"?pfase="+nextpfase+"'> nextPage </a> </button>");
		
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
	}
	private void pantallaPrincipal(PrintWriter out,HttpServletRequest req){
		String solicitante = req.getParameter("solicitante");
		out.println("<!DOCTYPE html> ");
		out.println("<html>");
		out.println("<head>");
		out.println("<script src="+req.getContextPath() + "/css/myScripts.js></script>");
		out.println("<link rel='stylesheet' type='text/css' href='" + req.getContextPath() + "/css/prueba.css' >");
		out.println("<meta charset='utf-8'/><title>Consultas</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1> Servicio de consulta de información sobre canales de TV </h1>");
	}
		
	
	private void mostrarFase11(PrintWriter out,HttpServletRequest req) {
		//pantallaPrincipal(out, req);
	
		String pfaseAux = req.getParameter("pfase");
		out.println("<h2> Consulta 1. Fase=" + pfaseAux+"</h2>");
		String slash = "/";
		String fecha = "2004\\12\\01";
		out.println("<h3> Selecciona la fecha.</h3>");
		out.println("<form>");
		out.println("<input type='hidden' name='pfase' id='pfase' value='12'>");
		out.println("<fieldset>");
		out.println("<input type='radio' name='fecha' value='"+fecha+"' > 2004\\12\\01");	
		out.println("</fieldset>");
		/*out.println("<input type='radio' name='pfase' value='02'   onclick = 'nextConsulta("+req.getRequestURL().toString()+"?pfase=02)' > Consulta 2 <br>");
		out.println("<input type='radio' name='pfase' value='11'    onclick = 'nextConsulta("+req.getRequestURL().toString()+"?pfase=11)' > Consulta 3 <br>");
		*/
		out.println("<br><input id='fecha' type='submit' value='Enviar'> <input type='submit' value='inicio' onclick='form.pfase.value=0'> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	
	}	
	private void mostrarFase12(PrintWriter out,HttpServletRequest req) {
		//pantallaPrincipal(out, req);
		String pfaseAux = req.getParameter("pfase");
		String fecha = req.getParameter("fecha");
		out.println("<h2> Consulta 1. Fecha="+fecha+" </h2>");
		String slash = "/"; 
		out.println("<h3> Selecciona un canal.</h3>");
		out.println("<form>");
		out.println("<input type='hidden' name='pfase' id='pfase' value='13'>");
		out.println("<input type='hidden' name='fecha' id='fecha' value='"+fecha+"'>");
		for (int i = 0; i < this.canalesList.size(); i++) {
			Canal canal = getCanalesList().get(i);
			out.println("<input type='radio' name='pcanal' value='"+canal.getNombreCanal()+"' > Nombre:"+canal.getNombreCanal()+", Idioma="+canal.getLang()+", Grupo="+canal.getGrupo()+"<br>");	
			
		}

		out.println("<br><input id='fecha' type='submit' value='Enviar'> <input type='submit' value='inicio' onclick='form.pfase.value=0'> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	
	}	
	private void mostrarFase13(PrintWriter out,HttpServletRequest req) {
		//pantallaPrincipal(out, req);
		String pfaseAux = req.getParameter("pfase");
		String fecha = req.getParameter("fecha");
		int faseAnt = Integer.parseInt(pfaseAux) -1;
		String canal = req.getParameter("pcanal");
		out.println("<h2> Consulta 1. Fecha="+fecha+", Canal="+canal+"</h2>");
		String amp = "&amp;"; 
		String back = req.getRequestURL().toString()+"?pfase="+faseAnt+amp+"fecha="+fecha;
		out.println("<h3> Este es el resultado.</h3>");
		out.println("<form name='final'>");
		out.println("<input type='hidden' name='pfase' id='pfase' value='12'>");
		out.println("<input type='hidden' name='fecha' id='fecha' value='"+fecha+"'>");
		Canal thisCanal = Canal.getCanalByName(this.getCanalesList(),canal);
		
		for (int i = 0; i < thisCanal.getProgramas().size(); i++) {
			Programa programa = thisCanal.getProgramas().get(i);
			//out.println("<input type='radio' name='pcanal' value='"+canal.getNombreCanal()+"' > Nombre:"+canal.getNombreCanal()+", Idioma="+canal.getLang()+", Grupo="+canal.getGrupo()+"<br>");	
			out.println((i+1)+".- Titulo="+programa.getNombrePrograma()+", Edad_Minima="+programa.getEdadMinima()+", Hora="+programa.getHoraInicio()+"<br>");
		}

		out.println("<br><input id='atras' type='submit' value='Atras' onclick='form.action="+back+"'> <input type='submit' value='inicio' onclick='form.action="+req.getRequestURL().toString()+"'> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	
	}	
	private void mostrarFase01(PrintWriter out,HttpServletRequest req) {
		//pantallaPrincipal(out, req);
		out.println("<h2> Bienvenido a este servicio</h2>");
		out.println("<a href='"+req.getRequestURL().toString()+"?pfase=02' > Haz click aqui para ver los ficheros de errores </a>");
		
		out.println("<p> Selecciona una consulta: </p>");
		out.println("<form>");
		out.println("<input type='radio' name='pfase' value='11'>"+" Consulta 1. Peliculas de un día en un canal");	
		/*out.println("<input type='radio' name='pfase' value='02'   onclick = 'nextConsulta("+req.getRequestURL().toString()+"?pfase=02)' > Consulta 2 <br>");
		out.println("<input type='radio' name='pfase' value='11'    onclick = 'nextConsulta("+req.getRequestURL().toString()+"?pfase=11)' > Consulta 3 <br>");
		*/out.println("<br><input id='but' type='submit' value='Enviar'>   ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	
	}	
	private String devuelveFase(String queryString){		 
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

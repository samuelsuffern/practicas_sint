
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.servlet.annotation.*;

@WebServlet("/Prueba")
public class Prueba extends HttpServlet {
	private String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	private ArrayList<BadTVML> warnings = new ArrayList<BadTVML>();
	private ArrayList<BadTVML> errors = new ArrayList<BadTVML>();
	
	
	private ArrayList<String> moreTVMLs = new ArrayList<String>();
	private ArrayList<String> visitedTVMLs = new ArrayList<String>();

	HashMap<String, Programacion> mapa = new HashMap<String, Programacion>();

	private PrintWriter salida;

	@Override
	public void init() {

		ArrayList<Canal> canalesList = new ArrayList<Canal>();
		Document doc = null;
		URL url = null;
		int error2 = 0;
		try {
			url = new URL("http://localhost:7063/sint/xml/");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(true); // factoria de parsers validadores
			dbf.setNamespaceAware(true);
			File schema = new File(getServletContext().getRealPath("/xsd/tvml.xsd"));
			dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, schema);

			DocumentBuilder db = dbf.newDocumentBuilder();
			MyHandler handler = new MyHandler();
			db.setErrorHandler(handler);
			try {
				doc = db.parse(new URL("http://localhost:7063/sint/xml/tvml-2004-12-01.xml").openStream());
				
			} catch (SAXException e) {
				//throw
				ArrayList<String> errores = handler.getErrors();
				errores.add(e.getMessage());
				error2 = 1;
				//TODO: handle exception
			}
			if(handler.getWarnings().size()>0) {
				BadTVML bad = new BadTVML();
				bad.setFich(url+"tvml-2004-12-01.xml");
				bad.setMessage(handler.getWarnings());
				warnings.add(bad);
				error2 = 1;
			}
			if(handler.getErrors().size()>0){
				BadTVML bad = new BadTVML();
				bad.setFich(url+"tvml-2004-12-01.xml");
				bad.setMessage(handler.getErrors());
				errors.add(bad);
				error2 = 1;				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		if(error2==0){

			leerXML(doc);
		}
		visitedTVMLs.add("tvml-2004-12-01.xml");
		for (int i = 0; i < moreTVMLs.size(); i++) {
			int readed = 0;
			for (String readedTVML : visitedTVMLs) {
				if (moreTVMLs.get(i).equals(readedTVML)) {
					readed = 1;
				}
			}
			if (readed == 0) {

				String newTVML = moreTVMLs.get(i);
				System.out.println("Tengo estos tvml *********** " + newTVML);
				Document doc2 = null;
				int error = 0;
				try {

					URL nextTVML = new URL(url, newTVML);
					DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
					dbf2.setValidating(true); // factoria de parsers validadores
					dbf2.setNamespaceAware(true);
					File schema = new File(getServletContext().getRealPath("/xsd/tvml.xsd"));
					dbf2.setAttribute(JAXP_SCHEMA_LANGUAGE, schema);

					DocumentBuilder db2 = dbf2.newDocumentBuilder();
					MyHandler handler2 = new MyHandler();
					db2.setErrorHandler(handler2);
					try {
						doc2 = db2.parse(nextTVML.openStream());
						
					}catch (SAXException e) {
						//throw
						ArrayList<String> errores = handler2.getErrors();
						errores.add(e.getMessage());
						error = 1;
						//TODO: handle exception
					}
					if(handler2.getWarnings().size()>0) {
						BadTVML bad = new BadTVML();
						bad.setFich(url+newTVML);
						bad.setMessage(handler2.getWarnings());
						warnings.add(bad);
						error = 1;
					}
					if(handler2.getErrors().size()>0){
						BadTVML bad = new BadTVML();
						bad.setFich(url+newTVML);
						bad.setMessage(handler2.getErrors());
						errors.add(bad);
						error = 1;
						
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(error==0){

					leerXML(doc2);
					visitedTVMLs.add(newTVML);
					moreTVMLs.remove(i);
					i--;
				}
				
			} else {
				moreTVMLs.remove(i);
				i--;
			}

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
		String auto = req.getParameter("auto");
		String fecha = req.getParameter("pdia");
		String canal = req.getParameter("pcanal");
		if (pfase == null) {
			pfase = "01";
		}
		if(auto==null){
			auto = "no";
		}

		switch (pfase) {
		case "01": 
			if(auto.equals("si")){
				fase01auto(res);
				// comprobariamos la password
			}else{
				pantallaPrincipal(out, req);
				mostrarFase01(out, req);
			}

			break;
		case "02": // fase de ficheros erróneos
		mostrarFase02(out,req);
			break;
		case "11": // fase para escoger fecha de la programación. Consulta1
			if(auto.equals("si")){
				fase11auto(res);
				// comprobariamos la password
			}else{
				pantallaPrincipal(out, req);
				mostrarFase11(out, req);
			}

			break;
		case "12":
			if(auto.equals("si")){
				fase12auto(res, fecha);
				// comprobariamos la password
			}else{
				pantallaPrincipal(out, req);
				mostrarFase12(out, req);
			}

			break;
		case "13":
			if(auto.equals("si")){
				fase13auto(res, fecha, canal);
				// comprobariamos la password
			}else{
				pantallaPrincipal(out, req);
				mostrarFase13(out, req);
			}

			break;
		default:
			if(auto.equals("si")){
				// comprobariamos la password
			}else{
				pantallaPrincipal(out, req);
				mostrarFase01(out, req);
			}

			break;
		}

	}

	public void fase01auto(HttpServletResponse res){
		res.setContentType("text/xml");
		res.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = res.getWriter();
		} catch (Exception e) {
			//TODO: handle exception
		}
		out.write("<?xml version='1.0' encoding = 'utf-8' ?>");
		out.write("<service>");
		out.write("<status>OK</status>");
		out.write("</service>");
	}


	public void fase02auto(HttpServletResponse res){
		res.setContentType("text/xml");
		res.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = res.getWriter();
		} catch (Exception e) {
			//TODO: handle exception
		}
		out.write("<?xml version='1.0' encoding = 'utf-8' ?>");
		out.write("<service>");
		out.write("<status>OK</status>");
		out.write("</service>");
	}
	public void fase11auto(HttpServletResponse res){
		res.setContentType("text/xml");
		res.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = res.getWriter();
		} catch (Exception e) {
			//TODO: handle exception
		}

		out.write("<?xml version='1.0' encoding = 'utf-8' ?>");
		out.write("<dias>");
		for (String fecha:getC1Fechas()) {
			
			out.write("<dia>"+fecha+"</dia>");
		}
		out.write("</dias>");
	}
	
	public void fase12auto(HttpServletResponse res,String fecha){
		res.setContentType("text/xml");
		res.setCharacterEncoding("iso-8859-1");
		PrintWriter out = null;
		try {
			out = res.getWriter();
		} catch (Exception e) {
			//TODO: handle exception
		}

		out.write("<?xml version='1.0' encoding = 'iso-8859-1' ?>");
		out.write("<canales>");
		
		for (Canal canal:getC1Canales(fecha)) {
			
			out.write("<canal idioma='"+canal.getLang()+"' grupo='"+canal.getGrupo()+"'>"+canal.getNombreCanal()+"</canal>");
		}
		out.write("</canales>");
	}
	
	public void fase13auto(HttpServletResponse res,String fecha,String canal){
		res.setContentType("text/xml");
		res.setCharacterEncoding("iso-8859-1");
		PrintWriter out = null;
		try {
			out = res.getWriter();
		} catch (Exception e) {
			//TODO: handle exception
		}

		out.write("<?xml version='1.0' encoding = 'iso-8859-1' ?>");
		out.write("<peliculas>");
		
		for (Programa p:getC1Peliculas(fecha, canal)) {
			
			out.write("<pelicula edad='"+p.getEdadMinima()+"' hora='"+p.getHoraInicio()+"' resumen='"+p.getComentario()+"'>"+p.getNombrePrograma()+"</pelicula>");
		}
		out.write("</peliculas>");
	}
	
	public ArrayList<String> getC1Fechas() {
		ArrayList<String> fechas = new ArrayList<String>();
		for (Programacion pr : mapa.values()) {
			fechas.add(pr.getFecha());
		}
		Collections.sort(fechas);
		return fechas;
	}

	public ArrayList<Canal> getC1Canales(String fecha) {

		for (Programacion prog : mapa.values()) {
			if (prog.getFecha().equals(fecha)) {
				ArrayList<Canal> lista = new ArrayList<Canal>(prog.getCanales());
				Collections.sort(lista);
				return lista;
			}
		}

		return null;
	}

	private void leerXML(Document doc) {
		ArrayList<Canal> canalesList = new ArrayList<Canal>();
		try {

			NodeList canales = doc.getElementsByTagName("Canal");
			NodeList fecha = doc.getElementsByTagName("Fecha");
			for (int i = 0; i < canales.getLength(); i++) {

				canalesList.add(obtenerCanales(canales.item(i)));

			}
			Programacion programacion = new Programacion(fecha.item(0).getTextContent(), canalesList);
			mapa.put(programacion.getFecha(), programacion);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	private void pantallaPrincipal(PrintWriter out, HttpServletRequest req) {
		String solicitante = req.getParameter("solicitante");
		out.println("<!DOCTYPE html> ");
		out.println("<html>");
		out.println("<head>");
		out.println("<script src=" + req.getContextPath() + "/css/myScripts.js></script>");
		out.println("<link rel='stylesheet' type='text/css' href='" + req.getContextPath() + "/css/prueba.css' >");
		out.println("<meta charset='utf-8'/><title>Consultas</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1> Servicio de consulta de información sobre canales de TV </h1>");
	}

	private void mostrarFase02(PrintWriter out, HttpServletRequest req) {
		// pantallaPrincipal(out, req);
		out.println("<!DOCTYPE html> ");
		out.println("<html>");
		out.println("<head>");
		out.println("<script src=" + req.getContextPath() + "/css/myScripts.js></script>");
		out.println("<link rel='stylesheet' type='text/css' href='" + req.getContextPath() + "/css/prueba.css' >");
		out.println("<meta charset='utf-8'/><title>Consultas</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1> Servicio de consulta de información sobre canales de TV </h1>");

		String pfaseAux = req.getParameter("pfase");
		out.println("<h2> Ficheros con warnings: "+warnings.size()+"</h2>");
		if(warnings.size()>0){
			out.println("<ul>");
			for (BadTVML badTVML : warnings) {
				out.println("<li>"+badTVML.getFich()+ "</li>");
				for(String message: badTVML.getMessage()){
					out.println("<li>"+message+ "</li>");
				}				
			}
			out.println("</ul>");
						
		}
		out.println("<h2> Ficheros con errores: "+errors.size()+"</h2>");
		if(errors.size()>0){
			out.println("<ul>");
			for (BadTVML badTVML : errors) {
				out.println("<li>"+badTVML.getFich()+ "</li>");
				for(String message: badTVML.getMessage()){
					out.println("<li>"+message+ "</li>");
				}				
			}
			out.println("</ul>");
						
		}

		
		out.println("<form id='formi' action=/sint/prueba>");
		out.println("<input type='hidden' name='pfase' id='pfase' value='02'>");
	
		out.println(
				"<br><input type='submit' value='Atras' onclick='goBack(01)'> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}
	private void mostrarFase11(PrintWriter out, HttpServletRequest req) {
		// pantallaPrincipal(out, req);

		String pfaseAux = req.getParameter("pfase");
		out.println("<h2> Consulta 1. Fase=" + pfaseAux + "</h2>");

		String slash = "/";
		out.println("<h3> Selecciona la fecha.</h3>");
		out.println("<form id='formi' action=/sint/prueba>");
		out.println("<input type='hidden' name='pfase' id='pfase' value='11'>");
		out.println("<fieldset>");
		ArrayList<String> fechas = getC1Fechas();
		for (String fecha : fechas) {
			out.println("<input type='radio' id='fecha' name='fecha' value='" + fecha + "' > " + fecha + "<br>");
		}
		out.println("</fieldset>");
		/*
		 * out.
		 * println("<input type='radio' name='pfase' value='02'   onclick = 'nextConsulta("
		 * +req.getRequestURL().toString()+"?pfase=02)' > Consulta 2 <br>"); out.
		 * println("<input type='radio' name='pfase' value='11'    onclick = 'nextConsulta("
		 * +req.getRequestURL().toString()+"?pfase=11)' > Consulta 3 <br>");
		 */
		out.println(
				"<br><input id='env' type='submit' value='Enviar' onclick='return nextConsulta(12)'> <input type='submit' value='Atras' onclick='goBack(01)'> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	private void mostrarFase12(PrintWriter out, HttpServletRequest req) {
		// pantallaPrincipal(out, req);
		String pfaseAux = req.getParameter("pfase");
		String fecha = req.getParameter("fecha");
		Programacion p = mapa.get(fecha);
		ArrayList<Canal> canales = new ArrayList<Canal>(getC1Canales(fecha));
		System.out.println(p.getFecha() + " esto es mi fecha *****");

		out.println("<h2> Consulta 1. Fecha=" + fecha + " </h2>");
		String slash = "/";
		out.println("<h3> Selecciona un canal.</h3>");
		out.println("<form id='formi' action='/sint/prueba'>");
		out.println("<input type='hidden' name='pfase' id='pfase' value='12'>");
		out.println("<input type='hidden' name='fecha' id='fecha' value='" + fecha + "'>");
		for (int i = 0; i < canales.size(); i++) {
			Canal canal = canales.get(i);
			out.println("<input type='radio' name='pcanal' value='" + canal.getNombreCanal() + "' > Nombre:"
					+ canal.getNombreCanal() + ", Idioma=" + canal.getLang() + ", Grupo=" + canal.getGrupo() + "<br>");

		}

		out.println(
				"<br><input id='fecha' type='submit' value='Enviar' onclick = 'return nextConsulta(13)'> <input type='submit' value='Atras' onclick='goBack(11)'> <input type='submit' value='inicio' onclick='goBack(01)'> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	private void mostrarFase13(PrintWriter out, HttpServletRequest req) {
		// pantallaPrincipal(out, req);
		String pfaseAux = req.getParameter("pfase");
		String fecha = req.getParameter("fecha");
		String canal = req.getParameter("pcanal");
		ArrayList<Programa> pelis = getC1Peliculas(fecha, canal);

		Programacion p = mapa.get(fecha);
		int faseAnt = Integer.parseInt(pfaseAux) - 1;
		out.println("<h2> Consulta 1. Fecha=" + fecha + ", Canal=" + canal + "</h2>");
		String amp = "&amp;";
		String back = req.getRequestURL().toString() + "?pfase=" + faseAnt + amp + "fecha=" + fecha;
		out.println("<h3> Este es el resultado.</h3>");
		out.println("<form name='formi'>");
		out.println("<input type='hidden' name='pfase' id='pfase' value='13'>");
		out.println("<input type='hidden' name='fecha' id='fecha' value='" + fecha + "'>");
		Canal thisCanal = Canal.getCanalByName(p.getCanales(), canal);

		for (int i = 0; i < pelis.size(); i++) {
			Programa programa = pelis.get(i);
			// out.println("<input type='radio' name='pcanal'
			// value='"+canal.getNombreCanal()+"' > Nombre:"+canal.getNombreCanal()+",
			// Idioma="+canal.getLang()+", Grupo="+canal.getGrupo()+"<br>");
			out.println((i + 1) + ".- Titulo=" + programa.getNombrePrograma() + ", Edad_Minima="
					+ programa.getEdadMinima() + ", Hora=" + programa.getHoraInicio() + "<br>");
		}

		out.println(
				"<br><input id='atras' type='submit' value='Atras' onclick='goBack(12)'>  <input type='submit' value='inicio' onclick='goBack(01)'> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	public ArrayList<Programa> getC1Peliculas(String fecha, String canal) {
		ArrayList<Programa> peliculas = new ArrayList<Programa>();

		Programacion prog = mapa.get(fecha);
		for (Canal c : prog.getCanales()) {
			if (c.getNombreCanal().equals(canal)) {
		
				for (Programa programa : c.getProgramas()) {
					if (programa.getCategoria().equals("Cine")) {
						peliculas.add(programa);
					}
				}
				Collections.sort(peliculas);
				return peliculas;

			}
		}

		return null;
	}

	private void mostrarFase01(PrintWriter out, HttpServletRequest req) {
		// pantallaPrincipal(out, req);
		String fase = req.getParameter("pfase");
		if (fase == null) {
			fase = "01";
		}
		String nextFase = nextFase(fase);
		out.println("<h2> Bienvenido a este servicio</h2>");
		out.println("<a href='" + req.getRequestURL().toString()
				+ "?pfase=02' > Haz click aqui para ver los ficheros de errores </a>");
		out.println("<p> Selecciona una consulta: </p>");
		out.println("<form name='form' action='/sint/prueba'>");
		out.println("<input type='radio' name='pfase' value='11' >" + " Consulta 1. Peliculas de un día en un canal");
		/*
		 * out.
		 * println("<input type='radio' name='pfase' value='02'   onclick = 'nextConsulta("
		 * +req.getRequestURL().toString()+"?pfase=02)' > Consulta 2 <br>");
		 * out.println("<input type='hidden' name='pfase' value='01'>"); out.
		 * println("<input type='radio' name='pfase' value='11'    onclick = 'nextConsulta("
		 * +req.getRequestURL().toString()+"?pfase=11)' > Consulta 3 <br>");
		 */
		out.println("<br><input id='but' type='submit' value='Enviar'>   ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");

	}

	private String nextFase(String fase) {
		String nextFase = "";
		switch (fase) {
		case "01":
			nextFase = "11";
			break;
		case "11":
			nextFase = "12";
			break;
		case "12":
			nextFase = "13";
			break;
		default:
			nextFase = "01";
			break;
		}
		return nextFase;
	}

	private String devuelveFase(String queryString) {
		String aux = "";
		switch (queryString) {
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
		Programa programa;
		// out.println("canal " + (i+1) + " --> ");
		for (int j = 0; j < hijosCanal.getLength(); j++) {
			if (hijosCanal.item(j).getNodeName().equals("NombreCanal")) {
				nombreCanal = hijosCanal.item(j).getTextContent();
			} else if (hijosCanal.item(j).getNodeName().equals("Programa")) {
				programa = obtenerPrograma(hijosCanal.item(j));
				// System.out.println(programa.toString());
				programas.add(programa);
				// out.println( programa.getNombrePrograma().getFirstChild().getNodeValue()+ "
				// "+programa.getEdadMinima()+", ");

			}
		}

		try {
			grupo = ((Element) canal).getElementsByTagName("Grupo").item(0).getTextContent();
		} catch (Exception e) {
			// TODO: handle exception

		}

		Canal canalAux = new Canal(lang, idCanal, nombreCanal, grupo, programas);
		System.out.println(canalAux.toString());
		return canalAux;
	}

	private Programa obtenerPrograma(Node programa) {
		// TODO Auto-generated method stub
		NamedNodeMap atributosP = programa.getAttributes();
		NodeList otraEmision;
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
		nombrePrograma = ((Element) programa).getElementsByTagName("NombrePrograma").item(0).getTextContent();
		categoria = ((Element) programa).getElementsByTagName("Categoria").item(0).getTextContent();
		horaInicio = ((Element) programa).getElementsByTagName("HoraInicio").item(0).getTextContent();

		String comentario = "";
		NodeList pHijos = ((Element) programa).getChildNodes(); // busco hijo que sea del tipo TEXT_NODE -> Comentario /
																// Resumen
		for (int i = 0; i < pHijos.getLength(); i++) {
			Node hijo = pHijos.item(i);
			if (hijo.getNodeType() == org.w3c.dom.Node.TEXT_NODE) {
				comentario += hijo.getNodeValue();
			}
			comentario = comentario.trim();
		}

		try {

			duracion = ((Element) programa).getElementsByTagName("Duracion").item(0).getTextContent();
		} catch (Exception e) {
			// TODO: handle exception
			horaFin = ((Element) programa).getElementsByTagName("HoraFin").item(0).getTextContent();
		}
		String tvml = "";
		try {
			otraEmision = ((Element) programa).getElementsByTagName("OtraEmision");
			tvml = ((Element) programa).getElementsByTagName("TVML").item(0).getTextContent();
			// System.out.println("tvml *************** "+tvml);
			int readed = 0;
			for (String readedTVML : visitedTVMLs) {
				if(readedTVML.equals(tvml)){
					readed = 1;
				}
			}
			for(String moreTVML : moreTVMLs){
				if(moreTVML.equals(tvml)){
					readed=1;
				}
			}
			if(readed==0){

				moreTVMLs.add(tvml);
			}

		} catch (Exception e) {

			// TODO: handle exception
		}

		Programa programaClass = new Programa(edadMinima, langs, nombrePrograma, categoria, horaInicio, duracion,
				horaFin, tvml, comentario);
		return programaClass;
	}

}

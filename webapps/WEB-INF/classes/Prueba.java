import java.io.*;
import java.net.URL;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.text.html.parser.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import 	org.w3c.dom.NodeList;

import javafx.scene.NodeBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.servlet.annotation.*;

@WebServlet ("/Prueba")
public class Prueba extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException
    {
        res.setCharacterEncoding("utf-8");
        res.setContentType("text/html");

        PrintWriter out = res.getWriter();

        String solicitante = req.getParameter("solicitante");
        
        out.println("<html>");
        out.println("<head>");

        out.println("<link rel='stylesheet' type='text/css' href='" + req.getContextPath() + "/css/prueba.css' >");
        out.println("<meta charset='utf-8'/><title>¡Hello World!</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("hola");
        try{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        
            // Leo programacion, es padre -> elimino #text
            // De programacion leo canales,es padre --> elimino #text
            // De canal, leo programas, es padre -> elimino #text
            Document doc = db.parse(new URL("http://gssi.det.uvigo.es/users/agil/public_html/SINT/19-20/tvml-2004-12-01.xml").openStream());
                
            
            NodeList programas = doc.getElementsByTagName("Programa");
        for (int i = 0; i < programas.getLength(); i++ ){

            NodeList childs = programas.item(i).getChildNodes();

            for(int k = 0; k < childs.getLength(); k++){
                out.println(childs.item(k).getNodeName()  + "--" + childs.item(k).getNodeValue() + " || ");

                if (childs.item(k).getNodeName().equals("#text") && !childs.item(k).getNodeValue().equals("null") && childs.item(k).getNodeValue().length() < 3){
                    programas.item(i).removeChild(childs.item(k));
                    k--;
    
                } else if (childs.item(k).getNodeName().equals("#text") ) {
                    Node item = doc.createElement("Resumen");
                    item.setNodeValue(childs.item(k).getNodeValue());
                    programas.item(i).replaceChild( item ,childs.item(k));

                   
                   
                   out.println("\n" +item.getNodeName()  + "  " + item.getNodeValue());
                 
                }
            }
        }



        for (int i = 0; i < programas.getLength(); i++ ){

            NodeList childs = programas.item(i).getChildNodes();

            for(int k = 0; k < childs.getLength(); k++){
                out.println("<br>");

                out.println(childs.item(k).getNodeName()  + "--" + childs.item(k).getNodeValue() + " || ");
            }
        }

            /*
            NodeList programacion = doc.getElementsByTagName("Programacion");
            NodeList canales = doc.getElementsByTagName("Canal");
            NodeList programas = doc.getElementsByTagName("Programa");

            */





            NodeList canales = doc.getElementsByTagName("Canal");
            Node padreCanal = canales.item(0).getParentNode();
            out.println("Nodo padre " + padreCanal.getNodeName() + " Tiene " +padreCanal.getChildNodes().getLength() + " hijos"+  "<br>");
            for(int i = 0 ; i <canales.getLength() ; i ++){
                //Element elem = (Element) canales.item(i);
                NamedNodeMap atributosC = canales.item(i).getAttributes();
                
                out.println("<br>");
                out.println(canales.item(i).getNodeName() + " - "+ atributosC.item(1).getNodeName() + " " +atributosC.item(1).getNodeValue() );
                out.println("<br>");
                if(canales.item(i).hasChildNodes()){
                    
                    NodeList hijosCanal = canales.item(i).getChildNodes();

                   



                    out.println("hijos del canal: " + hijosCanal.getLength());
                   // limpiar #text de un node que tiene hijos
                    for(int j = 0 ; j < hijosCanal.getLength() ; j++){
                        Node hijoC = hijosCanal.item(j);
                        if(!(hijoC.getNodeName().equals("#text"))){
                            
                            out.println(hijosCanal.item(j).getNodeName() + " - ");
                            
                        }else{
                            
                            canales.item(i).removeChild(hijoC);
                            j--;
                        }


                }

                out.println("<br>"+hijosCanal.item(0).getChildNodes().item(0).getNodeValue());
                       
                }

                    }

            // Programa edadminima = "3" langs = "es"

        	//System.out.println(node.item(0).getNodeValue());
        	
        }catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        out.println("<h1>hola "+solicitante+"</h1>");
        out.println("adios");

        System.out.println("mensaje de log con System.out.println()");

        this.log("mensaje de log con log()");

        out.println("</body>");
        out.println("</html>");
    }
}
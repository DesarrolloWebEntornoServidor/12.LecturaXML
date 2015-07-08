package es.dwes.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.dwes.pojos.Empleado;


@WebServlet("/LeeXMLConXPath")
public class LeeXMLConXPath extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            
            String ruta = getServletContext().getRealPath("/xml");
    		String nombreArchivo = File.separator + "Empleados.xml";

    		doc = builder.parse(ruta + nombreArchivo);
 
            // Create XPathFactory object
            XPathFactory xpathFactory = XPathFactory.newInstance();
 
            // Create XPath object
            XPath xpath = xpathFactory.newXPath();
 
            String name = getNombreEmpleadoPorId(doc, xpath, 4);
            out.println("Nombre del empleado con ID 4: " + name + " <br />");
 
            List<String> names = getEmpleadosPorEdad(doc, xpath, 30);
            out.println("Nombre de empleado(s) con 'edad>30': " + Arrays.toString(names.toArray()) + " <br />");
 
            List<String> femaleEmps = getNombreEmpleadas(doc, xpath);
            out.println("Nombres de empleados de género femenino: " +
                    Arrays.toString(femaleEmps.toArray()) + " <br />");
 
 
            // tabla de empleados
            out.println("Lista empleados" + " <br />");
            List<Empleado> listaEmpleados = getEmpleados(doc);
            out.println("<table border=\"1\">");
            out.println("<tr>");
            out.println("  <th>ID</th>");
            out.println("  <th>Nombre</th>");
            out.println("  <th>Género</th>");
            out.println("  <th>Edad</th>");
            out.println("  <th>Oficio</th>");
            out.println("</tr>");
            for (Empleado emp : listaEmpleados) {
            	out.println("<tr>");
            	out.println("  <td>" + emp.getId() + "</td>");
            	out.println("  <td>" + emp.getNombre() + "</td>");
            	out.println("  <td>" + emp.getGenero() + "</td>");
            	out.println("  <td>" + emp.getEdad() + "</td>");
            	out.println("  <td>" + emp.getOficio() + "</td>");
            }
            out.println("</table>");
            
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(out);
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

    private static List<String> getNombreEmpleadas(Document doc, XPath xpath) {
        List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                xpath.compile("/Empleados/Empleado[genero='Female']/nombre/text()");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }
 
 
    private static List<String> getEmpleadosPorEdad(Document doc, XPath xpath, int age) {
        List<String> list = new ArrayList<>();
        try {
            XPathExpression expr =
                xpath.compile("/Empleados/Empleado[edad>" + age + "]/nombre/text()");
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return list;
    }
 
 
    private static String getNombreEmpleadoPorId(Document doc, XPath xpath, int id) {
        String name = null;
        try {
            XPathExpression expr =
                xpath.compile("/Empleados/Empleado[@id='" + id + "']/nombre/text()");
            name = (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
 
        return name;
    }

    /*    
    private static List<Empleado> getEmployees(Document doc, XPath xpath) {
        List<Empleado> listaEmpleados = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                xpath.compile("/Empleados/Empleado");
            //evaluate expression result on XML document
            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
            	
            	NodeList elementos = nodes.item(i).getChildNodes();
            	for (int j = 0; i < elementos.getLength(); i++) {
            		nodes.item(i).getNodeValue()
            	}
                list.add(nodes.item(i).getNodeValue());
            }    
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return listaEmpleados;
    }
    */

    // Este método no usa XPath    
    private static List<Empleado> getEmpleados(Document doc) {
        List<Empleado> listaEmpleados = new ArrayList<Empleado>();
       	doc.getDocumentElement().normalize();
       	// System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
       	NodeList nodeList = doc.getElementsByTagName("Empleado");
       	//now XML is loaded as Document in memory, lets convert it to Object List
       	for (int i = 0; i < nodeList.getLength(); i++) {
       		listaEmpleados.add(getEmpleado(nodeList.item(i)));
       	}
		return listaEmpleados;
    }


	private static Empleado getEmpleado(Node node) {
		//XMLReaderDOM domReader = new XMLReaderDOM();
		Empleado emp = new Empleado();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			emp.setId(Integer.parseInt(element.getAttribute("id")));
			emp.setNombre(getTagValue("nombre", element));
			emp.setEdad(Integer.parseInt(getTagValue("edad", element)));
			emp.setGenero(getTagValue("genero", element));
			emp.setOficio(getTagValue("oficio", element));
		}
		return emp;
	}

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
	
}

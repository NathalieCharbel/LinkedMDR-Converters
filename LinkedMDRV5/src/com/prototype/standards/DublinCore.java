package com.prototype.standards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.prototype.upload.FileLinked;

public class DublinCore {
	// Name of the Directory which contains the transformed file on the server
	private static final String SAVE_DIR = "uploads";
	
	/** Execute the dublin core transformation based on apache tika**/
	public void doDc(HttpServletRequest request, FileLinked fileLinked) throws TransformerConfigurationException{

		
		 String appPath = request.getServletContext().getRealPath("");
	        
	     // Path of the transformed file
	     String savePath = appPath + File.separator + SAVE_DIR + File.separator+fileLinked.getFileName()+"_dublincore.xml";
	     //System.out.println("savePath"+savePath);
         
	     File file = new File(fileLinked.getFilePath());

		 //Parser method parameters
		 Parser parser = new AutoDetectParser();
		 DefaultHandler handler =new DefaultHandler();
		 Metadata metadata = new Metadata();
		 FileInputStream inputstream;
		 
		 try {
			inputstream = new FileInputStream(file);
			ParseContext context = new ParseContext();
			  
			parser.parse(inputstream, handler, metadata, context);
			// System.out.println(handler.toString());
			
			// getting the list of all meta data elements 
			String[] metadataNames = metadata.names();
			 
			// Create a xml file /dublin core
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("DCmetadata");
			doc.appendChild(rootElement);
			
			// set attribute to root element
			Attr attr = doc.createAttribute("xmlns:dc");
			attr.setValue("http://purl.org/dc/elements/1.1/");
			rootElement.setAttributeNode(attr);
			
			// set attribute to root element
			Attr attr1 = doc.createAttribute("xmlns:dcterms");
			attr1.setValue("http://purl.org/dc/terms/");
			rootElement.setAttributeNode(attr1);
			
			// Add Element dc:identifier
			Element dublinCoreIdentifier = doc.createElement("dc:identifier");
			dublinCoreIdentifier.appendChild(doc.createTextNode(fileLinked.getFilePath()));
			
			rootElement.appendChild(dublinCoreIdentifier);
	 			
	        for (String name : metadata.names()) {
              if(name.equals("dc:title") || name.equals("dc:creator") ||name.equals("dc:description") ||name.equals("dc:date") || 
            		  name.equals("dc:type") || name.equals("dc:format") || name.equals("dcterms:references") || name.equals("dcterms:requires") ||
            		  		name.equals("dcterms:temporal") || name.equals("dcterms:spatial") || name.equals("dc:relation") ||
            		  		name.equals("dc:publisher") ||name.equals("dcterms:created") || name.equals("dcterms:modified") ||
            		  		name.equals("dc:rights") || name.equals("dc:coverage") || name.equals("dc:language")){
            	  
            	  
            	// Add elements dublin core
      			Element dublinCoreElement = doc.createElement(name);
      			dublinCoreElement.appendChild(doc.createTextNode(metadata.get(name)));
      			
      			rootElement.appendChild(dublinCoreElement);
		              }
	   
			      }
			      
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(savePath));
			transformer.transform(source, result);
			// System.out.println("File saved!");
			
			// modify the path dublincore
			fileLinked.setFilePathDC(savePath);
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TikaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	     

}

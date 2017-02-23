package com.prototype.linkedmdr;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.prototype.upload.FileLinked;


public class LinkedMdr {

	private String appPath = "";
	
	/** Execute the linkedmdr transformation**/
	public void doLinkedMDR(HttpServletRequest request,FileLinked fileLinked) throws TransformerConfigurationException{
		// Constructs path of the directory to save the transformed fiel
        appPath = request.getServletContext().getRealPath("");
		try {
			
			// First xpath will be used in order to detect a tag dc, tei, mpeg-7
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        
	        // The file in which we are using the request
	        File fileXML = new File(fileLinked.getFilePath());
	        Document xml = builder.parse(fileXML);
	        
	        // Root of the document 
	        Element root = xml.getDocumentElement();
	        XPathFactory xpf = XPathFactory.newInstance();
	        XPath path = xpf.newXPath();
	        
	        // Tags we want to find
	        String expressionDC = "/DCmetadata";
	        String expressionTEI = "/TEI";
	        String expressionMPEG7 = "/Mpeg7";
	        
	        // Execute the xpath request, true if the xpath finds the tag
	        boolean boolDC = (Boolean)path.evaluate(expressionDC, root, XPathConstants.BOOLEAN);
	        boolean boolTEI = (Boolean)path.evaluate(expressionTEI, root, XPathConstants.BOOLEAN);
	        boolean boolMPEG7 = (Boolean)path.evaluate(expressionMPEG7, root, XPathConstants.BOOLEAN);
	        
	        String pathTransformation=null;
	        
	        // Apply the xslt
	        if (boolDC){
	        	// Transform 
				pathTransformation = transformationToDC(fileLinked.getFilePath());
				
				// Modify the linkedmdr path of the file
				fileLinked.setFilePathLinkedMDR(pathTransformation);
				
	        }
	        else if (boolTEI){
	        	// Transform 
				pathTransformation = transformationToTEI(fileLinked.getFilePath());
				
				// Modify the linkedmdr path of the file
				fileLinked.setFilePathLinkedMDR(pathTransformation);
	        	
	        }
	        else if (boolMPEG7){
	        	// Transform 
				pathTransformation = transformationToMPEG7(fileLinked.getFilePath());
				
				// Modify the linkedmdr path of the file
				fileLinked.setFilePathLinkedMDR(pathTransformation);
	        	
	        }
	      
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String transformationToDC(String pathFile){
		TransformerFactory factory = TransformerFactory.newInstance();
		
		// File xslt
        Source xslt = new StreamSource(new File(appPath + File.separator +"XSLT/dc2rdf.xslt"));
        Transformer transformer;
        
        // Path of the transformed file
        String pathTransformation="";
		try {
			transformer = factory.newTransformer(xslt);
			// Input file
			Source text = new StreamSource(new File(pathFile));
			// Output file and rewrite
		    pathTransformation = pathFile+"_linkedmdr.xml";
	        transformer.transform(text, new StreamResult(new File(pathTransformation)));
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pathTransformation;
	}
	
	public String transformationToTEI(String pathFile){
		TransformerFactory factory = TransformerFactory.newInstance();
		
		// File xslt
        Source xslt = new StreamSource(new File(appPath + File.separator +"XSLT/tei2rdf.xslt"));
        Transformer transformer;
        
        // Path of the transformed file
        String pathTransformation="";
		try {
			transformer = factory.newTransformer(xslt);
			// Input file
			Source text = new StreamSource(new File(pathFile));
			// Output file and rewrite
		    pathTransformation = pathFile+"_linkedmdr.xml";
	        transformer.transform(text, new StreamResult(new File(pathTransformation)));
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pathTransformation;
	}
	
	public String transformationToMPEG7(String pathFile){
		TransformerFactory factory = TransformerFactory.newInstance();
		
		// File xslt
        Source xslt = new StreamSource(new File(appPath + File.separator +"XSLT/mpeg72rdf.xslt"));
        Transformer transformer;
        
        // Path of the transformed file
        String pathTransformation="";
		try {
			transformer = factory.newTransformer(xslt);
			// Input file
			Source text = new StreamSource(new File(pathFile));
			// Output file and rewrite
		    pathTransformation = pathFile+"_linkedmdr.xml";
	        transformer.transform(text, new StreamResult(new File(pathTransformation)));
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pathTransformation;
	}
}

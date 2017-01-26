package com.prototype.linkedmdr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


public class LinkedMdr {
	// Name of the Directory which contains the transformed file
	private static final String SAVE_DIR = "uploads";
	private String appPath = "uploads";
	
	/** Execute the dublin core transformation based on apache tika**/
	public void doLinkedMDR(HttpServletRequest request, HttpServletResponse response, String pathFile,String fileName) throws TransformerConfigurationException{
		
		try {
			
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         File fileXML = new File(pathFile);
	         Document xml = builder.parse(fileXML);
	         Element root = xml.getDocumentElement();
	         XPathFactory xpf = XPathFactory.newInstance();
	         XPath path = xpf.newXPath();
	         
	         String expressionDC = "/DCmetadata";
	         String expressionTEI = "/TEI";
	         String expressionMPEG7 = "/Mpeg7";
	         
        	// Constructs path of the directory to save uploaded file
	        appPath = request.getServletContext().getRealPath("");
  
	        // Save the transformed file (dc/xml) on the server
            String savePath = appPath + File.separator + SAVE_DIR + File.separator+ fileName+"";
	         
	        boolean boolDC = (Boolean)path.evaluate(expressionDC, root, XPathConstants.BOOLEAN);
	        boolean boolTEI = (Boolean)path.evaluate(expressionDC, root, XPathConstants.BOOLEAN);
	        boolean boolMPEG7 = (Boolean)path.evaluate(expressionDC, root, XPathConstants.BOOLEAN);
	        
	        String pathTransformation=null;
	        
	        if (boolDC){
	        	// Transform 
				pathTransformation = transformationToDC(savePath);
	        }
	        else if (boolTEI){
	        	
	        }
	        else if (boolMPEG7){
	        	
	        }
	       
					
			// LinkedMDR Servlet response to the client (force the download)
			response.setContentType("application/force-download");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Type", "application/octet-stream");
	        // Delete the extension of the current file
	        if (fileName.indexOf(".") > 0){
	            fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName+"_linkedmdr.xml");
			
			// Write the file saved on the server to the client 
			File f= new File(pathTransformation);
			FileInputStream inStream = new FileInputStream(f);
			// Obtains response's output stream
	        OutputStream outStream1 = response.getOutputStream();
	         
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	         
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream1.write(buffer, 0, bytesRead);
	        }         
	        
	        // Close the streams
	        inStream.close();
	        outStream1.close(); 
	        
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
		// File xsl
        Source xslt = new StreamSource(new File(appPath + File.separator +"XSLT/dc2rdf.xslt"));
        Transformer transformer;
        // Path of the transformed file
        String pathTransformation="";
		try {
			transformer = factory.newTransformer(xslt);
			//input file
			Source text = new StreamSource(new File(pathFile));
			//output file and rewrite
			
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
        String pathTransformation="";
		return pathTransformation;
	}
	
	public String transformationToMPEG7(String pathFile){
        // Path of the transformed file
        String pathTransformation="";
		return pathTransformation;
	}
}

package com.prototype.dc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

public class DublinCore {
	// Name of the Directory which contains the transformed file on the server
	private static final String SAVE_DIR = "uploads";
	private String appPath = "uploads";
	
	/** Execute the dublin core transformation based on apache tika**/
	public void doDc(HttpServletRequest request, HttpServletResponse response, String pathFile,String fileName) throws TransformerConfigurationException{
		
		byte[] file;
		try {
			file = Files.toByteArray(new File(pathFile));
			
			// Parser auto
			AutoDetectParser tikaParser = new AutoDetectParser();
			
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
	        TransformerHandler handler1 = factory.newTransformerHandler();
	        
	        // Transformation properties
	        handler1.getTransformer().setOutputProperty(OutputKeys.METHOD, "XML");
	        handler1.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
	        handler1.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        handler1.setResult(new StreamResult(out));
	      
	        // Transform the file with apache tika
	        tikaParser.parse(new ByteArrayInputStream(file), handler1, new Metadata());
	        // System.out.println(new String(out.toByteArray(), "UTF-8"));
	        
	        // Constructs path of the directory to save uploaded file
	        appPath = request.getServletContext().getRealPath("");
	       
	        // Save the transformed file (dc/xml) on the server (write in the save path)
            String savePath = appPath + File.separator + SAVE_DIR + File.separator+ fileName+"";
            
	        FileOutputStream outStream = new FileOutputStream(savePath);
			outStream.write(out.toByteArray());
			outStream.close();
			
			// Transform the xhtml file to dc/xml file and save the new file
			String pathTransformationDc = transformationToDC(savePath);
			
			// The file has been saved on the server side, now we are going to add the media uri (identifier tag) for linkedmdr			
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
			domFactory.setIgnoringComments(true);
			DocumentBuilder builder;
			try {
				builder = domFactory.newDocumentBuilder();
				String valueURI=pathFile;
				Document doc = builder.parse(new File(pathTransformationDc)); 
				
				// Find DCmetadata tag
				NodeList nodes = doc.getElementsByTagName("DCmetadata");
				
				// Add dc:identifier tag
				Text a = doc.createTextNode(valueURI); 
				Element p = doc.createElement("dc:identifier"); 
				p.appendChild(a); 

				nodes.item(0).insertBefore(p, nodes.item(0).getFirstChild());
				
				// Save the new file (rewriting the pathTransformationDc)
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				DOMSource source = new DOMSource(doc);
			    Transformer transformer = transformerFactory.newTransformer();
			    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			    StreamResult result = new StreamResult(pathTransformationDc);
			    transformer.transform(source, result);		
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			// LinkedMDR Servlet response to the client (force the download)
			response.setContentType("application/force-download");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Type", "application/octet-stream");
			// We need to delete the extension if you want to rename the file
			// Delete the extension of the current file (
	        if (fileName.indexOf(".") > 0){
	            fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName+"_dublincore.xml");
			
			// Write the file saved on the server to the client 
			File f= new File(pathTransformationDc);
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
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String transformationToDC(String pathFile){
		TransformerFactory factory = TransformerFactory.newInstance();
		// File xsl
        Source xslt = new StreamSource(new File(appPath + File.separator +"XSLT/dc_xml.xsl"));
        Transformer transformer;
        // Path of the transformed file
        String pathTransformation="";
		try {
			transformer = factory.newTransformer(xslt);
			//input file
			Source text = new StreamSource(new File(pathFile));
			//output file and rewrite
		    pathTransformation = pathFile+"_dublincore.xml";
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

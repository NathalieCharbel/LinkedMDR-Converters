package com.linkedmdr.dc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

public class DublinCore {
	// Name of the Directory which contains the transformed file
	private static final String SAVE_DIR = "uploads";
	
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
	        String appPath = request.getServletContext().getRealPath("");
	        // Delete the extension of the current file
	        if (fileName.indexOf(".") > 0){
	            fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
	        
	        // Save the transformed file (dc/xml) on the server
            String savePath = appPath + File.separator + SAVE_DIR + File.separator+ fileName+"_dublincore.xml";
	        FileOutputStream outStream = new FileOutputStream(savePath);
			outStream.write(out.toByteArray());
			outStream.close();
			
			// LinkedMDR Servlet response to the client (force the download)
			response.setContentType("application/force-download");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName+"_dublincore.xml");
			
			// Write the file saved on the server to the client 
			File f= new File(savePath);
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
}

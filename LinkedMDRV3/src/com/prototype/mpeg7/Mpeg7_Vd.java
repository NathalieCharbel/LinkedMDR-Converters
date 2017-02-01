package com.prototype.mpeg7;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class Mpeg7_Vd {
	
	    // Name of the Directory which contains the transformed file
	 	private static final String SAVE_DIR = "uploads";
		
	 	/** Execute the mpeg-7 transformation based on Mpeg-7 visual descriptors**/
		public void doMPEG7(HttpServletRequest request, HttpServletResponse response, String pathFile, String fileName) {
			
			try {
			    // Constructs path of the directory to save the transformed file
		        String appPath = request.getServletContext().getRealPath("");
		        
		        // Path of the transformed file
		    	// Delete the extension of the file
		        if (fileName.indexOf(".") > 0){
		            fileName = fileName.substring(0, fileName.lastIndexOf("."));
				}
	            String savePath = appPath + File.separator + SAVE_DIR + File.separator+fileName+"_mpeg7.xml";
	            //System.out.println("savePath"+savePath);
	            
				// Run mpeg7 visual descriptors
				Process process = new ProcessBuilder(appPath+"/MPEG7/vde.exe","-d","SC","DC","HT","EH", "-i",pathFile, "-o",savePath).start();
				

				// The file has been saved on the server side, now we are going to add the media uri (identifier tag) for linkedmdr			
				DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
				domFactory.setIgnoringComments(true);
				DocumentBuilder builder;
				try {
					builder = domFactory.newDocumentBuilder();
					String valueURI=pathFile;
					Document doc = builder.parse(new File(savePath)); 
					
					// Find the mpeg7 tag
					NodeList nodes = doc.getElementsByTagName("Mpeg7");
					Text a = doc.createTextNode(valueURI); 
					
					// Add MediaUri tag 
					Element p = doc.createElement("MediaUri"); 
					p.appendChild(a); 
					nodes.item(0).insertBefore(p, nodes.item(0).getFirstChild());
					
					// Save the file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					DOMSource source = new DOMSource(doc);
				    Transformer transformer = transformerFactory.newTransformer();
				    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				    
				    StreamResult result = new StreamResult(savePath);
				    transformer.transform(source, result);		
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				// LinkedMDR Servlet response to the client (force the download)
				response.setContentType("application/force-download");
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader("Content-Type", "application/octet-stream");
				response.setHeader("Content-Disposition","attachment; filename=\""+fileName+"_mpeg7.xml");
				
				// Create the attached file
				File f= new File(savePath);
				f.createNewFile();
				
				// Write the file on the stream
				FileInputStream inStream = new FileInputStream(f);
				
				// Obtains response's output stream
		        OutputStream outStream1 = response.getOutputStream();
		         
		        byte[] buffer = new byte[4096];
		        int bytesRead = -1;
		         
		        while ((bytesRead = inStream.read(buffer)) != -1) {
		            outStream1.write(buffer, 0, bytesRead);
		        }
		        
		        // Close streams
		        inStream.close();
		        outStream1.close(); 			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}

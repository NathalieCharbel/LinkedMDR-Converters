package com.prototype.standards;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
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

import com.prototype.upload.FileLinked;

public class Mpeg7_Vd {
	
	    // Name of the Directory which contains the transformed file
	 	private static final String SAVE_DIR = "uploads";
		
	 	/** Execute the mpeg-7 transformation based on Mpeg-7 visual descriptors**/
		public void doMPEG7(HttpServletRequest request, FileLinked fileLinked) {
			
			try {
			    // Constructs path of the directory to save the transformed file
		        String appPath = request.getServletContext().getRealPath("");
		        
		        // Path of the transformed file
	            String savePath = appPath + File.separator + SAVE_DIR + File.separator+fileLinked.getFileName()+"_mpeg7.xml";
	            //System.out.println("savePath"+savePath);
	            
				// Run mpeg7 visual descriptors
				 Process pr= Runtime.getRuntime().exec(appPath+"MPEG7/vde.exe -d SC DC HT EH -i " +fileLinked.getFilePath()+" -o "+savePath);
				 System.out.println(appPath+"MPEG7/vde.exe -d SC DC HT EH -i"+fileLinked.getFilePath()+"-o"+savePath);
	             try {
					pr.waitFor();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// The file has been saved on the server side, now we are going to add the media uri (identifier tag) for linkedmdr			
				DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
				domFactory.setIgnoringComments(true);
				DocumentBuilder builder;
				try {
					builder = domFactory.newDocumentBuilder();
					String valueURI=fileLinked.getFilePath();
					Document doc = builder.parse(new File(savePath)); 
					
					// Find the mpeg7 tag
					NodeList nodes = doc.getElementsByTagName("Mpeg7");
					Text a = doc.createTextNode(valueURI); 
					
					// Add MediaUri tag 
					Element p = doc.createElement("MediaUri"); 
					p.appendChild(a); 
					nodes.item(0).insertBefore(p, nodes.item(0).getFirstChild());
					
					// Save the new xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					DOMSource source = new DOMSource(doc);
				    Transformer transformer = transformerFactory.newTransformer();
				    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				    
				    StreamResult result = new StreamResult(savePath);
				    transformer.transform(source, result);	
				    
				    // modify the file path mpeg7vd
				    fileLinked.setFilePathMpeg7Vd(savePath);
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}

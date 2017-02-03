package com.prototype.standards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.prototype.upload.FileLinked;
import com.prototype.upload.UnZipTEI;

public class Tei {
	
	 // Name of the Directory which contains the transformed file
 	private static final String SAVE_DIR = "uploads";
 	
	/** TEI Standard to XML (based on OxGarage) **/
    @SuppressWarnings("deprecation")
	public void doTei(HttpServletRequest request, FileLinked fileLinked) throws ServletException, IOException {
    	
	    // Call the web service OxGarage when you have an uploaded file
		
    	// Path of the document uploaded on the server side
		String path = fileLinked.getFilePath();
		
		//System.out.println("PATH path"+path);
		
		
		// Gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
        String fileOxgarage=fileLinked.getFileName()+"_tei.xml";
		String pathout = appPath + File.separator + SAVE_DIR + File.separator+ fileOxgarage;
		
		// Document .docx to tei/xml
		String stringUrl = "http://www.tei-c.org/ege-webservice//Conversions/docx%3Aapplication%3Avnd.openxmlformats-officedocument.wordprocessingml.document/TEI%3Atext%3Axml";
		
		// Request http to oxGarage
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(stringUrl);

		FileBody bin = new FileBody(new File(path));
		StringBody comment = new StringBody("Filename: " + path);

		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("bin", bin);
		reqEntity.addPart("comment", comment);
		httppost.setEntity(reqEntity);
		
		// Response of oxGarage
		HttpResponse responseHttp = httpclient.execute(httppost);
		HttpEntity resEntity = responseHttp.getEntity();
		
		if ( resEntity != null ) {
			// get the name of the file sent by oxGarage (.xml or .zip)
			String contentDisp = responseHttp.getFirstHeader("Content-Disposition").getValue();
			String[] items = contentDisp.split(";");
	        for (String s : items) {
	            if (s.trim().startsWith("filename")) {
	              fileOxgarage= s.substring(s.indexOf("=") + 2, s.length()-1);
	              System.out.println("fileOxgarage"+fileOxgarage);
	            }
	        }
	                       
			// Get the result from oxGarage
	        InputStream instream = resEntity.getContent();
	        
			// Write the result in pathout
			 File yourFile = new File(pathout);
	         yourFile.createNewFile(); // if file already exists will do nothing 
	        
	         FileOutputStream output = new FileOutputStream(yourFile, false); 
	        
	         try {
	            int l;
	            byte[] tmp = new byte[2048];
	            while ( (l = instream.read(tmp)) != -1 ) {
	            	System.out.println(l);
	                output.write(tmp, 0, l);
	            }
	         } finally {
	            output.close();
	            instream.close();
	         } // File has been saved
			
	        // If the file is a zip...unzip
	        String extension = "";

	        int i = fileOxgarage.lastIndexOf('.');
	        if (i > 0) {
	            extension = fileOxgarage.substring(i+1);
	        }
	        
	        // Unzip the file
	        if(extension.equals("zip")){
	        	UnZipTEI unzip = new UnZipTEI();
	        	String pathunzip = appPath + File.separator + SAVE_DIR;
	        	// change the pathout to the new tei.xml file which is contains in the zip
	        	pathout=unzip.unZipIt(pathout, pathunzip);
	        }
	        
	        // Before sending the file to the client we will add the tag media uri on the file (for linkedmdr)
	        // The file has been saved on the server side, now we are going to add the media uri (identifier tag) for linkedmdr			
 			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
 			domFactory.setIgnoringComments(true);
 			DocumentBuilder builder;
	     			try {
	     				builder = domFactory.newDocumentBuilder();
	     				String valueURI=fileLinked.getFilePath();
	     				Document doc = builder.parse(new File(pathout)); 
	     				
	     				// Find the teiHeader tag
	     				NodeList nodes = doc.getElementsByTagName("teiHeader");
	     				
	     				// Add the ident tag with the valueURI (pathfile)
	     				Text a = doc.createTextNode("URI:"+valueURI); 
	     				Element p = doc.createElement("sourceDesc");  
	     				Element p1 = doc.createElement("p"); 
	     				p1.appendChild(a);
	     				p.appendChild(p1);
	     				nodes.item(0).insertBefore(p, nodes.item(0).getFirstChild());
	     				
	     				// Save the file
	     				TransformerFactory transformerFactory = TransformerFactory.newInstance();
	     				DOMSource source = new DOMSource(doc);
	     			    Transformer transformer = transformerFactory.newTransformer();
	     			    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	     			    StreamResult result = new StreamResult(pathout);
	     			    transformer.transform(source, result);
	     			    
	     			    // Modify the tei path
	     			    fileLinked.setFilePathTei(pathout);
	     			    
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

		}
    }

}

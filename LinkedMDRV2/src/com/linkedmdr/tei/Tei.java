package com.linkedmdr.tei;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class Tei {
		
	/** TEI Standard to XML (based on OxGarage) **/
    @SuppressWarnings("deprecation")
	public void doTei(HttpServletRequest request, HttpServletResponse response, String pathFile) throws ServletException, IOException {
    	
	    // Call the web service OxGarage when you have an uploaded file
		
    	// Path of the document uploaded on the server side
		String path = pathFile;
		
		// Gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
        String fileOxgarage=null;
		String pathout = appPath + File.separator + fileOxgarage;
		
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
		        }
			
			
			// LinkedMDR Servlet response to the client (force the download)
			response.setContentType("application/force-download");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileOxgarage);
			
			// Create  the attached file 
			File f= new File(pathout);
			
			FileInputStream inStream = new FileInputStream(f);
			
			// obtains response's output stream
	        OutputStream outStream = response.getOutputStream();
	         
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	         
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	        
	        // Close streams        
	        inStream.close();
	        outStream.close(); 

		}
    }

}

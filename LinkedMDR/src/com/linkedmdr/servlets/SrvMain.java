package com.linkedmdr.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;

import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Servlet implementation class SrvMain
 */
@WebServlet("/SrvMain")
@MultipartConfig
public class SrvMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Name of the Directory which contains the uploaded files
	private static final String SAVE_DIR = "uploadFiles";
	// Path of the file
	private static String pathFile = null;
	

    /**
     * Default constructor. 
     */
    public SrvMain() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// Parameters sent by the client
		Part uploadFile = request.getPart("uploadFile");
		String dc = request.getParameter("dublincore");
		String tei = request.getParameter("tei");
		String mpeg7 = request.getParameter("mpeg-7");
		String linkedmdr= request.getParameter("linkedmdr");
		
		// Do Upload
		if (uploadFile !=null){
			pathFile=doUpload(request,response,uploadFile);
		}
		
		// Do Dublin Core
		if (dc != null && dc.equals("dublincore")){
			
		}
		
		// Do TEI
		if (tei != null && tei.equals("tei")){
			doTei(request,response);	
		}
		
		// Do MPEG-7
		if (mpeg7 != null && mpeg7.equals("mpeg-7")){
			
		}
		
		// Do LinkedMDR
		if (linkedmdr != null && linkedmdr.equals("linkedmdr")){
			
		}
	  }
	
	/**Upload the file on server **/
	 protected String doUpload(HttpServletRequest request, HttpServletResponse response, Part part) throws ServletException, IOException {

	    // Gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
        
        // Constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR;
         
        // Creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        
        // Extract name of the file
        String fileName=extractFileName(part);
        
        // Save the file
        part.write(savePath + File.separator + fileName);
        
        // Return path of the file 
        return savePath + File.separator + fileName;
       
	}
    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
            	System.out.println(s.substring(s.indexOf("=") + 2, s.length()-1));
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";    
	}
    
    /** Dublin Core Standard to XML **/
    protected void doDC(HttpServletRequest request) throws ServletException, IOException {
    
    }
    
    /** TEI Standard to XML **/
    @SuppressWarnings("deprecation")
	protected void doTei(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
	    // Call the web service OxGarage when you have an uploaded file
		
    	// Path of the document on the server side
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
			response.setHeader("Content-Disposition","attachment; filename=\"" + "tei.zip");
			
			File f= new File(pathout);
			
			FileInputStream inStream = new FileInputStream(f);
			
			// obtains response's output stream
	        OutputStream outStream = response.getOutputStream();
	         
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	         
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	         
	        inStream.close();
	        outStream.close(); 

		}
}
    /** MPEG-7 Standard to XML **/
    protected void doMPEG7(HttpServletRequest request) throws ServletException, IOException {
    
    }
    
    /** LinkedMDR Standard to RDF **/
    protected void doLinkedMDR(HttpServletRequest request) throws ServletException, IOException {
    
    }
}

package com.linkedmdr.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.linkedmdr.dc.DublinCore;
import com.linkedmdr.mpeg7.Mpeg7_Caliph;
import com.linkedmdr.mpeg7.Mpeg7_Vd;
import com.linkedmdr.tei.Tei;
import com.linkedmdr.upload.FileLinked;

/**
 * Servlet implementation class SrvMain
 */

@MultipartConfig
public class SrvMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Name of the Directory which contains the uploaded files
	private static final String SAVE_DIR = "uploads";

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
		 
	    // Instanciate a fileLinked which represents the uploaded file
     	FileLinked fileLinked=new FileLinked();
     	
		/** Upload part**/
		// Check if we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	    if (isMultipart) {	
	    	
	    	// Row id of the current download
	    	String row_id=null;
	    	
	    	// Create a factory for file items
	        FileItemFactory factory = new DiskFileItemFactory();
	        // Create a new file upload handler
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        
	        // Parse the request
		    try {
		        List<FileItem> items = upload.parseRequest(request);
		        Iterator<FileItem> iterator = items.iterator();
		        while (iterator.hasNext()) {		        	
		            FileItem item = (FileItem) iterator.next();
		            
		            // If item parameter is an upload file
		            if (!item.isFormField() && item.getFieldName().equals(row_id)) {
		            	
		            	// Get name of the parameter
		                String fileName = item.getName(); 
		                
	                	//  set the name of the file
	                	fileLinked.setFileName(fileName);
	                	
		                // Gets absolute path of the web application
				        String appPath = request.getServletContext().getRealPath("");
		                File path = new File(appPath + File.separator + SAVE_DIR);
		                if (!path.exists()) {
		                    boolean status = path.mkdirs();
		                }
		                
		                // Constructs path of the directory to save uploaded file
		                String savePath = appPath + File.separator + SAVE_DIR + File.separator+ fileName;
		                
		                // set fileLinked path
		                fileLinked.setFilePath(savePath);
		                //System.out.println(savePath);
		                
		                // Save file on server side
		                File uploadedFile = new File(savePath);
		                item.write(uploadedFile);
		                
		            }
		            
		            // Other regular form (checkbox, radio...)
		            else if(item.isFormField()) {
		            	
		                String name = item.getFieldName();
		                String value = item.getString();
		                
		                // check the id of the button download
		                if(name.equals("row_id") && value!=null){
		                	//System.out.println("Row_id :"+value);
		                	row_id=value;
		                }
		                else if(name.equals("selected_item_dc") && value.equals("dc")){
		                	//System.out.println("dc :"+value);
		                	fileLinked.setDc(true);
		                }
		                else if(name.equals("selected_item_tei") && value.equals("tei")){
		                	//System.out.println("tei :"+value);
		                	fileLinked.setTei(true);
		                }
		                else if(name.equals("selected_item_mpeg7-vd") && value.equals("mpeg7-vd")){
		                	//System.out.println("Mpeg7 :"+value);
		                	fileLinked.setMpeg7Vd(true);
		                }
		                else if(name.equals("selected_item_mpeg7-caliph") && value.equals("mpeg7-caliph")){
		                	//System.out.println("Mpeg7 :"+value);
		                	fileLinked.setMpeg7Caliph(true);
		                }
		                else if(name.equals("selected_item_linkedmdr") && value.equals("linkedmdr")){
		                	//System.out.println("linkedmdr :"+value);
		                	fileLinked.setLinkedmdr(true);
		                }
                	     
		            }
		        }
		    } catch (FileUploadException e) {
		        e.printStackTrace();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	    }
	
	    /**Tranformations**/
     	
     	// DC
     	if (fileLinked.getDc()!=null && fileLinked.getDc()==true){
	     	DublinCore dc1 = new DublinCore();
			try {
				dc1.doDc(request,response,fileLinked.getFilePath(), fileLinked.getFileName());
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     	}
     	
 		// TEI
     	if (fileLinked.getTei()!=null && fileLinked.getTei()==true ){
	     	Tei tei1 = new Tei();
			tei1.doTei(request,response,fileLinked.getFilePath());
     	}
     	
     	// MPEG7-Vd
     	if (fileLinked.getMpeg7Vd()!=null && fileLinked.getMpeg7Vd()==true){
	     	Mpeg7_Vd mpeg7 = new Mpeg7_Vd();
			mpeg7.doMPEG7(request,response,fileLinked.getFilePath(), fileLinked.getFileName());
     	}
     	
     	// MPEG7-Caliph
     	if (fileLinked.getMpeg7Caliph()!=null && fileLinked.getMpeg7Caliph()==true){
	     	Mpeg7_Caliph mpeg7 = new Mpeg7_Caliph();
			mpeg7.doMPEG7(request,response,fileLinked.getFilePath(), fileLinked.getFileName());
     	}

	  }
}

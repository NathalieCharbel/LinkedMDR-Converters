package com.prototype.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

import com.prototype.linkedmdr.LinkedMdr;
import com.prototype.standards.DublinCore;
import com.prototype.standards.Mpeg7_Vd;
import com.prototype.standards.Tei;
import com.prototype.upload.FileLinked;

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
		
		// Instantiate a file list
		ArrayList<FileLinked> fileList= new ArrayList<FileLinked>(); 	
		
        // Gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
     	
		/** Upload part**/
		// Check if we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	    if (isMultipart) {	
	    	
	    	// Create a factory for file items
	        FileItemFactory factory = new DiskFileItemFactory();
	        // Create a new file upload handler
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        
	        // Parse the request
		    try {
		        List<FileItem> items = upload.parseRequest(request);
		        Iterator<FileItem> iterator = items.iterator();
		        int uniqueFileId=0;
		        while (iterator.hasNext()) {		        	
		            FileItem item = (FileItem) iterator.next();
		            
		            // If item parameter is an upload file
		            if (!item.isFormField()) {
		            	
		            	// Instantiate a fileLinked which represents the uploaded file
			         	FileLinked fileLinked=new FileLinked();
		            	
		            	// Get name of the parameter
		                String fileName = item.getName(); 
		                
	                	// Set the name of the file
	                	fileLinked.setFileName(fileName);
	                	System.out.println("File name : "+fileName);
	                	
	                	// Set unique id (link with the checkboxes)
	                	fileLinked.setFileId(uniqueFileId);
	                	System.out.println("File id :"+uniqueFileId);
	                	
	                	// Create directory uploads
		                File path = new File(appPath + File.separator + SAVE_DIR);
		                if (!path.exists()) {
		                    boolean status = path.mkdirs();
		                }
		                
		                // Constructs path of the directory to save uploaded file
		                String savePath = appPath + File.separator + SAVE_DIR + File.separator+ fileName;
		                System.out.println("Save path : "+savePath);
		                
		                // set fileLinked path
		                fileLinked.setFilePath(savePath);
		                //System.out.println(savePath);
		                
		                // Save file on server side
		                File uploadedFile = new File(savePath);
		                item.write(uploadedFile);
		                
		                // Add the file on the list
		                fileList.add(fileLinked);
		                
		                // Incremente unique id
		                uniqueFileId++;
		                
		            }
		            
		            // Other regular form (checkbox, radio...)
		            else if(item.isFormField()) {
		            	
		                String name = item.getFieldName();
		                String value = item.getString();
		                
		                //System.out.println("Name: "+name);
		                //System.out.println("Value: "+value);

		                if(name.equals("dc")){
		                	//System.out.println("dc :"+value);
		                	// check the id
		                	int valueDC= Integer.parseInt(value);           	
		                	// check on the list the file id and set dc to true
		                	fileList.get(valueDC).setDc(true);
		                }
		                else if(name.equals("tei")){
		                	//System.out.println("tei :"+value);
		                	int valueTEI= Integer.parseInt(value);
		                	// check on the list the file id and set dc to true
		                	fileList.get(valueTEI).setTei(true);
		                }
		                else if(name.equals("mpeg7")){
		                	//System.out.println("Mpeg7 :"+value);
		                	int valueMpeg7= Integer.parseInt(value);
		                	// check on the list the file id and set dc to true
		                	fileList.get(valueMpeg7).setMpeg7Vd(true);
		                }
		                else if(name.equals("linkedmdr")){
		                	//System.out.println("linkedmdr :"+value);
		                	int valueLinkedmdr= Integer.parseInt(value);
		                	// check on the list the file id and set dc to true
		                	fileList.get(valueLinkedmdr).setLinkedmdr(true);
		                }
                	     
		            }
		        }
		    } catch (FileUploadException e) {
		        e.printStackTrace();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	    }
	
	    /**Tranformations standards**/
	    
	    // Create a zip which contains the files
	    
	    byte[] bufferZip = new byte[1024];
		FileOutputStream fos = new FileOutputStream(appPath + File.separator + SAVE_DIR + File.separator+"standards.zip");
		String zipPath=appPath + File.separator + SAVE_DIR + File.separator+"standards.zip";
		ZipOutputStream zos = new ZipOutputStream(fos);
		
	    // loop through the list of file and transform the files with standards or linkedmdr
	    for(FileLinked fileLinked: fileList) {
	    	// DC
		    if (fileLinked.getDc()!=null && fileLinked.getDc()==true){
		     	DublinCore dc1 = new DublinCore();
				try {				
					// do Dc (modify the dublin core path of the file)
					dc1.doDc(request,fileLinked);
					
					// now we can add the file to the zip if the path is not null
					if(fileLinked.getFilePathDC()!=null){
					    
				        String newFileName=fileLinked.getFileName();
				        // Delete the extension of the file
				        if (newFileName.indexOf(".") > 0){
				        	newFileName = newFileName.substring(0, newFileName.lastIndexOf("."));
						}
						
						ZipEntry ze= new ZipEntry(newFileName+"_dublincore.xml");
						zos.putNextEntry(ze);
						
						//System.out.println("path dublin core:"+fileLinked.getFilePathDC());
						FileInputStream in = new FileInputStream(fileLinked.getFilePathDC());
						// Write the file on the zip
						int len;
						while ((len = in.read(bufferZip)) > 0) {
							zos.write(bufferZip, 0, len);
						}
	
						in.close();
						zos.closeEntry();
					}
							
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	     	}
	     	
	 		// TEI
	     	if (fileLinked.getTei()!=null && fileLinked.getTei()==true ){
		     	Tei tei1 = new Tei();
		     	// do TEi (modify the tei path of the file)
				tei1.doTei(request,fileLinked);
				
				// now we can add the file to the zip if the path is not null
				if(fileLinked.getFilePathTei()!=null){
				    
				    String newFileName=fileLinked.getFileName();
				    // Delete the extension of the file
				    if (newFileName.indexOf(".") > 0){
				    	newFileName = newFileName.substring(0, newFileName.lastIndexOf("."));
					}
					
					ZipEntry ze= new ZipEntry(newFileName+"_tei.xml");
					zos.putNextEntry(ze);
					
					//System.out.println("path tei:"+fileLinked.getFilePathTei());
					FileInputStream in = new FileInputStream(fileLinked.getFilePathTei());
					// Write the file on the zip
					int len;
					while ((len = in.read(bufferZip)) > 0) {
						zos.write(bufferZip, 0, len);
					}

					in.close();
					zos.closeEntry();
				}
	     	}
	     	
	     	// MPEG7-Vd
	     	if (fileLinked.getMpeg7Vd()!=null && fileLinked.getMpeg7Vd()==true){
		     	Mpeg7_Vd mpeg7 = new Mpeg7_Vd();
				mpeg7.doMPEG7(request,fileLinked);
				
				// now we can add the file to the zip if the path is not null
				//System.out.println("path mpeg7:"+fileLinked.getFilePathMpeg7Vd());
				if(fileLinked.getFilePathMpeg7Vd()!=null){
				    
				    String newFileName=fileLinked.getFileName();
				    // Delete the extension of the file
				    if (newFileName.indexOf(".") > 0){
				    	newFileName = newFileName.substring(0, newFileName.lastIndexOf("."));
					}
					
					ZipEntry ze= new ZipEntry(newFileName+"_mpeg7.xml");
					zos.putNextEntry(ze);
					
					//System.out.println("path mpeg7:"+fileLinked.getFilePathMpeg7Vd());
					FileInputStream in = new FileInputStream(fileLinked.getFilePathMpeg7Vd());
					// Write the file on the zip
					int len;
					while ((len = in.read(bufferZip)) > 0) {
						zos.write(bufferZip, 0, len);
					}

					in.close();
					zos.closeEntry();
				}
	     	}
	     	
	     	/**Tranformation LinkedMDR**/
	        // LinkedMDR
	     	if (fileLinked.getLinkedmdr()!=null && fileLinked.getLinkedmdr()==true){
		     	LinkedMdr linkedmdr = new LinkedMdr();
		
				try {
					linkedmdr.doLinkedMDR(request,fileLinked);
					// now we can add the file to the zip if the path is not null
					if(fileLinked.getFilePathLinkedMDR()!=null){
					    
					    String newFileName=fileLinked.getFileName();
					    // Delete the extension of the file
					    if (newFileName.indexOf(".") > 0){
					    	newFileName = newFileName.substring(0, newFileName.lastIndexOf("."));
						}
						
						ZipEntry ze= new ZipEntry(newFileName+"linkedmdr.xml");
						zos.putNextEntry(ze);
						
						//System.out.println("path linkedmdr:"+fileLinked.getFilePathLinkedMDR());
						FileInputStream in = new FileInputStream(fileLinked.getFilePathLinkedMDR());
						// Write the file on the zip
						int len;
						while ((len = in.read(bufferZip)) > 0) {
							zos.write(bufferZip, 0, len);
						}

						in.close();
						zos.closeEntry();
					}
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	     	}
	   }
	    
		// Close the zip
		zos.close();
		
		/** Servlet response to the client (force the download)**/
		response.setContentType("application/force-download");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Disposition","attachment; filename=\"standards.zip");
		
		// Create the attached file
		File f= new File(zipPath);
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
		
	  } 
}

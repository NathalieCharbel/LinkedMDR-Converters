package com.linkedmdr.mpeg7;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Mpeg7_Vd {
	
	    // Name of the Directory which contains the transformed file
	 	private static final String SAVE_DIR = "uploads";
		
	 	/** Execute the mpeg-7 transformation based on Mpeg-7 visual descriptors**/
		public void doMPEG7(HttpServletRequest request, HttpServletResponse response, String pathFile, String fileName) {
			
			try {
			    // Constructs path of the directory to save the transformed file
		        String appPath = request.getServletContext().getRealPath("");
		        // Delete the extension of the file
		        if (fileName.indexOf(".") > 0){
		            fileName = fileName.substring(0, fileName.lastIndexOf("."));
				}
		        // Path of the transformed file
	            String savePath = appPath + File.separator + SAVE_DIR + File.separator+fileName+"_mpeg7.xml";
	            //System.out.println("savePath"+savePath);
	            
				// Run mpeg7 visual descriptors
				Process process = new ProcessBuilder(appPath+"/MPEG7/vde.exe","-d","SC","DC","HT","EH", "-i",pathFile, "-o",savePath).start();
				
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

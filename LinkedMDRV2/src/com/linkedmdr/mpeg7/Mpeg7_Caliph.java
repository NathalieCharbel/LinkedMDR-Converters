package com.linkedmdr.mpeg7;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Mpeg7_Caliph {
	
	    // Name of the Directory which contains the transformed file
	 	private static final String SAVE_DIR = "uploads";
		
	 	/** Execute the mpeg-7 transformation based on Mpeg-7 visual descriptors**/
		public void doMPEG7(HttpServletRequest request, HttpServletResponse response, String pathFile, String fileName) {
			
		    // Constructs path of the directory to save the transformed file
	        String appPath = request.getServletContext().getRealPath("");
	        
			// Run mpeg7 caliph
			try {
				Process process = new ProcessBuilder(appPath+"/CaliphEmir_v0.9.27/runCaliph.bat").start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}

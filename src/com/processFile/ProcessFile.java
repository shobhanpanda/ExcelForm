package com.processFile;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;



@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100)  
public class ProcessFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 private static final String UPLOAD_DIR = "uploads";
     
	    protected void doPost(HttpServletRequest request,
	            HttpServletResponse response) throws ServletException, IOException {
	        // gets absolute path of the web application
	        String applicationPath = request.getServletContext().getRealPath("");
	        // constructs path of the directory to save uploaded file
	        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
	         
	        // creates the save directory if it does not exists
	        File fileSaveDir = new File(uploadFilePath);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdirs();
	        }
	        System.out.println("Upload File Directory="+fileSaveDir.getAbsolutePath());
	        
	        String fileName = null;
	        //Get all the parts from request and write it to the file on server
	        for (Part part : request.getParts()) {
	            fileName = getFileName(part);
	            part.write(uploadFilePath + File.separator + fileName);
	        }
	        System.out.println(fileName + " File uploaded successfully!");
	        String filePath=fileSaveDir.getCanonicalPath()+File.separator+fileName ;
	        System.out.println(filePath);
	        request.setAttribute("filePath",filePath );
	        RequestDispatcher rd=request.getRequestDispatcher("formatFile");
	        rd.forward(request, response);
	    }
	 
	    /**
	     * Utility method to get file name from HTTP header content-disposition
	     */
	    private String getFileName(Part part) {
	        String contentDisp = part.getHeader("content-disposition");
	        System.out.println("content-disposition header= "+contentDisp);
	        String[] tokens = contentDisp.split(";");
	        for (String token : tokens) {
	            if (token.trim().startsWith("filename")) {
	                return token.substring(token.indexOf("=") + 2, token.length()-1);
	            }
	        }
	        return "";
	    }

}

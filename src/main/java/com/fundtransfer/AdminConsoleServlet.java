
package com.fundtransfer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;
import org.json.JSONObject;

import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class AdminConsoleServlet
 * This class used to read the Credentials(X-APIKey and SharedSecret) from Admin
 * Console page
 */
@WebServlet("/AdminConsoleServlet")
public class AdminConsoleServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;
	static String absoluteFilePath = "";
	BufferedWriter output ;
	/**
	 * @see HttpServlet#HttpServlet()
	 *//*
	public AdminConsoleServlet() {
		super();
	}
*/
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException,
	        IOException {
		
		System.out.println("AdminConsoleServlet Start>>>");
		String apiKey = request.getParameter("apiKey");
		String sharedSecret = request.getParameter("sharedSecret");
		JSONObject outputJson = new JSONObject();
		PrintWriter out = response.getWriter();
		//HttpSession session = null;
		HttpSession session = request.getSession();
		String your_os = System.getProperty("os.name").toLowerCase();
		
		String workingDir = System.getProperty("java.io.tmpdir");
		System.out.println("workingDir: " +workingDir);
		if (your_os.indexOf("win") >= 0) {
			 System.out.println("Windows");
			//if windows
			absoluteFilePath = workingDir + "\\wsiSampleAppPoccertificateTemp" + "\\";
 
		} else if (your_os.indexOf("nix") >= 0 || 
                your_os.indexOf("nux") >= 0 || 
                your_os.indexOf("mac") >= 0) {
				System.out.println("MAC");
			//if unix or mac 
			absoluteFilePath = workingDir + "/wsiSampleAppPoccertificateTemp" + "/";

		}
		{
			Enumeration paramaterNames = request.getParameterNames();
			while(paramaterNames.hasMoreElements() ) {
			       System.out.println(paramaterNames.nextElement());
			} 
			
			String fileName="";
			if (ServletFileUpload.isMultipartContent(request))
			{
				final FileItemFactory factory = new DiskFileItemFactory();
	    		final ServletFileUpload upload = new ServletFileUpload(factory);
	    		File destinationDir = new File(absoluteFilePath);
	    		deleteDirectory(destinationDir);
	    		/*if (destinationDir.exists()) {
	    			destinationDir.delete();
	    			System.out.println("wsiSampleAppPoccertificateTemp Deleted");
	    		}*/
	    		destinationDir.mkdir();
	    		System.out.println("wsiSampleAppPoccertificateTemp Created");
	    		//String password = request.getParameter("password");
				File file = new File(absoluteFilePath + "password.txt");
				  output = new BufferedWriter(new FileWriter(file));
		            output.write("password");
	    		List items = null;
				try {
					items = upload.parseRequest(request);
					
					Iterator iter = items.iterator();
					while (iter.hasNext()) {
					    FileItem item = (FileItem) iter.next();

					    if (item.isFormField()) {

					      String name = item.getFieldName();//text1
					      String value = item.getString();
					      FileWriter fw = new FileWriter(file.getAbsoluteFile());
							BufferedWriter bw = new BufferedWriter(fw);
							bw.write(value);
							bw.close();

					    } else {
					    	item.write(file);
					    }
					}
				} catch (FileUploadException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		final Iterator itr = items.iterator();
				while(itr.hasNext())
				 {
					  FileItem item = (FileItem) itr.next();
					 if(item.isFormField())
					 {
						 continue;
					 }
					 else
					 {
					        fileName = item.getName();
					        //This fix is for IE since IE returns the absolute location of the file in the system.
					        final int index = fileName.lastIndexOf('\\');
					        fileName = fileName.substring(index+1);
	                        final File targetFile = new File(destinationDir,fileName);
	                       // targetFile.getParentFile().mkdirs();
	                        if (!targetFile.exists()){
	                        	targetFile.createNewFile();
	                        } 
	                         System.out.println("Target file "+destinationDir+"  "+fileName);
	                         try {
								item.write(targetFile);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
					 }
				 }
			}	
		}
		
		if (apiKey != null && sharedSecret != null) {
			session = request.getSession();
			System.out.println("Putting new apikey, ss in session>>"+apiKey);
			session.setAttribute("apiKey", apiKey);
			session.setAttribute("sharedSecret", sharedSecret);
		}
		try {
			outputJson.put("apiKey", session.getAttribute("apiKey"));
			outputJson.put("sharedSecret", session.getAttribute("sharedSecret"));
			response.setContentType("application/json");
			out.print(outputJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		output.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException,
	        IOException {
	}
	static public boolean deleteDirectory(File path) {
	    if (path.exists()) {
	        File[] files = path.listFiles();
	        for (int i = 0; i < files.length; i++) {
	            if (files[i].isDirectory()) {
	                deleteDirectory(files[i]);
	            } else {
	                files[i].delete();
	            }
	        }
	    }
	    return (path.delete());
	}
	public static String getAbsolutePatOfFolder(){
		
		return absoluteFilePath;
		
	}

}

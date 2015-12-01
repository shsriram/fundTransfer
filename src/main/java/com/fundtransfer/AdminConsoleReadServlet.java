
package com.fundtransfer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.fundtransfer.config.ConfigValues;

/**
 * Servlet implementation class AdminConsoleServlet
 * This class used to read the Credentials(X-APIKey and SharedSecret) from Admin
 * Console page
 */
@WebServlet("/AdminConsoleReadServlet")
public class AdminConsoleReadServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;
	String certName;
	String password1;
	String pass;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminConsoleReadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException,
	        IOException {
		System.out.println("AdminConsoleReadServlet Start>>>");
		HttpSession session = request.getSession();
		String apiKey = (String) session.getAttribute("apiKey");
		String sharedSecret = (String) session
		        .getAttribute("sharedSecret");
		JSONObject outputJson = new JSONObject();
		PrintWriter out = response.getWriter();
		System.out.println("session get apiKey:"+apiKey);
		System.out.println("session get sharedSecret:"+sharedSecret);
		String workingDir1 = System.getProperty("user.dir");
		String absoluteFilePath1 = workingDir1 + "\\wsiSampleAppPoccertificateTemp" + "\\";
		List<String> results = new ArrayList<String>();
		File fi = new File(absoluteFilePath1);
		if(fi.exists()){
			File[] files = new File(absoluteFilePath1).listFiles();
			//If this pathname does not denote a directory, then listFiles() returns null. 

			for (File file : files) {
				System.out.println("????"+files.length);
				if(files.length == 2){
			    if (file.isFile()) {
			        results.add(file.getName());
			        if(!file.getName().contains("password")){
			        	 certName = file.getName();
			        	System.out.println(certName);
			        }else if(file.getName().contains("password")){
			        	String sCurrentLine;
			        	password1=file.getName();
			        	BufferedReader	br = new BufferedReader(new FileReader(absoluteFilePath1+password1));

						while ((sCurrentLine = br.readLine()) != null) {
							pass = sCurrentLine;
						}
			        }
			    }}else{
			    	pass = "password";
			    }
			}
		}
		if (apiKey == null || sharedSecret == null) {
			apiKey = (String) new ConfigValues().getPropValues().get(
			        "apiKey");
			sharedSecret = (String) new ConfigValues()
			        .getPropValues().get("sharedSecret");
		}if(certName == null && pass == null){
			//outputJson.put("cert", certName);
			pass = "password";
		}
		try {
			outputJson.put("apiKey", apiKey);
			outputJson.put("sharedSecret", sharedSecret);
			outputJson.put("password", pass);
			response.setContentType("application/json");
			out.print(outputJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException,
	        IOException {
	}

}
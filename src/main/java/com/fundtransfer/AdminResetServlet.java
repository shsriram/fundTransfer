package com.fundtransfer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class AdminResetServlet This class used to reset the
 * admin console window to default Credentials(X-APIKey and SharedSecret)
 */
@WebServlet("/AdminResetServlet")
public class AdminResetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
String certName;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminResetServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("AdminResetServlet Start>>>");
		String path = AdminConsoleServlet.getAbsolutePatOfFolder();
		
		HttpSession session = request.getSession();		
		JSONObject outputJson = new JSONObject();
		PrintWriter out = response.getWriter();
		String apiKey = (String) new ConfigValues().getPropValues().get("apiKey");
		String sharedSecret = (String) new ConfigValues().getPropValues().get(
				"sharedSecret");
		session.setAttribute("apiKey", apiKey);
		session.setAttribute("sharedSecret", sharedSecret);
		session.setAttribute(certName, "password");
		String filePath = TestServlet.getPath();
		File fi = new File(filePath);
		deleteDirectory(fi);
		try {
			outputJson.put("apiKey", apiKey);
			outputJson.put("sharedSecret", sharedSecret);
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
			HttpServletResponse response) throws ServletException, IOException {
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
}
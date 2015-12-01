
package com.fundtransfer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.fundtransfer.config.ConfigValues;
import com.fundtransfer.util.XpayTokenGenerator;
import com.fundtransfer.util.FundTransferUtility;

/**
 * Servlet implementation class ActionServlet
 * This class makes the AccountFundingTransaction API call sends reponse to
 * client in JSON pretty print format
 */

public class OCTResponseServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;
	String apiKey = null;
	String sharedSecret = null;
	public OCTResponseServlet() {
	}

	protected void doGet(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException,
	        IOException {
		System.out.println("OCTResponseServlet Start>>>");

		String payload = "";
		String senderPAN = null;
		String recipientPAN = null;
		String newpayload = "";
		String endpoint = "";
		String token = "";
		String res = "";
		HttpSession session = request.getSession();
		
		
		apiKey = (String) session.getAttribute("apiKey");

		if (apiKey == null) {
			apiKey = (String) new ConfigValues().getPropValues().get(
			        "apiKey");
		}
		sharedSecret = (String) session.getAttribute("sharedSecret");
		if (sharedSecret == null) {
			sharedSecret = (String) new ConfigValues()
			        .getPropValues().get("sharedSecret");
		}
		try {
			payload = (String) new ConfigValues().getPropValues()
			        .get("payloadOCT");
			JSONObject jsonObject = new JSONObject(payload);
			jsonObject.put("amount", request.getParameter("amount"));
			senderPAN = (String) session.getAttribute("senderPAN");
			recipientPAN = (String) session
			        .getAttribute("recipientPAN");
			if (senderPAN != null) {
				jsonObject.put("senderAccountNumber", senderPAN);
			}
			if (recipientPAN != null) {
				jsonObject.put("recipientPrimaryAccountNumber",
				        recipientPAN);
			}
			RestWebServiceClient client = new RestWebServiceClient();
			newpayload = jsonObject.toString();
			endpoint = (String) new ConfigValues().getPropValues()
			        .get("urlOCT");// + "?apikey=" + apiKey;
			res = client.getResponse(request,newpayload, endpoint, getBasicAuthenticationEncoding());
			if (res.startsWith("{")) {
				res = FundTransferUtility.convertToPrettyJsonstring(res);
			}
			JSONObject outputJson = new JSONObject();
			PrintWriter out = response.getWriter();
			outputJson.put("response", res);
			outputJson.put("token", token);
			response.setContentType("application/json");
			out.print(outputJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException,
	        IOException {
	}
	public String getBasicAuthenticationEncoding() {
	      /*  String apikey = "PY54O8PERSPA3B5607LP21Jh7j4azMrBoqS5Jk0Dzj2T9a0oQ";
			String password = "9eGUiSr9NNT5n8szsX8rWC8BwA395ziFXQd";*/
	        String userPassword = apiKey + ":" + sharedSecret;
	        return new String(Base64.encodeBase64(userPassword.getBytes()));
	    }
}

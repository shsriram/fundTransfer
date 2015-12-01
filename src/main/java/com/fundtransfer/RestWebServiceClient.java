
package com.fundtransfer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

/*
 * This class used to make API calls and get response from servlet
 */

public class RestWebServiceClient {

	public String getResponse(HttpServletRequest request, String payload, String endpoint, String BasicAuth) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException{
		HttpsURLConnection conn = null;
		OutputStream os;
		BufferedReader br = null;
		InputStream is;
		String output;
		String op = "";
		URL url = null;
		url = new URL(endpoint);
		String fileName = null;
		String certificateName = null;
		String password = null;
		String absoluteFilePath = "";
		String workingDir = System.getProperty("java.io.tmpdir");
		String workingDirJs = System.getProperty("user.dir");
		String sCurrentLine;
		String your_os = System.getProperty("os.name").toLowerCase();
		
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
		
		File destinationDir = new File(absoluteFilePath);
		
		if(destinationDir.exists() && destinationDir.listFiles().length==2){
			for (final File fileEntry : destinationDir.listFiles()) {
		        if (fileEntry.isDirectory()) {
		            
		        } else {
		            System.out.println(fileEntry.getName());
		            fileName =  fileEntry.getName();
		        }	if(!fileName.equals("password.txt")){
		        	certificateName = fileName;
		      }
		    }
			if(fileName.equals("password.txt")){
				br = new BufferedReader(new FileReader(absoluteFilePath+"password.txt"));
				 
				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
					password = sCurrentLine;
				}br.close();
	 
			}
			System.setProperty("javax.net.ssl.keyStore",(absoluteFilePath+certificateName));
			System.setProperty("javax.net.ssl.keyStorePassword", password);
		/*	System.setProperty("javax.net.ssl.trustStore", getClass()
	                .getClassLoader().getResource("VDP.jks")
	                .getFile());
	        System.setProperty("javax.net.ssl.trustStorePassword", "tcserver");*/
		}else{
			/*InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/clientkeystore.jks");
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
		 
		    File targetFile = new File(workingDir);
		    OutputStream outStream = new FileOutputStream(targetFile);
		    outStream.write(buffer);*/
			
			//InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/clientkeystore.jks");
		/*	
			System.setProperty("javax.net.ssl.keyStore",System.getenv().get("-Djavax.net.ssl.keyStore"));
			System.setProperty("javax.net.ssl.keyStorePassword", System.getenv().get("-Djavax.net.ssl.keyStorePassword"));*/
			
			//File tempFileName = File.createTempFile("resource-", ".jks");
			
			//Files.copy(inputStream, tempFileName.getAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
			//FileInputStream input = new FileInputStream(tempFileName.toFile());
			//String workingTempDir = System.getProperty("java.io.tmpdir");
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/clientkeystore.jks");
			final File tempFile = File.createTempFile("resource-", ".jks");
			System.out.println("Before Free Space : " + tempFile.getFreeSpace());
			
	        FileOutputStream out = new FileOutputStream(tempFile);
	            IOUtils.copy(inputStream, out);
	            System.out.println("file copied from inputstream to outputstream");
	        
	        
			System.out.println("tempFile : " + tempFile.getAbsolutePath());
			System.out.println("After Free Space : " + tempFile.getFreeSpace());
			System.setProperty("javax.net.ssl.keyStore",tempFile.getAbsolutePath());
			System.out.println(" keyStore Loaded");
			System.setProperty("javax.net.ssl.keyStorePassword", "password");
			
			
			//System.out.println("!!!!!!!!!!" +getStringFromInputStream(inputStream));
			
			/*URL urls = getClass().getClassLoader().getResource("/myfile.ext");
			System.out.println("urls.getPath()"+urls.getPath());*/
			
		//	String strapc = RestWebServiceClient.class.getClassLoader().getResource("/clientkeystore.jks").getPath();
		//	System.out.println("strapc"+strapc);
			
		/*	System.setProperty("javax.net.ssl.keyStore",("C:\\Users\\akadarma\\Documents\\FundTransferWSI\\src\\main\\resources\\clientkeystore.jks")) ;
			System.setProperty("javax.net.ssl.keyStorePassword", "password");*/
			
			/*InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/clientkeystore.jks");
			 KeyStore keystore = KeyStore.getInstance("JKS");
			    keystore.load(inputStream, "password".toCharArray());*/
			    /*String path = request.getServletContext().getRealPath("WEB-INF/classes/clientkeystore.jks");
			    System.out.println("*************path *********"+path);*/

			/*ServletContext sc = null;
			String path="/classes/clientkeystore.jks";
			String realPath = sc.getRealPath(path);
			System.out.println("realPath : "+realPath);*/
			
			
		}
		
		
        

		/*System.setProperty("javax.net.ssl.trustStore", getClass()
                .getClassLoader().getResource("VDP.jks")
                .getFile());
        System.setProperty("javax.net.ssl.trustStorePassword", "tcserver");
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true"); */
        
		/*String userid = "TWBLR843PFYF5HWUCCM121oepuZKI93oR2FIWcJZEyN1ixPak";
		String password = "j7J6LcL";*/
        
       /* String userid = "PY54O8PERSPA3B5607LP21Jh7j4azMrBoqS5Jk0Dzj2T9a0oQ";
		String password = "9eGUiSr9NNT5n8szsX8rWC8BwA395ziFXQd";*/
        
		
		

		conn = (HttpsURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		//conn.setRequestProperty  ("Authorization", "Basic " +userid+":"+password);
		conn.setRequestProperty("Authorization", "Basic "+BasicAuth);
		conn.setRequestProperty("Pragma", " akamai-x-flush-log,akamai-x-cache-on,akamai-x-get-cache-key,akamai-x-check-cacheable,akamai-x-serial-no,akamai-x-get-extracted-values,akamai-x-get-true-cache-key,akamai-x-get-request-id,akamai-x-cache-remote-on");
		conn.setRequestProperty("Cookie", "inovant_akpragma_allowed=hW8zk7a3bhQB");
		
		//conn.setRequestProperty  ("Authorization", "Basic" +"UFk1NE84UEVSU1BBM0I1NjA3TFAyMUpoN2o0YXpNckJvcVM1SmswRHpqMlQ5YTBvUTo5ZUdVaVNyOU5OVDVuOHN6c1g4cldDOEJ3QTM5NXppRlhRZA==");
		//Authorization: Basic UFk1NE84UEVSU1BBM0I1NjA3TFAyMUpoN2o0YXpNckJvcVM1SmswRHpqMlQ5YTBvUTo5ZUdVaVNyOU5OVDVuOHN6c1g4cldDOEJ3QTM5NXppRlhRZA==
		
		os = conn.getOutputStream();
		os.write(payload.getBytes());
		os.flush();
		System.out.println(conn.getResponseCode());
		System.out.println("Header Fields: " +conn.getHeaderFields());
		System.out.println(conn.getResponseCode());
		System.out.println(conn.getResponseMessage());
		if (conn.getResponseCode() >= 400) {
			is = conn.getErrorStream();			
		} else {
			is = conn.getInputStream();
		}
		br = new BufferedReader(new InputStreamReader(is));
		while ((output = br.readLine()) != null) {
			op += output;
		}
		conn.disconnect();
		return op;
	}
	
	/*private String getBasicAuthenticationEncoding() {
        String userid = "PY54O8PERSPA3B5607LP21Jh7j4azMrBoqS5Jk0Dzj2T9a0oQ";
		String password = "9eGUiSr9NNT5n8szsX8rWC8BwA395ziFXQd";
        String userPassword = userid + ":" + password;
        return new String(Base64.encodeBase64(userPassword.getBytes()));
    }*/
	
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			System.out.println("br.toString : "+br.toString());
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
}

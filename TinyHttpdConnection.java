/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.Socket;

/**
 *
 * @author benhur
 */
class TinyHttpdConnection extends Thread {

    Socket sock;

    TinyHttpdConnection(Socket s) {
        sock = s;
        setPriority(NORM_PRIORITY - 1);
        start();
    }

    public void run() {
        System.out.println("=========");
        OutputStream out = null;
        try {
            out = sock.getOutputStream();
            DataInputStream d = new DataInputStream(
                    sock.getInputStream());
            String req = d.readLine();
	    String operation = "";
	    String vals = "";
	    String outpt = "";
            System.out.println("Request: " + req);
            StringTokenizer st = new StringTokenizer(req);
            if ((st.countTokens() >= 2) && st.nextToken().equals("GET")) {

                if ((req = st.nextToken()).startsWith("/process/")) {

                    if(req.contains("?")&&req.contains("&")){

		    operation = req.substring(req.lastIndexOf("/")+1, req.indexOf("?"));
		    vals = req.substring(req.indexOf("?")+1);		    
                    outpt=exec(operation, vals);
			}
		else outpt="Invalid syntax";
                }
		else outpt = "unable to compute";
                    try {
//                        
			out.write(outpt.getBytes());
                    } catch (Exception e) {
                        new PrintStream(out).println("404 Unable to compute");
                        System.out.println("404: " + outpt);

                    }
                    
            } else {
                    new PrintStream(out).println("400 Bad Request");
                    System.out.println("400 Bad Request: " + outpt);
                    sock.close();
                
            }
        } catch (IOException e) {
            System.out.println("Generic I/O error " + e);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                System.out.println("I/O error on close" + ex);
            }
        }
    }
    
    public static String exec(String cmd, String vls) {	

      String linez = null;
    try {
	String line;

      String command = "bash add.sh "+cmd+" "+vls;

      Process p = Runtime.getRuntime().exec(command);

      BufferedReader bri = new BufferedReader
        (new InputStreamReader(p.getInputStream()));
      BufferedReader bre = new BufferedReader
        (new InputStreamReader(p.getErrorStream()));
      while ((line = bri.readLine()) != null) {
        linez =line;
      }
      bri.close();
      while ((line = bre.readLine()) != null) {
        linez =line;
      }
      bre.close();
      p.waitFor();
     // System.out.println("Done.");
    }
    catch (Exception err) {
      err.printStackTrace();
      linez = "unable to compute";
    }
    return linez;
  }
    
}

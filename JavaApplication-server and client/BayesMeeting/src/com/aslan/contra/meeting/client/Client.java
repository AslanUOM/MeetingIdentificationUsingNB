package com.aslan.contra.meeting.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	private static Socket socket;

	public static void main(String args[]) {
		try {
			String host = "localhost";
			int port = 25000;
			InetAddress address = InetAddress.getByName(host);
			socket = new Socket(address, port);

			// Send the message to the server
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);

			// String dataValue = "2.35,1.0,1.0,10.56";
			String dataValue = "219.8523,1000,0,44.963";

			// String sendMessage = number + "\n";
			String sendMessageNew = dataValue + "\n";

			bw.write(sendMessageNew);
			bw.flush();
			System.out
					.println("Message sent to the server : " + sendMessageNew);

			// Get the return message from the server
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String message = br.readLine();
			System.out.println("Message received from the server : " + message);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			// Closing the socket
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

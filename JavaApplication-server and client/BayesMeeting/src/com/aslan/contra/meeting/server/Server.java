package com.aslan.contra.meeting.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import com.aslan.contra.meeting.server.NaiveBayesClassifier.NaiveBayesClassifier;

public class Server {

	private static Socket socket;

	public static void main(String[] args) {
		try {

			int port = 25000;
			ServerSocket serverSocket = new ServerSocket(port);
			System.out
					.println("Server Started and listening to the port 25000");

			// Server is running always. This is done using this while(true)
			// loop
			while (true) {
				// Reading the message from the client
				socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String dataValue = br.readLine();
				System.out.println("Message received from client is "
						+ dataValue);

				//forming the return message
				String returnMessage = "";
				try {

					// NaiveBayesClassifier object
					NaiveBayesClassifier obj = new NaiveBayesClassifier();

					// locations for training and test data set
					String trainingCsvFile = "PATH_TO_TRAINIG_DATA_SET";
					// String testCsvFile =
					// "PATH_TO_TEST_DATA_SET";

					ArrayList<double[]> trainingDataset = obj
							.loadCsv(trainingCsvFile);
					// ArrayList<double[]> testDataset =
					// obj.loadCsv(testCsvFile);
					ArrayList<double[]> testDataset = new ArrayList<double[]>();
					// make sure 4 is number of columns in test data set
					String[] dataValueArrayString = dataValue.split(",");
					double[] dataValueArray = new double[4];

					for (int i = 0; i < dataValueArray.length; i++) {
						dataValueArray[i] = Double
								.parseDouble(dataValueArrayString[i]);
					}
					testDataset.add(dataValueArray);

					// prepare model
					Map<Double, ArrayList<double[]>> summaries = obj
							.summarizeByClass(trainingDataset);

					// test model
					ArrayList<String> predictions = obj.getPredictions(
							summaries, testDataset);

					for (int i = 0; i < predictions.size(); i++) {
						String x = predictions.get(i);
						returnMessage += "Prediction: " + x + " __ ";
					}

				} catch (Exception e) {
					
				}

				// Sending the response back to the client.
				OutputStream os = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os);
				BufferedWriter bw = new BufferedWriter(osw);
				bw.write(returnMessage);
				System.out.println("Message sent to the client is "
						+ returnMessage);
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
			}
		}
	}
}

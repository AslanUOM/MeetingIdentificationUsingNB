package com.aslan.contra.meeting.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
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

				// if the message from developer
				String[] tempString = dataValue.split(",");
				String mesString = tempString[0];
				int userID = Integer.parseInt(tempString[1]);

				// forming the return message
				String returnMessage = "";

				if (mesString.equals("IsUserInMeeting")) {

					String url = "jdbc:mysql://localhost:3306/";
					String dbName = "meeting";
					String driver = "com.mysql.jdbc.Driver";
					String userName = "root";
					String password = "mysql";

					Class.forName(driver).newInstance();
					Connection conn = DriverManager.getConnection(url + dbName,
							userName, password);

					try {
						Class.forName(driver).newInstance();
						conn = DriverManager.getConnection(url + dbName,
								userName, password);
						Statement st = conn.createStatement();
						ResultSet res = st
								.executeQuery("SELECT * FROM information where userID="
										+ userID + " ");
						boolean msg = false;
						while (res.next()) {
							int id = res.getInt("userID");
							msg = res.getBoolean("isMeeting");
							System.out.println(id + "\t" + msg);
						}
						if (msg) {
							returnMessage = "User is in a Meeting ! :) taken by database";
						} else {
							returnMessage = "User is not in a Meeting :/ taken by database";
						}
					} catch (Exception e) {

					} finally {
						System.out.println("from db" + returnMessage);
						conn.close();
					}

					System.out.println("database access is successful");
				}
				// to data set access
				else {

					try {

						// NaiveBayesClassifier object
						NaiveBayesClassifier obj = new NaiveBayesClassifier();

						// locations for training and test data set
						String trainingCsvFile = "resources/datasets/training/trainingData.csv";

						String tra = new java.io.File("").getAbsolutePath();
						// String testCsvFile =
						// "PATH_TO_TEST_DATA_SET";

						ArrayList<double[]> trainingDataset = obj
								.loadCsv(trainingCsvFile);
						// ArrayList<double[]> testDataset =
						// obj.loadCsv(testCsvFile);
						ArrayList<double[]> testDataset = new ArrayList<double[]>();
						// make sure 4 is number of columns in test data set
						String[] dataValueArrayString = dataValue.split(",");
						double[] dataValueArray = new double[6];

						for (int i = 0; i < dataValueArray.length; i++) {
							dataValueArray[i] = Double
									.parseDouble(dataValueArrayString[i]);
						}

						double Light = dataValueArray[0];
						double SoundLevel = dataValueArray[3];
						double LinearAcceleration = dataValueArray[4];

						// get userID
						int uID = (int) dataValueArray[5];

						boolean Light_M = false;
						boolean SoundLevel_M = false;
						boolean LinearAcceleration_M = false;

						if (100 < Light && Light < 300)
							Light_M = true;
						if (40 < SoundLevel && SoundLevel < 70)
							SoundLevel_M = true;
						if (0.0 <= LinearAcceleration && LinearAcceleration < 2)
							LinearAcceleration_M = true;

						// test set ignore linear acceleration
						double[] dataValueArrayForTest = new double[4];
						for (int i = 0; i < dataValueArray.length - 2; i++) {
							dataValueArrayForTest[i] = dataValueArray[i];
						}

						testDataset.add(dataValueArrayForTest);

						// prepare model
						Map<Double, ArrayList<double[]>> summaries = obj
								.summarizeByClass(trainingDataset);

						// test model
						ArrayList<String> predictions = obj.getPredictions(
								summaries, testDataset);

						// for (int i = 0; i < predictions.size(); i++) {
						String x = predictions.get(0);
						// returnMessage += "Prediction: " + x + " __ ";
						// }

						int isInMeetingNB = Integer.parseInt(x);
						boolean isMeetingNB = false;

						if (isInMeetingNB == 1)
							isMeetingNB = true;

						boolean isMeeting = false;

						// ABC = (Light_M && SoundLevel_M &&
						// LinearAcceleration_M)
						// AB!CD= (Light_M && SoundLevel_M &&
						// !LinearAcceleration_M
						// && isMeetingNB)
						// A!BCD= (Light_M && !SoundLevel_M &&
						// LinearAcceleration_M
						// && isMeetingNB)
						// !ABCD= (!Light_M && SoundLevel_M &&
						// LinearAcceleration_M
						// && isMeetingNB)

						if ((Light_M && SoundLevel_M && LinearAcceleration_M)
								|| (Light_M && SoundLevel_M
										&& !LinearAcceleration_M && isMeetingNB)
								|| (Light_M && !SoundLevel_M
										&& LinearAcceleration_M && isMeetingNB)
								|| (!Light_M && SoundLevel_M
										&& LinearAcceleration_M && isMeetingNB))
							isMeeting = true;

						if (isMeeting) {
							returnMessage = "User is in a Meeting ! :)";

							String url = "jdbc:mysql://localhost:3306/";
							String dbName = "meeting";
							String driver = "com.mysql.jdbc.Driver";
							String userName = "root";
							String password = "mysql";

							Class.forName(driver).newInstance();
							Connection conn = DriverManager.getConnection(url
									+ dbName, userName, password);

							try {
								Class.forName(driver).newInstance();
								conn = DriverManager.getConnection(
										url + dbName, userName, password);
								Statement st = conn.createStatement();
								int res = st
										.executeUpdate("UPDATE information SET isMeeting = "
												+ 1
												+ " WHERE userID = "
												+ uID
												+ " ");

							} catch (Exception e) {

							} finally {
								System.out.println("from db" + returnMessage);
								conn.close();
							}

							System.out.println("database updated successfully");

						} else {
							returnMessage = "User is not in a Meeting :/";

							String url = "jdbc:mysql://localhost:3306/";
							String dbName = "meeting";
							String driver = "com.mysql.jdbc.Driver";
							String userName = "root";
							String password = "mysql";

							Class.forName(driver).newInstance();
							Connection conn = DriverManager.getConnection(url
									+ dbName, userName, password);

							try {
								Class.forName(driver).newInstance();
								conn = DriverManager.getConnection(
										url + dbName, userName, password);
								Statement st = conn.createStatement();
								int res = st
										.executeUpdate("UPDATE information SET isMeeting = "
												+ 0
												+ " WHERE userID = "
												+ uID
												+ " ");

							} catch (Exception e) {

							} finally {
								System.out.println("from db" + returnMessage);
								conn.close();
							}

							System.out.println("database updated successfully");
						}

					} catch (Exception e) {

					}

					// this is where the upper else is ending
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

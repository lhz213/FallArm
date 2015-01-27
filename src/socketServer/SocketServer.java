package socketServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import webServer.data.Contact;
import webServer.data.Patient;
import webServer.data.Record;
import webServer.db.DbUnit;
import webServer.exceptions.FallArmDbFailure;

public class SocketServer extends Thread {
	private final static int DEFAULT_PORT = 7000;
	private final static int NUMBER_OF_THREADS = 10;
	private final static int FALL = 1;
	private final static int MAYBE = 2;
	private final static int NORMAL = 3;
	private final static int SUCCEED = 6;
	private final static int FAILED = 7;
	private final static int UNKNOW = 9;

	ServerSocket theServer;

	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
		}
		if (port <= 0 || port >= 65536)
			port = DEFAULT_PORT;
		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Server Socket Start!!");
			for (int i = 0; i < NUMBER_OF_THREADS; i++) {
				System.out.println("Create num_threads " + i + " Port:" + port);
				SocketServer pes = new SocketServer(ss);
				pes.start();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public SocketServer(ServerSocket ss) {
		theServer = ss;
	}

	public void run() {
		while (true) {
			try {
				boolean login;
				int result;
				DataOutputStream output;
				DataInputStream input;
				Socket connection = theServer.accept();
				input = new DataInputStream(connection.getInputStream());
				output = new DataOutputStream(connection.getOutputStream());
				String inputStr;
				// System.out.println("Accept Client!");
				// System.out.println("Client Connected and Start get I/O!!");
				while (true) {
					inputStr = input.readUTF();
					// System.out.println("Client ==> " + inputStr);
					if (inputStr.substring(0, 4).equals("TEST")) {
						System.out.println("iPhone!");
						// Test for connection
						output.writeUTF("Welcome");
						System.out.println("Server ==> Connection successful");
					} else if (inputStr.substring(0, 4).equals("DATA")) {
						// Passing the data
						result = processData(inputStr.substring(5));
						output.writeUTF("OK");

						// state machine
						// result should be a four digital number,
						// Thousands means FALL, MAYBE, or NORMAL
						// Hundreds means store into database SUCCEED or FAILED
						// Tens means send email to contact SUCCEED or FAILED
						// Units means send SMS to contact SUCCEED or FAILED

						for (int i = 0; i < 4; i++) {
							int state = (int) (result
									/ (int) Math.pow(10.0, (double) 3
											- (double) i) % 10);
							// System.out.println(state);
							switch (state) {
							case SocketServer.FALL:
								System.out.print("Server ==> Fall");
								break;
							case SocketServer.MAYBE:
								System.out.print("Server ==> Maybe");
								break;
							case SocketServer.NORMAL:
								i = 10;
								break;
							case SocketServer.SUCCEED:
								System.out.print(" Succeed");
								break;
							case SocketServer.FAILED:
								System.out.print(" Failed");
								break;
							case SocketServer.UNKNOW:
								i = 10;
								System.out.println("Server ==> Record Error");
								break;
							default:
								i = 10;
								System.out.println("Server ==> Unknow issue");
							}
							if (i == 3)
								System.out.println("\n");
						}
					} else if (inputStr.substring(0, 4).equals("SIGN")) {
						// Patient login
						login = login(input.readUTF().substring(4));
						if (!login) {
							System.out.println("Server ==> Login failed!");
							output.writeUTF("User name or passowrd incorrect");
						} else {
							System.out.println("Server ==> Login successed!");
							output.writeUTF("Welcome back");
						}
					}
					output.writeUTF("Format incorrect!");
					output.flush();
				}
			} catch (IOException e) {
			}
		}
	}

	private int processData(String str) {
		Record record = Record.parseRecord(str);
		String stateStr = "";
		if (record == null) {
			return SocketServer.UNKNOW;
		}
		double x = Math.pow(record.getData()[Record.ACCELEROMETER_X_INDEX], 2);
		double y = Math.pow(record.getData()[Record.ACCELEROMETER_Y_INDEX], 2);
		double z = Math.pow(record.getData()[Record.ACCELEROMETER_Z_INDEX], 2);
		int level = (int) Math.sqrt(x + y + z);
		if (level < 4) {
			stateStr += SocketServer.FALL;
			if (storeRecord(record, Record.RECORD_TAG_FALL))
				stateStr += SocketServer.SUCCEED;
			else
				stateStr += SocketServer.FAILED;
			if (sendEmail(record, Record.RECORD_TAG_FALL))
				stateStr += SocketServer.SUCCEED;
			else
				stateStr += SocketServer.FAILED;
			if (sendSMS(record, Record.RECORD_TAG_FALL))
				stateStr += SocketServer.SUCCEED;
			else
				stateStr += SocketServer.FAILED;
		} else if (level < 7) {
			stateStr += SocketServer.MAYBE;
			if (storeRecord(record, Record.RECORD_TAG_MAYBE))
				stateStr += SocketServer.SUCCEED;
			else
				stateStr += SocketServer.FAILED;
			if (sendEmail(record, Record.RECORD_TAG_MAYBE))
				stateStr += SocketServer.SUCCEED;
			else
				stateStr += SocketServer.FAILED;
			if (sendSMS(record, Record.RECORD_TAG_MAYBE))
				stateStr += SocketServer.SUCCEED;
			else
				stateStr += SocketServer.FAILED;
		} else {
			stateStr = "" + SocketServer.NORMAL + SocketServer.FAILED
					+ SocketServer.FAILED + SocketServer.FAILED;
		}
		// Normal means 3777, NORMAIL, FAILED, FAILED, FAILED
		return Integer.parseInt(stateStr);
	}

	private boolean storeRecord(Record record, int state) {
		try {
			return DbUnit.insertNewRecordWithRetry(record, state);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
			return false;
		} catch (SQLException ex) {
			System.out.println("SQL Exception");
			ex.printStackTrace();
			return false;
		}
	}

	private boolean sendEmail(Record record, int state) {
		ArrayList<Contact> contactList = null;
		ArrayList<Patient> patientList = null;
		try {
			contactList = DbUnit.searchContact(
					DbUnit.SEARCH_CONTACT_BY_PATIENT_ID,
					"" + record.getPatientId());
			patientList = DbUnit.searchPatient(DbUnit.SEARCH_PATIENT_BY_ID, ""
					+ record.getPatientId());
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
			return false;
		}
		try {
			Iterator<Contact> it = contactList.iterator();
			String targetEmailAddress = "";
			String patientName = patientList.get(0).getFirstName() + " "
					+ patientList.get(0).getLastName();
			while (it.hasNext()) {
				targetEmailAddress += it.next().getEmailAddress() + ",";
			}
			targetEmailAddress = targetEmailAddress.substring(0,
					targetEmailAddress.lastIndexOf(','));
			System.out.println(targetEmailAddress);
			EmailSender.sendEamil(patientName, targetEmailAddress);

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private boolean sendSMS(Record record, int state) {
		ArrayList<Contact> contactList = null;
		ArrayList<Patient> patientList = null;
		try {
			contactList = DbUnit.searchContact(
					DbUnit.SEARCH_CONTACT_BY_PATIENT_ID,
					"" + record.getPatientId());
			patientList = DbUnit.searchPatient(DbUnit.SEARCH_PATIENT_BY_ID, ""
					+ record.getPatientId());
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
			return false;
		}
		try {
			Iterator<Contact> it = contactList.iterator();
			String targetEmailAddress = "";
			String patientName = patientList.get(0).getFirstName() + " "
					+ patientList.get(0).getLastName();
			while (it.hasNext()) {
				targetEmailAddress += it.next().getEmailAddress() + ",";
				SMSSender.sendSMS(patientName, targetEmailAddress);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private boolean login(String str) {
		String data[] = str.split(",");
		try {
			String userName = data[0];
			String password = data[1];
			if (DbUnit.loginWithPatient(userName, password) != null)
				return true;
			else
				return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}

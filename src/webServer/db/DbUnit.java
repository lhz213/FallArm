package webServer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;

import webServer.data.Contact;
import webServer.data.Nurse;
import webServer.data.Patient;
import webServer.data.Record;
import webServer.exceptions.FallArmDbFailure;

public class DbUnit {
	private static final int MAX_TIMES_TO_RETRY = 3;

	public static final int SEARCH_ALL_PATIENT = 0;
	public static final int SEARCH_PATIENT_BY_ID = 1;
	public static final int SEARCH_PATIENT_BY_EMAIL = 2;
	public static final int SEARCH_PATIENT_BY_NAME = 3;
	public static final int SEARCH_PATINET_BY_NURSE_ID = 4;

	public static final int SEARCH_ALL_RECORD = 10;
	public static final int SEARCH_RECORD_BY_ID = 11;
	public static final int SEARCH_RECORD_BY_PATIENT_ID = 12;
	public static final int SEARCH_RECORD_BY_PATIENT_EMAIL = 13;

	public static final int SEARCH_ALL_CONTACT = 20;
	public static final int SEARCH_CONTACT_BY_ID = 21;
	public static final int SEARCH_CONTACT_BY_EMAIL = 22;
	public static final int SEARCH_CONTACT_BY_PHONE = 23;
	public static final int SEARCH_CONTACT_BY_NAME = 24;
	public static final int SEARCH_CONTACT_BY_PATIENT_ID = 25;
	public static final int SEARCH_CONTACT_BY_PATIENT_EMAIL = 26;

	public static final int SEARCH_ALL_NURSE = 30;
	public static final int SEARCH_NURSE_BY_ID = 31;
	public static final int SEARCH_NURSE_BY_EMAIL = 32;
	public static final int SEARCH_NURSE_BY_NAME = 33;

	private static int searchPatientIdByEmail(String emailAddress)
			throws FallArmDbFailure, SQLException {
		int patientId;
		String selectPatientIdByEmailSql = "SELECT patient_id FROM patient WHERE email_address = ? ;";
		Connection dbConn = DataSourceConf.getConnection();
		PreparedStatement selectPatientIdByEmailStmt = dbConn
				.prepareStatement(selectPatientIdByEmailSql);
		selectPatientIdByEmailStmt.setString(1, emailAddress);
		ResultSet result = null;
		try {
			result = selectPatientIdByEmailStmt.executeQuery();
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.NO_PATIENT);
			}
			patientId = result.getInt("patient_id");
		} finally {
			result.close();
			selectPatientIdByEmailStmt.close();
			dbConn.close();
		}
		return patientId;
	}

	private static ArrayList<Patient> searchPatientBy(String searchPatientSql)
			throws SQLException, FallArmDbFailure {
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		Patient patient = null;
		int id;
		String emailAddress;
		String firstName;
		String lastName;
		Calendar birthdate = Calendar.getInstance();
		int gender;
		String homeAddress;
		int nurseId;
		Connection dbConn = DataSourceConf.getConnection();
		Statement selectPatientStmt = dbConn.createStatement();
		ResultSet result = null;
		try {
			result = selectPatientStmt.executeQuery(searchPatientSql);
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.PATIENT_TABLE_EMPTY);
			}
			do {
				id = result.getInt("patient_id");
				emailAddress = result.getString("email_Address");
				firstName = result.getString("first_name");
				lastName = result.getString("last_name");
				birthdate.setTime(result.getDate("birthdate"));
				gender = result.getInt("gender");
				homeAddress = result.getString("home_address");
				nurseId = result.getInt("related_nurse_id");

				patient = new Patient(id, emailAddress, firstName, lastName,
						birthdate, gender, homeAddress, nurseId);

				patientList.add(patient);
			} while (result.next());
		} finally {
			result.close();
			selectPatientStmt.close();
			dbConn.close();
		}
		return patientList;
	}

	public static ArrayList<Patient> searchPatient(int type, String keyword)
			throws SQLException, FallArmDbFailure {
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		String searchPatientSql = "SELECT * FROM patient";
		switch (type) {
		case SEARCH_ALL_PATIENT:
			break;
		case SEARCH_PATIENT_BY_ID:
			try {
				int id = Integer.parseInt(keyword);
				searchPatientSql += " WHERE patient_id = " + id;
			} catch (NumberFormatException ex) {
				System.out.println("Patient ID format incorrect");
				return null;
			}
			break;
		case SEARCH_PATIENT_BY_EMAIL:
			String email = keyword;
			searchPatientSql += " WHERE email_address = \'" + email + "\'";
			break;
		case SEARCH_PATIENT_BY_NAME:
			String name[] = keyword.split(" ");
			searchPatientSql += " WHERE first_name = \'";
			if (name.length == 2) {
				searchPatientSql += name[0] + "\' AND last_name = \'" + name[1];
			} else if (name.length == 1) {
				searchPatientSql += name[0] + "\' OR last_name = \'" + name[0];
			} else if (name.length > 2) {
				searchPatientSql += name[0] + "\' OR last_name = \'" + name[0];
				for (int i = 1; i < name.length; i++) {
					searchPatientSql += "\' OR first_name = \'" + name[i]
							+ "\' OR last_name = \'" + name[i];
				}
			} else {
				return null;
			}
			searchPatientSql += "\'";
			break;
		case SEARCH_PATINET_BY_NURSE_ID:
			try {
				int id = Integer.parseInt(keyword);
				searchPatientSql += " WHERE related_nurse_id = " + id;
			} catch (NumberFormatException ex) {
				System.out.println("Nurse ID format incorrect");
				return null;
			}
			break;
		default:
			System.out.println("Search keywords error!");
			return null;
		}
		searchPatientSql += " ORDER BY patient_id;";
		try {
			patientList = searchPatientBy(searchPatientSql);
			return patientList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static int searchTotalNumberOfPatient() throws SQLException,
			FallArmDbFailure {
		int total = 0;
		String totalNumberOfPatientSql = "SELECT COUNT(*) FROM patient;";
		Connection dbConn = DataSourceConf.getConnection();
		Statement totalNumberOfPatientStmt = dbConn.createStatement();
		ResultSet result = null;
		try {
			result = totalNumberOfPatientStmt
					.executeQuery(totalNumberOfPatientSql);
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.PATIENT_TABLE_EMPTY);
			}
			total = result.getInt(1);
		} finally {
			result.close();
			totalNumberOfPatientStmt.close();
			dbConn.close();
		}
		return total;
	}

	public static int searchRelatedNumberOfPatient(int nurseId)
			throws SQLException, FallArmDbFailure {
		int related = 0;
		String relatedNumberOfPatientSql = "SELECT COUNT(*) FROM patient WHERE related_nurse_id = "
				+ nurseId + ";";
		Connection dbConn = DataSourceConf.getConnection();
		Statement relatedNumberOfPatientStmt = dbConn.createStatement();
		ResultSet result = null;
		try {
			result = relatedNumberOfPatientStmt
					.executeQuery(relatedNumberOfPatientSql);
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.PATIENT_TABLE_EMPTY);
			}
			related = result.getInt(1);
		} finally {
			result.close();
			relatedNumberOfPatientStmt.close();
			dbConn.close();
		}
		return related;
	}

	private static void doInsertNewPatient(String emailAddress,
			String password, String firstName, String lastName,
			String birthdateStr, String genderStr, String homeAddress,
			String relatedNuresIdStr, Connection dbConn)
			throws FallArmDbFailure, SQLException {
		int rowsAffected;
		int gender;
		int relateNurseId;
		try {
			gender = Integer.parseInt(genderStr);
			relateNurseId = Integer.parseInt(relatedNuresIdStr);
		} catch (Exception ex) {
			System.out.println("Bad Gender or Bad related nurse ID!");
			return;
		}
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date utilDate;
		try {
			utilDate = bartDateFormat.parse(birthdateStr);
		} catch (ParseException e) {
			throw new FallArmDbFailure(FallArmDbFailure.DATE_FORMAT);
		}
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		String insertNewPatientSql = "INSERT INTO patient (email_address, password, first_name, last_name, birthdate, gender, home_address, related_nurse_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement insertNewPatientStmt = dbConn
				.prepareStatement(insertNewPatientSql);
		insertNewPatientStmt.setString(1, emailAddress);
		insertNewPatientStmt.setString(2, password);
		insertNewPatientStmt.setString(3, firstName);
		insertNewPatientStmt.setString(4, lastName);
		insertNewPatientStmt.setDate(5, sqlDate);
		insertNewPatientStmt.setInt(6, gender);
		insertNewPatientStmt.setString(7, homeAddress);
		insertNewPatientStmt.setInt(8, relateNurseId);
		try {
			rowsAffected = insertNewPatientStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			insertNewPatientStmt.close();
		}
	}

	private static void insertNewPatient(String emailAddress, String password,
			String firstName, String lastName, String birthdateStr,
			String genderStr, String homeAddress, String relatedNuresIdStr)
			throws FallArmDbFailure, SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doInsertNewPatient(emailAddress, password, firstName, lastName,
						birthdateStr, genderStr, homeAddress,
						relatedNuresIdStr, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean insertNewPatientWithRetry(String emailAddress,
			String password, String firstName, String lastName,
			String birthdateStr, String genderStr, String homeAddress,
			String relatedNuresIdStr) throws FallArmDbFailure, SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				insertNewPatient(emailAddress, password, firstName, lastName,
						birthdateStr, genderStr, homeAddress, relatedNuresIdStr);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	private static void doUpdatePatientInfo(int patientId,
			Hashtable<String, String> args, Connection dbConn)
			throws FallArmDbFailure, SQLException {
		int rowsAffected;
		Patient patient = DbUnit.searchPatient(DbUnit.SEARCH_PATIENT_BY_ID,
				"" + patientId).get(0);

		String updatePatientInfoSql = "UPDATE patient SET";
		for (String key : args.keySet()) {
			if (key.equals("gender") || key.equals("nurseId")) {
				updatePatientInfoSql += " \'" + key + "\' = " + args.get(key)
						+ ",";
			} else {
				updatePatientInfoSql += " \'" + key + "\' = \'" + args.get(key)
						+ "\',";
			}
		}
		updatePatientInfoSql += updatePatientInfoSql.substring(0,
				updatePatientInfoSql.lastIndexOf(','));
		updatePatientInfoSql += " WHERE patient_id = ? AND email_address = ? AND related_nurse_id = ?;";
		PreparedStatement updatePatientInfoStmt = dbConn
				.prepareStatement(updatePatientInfoSql);
		updatePatientInfoStmt.setInt(1, patientId);
		updatePatientInfoStmt.setString(2, patient.getEmailAddress());
		updatePatientInfoStmt.setInt(3, patient.getRelatedNurseId());
		try {
			rowsAffected = updatePatientInfoStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			updatePatientInfoStmt.close();
		}
	}

	private static void updatePatientInfo(int patientId,
			Hashtable<String, String> args) throws FallArmDbFailure,
			SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doUpdatePatientInfo(patientId, args, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean updatePatientInfoWithRetry(int patientId,
			Hashtable<String, String> args) throws FallArmDbFailure,
			SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				updatePatientInfo(patientId, args);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	private static void doDeletePatient(String idOrEmail, Connection dbConn)
			throws FallArmDbFailure, SQLException {
		int rowsAffected;
		String deletePatientSql = "DELETE FROM patient WHERE patient_id = "
				+ idOrEmail + " OR email_address = \'" + idOrEmail + "\';";
		PreparedStatement deletePatientStmt = dbConn
				.prepareStatement(deletePatientSql);
		try {
			rowsAffected = deletePatientStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			deletePatientStmt.close();
		}
	}

	private static void deletePatient(String idOrEmail)
			throws FallArmDbFailure, SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doDeletePatient(idOrEmail, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean deletePatientWithRetry(String idOrEmail)
			throws FallArmDbFailure, SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				deletePatient(idOrEmail);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	private static ArrayList<Nurse> searchNurseBy(String searchNurseSql)
			throws FallArmDbFailure, SQLException {
		ArrayList<Nurse> nurseList = new ArrayList<Nurse>();
		Nurse nurse;
		int nurseId;
		String emailAddress;
		String firstName;
		String lastName;
		int gender;
		Calendar birthdate = Calendar.getInstance();
		Connection dbConn = DataSourceConf.getConnection();
		Statement searchNurseStmt = dbConn.createStatement();
		ResultSet result = null;
		try {
			result = searchNurseStmt.executeQuery(searchNurseSql);
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.NURSE_TABLE_EMPTY);
			}
			do {
				nurseId = result.getInt("nurse_id");
				emailAddress = result.getString("email_Address");
				firstName = result.getString("first_name");
				lastName = result.getString("last_name");
				birthdate.setTime(result.getDate("birthdate"));
				gender = result.getInt("gender");

				nurse = new Nurse(nurseId, emailAddress, firstName, lastName,
						birthdate, gender);

				nurseList.add(nurse);
			} while (result.next());
		} finally {
			searchNurseStmt.close();
			result.close();
			dbConn.close();
		}
		return nurseList;
	}

	public static ArrayList<Nurse> searchNurse(int type, String keyword)
			throws SQLException, FallArmDbFailure {
		ArrayList<Nurse> nurseList = new ArrayList<Nurse>();
		String searchNurseSql = "SELECT * FROM nurse";
		switch (type) {
		case SEARCH_ALL_NURSE:
			break;
		case SEARCH_NURSE_BY_ID:
			try {
				int id = Integer.parseInt(keyword);
				searchNurseSql += " WHERE nurse_id = " + id;
			} catch (NumberFormatException ex) {
				System.out.println("Nurse ID format incorrect");
				return null;
			}
			break;
		case SEARCH_NURSE_BY_EMAIL:
			String email = keyword;
			searchNurseSql += " WHERE email_address = \'" + email + "\'";
			break;
		case SEARCH_NURSE_BY_NAME:
			String name[] = keyword.split(" ");
			searchNurseSql += " WHERE first_name = \'";
			if (name.length == 2) {
				searchNurseSql += name[0] + "\' AND last_name = \'" + name[1];
			} else if (name.length == 1) {
				searchNurseSql += name[0] + "\' OR last_name = \'" + name[0];
			} else if (name.length > 2) {
				searchNurseSql += name[0] + "\' OR last_name = \'" + name[0];
				for (int i = 1; i < name.length; i++) {
					searchNurseSql += "\' OR first_name = \'" + name[i]
							+ "\' OR last_name = \'" + name[i];
				}
			} else {
				return null;
			}
			searchNurseSql += "\'";
			break;
		default:
			return null;
		}
		searchNurseSql += " ORDER BY nurse_id;";
		try {
			nurseList = searchNurseBy(searchNurseSql);
			return nurseList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static void doInsertNewNurse(String emailAddress, String password,
			String firstName, String lastName, String birthdateStr,
			String genderStr, Connection dbConn) throws FallArmDbFailure,
			SQLException {
		int rowsAffected;
		int gender;
		try {
			gender = Integer.parseInt(genderStr);
		} catch (Exception ex) {
			System.out.println("Bad Gender!");
			return;
		}
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date utilDate;
		try {
			utilDate = bartDateFormat.parse(birthdateStr);
		} catch (ParseException e) {
			throw new FallArmDbFailure(FallArmDbFailure.DATE_FORMAT);
		}
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		String insertNewNurseSql = "INSERT INTO nurse (email_address, password, first_name, last_name, birthdate, gender) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement insertNewNurseStmt = dbConn
				.prepareStatement(insertNewNurseSql);
		insertNewNurseStmt.setString(1, emailAddress);
		insertNewNurseStmt.setString(2, password);
		insertNewNurseStmt.setString(3, firstName);
		insertNewNurseStmt.setString(4, lastName);
		insertNewNurseStmt.setDate(5, sqlDate);
		insertNewNurseStmt.setInt(6, gender);
		try {
			rowsAffected = insertNewNurseStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			insertNewNurseStmt.close();
		}
	}

	private static void insertNewNurse(String emailAddress, String password,
			String firstName, String lastName, String birthdateStr,
			String genderStr) throws FallArmDbFailure, SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doInsertNewNurse(emailAddress, password, firstName, lastName,
						birthdateStr, genderStr, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean insertNewNurseWithRetry(String emailAddress,
			String password, String firstName, String lastName,
			String birthdateStr, String genderStr) throws FallArmDbFailure,
			SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				insertNewNurse(emailAddress, password, firstName, lastName,
						birthdateStr, genderStr);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	private static void doUpdateNurseInfo(int nurseId,
			Hashtable<String, String> args, Connection dbConn)
			throws FallArmDbFailure, SQLException {
		int rowsAffected;
		Nurse nurse = DbUnit.searchNurse(DbUnit.SEARCH_NURSE_BY_ID,
				"" + nurseId).get(0);

		String updateNurseInfoSql = "UPDATE nurse SET";
		for (String key : args.keySet()) {
			if (key.equals("gender")) {
				updateNurseInfoSql += " \'" + key + "\' = " + args.get(key)
						+ ",";
			} else {
				updateNurseInfoSql += " \'" + key + "\' = \'" + args.get(key)
						+ "\',";
			}
		}
		updateNurseInfoSql += updateNurseInfoSql.substring(0,
				updateNurseInfoSql.lastIndexOf(','));
		updateNurseInfoSql += " WHERE nurse_id = ? AND email_address = ?;";
		PreparedStatement updateNurseInfoStmt = dbConn
				.prepareStatement(updateNurseInfoSql);
		updateNurseInfoStmt.setInt(1, nurseId);
		updateNurseInfoStmt.setString(2, nurse.getEmailAddress());
		try {
			rowsAffected = updateNurseInfoStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			updateNurseInfoStmt.close();
		}
	}

	private static void updateNurseInfo(int nurseId,
			Hashtable<String, String> args) throws FallArmDbFailure,
			SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doUpdateNurseInfo(nurseId, args, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean updateNurseInfoWithRetry(int nurseId,
			Hashtable<String, String> args) throws FallArmDbFailure,
			SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				updateNurseInfo(nurseId, args);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	private static void doDeleteNurse(String idOrEmail, Connection dbConn)
			throws FallArmDbFailure, SQLException {
		int rowsAffected;
		String deleteNurseSql = "DELETE FROM nurse WHERE nurse_id = "
				+ idOrEmail + " OR email_address = \'" + idOrEmail + "\';";
		PreparedStatement deleteNurseStmt = dbConn
				.prepareStatement(deleteNurseSql);
		try {
			rowsAffected = deleteNurseStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			deleteNurseStmt.close();
		}
	}

	private static void deleteNurse(String idOrEmail) throws FallArmDbFailure,
			SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doDeleteNurse(idOrEmail, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean deleteNurseWithRetry(String idOrEmail)
			throws FallArmDbFailure, SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				deleteNurse(idOrEmail);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	private static ArrayList<Contact> searchContactBy(String searchContactSql)
			throws FallArmDbFailure, SQLException {
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		Contact contact = null;
		int contactId;
		String emailAddress;
		String firstName;
		String lastName;
		int gender;
		int patientId;
		String phoneNumber;
		Connection dbConn = DataSourceConf.getConnection();
		Statement searchContactStmt = dbConn.createStatement();
		ResultSet result = null;
		try {
			result = searchContactStmt.executeQuery(searchContactSql);
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.CONTACT_TABLE_EMPTY);
			}
			do {
				contactId = result.getInt("contact_id");
				patientId = result.getInt("patient_id");
				emailAddress = result.getString("email_address");
				phoneNumber = result.getString("phone");
				firstName = result.getString("first_name");
				lastName = result.getString("last_name");
				gender = result.getInt("gender");

				contact = new Contact(contactId, patientId, emailAddress,
						phoneNumber, firstName, lastName, gender);

				contactList.add(contact);
			} while (result.next());
		} finally {
			searchContactStmt.close();
			result.close();
			dbConn.close();
		}
		return contactList;
	}

	public static ArrayList<Contact> searchContact(int type, String keyword)
			throws SQLException, FallArmDbFailure {
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		String searchContactSql = "SELECT * FROM emergency_contact_list";
		switch (type) {
		case SEARCH_ALL_CONTACT:
			break;
		case SEARCH_CONTACT_BY_ID:
			try {
				int id = Integer.parseInt(keyword);
				searchContactSql += " WHERE contact_id = " + id;
			} catch (NumberFormatException ex) {
				System.out.println("Contact ID format incorrect");
				return null;
			}
			break;
		case SEARCH_CONTACT_BY_EMAIL:
			String email = keyword;
			searchContactSql += " WHERE email_address = \'" + email + "\'";
			break;
		case SEARCH_CONTACT_BY_PHONE:
			String phoneNumber = keyword;
			searchContactSql += " WHERE phone =\'" + phoneNumber + "\'";
		case SEARCH_CONTACT_BY_NAME:
			String name[] = keyword.split(" ");
			searchContactSql += " WHERE first_name = \'";
			if (name.length == 2) {
				searchContactSql += name[0] + "\' AND last_name = \'" + name[1];
			} else if (name.length == 1) {
				searchContactSql += name[0] + "\' OR last_name = \'" + name[0];
			} else if (name.length > 2) {
				searchContactSql += name[0] + "\' OR last_name = \'" + name[0];
				for (int i = 1; i < name.length; i++) {
					searchContactSql += "\' OR first_name = \'" + name[i]
							+ "\' OR last_name = \'" + name[i];
				}
			} else {
				return null;
			}
			searchContactSql += "\'";
			break;
		case SEARCH_CONTACT_BY_PATIENT_ID:
			try {
				int id = Integer.parseInt(keyword);
				searchContactSql += " WHERE patient_id = " + id;
			} catch (NumberFormatException ex) {
				System.out.println("Patient ID format incorrect");
				return null;
			}
			break;
		case SEARCH_CONTACT_BY_PATIENT_EMAIL:
			try {
				int id = searchPatientIdByEmail(keyword);
				searchContactSql += " WHERE patient_id = " + id;
			} catch (FallArmDbFailure ex) {
				System.out.println(ex.getReasonStr());
				return null;
			}
			break;
		default:
			return null;
		}
		searchContactSql += " ORDER BY contact_id;";
		try {
			contactList = searchContactBy(searchContactSql);
			return contactList;
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static void doInsertNewContact(String patientIdStr,
			String emailAddress, String phoneNumberStr, String firstName,
			String lastName, String genderStr, Connection dbConn)
			throws FallArmDbFailure, SQLException {
		int rowsAffected;
		int patientId;
		int gender;
		try {
			patientId = Integer.parseInt(patientIdStr);
			gender = Integer.parseInt(genderStr);
		} catch (Exception ex) {
			System.out.println("Bad Gender!");
			return;
		}
		String insertNewContactSql = "INSERT INTO emergency_contact_list (patient_id, first_name, last_name, gender, email_address, phone) VALUES (?, ?, ?, ?, ?, ?);";
		PreparedStatement insertNewContactStmt = dbConn
				.prepareStatement(insertNewContactSql);
		insertNewContactStmt.setInt(1, patientId);
		insertNewContactStmt.setString(2, firstName);
		insertNewContactStmt.setString(3, lastName);
		insertNewContactStmt.setInt(4, gender);
		insertNewContactStmt.setString(5, emailAddress);
		insertNewContactStmt.setString(6, phoneNumberStr);
		try {
			rowsAffected = insertNewContactStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			insertNewContactStmt.close();
		}
	}

	private static void insertNewContact(String patientIdStr,
			String emailAddress, String phoneNumberStr, String firstName,
			String lastName, String genderStr) throws FallArmDbFailure,
			SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doInsertNewContact(patientIdStr, emailAddress, phoneNumberStr,
						firstName, lastName, genderStr, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean insertNewContactWithRetry(String patientIdStr,
			String emailAddress, String phoneNumberStr, String firstName,
			String lastName, String genderStr) throws FallArmDbFailure,
			SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				insertNewContact(patientIdStr, emailAddress, phoneNumberStr,
						firstName, lastName, genderStr);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	private static void doUpdateContactInfo(int contactId,
			Hashtable<String, String> args, Connection dbConn)
			throws FallArmDbFailure, SQLException {
		int rowsAffected;
		Contact contact = DbUnit.searchContact(DbUnit.SEARCH_CONTACT_BY_ID,
				"" + contactId).get(0);

		String updateContactInfoSql = "UPDATE emergency_contact_list SET";
		for (String key : args.keySet()) {
			if (key.equals("gender")) {
				updateContactInfoSql += " \'" + key + "\' = " + args.get(key)
						+ ",";
			} else {
				updateContactInfoSql += " \'" + key + "\' = \'" + args.get(key)
						+ "\',";
			}
		}
		updateContactInfoSql += updateContactInfoSql.substring(0,
				updateContactInfoSql.lastIndexOf(','));
		updateContactInfoSql += " WHERE contact_id = ? AND email_address = ? AND phone = ?;";
		PreparedStatement updateContactInfoStmt = dbConn
				.prepareStatement(updateContactInfoSql);
		updateContactInfoStmt.setInt(1, contactId);
		updateContactInfoStmt.setString(2, contact.getEmailAddress());
		updateContactInfoStmt.setString(3, contact.getPhoneNumber());
		try {
			rowsAffected = updateContactInfoStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			updateContactInfoStmt.close();
		}
	}

	private static void updateContactInfo(int contactId,
			Hashtable<String, String> args) throws FallArmDbFailure,
			SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doUpdateContactInfo(contactId, args, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean updateContactInfoWithRetry(int contactId,
			Hashtable<String, String> args) throws FallArmDbFailure,
			SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				updateContactInfo(contactId, args);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	private static void doDeleteContact(String contactId, Connection dbConn)
			throws FallArmDbFailure, SQLException {
		int rowsAffected;
		String deleteContactSql = "DELETE FROM emergency_contact_list WHERE contact_id = "
				+ contactId + ";";
		PreparedStatement deleteContactStmt = dbConn
				.prepareStatement(deleteContactSql);
		try {
			rowsAffected = deleteContactStmt.executeUpdate();
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			deleteContactStmt.close();
		}
	}

	private static void deleteContact(String contactId)
			throws FallArmDbFailure, SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doDeleteContact(contactId, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean deleteContactWithRetry(String contactId)
			throws FallArmDbFailure, SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				deleteContact(contactId);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	public static int searchTotalNumberOfContact(int patientId)
			throws FallArmDbFailure, SQLException {
		int total = 0;
		String searchTotalNumberOfContactByPatientSql = "SELECT COUNT(*) FROM emergency_contact_list WHERE patient_id = "
				+ patientId + ";";
		Connection dbConn = DataSourceConf.getConnection();
		PreparedStatement searchTotalNumberOfContactByPatientStmt = dbConn
				.prepareStatement(searchTotalNumberOfContactByPatientSql);
		ResultSet result = null;
		try {
			result = searchTotalNumberOfContactByPatientStmt.executeQuery();
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.NO_PATIENT);
			}
			total = result.getInt(1);
		} finally {
			searchTotalNumberOfContactByPatientStmt.close();
			result.close();
			dbConn.close();
		}
		return total;
	}

	private static ArrayList<Record> searchRecordBy(String searchRecordSql)
			throws SQLException, FallArmDbFailure {
		ArrayList<Record> recordList = new ArrayList<Record>();
		Record record = null;
		int recordId;
		int patientId;
		int state = -1;

		Connection dbConn = DataSourceConf.getConnection();
		Statement selectAllRecordStmt = dbConn.createStatement();
		ResultSet result = null;
		try {
			result = selectAllRecordStmt.executeQuery(searchRecordSql);
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.RECORD_TABLE_EMPTY);
			}
			do {
				double[] data = new double[8];
				Calendar datetime = Calendar.getInstance();
				recordId = result.getInt("record_id");
				patientId = result.getInt("patient_id");
				datetime.setTime(result.getTimestamp("datetime"));
				data[0] = result.getDouble("acc_x");
				data[1] = result.getDouble("acc_y");
				data[2] = result.getDouble("acc_z");
				data[3] = result.getDouble("gyr_x");
				data[4] = result.getDouble("gyr_y");
				data[5] = result.getDouble("gyr_z");
				data[6] = result.getDouble("longitude");
				data[7] = result.getDouble("latitude");
				state = result.getInt("state");

				record = new Record(recordId, patientId, datetime, data, state);

				recordList.add(record);
			} while (result.next());
		} finally {
			selectAllRecordStmt.close();
			result.close();
			dbConn.close();
		}
		return recordList;
	}

	public static ArrayList<Record> searchRecord(int type, String keyword)
			throws SQLException, FallArmDbFailure {
		ArrayList<Record> recordList = new ArrayList<Record>();
		String searchRecordSql = "SELECT * FROM fall_record";
		switch (type) {
		case SEARCH_ALL_RECORD:
			break;
		case SEARCH_RECORD_BY_ID:
			try {
				int id = Integer.parseInt(keyword);
				searchRecordSql += " WHERE record_id = " + id;
			} catch (NumberFormatException ex) {
				System.out.println("Record id format incorrect");
				return null;
			}
			break;
		case SEARCH_RECORD_BY_PATIENT_ID:
			try {
				int id = Integer.parseInt(keyword);
				searchRecordSql += " WHERE patient_id = " + id;
			} catch (NumberFormatException ex) {
				System.out.println("Patient id format incorrect");
				return null;
			}
			break;
		case SEARCH_RECORD_BY_PATIENT_EMAIL:
			try {
				int id = searchPatientIdByEmail(keyword);
				searchRecordSql += " WHERE patient_id = " + id;
			} catch (FallArmDbFailure ex) {
				System.out.println(ex.getReasonStr());
				return null;
			}
			break;
		default:
			return null;
		}
		searchRecordSql += " ORDER BY datetime DESC;";
		try {
			recordList = searchRecordBy(searchRecordSql);
			return recordList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static int searchTotalNumberOfRecordByPatient(int state,
			int patientId) throws SQLException, FallArmDbFailure {
		int total = 0;
		String searchTotalNumberOfFallByPatientSql = "SELECT COUNT(*) FROM fall_record WHERE patient_id = "
				+ patientId + " AND state =  " + state + ";";
		Connection dbConn = DataSourceConf.getConnection();
		PreparedStatement searchTotalNumberOfFallByPatientStmt = dbConn
				.prepareStatement(searchTotalNumberOfFallByPatientSql);
		ResultSet result = null;
		try {
			result = searchTotalNumberOfFallByPatientStmt.executeQuery();
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.NO_PATIENT);
			}
			total = result.getInt(1);
		} finally {
			searchTotalNumberOfFallByPatientStmt.close();
			result.close();
			dbConn.close();
		}
		return total;
	}

	public static int searchTotalNumberOfRecordByNurse(int state, int nurseId)
			throws SQLException, FallArmDbFailure {
		int total = 0;
		ArrayList<Patient> relatedPatientList = DbUnit.searchPatient(
				DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurseId);
		try {
			Iterator<Patient> it = relatedPatientList.iterator();
			while (it.hasNext()) {
				total += DbUnit.searchTotalNumberOfRecordByPatient(state, it
						.next().getId());
			}
		} catch (Exception ex) {
			System.out.println("Search total number of fall failed!");
		}
		return total;
	}

	private static void doInsertNewRecord(int patientId, double acc_x,
			double acc_y, double acc_z, double gyr_x, double gyr_y,
			double gyr_z, double longitude, double latitude, int state,
			Connection dbConn) throws FallArmDbFailure, SQLException {
		java.sql.Timestamp now = new java.sql.Timestamp(
				System.currentTimeMillis());
		int rowsAffected;
		String insertNewRecordSql = "INSERT INTO fall_record (patient_id, datetime, acc_x, acc_y, acc_z, gyr_x, gyr_y, gyr_z, longitude, latitude, state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement insertNewRecordStmt = dbConn
				.prepareStatement(insertNewRecordSql);
		insertNewRecordStmt.setInt(1, patientId);
		insertNewRecordStmt.setTimestamp(2, now);
		insertNewRecordStmt.setDouble(3, acc_x);
		insertNewRecordStmt.setDouble(4, acc_y);
		insertNewRecordStmt.setDouble(5, acc_z);
		insertNewRecordStmt.setDouble(6, gyr_x);
		insertNewRecordStmt.setDouble(7, gyr_y);
		insertNewRecordStmt.setDouble(8, gyr_z);
		insertNewRecordStmt.setDouble(9, longitude);
		insertNewRecordStmt.setDouble(10, latitude);
		insertNewRecordStmt.setInt(11, state);
		try {
			rowsAffected = insertNewRecordStmt.executeUpdate();
			System.out.println("" + rowsAffected);
			if (rowsAffected != 1) {
				if (rowsAffected == 0) {
					throw new FallArmDbFailure(FallArmDbFailure.RETRY);
				}
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						"Expected only one row to be affected by the deposit operation");
			}
		} catch (Exception ex) {
			dbConn.rollback();
		} finally {
			insertNewRecordStmt.close();
		}
	}

	private static void insertNewRecord(int patientId, double acc_x,
			double acc_y, double acc_z, double gyr_x, double gyr_y,
			double gyr_z, double longitude, double latitude, int state)
			throws FallArmDbFailure, SQLException {
		try (Connection dbConn = DataSourceConf.getConnection()) {
			dbConn.setAutoCommit(false);
			try {
				doInsertNewRecord(patientId, acc_x, acc_y, acc_z, gyr_x, gyr_y,
						gyr_z, longitude, latitude, state, dbConn);
				dbConn.commit();
			} catch (FallArmDbFailure ex) {
				dbConn.rollback();
				throw ex;
			} catch (Exception ex) {
				dbConn.rollback();
				throw new FallArmDbFailure(FallArmDbFailure.STMT_FAILED,
						ex.getMessage());
			} finally {
				dbConn.close();
			}
		}
	}

	public static boolean insertNewRecordWithRetry(int patientId, double acc_x,
			double acc_y, double acc_z, double gyr_x, double gyr_y,
			double gyr_z, double longitude, double latitude, int state)
			throws FallArmDbFailure, SQLException {
		int numAttempts = 0;
		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				insertNewRecord(patientId, acc_x, acc_y, acc_z, gyr_x, gyr_y,
						gyr_z, longitude, latitude, state);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	public static boolean insertNewRecordWithRetry(Record record, int state)
			throws FallArmDbFailure, SQLException {
		int numAttempts = 0;
		int patientId = record.getPatientId();
		double acc_x = record.getData()[Record.ACCELEROMETER_X_INDEX];
		double acc_y = record.getData()[Record.ACCELEROMETER_Y_INDEX];
		double acc_z = record.getData()[Record.ACCELEROMETER_Z_INDEX];
		double gyr_x = record.getData()[Record.GYROSCOPE_X_INDEX];
		double gyr_y = record.getData()[Record.GYROSCOPE_Y_INDEX];
		double gyr_z = record.getData()[Record.GYROSCOPE_Z_INDEX];
		double longitude = record.getData()[Record.LONGITUDE_INDEX];
		double latitude = record.getData()[Record.LATITUDE_INDEX];

		while (numAttempts < MAX_TIMES_TO_RETRY) {
			try {
				insertNewRecord(patientId, acc_x, acc_y, acc_z, gyr_x, gyr_y,
						gyr_z, longitude, latitude, state);
				return true;
			} catch (FallArmDbFailure dbFailureEx) {
				if (dbFailureEx.getFailureReason() != FallArmDbFailure.RETRY) {
					throw dbFailureEx;
				}
			}
			numAttempts++;
		}
		throw new FallArmDbFailure(
				FallArmDbFailure.RETRY_LIMIT_EXCEEDED,
				MAX_TIMES_TO_RETRY
						+ " attempts to perform the update were unsuccessful.  Aborting operation.");
	}

	public static Patient loginWithPatient(String userName, String password)
			throws FallArmDbFailure, SQLException {
		Patient patient = null;
		int patientId;
		String firstName;
		String lastName;
		int gender;
		String emailAddress;
		String homeAddress;
		Calendar birthdate = Calendar.getInstance();
		int nurseId;
		String patientLoginSql = "SELECT * FROM patient WHERE email_address = ? AND password = ?;";
		Connection dbConn = DataSourceConf.getConnection();
		PreparedStatement patientLoginStmt = dbConn
				.prepareStatement(patientLoginSql);
		patientLoginStmt.setString(1, userName);
		patientLoginStmt.setString(2, password);
		ResultSet result = null;
		try {
			result = patientLoginStmt.executeQuery();
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.NO_PATIENT);
			}
			patientId = result.getInt("patient_id");
			emailAddress = result.getString("email_Address");
			firstName = result.getString("first_name");
			lastName = result.getString("last_name");
			birthdate.setTime((java.util.Date) result.getDate("birthdate"));
			gender = result.getInt("gender");
			homeAddress = result.getString("home_address");
			nurseId = result.getInt("related_nurse_id");

			patient = new Patient(patientId, emailAddress, firstName, lastName,
					birthdate, gender, homeAddress, nurseId);
			System.out.println(patient);
		} finally {
			patientLoginStmt.close();
			result.close();
			dbConn.close();
		}
		return patient;
	}

	public static Nurse loginWithNurse(String userName, String password)
			throws FallArmDbFailure, SQLException {
		Nurse nurse = null;
		int id;
		String firstName;
		String lastName;
		int gender;
		String emailAddress;
		Calendar birthdate = Calendar.getInstance();
		String nurseLoginSql = "SELECT * FROM nurse WHERE email_address = ? AND password = ?;";
		Connection dbConn = DataSourceConf.getConnection();
		PreparedStatement nurseLoginStmt = dbConn
				.prepareStatement(nurseLoginSql);
		nurseLoginStmt.setString(1, userName);
		nurseLoginStmt.setString(2, password);
		ResultSet result = null;
		try {
			result = nurseLoginStmt.executeQuery();
			if (!result.next()) {
				throw new FallArmDbFailure(FallArmDbFailure.NO_NURSE);
			}
			id = result.getInt("nurse_id");
			firstName = result.getString("first_name");
			lastName = result.getString("last_name");
			gender = result.getInt("gender");
			emailAddress = result.getString("email_Address");
			birthdate.setTime(result.getDate("birthdate"));

			nurse = new Nurse(id, emailAddress, firstName, lastName, birthdate,
					gender);
		} finally {
			nurseLoginStmt.close();
			result.close();
			dbConn.close();
		}
		return nurse;
	}
}

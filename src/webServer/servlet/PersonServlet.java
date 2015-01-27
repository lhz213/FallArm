package webServer.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.http.HTTPException;

import webServer.data.Contact;
import webServer.data.Nurse;
import webServer.data.Patient;
import webServer.data.Record;
import webServer.db.DbUnit;
import webServer.exceptions.FallArmDbFailure;

public class PersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PERSONAL_INFO_URL = "/personal_info.jsp";
	private static final String NURSE_INDEX_URL = "/nurse_index.jsp";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws HTTPException, IOException, ServletException {
		ServletContext context = getServletContext();
		String role = request.getParameter("role");
		String delete = request.getParameter("operation");
		if (role.equals("patient")) {
			doOperateWithPatient(request);
		} else if (role.equals("nurse")) {
			doOperateWithNurse(request);
		} else if (role.equals("contact")) {
			doOperateWithContact(request);
		} else {
			System.out.println("Role cannot recognise in doGet() function.");
		}
		if (delete.equals("delete")) {
			RequestDispatcher dispatcher = context
					.getRequestDispatcher(NURSE_INDEX_URL);
			dispatcher.forward(request, response);
		} else {
			RequestDispatcher dispatcher = context
					.getRequestDispatcher(PERSONAL_INFO_URL);
			dispatcher.forward(request, response);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws HTTPException, IOException, ServletException {
		ServletContext context = getServletContext();
		String role = request.getParameter("role");
		if (role.equals("patient")) {
			doInsertPatient(request);
		} else if (role.equals("nurse")) {
			doInsertNurse(request);
		} else if (role.equals("contact")) {
			doInsertContact(request);
		} else if (role.equals("patientUpdate")) {
			doUpdatePatient(request);
		} else if (role.equals("nurseUpdate")) {
			doUpdateNurse(request);
		} else if (role.equals("contactUpdate")) {
			doUpdateContact(request);
		} else {
			System.out.println("Role cannot recognise in doPost() function.");
		}
		RequestDispatcher dispatcher = context
				.getRequestDispatcher(NURSE_INDEX_URL);
		dispatcher.forward(request, response);
	}

	private void doOperateWithPatient(HttpServletRequest request) {
		String operation = request.getParameter("operation");
		if (operation.equals("edit")) {
			doEditPatient(request);
		} else if (operation.equals("add")) {
			doAddPatient(request);
		} else if (operation.equals("search")) {
			doSearchPatient(request);
		} else if (operation.equals("delete")) {
			doDeletePatient(request);
		} else {
			System.out.println("OPERATION is wrong.");
		}
	}

	private void doEditPatient(HttpServletRequest request) {
		request.setAttribute("role", "patient");
		doSearchPatient(request);
		request.removeAttribute("operation");
		request.setAttribute("operation", "edit");
	}

	private void doAddPatient(HttpServletRequest request) {
		request.setAttribute("role", "patient");
		request.setAttribute("operation", "add");
	}

	private void doSearchPatient(HttpServletRequest request) {
		Patient patient = null;
		String id = request.getParameter("id");
		ArrayList<Record> recordList = null;
		ArrayList<Contact> contactList = null;
		try {
			patient = DbUnit.searchPatient(DbUnit.SEARCH_PATIENT_BY_ID, id)
					.get(0);
			recordList = DbUnit.searchRecord(
					DbUnit.SEARCH_RECORD_BY_PATIENT_ID, id);
			contactList = DbUnit.searchContact(
					DbUnit.SEARCH_CONTACT_BY_PATIENT_ID, id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		}
		request.setAttribute("role", "patient");
		request.setAttribute("operation", "search");
		request.setAttribute("patient", patient);
		request.setAttribute("recordList", recordList);
		request.setAttribute("contactList", contactList);
	}

	private void doInsertPatient(HttpServletRequest request) {
		String emailAddress = request.getParameter("email_address");
		String password = request.getParameter("password");
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String birthdateStr = request.getParameter("birthdate");
		String genderStr = request.getParameter("gender");
		String homeAddress = request.getParameter("home_address");
		String relatedNurseIdStr = request.getParameter("related_nurse_id");
		boolean flag = false;
		try {
			flag = DbUnit.insertNewPatientWithRetry(emailAddress, password,
					firstName, lastName, birthdateStr, genderStr, homeAddress,
					relatedNurseIdStr);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse) session.getAttribute("nurse");
		try {
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} catch (SQLException | FallArmDbFailure e) {
			e.printStackTrace();
		}
		request.setAttribute("info", "Insert New Patient ");
		request.setAttribute("role", "patient");
		request.setAttribute("operation", "insert");
		request.setAttribute("flag", flag);
	}

	private void doUpdatePatient(HttpServletRequest request) {
		String patientId = request.getParameter("id");
		String emailAddress = request.getParameter("email_address");
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String birthdateStr = request.getParameter("birthdate");
		String genderStr = request.getParameter("gender");
		String homeAddress = request.getParameter("home_address");
		String relatedNurseIdStr = request.getParameter("related_nurse_id");
		boolean flag = false;
		try {
			Patient patient = DbUnit.searchPatient(DbUnit.SEARCH_PATIENT_BY_ID,
					patientId).get(0);
			Hashtable<String, String> args = new Hashtable<String, String>();
			if (!(patient.getEmailAddress().equals(emailAddress)))
				args.put("email_address", emailAddress);
			if (!(patient.getFirstName().equals(firstName)))
				args.put("first_name", firstName);
			if (!(patient.getLastName().equals(lastName)))
				args.put("last_name", lastName);
			if (!(new SimpleDateFormat("yyyy-MM-dd").format(patient
					.getBirthdate().getTime()).equals(birthdateStr)))
				args.put("birthdate", birthdateStr);
			if (!((patient.getGender() + "")).equals(genderStr))
				args.put("gender", genderStr);
			if (!(patient.getHomeAddress().equals(homeAddress)))
				args.put("hone_address", homeAddress);
			if (!(patient.getRelatedNurseId() + "").equals(relatedNurseIdStr))
				args.put("related_nurse_id", relatedNurseIdStr);
			flag = DbUnit.updatePatientInfoWithRetry(
					Integer.parseInt(patientId), args);
		} catch (SQLException | FallArmDbFailure e) {
			System.out
					.println("Can not find patient when update patient info!");
		}
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse) session.getAttribute("nurse");
		try {
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} catch (SQLException | FallArmDbFailure e) {
			e.printStackTrace();
		}
		request.setAttribute("info", "Update Patient ");
		request.setAttribute("role", "patient");
		request.setAttribute("operation", "update");
		request.setAttribute("flag", flag);
	}

	private void doDeletePatient(HttpServletRequest request) {
		String patientId = request.getParameter("id");
		boolean flag = false;
		try {
			flag = DbUnit.deletePatientWithRetry(patientId);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse) session.getAttribute("nurse");
		try {
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} catch (SQLException | FallArmDbFailure e) {
			e.printStackTrace();
		}
		request.setAttribute("info", "Delete Patient ");
		request.setAttribute("role", "patient");
		request.setAttribute("operation", "delete");
		request.setAttribute("flag", flag);
	}

	private void doOperateWithNurse(HttpServletRequest request) {
		String operation = request.getParameter("operation");
		if (operation.equals("edit")) {
			doEditNurse(request);
		} else if (operation.equals("add")) {
			doAddNurse(request);
		} else if (operation.equals("search")) {
			doSearchNurse(request);
		} else if (operation.equals("delete")) {
			doDeleteNurse(request);
		} else {
			System.out.println("OPERATION is wrong.");
		}
	}

	private void doEditNurse(HttpServletRequest request) {
		request.setAttribute("role", "nurse");
		doSearchNurse(request);
		request.removeAttribute("operation");
		request.setAttribute("operation", "edit");
	}

	private void doAddNurse(HttpServletRequest request) {
		request.setAttribute("role", "nurse");
		request.setAttribute("operation", "add");
	}

	private void doSearchNurse(HttpServletRequest request) {
		Nurse nurse = null;
		String id = request.getParameter("id");
		ArrayList<Patient> patientList = null;
		try {
			nurse = DbUnit.searchNurse(DbUnit.SEARCH_NURSE_BY_ID, id).get(0);
			patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		}
		request.setAttribute("role", "nurse");
		request.setAttribute("operation", "search");
		request.setAttribute("nurse", nurse);
		request.setAttribute("patientList", patientList);
	}

	private void doInsertNurse(HttpServletRequest request) {
		String emailAddress = request.getParameter("email_address");
		String password = request.getParameter("password");
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String birthdateStr = request.getParameter("birthdate");
		String genderStr = request.getParameter("gender");
		boolean flag = false;
		try {
			flag = DbUnit.insertNewNurseWithRetry(emailAddress, password,
					firstName, lastName, birthdateStr, genderStr);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse) session.getAttribute("nurse");
		try {
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} catch (SQLException | FallArmDbFailure e) {
			e.printStackTrace();
		}
		request.setAttribute("info", "Insert New Nurse ");
		request.setAttribute("role", "nurse");
		request.setAttribute("operation", "insert");
		request.setAttribute("flag", flag);
	}

	private void doUpdateNurse(HttpServletRequest request) {
		String nurseId = request.getParameter("id");
		String emailAddress = request.getParameter("email_address");
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String birthdateStr = request.getParameter("birthdate");
		String genderStr = request.getParameter("gender");
		boolean flag = false;
		try {
			Nurse nurse = DbUnit
					.searchNurse(DbUnit.SEARCH_NURSE_BY_ID, nurseId).get(0);
			Hashtable<String, String> args = new Hashtable<String, String>();
			if (!(nurse.getEmailAddress().equals(emailAddress)))
				args.put("email_address", emailAddress);
			if (!(nurse.getFirstName().equals(firstName)))
				args.put("first_name", firstName);
			if (!(nurse.getLastName().equals(lastName)))
				args.put("last_name", lastName);
			if (!(new SimpleDateFormat("yyyy-MM-dd").format(nurse
					.getBirthdate()).equals(birthdateStr)))
				args.put("birthdate", birthdateStr);
			if (!((nurse.getGender() + "")).equals(genderStr))
				args.put("gender", genderStr);
			flag = DbUnit.updateNurseInfoWithRetry(Integer.parseInt(nurseId),
					args);
		} catch (SQLException | FallArmDbFailure e) {
			System.out.println("Can not find nurse when update nurse info!");
		}
		request.setAttribute("role", "nurse");
		request.setAttribute("operation", "update");
		request.setAttribute("flag", flag);
	}

	private void doDeleteNurse(HttpServletRequest request) {
		String nurseId = request.getParameter("id");
		boolean flag = false;
		try {
			flag = DbUnit.deleteNurseWithRetry(nurseId);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse) session.getAttribute("nurse");
		try {
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} catch (SQLException | FallArmDbFailure e) {
			e.printStackTrace();
		}
		request.setAttribute("info", "Delete Nurse ");
		request.setAttribute("role", "nurse");
		request.setAttribute("operation", "delete");
		request.setAttribute("flag", flag);
	}

	private void doOperateWithContact(HttpServletRequest request) {
		String operation = request.getParameter("operation");
		if (operation.equals("edit")) {
			doEditContact(request);
		} else if (operation.equals("add")) {
			doAddContact(request);
		} else if (operation.equals("search")) {
			doSearchContact(request);
		} else if (operation.equals("delete")) {
			doDeleteContact(request);
		} else {
			System.out.println("OPERATION is wrong.");
		}
	}

	private void doEditContact(HttpServletRequest request) {
		request.setAttribute("role", "contact");
		doSearchContact(request);
		request.removeAttribute("operation");
		request.setAttribute("operation", "edit");
	}

	private void doAddContact(HttpServletRequest request) {
		request.setAttribute("role", "contact");
		request.setAttribute("operation", "add");
	}

	private void doSearchContact(HttpServletRequest request) {
		Contact contact = null;
		String id = request.getParameter("id");
		try {
			contact = DbUnit.searchContact(DbUnit.SEARCH_CONTACT_BY_ID, id)
					.get(0);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		}
		request.setAttribute("role", "contact");
		request.setAttribute("operation", "search");
		request.setAttribute("contact", contact);
	}

	private void doInsertContact(HttpServletRequest request) {
		String patientIdStr = request.getParameter("patient_id");
		String emailAddress = request.getParameter("email_address");
		String phoneNumberStr = request.getParameter("phone");
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String genderStr = request.getParameter("gender");
		boolean flag = false;
		try {
			flag = DbUnit.insertNewContactWithRetry(patientIdStr, emailAddress,
					phoneNumberStr, firstName, lastName, genderStr);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse) session.getAttribute("nurse");
		try {
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} catch (SQLException | FallArmDbFailure e) {
			e.printStackTrace();
		}
		request.setAttribute("info", "Insert New Contact ");
		request.setAttribute("role", "contact");
		request.setAttribute("operation", "insert");
		request.setAttribute("flag", flag);
	}

	private void doUpdateContact(HttpServletRequest request) {
		String contactId = request.getParameter("id");
		String patientIdStr = request.getParameter("patient_id");
		String emailAddress = request.getParameter("email_address");
		String phoneNumberStr = request.getParameter("phone");
		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String genderStr = request.getParameter("gender");
		boolean flag = false;
		try {
			Contact contact = DbUnit.searchContact(DbUnit.SEARCH_CONTACT_BY_ID,
					contactId).get(0);
			Hashtable<String, String> args = new Hashtable<String, String>();
			if (!(contact.getPatientId() + "").equals(patientIdStr))
				args.put("patient_id", patientIdStr);
			if (!(contact.getEmailAddress().equals(emailAddress)))
				args.put("email_address", emailAddress);
			if (!(contact.getPhoneNumber().equals(phoneNumberStr)))
				args.put("phone", phoneNumberStr);
			if (!(contact.getFirstName().equals(firstName)))
				args.put("first_name", firstName);
			if (!(contact.getLastName().equals(lastName)))
				args.put("last_name", lastName);
			if (!((contact.getGender() + "")).equals(genderStr))
				args.put("gender", genderStr);
			flag = DbUnit.updateContactInfoWithRetry(
					Integer.parseInt(contactId), args);
		} catch (SQLException | FallArmDbFailure e) {
			System.out
					.println("Can not find contact when update contact info!");
		}
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse) session.getAttribute("nurse");
		try {
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} catch (SQLException | FallArmDbFailure e) {
			e.printStackTrace();
		}
		request.setAttribute("info", "Update Contact ");
		request.setAttribute("role", "contact");
		request.setAttribute("operation", "update");
		request.setAttribute("flag", flag);

	}

	private void doDeleteContact(HttpServletRequest request) {
		String contactId = request.getParameter("id");
		boolean flag = false;
		try {
			flag = DbUnit.deleteContactWithRetry(contactId);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		HttpSession session = request.getSession();
		Nurse nurse = (Nurse) session.getAttribute("nurse");
		try {
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} catch (SQLException | FallArmDbFailure e) {
			e.printStackTrace();
		}
		request.setAttribute("info", "Delete Contact ");
		request.setAttribute("role", "contact");
		request.setAttribute("operation", "delete");
		request.setAttribute("flag", flag);
	}

}

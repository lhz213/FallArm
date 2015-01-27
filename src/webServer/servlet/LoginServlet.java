package webServer.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String LOGIN_PATIENT_URL = "/patient_index.jsp";
	private static final String LOGIN_NURSE_URL = "/nurse_index.jsp";
	private static final String LOGIN_ERROR_URL = "/login_error.html";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws HTTPException, IOException, ServletException {
		ServletContext context = getServletContext();
		HttpSession session = request.getSession();
		Patient patient;
		Nurse nurse;
		String email;
		String password;
		email = request.getParameter("email");
		password = request.getParameter("password");
		try {
			patient = DbUnit.loginWithPatient(email, password);
			ArrayList<Record> recordList = DbUnit.searchRecord(
					DbUnit.SEARCH_RECORD_BY_PATIENT_ID, patient.getId() + "");
			ArrayList<Contact> contactList = DbUnit.searchContact(
					DbUnit.SEARCH_CONTACT_BY_PATIENT_ID, patient.getId() + "");
			int totalNumberOfPatient = DbUnit.searchTotalNumberOfPatient();
			int totalNumberOfContact = DbUnit
					.searchTotalNumberOfContact(patient.getId());
			int totalNumberOfFall = DbUnit.searchTotalNumberOfRecordByPatient(
					Record.RECORD_TAG_FALL, patient.getId());
			int totalNumberOfMaybe = DbUnit.searchTotalNumberOfRecordByPatient(
					Record.RECORD_TAG_MAYBE, patient.getId());
			session.setAttribute("patient", patient);
			request.setAttribute("table", "Record List");
			request.setAttribute("recordList", recordList);
			request.setAttribute("contactList", contactList);
			session.setAttribute("totalNumberOfPatient", totalNumberOfPatient);
			session.setAttribute("totalNumberOfContact", totalNumberOfContact);
			session.setAttribute("totalNumberOfFall", totalNumberOfFall);
			session.setAttribute("totalNumberOfMaybe", totalNumberOfMaybe);
			RequestDispatcher dispatcher = context
					.getRequestDispatcher(LOGIN_PATIENT_URL);
			dispatcher.forward(request, response);
			return;
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		try {
			nurse = DbUnit.loginWithNurse(email, password);
			ArrayList<Patient> patientList = DbUnit.searchPatient(
					DbUnit.SEARCH_PATINET_BY_NURSE_ID, "" + nurse.getId());
			int totalNumberOfPatient = DbUnit.searchTotalNumberOfPatient();
			int relatedNumberOfPatient = DbUnit
					.searchRelatedNumberOfPatient(nurse.getId());
			int totalNumberOfFall = DbUnit.searchTotalNumberOfRecordByNurse(1,
					nurse.getId());
			int totalNumberOfMaybe = DbUnit.searchTotalNumberOfRecordByNurse(2,
					nurse.getId());
			session.setAttribute("nurse", nurse);
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
			session.setAttribute("totalNumberOfPatient", totalNumberOfPatient);
			session.setAttribute("relatedNumberOfPatient",
					relatedNumberOfPatient);
			session.setAttribute("totalNumberOfFall", totalNumberOfFall);
			session.setAttribute("totalNumberOfMaybe", totalNumberOfMaybe);
			RequestDispatcher dispatcher = context
					.getRequestDispatcher(LOGIN_NURSE_URL);
			dispatcher.forward(request, response);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
			RequestDispatcher dispatcher = context
					.getRequestDispatcher(LOGIN_ERROR_URL);
			dispatcher.forward(request, response);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}

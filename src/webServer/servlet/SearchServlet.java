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
import javax.xml.ws.http.HTTPException;

import webServer.data.Contact;
import webServer.data.Patient;
import webServer.data.Person;
import webServer.db.DbUnit;
import webServer.exceptions.FallArmDbFailure;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SEARCH_RESULT_URL = "/nurse_index.jsp";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws HTTPException, IOException, ServletException {
		ServletContext context = getServletContext();
		String role = request.getParameter("role");
		System.out.println("role" + role);
		if (role == null || role.equals("")) {
			System.out.println("Search Bar");
			ArrayList<Patient> patientList = null;
			String keyword = request.getParameter("keyword");
			patientList = doSearchPatient(keyword);
			request.setAttribute("table", "Patient List");
			request.setAttribute("patientList", patientList);
		} else if (role.equals("contact")) {
			System.out.println("Search Contact");
			ArrayList<Contact> contactList = null;
			Person preson = null;
			String keyword;
			keyword = request.getParameter("patientId");
			contactList = doSearchContact(keyword);
			preson = doSearchPatient(keyword).get(0);
			request.setAttribute("table", "Contact List");
			request.setAttribute("contactList", contactList);
			request.setAttribute("person", preson);
		}

		RequestDispatcher dispatcher = context
				.getRequestDispatcher(SEARCH_RESULT_URL);
		dispatcher.forward(request, response);

	}

	private ArrayList<Contact> doSearchContact(String keyword) {
		ArrayList<Contact> contactList = null;
		try {
			contactList = DbUnit.searchContact(
					DbUnit.SEARCH_CONTACT_BY_PATIENT_ID, keyword);
		} catch (FallArmDbFailure ex) {
			System.out.println(ex.getReasonStr());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return contactList;
	}

	private ArrayList<Patient> doSearchPatient(String keyword) {
		ArrayList<Patient> patientList = null;
		int searchType = -1;
		try {
			int id = Integer.parseInt(keyword);
			if (id > 100000 && id < 500000) {
				searchType = DbUnit.SEARCH_PATIENT_BY_ID;
			} else if (id > 500000 && id < 1000000) {
				searchType = DbUnit.SEARCH_PATINET_BY_NURSE_ID;
			}
		} catch (NumberFormatException ex) {
			if (keyword.trim().equals("") || keyword == null) {
				searchType = DbUnit.SEARCH_ALL_PATIENT;
			} else if (keyword.indexOf('@') != -1
					&& (keyword.lastIndexOf('.') - keyword.indexOf('@') > 0)) {
				searchType = DbUnit.SEARCH_PATIENT_BY_EMAIL;
			} else {
				searchType = DbUnit.SEARCH_PATIENT_BY_NAME;
			}
		}
		try {
			patientList = DbUnit.searchPatient(searchType, keyword);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FallArmDbFailure e) {
			System.out.println(e.getReasonStr());
		}
		return patientList;
	}
}

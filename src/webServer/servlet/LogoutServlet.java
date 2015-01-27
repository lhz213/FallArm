package webServer.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String INDEX_LOGIN_URL = "/login.html";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws HTTPException, IOException, ServletException {
		ServletContext context = getServletContext();
		request.getSession().invalidate();
		RequestDispatcher dispatcher = context
				.getRequestDispatcher(INDEX_LOGIN_URL);
		dispatcher.forward(request, response);
	}

}

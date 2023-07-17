package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/user/*")
public class UserController extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		
		RequestDispatcher rd = null;
		switch (action) {
		case "list":
			rd = request.getRequestDispatcher("/WEB-INF/view/user/list.jsp");
			rd.forward(request, response);
			break;
		case "login":
			if (request.getMethod().equals("GET")) {
				rd = request.getRequestDispatcher("/WEB-INF/view/user/login.jsp");
				rd.forward(request, response);
			} else {
				
			}
			break;
		case "register":
			if (request.getMethod().equals("GET")) {
				rd = request.getRequestDispatcher("/WEB-INF/view/user/register.jsp");
				rd.forward(request, response);
			} else {
				String uid = request.getParameter("uid");
				Part filePart = request.getPart("profile");
				String filename = filePart.getSubmittedFileName();
				int dotPosition = filename.indexOf(".");
				String firstPart = filename.substring(0, dotPosition);
				filename = filename.replace(firstPart, uid);
				filePart.write(PROFILE_PATH + filename);
				
				response.sendRedirect("/bbs/user/login");
			}
			break;
		}
	}


}

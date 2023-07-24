package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AsideController
 */
@WebServlet("/aside/*")
public class AsideController extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		HttpSession session = request.getSession();
		
		PrintWriter out = response.getWriter();
		switch(action) {
		case "stateMsg":
			String stateMsg = request.getParameter("stateMsg");
			session.setAttribute("stateMsg", stateMsg);
			out.print("0");
			break;
		}
	}

}

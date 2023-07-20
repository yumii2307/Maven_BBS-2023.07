package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.BoardDao;
import db.ReplyDao;
import entity.Reply;

@WebServlet("/reply/*")
public class ReplyController extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		HttpSession session = request.getSession();
		String sessionUid = (String) session.getAttribute("uid");
		session.setAttribute("menu", "board");
		BoardDao bDao = new BoardDao();
		ReplyDao rDao = new ReplyDao();
		
		switch (action) {
		case "write":
			int bid = Integer.parseInt(request.getParameter("bid"));
			String uid = request.getParameter("uid");
			String comment = request.getParameter("comment");
			int isMine = (uid.equals(sessionUid)) ? 1 : 0;
			Reply reply = new Reply(comment, isMine, sessionUid, bid);
			rDao.insertReply(reply);
			rDao.increaseReplyCount(bid);
			response.sendRedirect("/bbs/board/detail?bid=" + bid + "&uid=" + uid + "&option=DNI");
			break;
		}
	}

}

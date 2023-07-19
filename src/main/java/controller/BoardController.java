package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.BoardDao;
import entity.Board;

/**
 * Servlet implementation class BoardController
 */
@WebServlet("/board/*")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1,		// 1 MB
		maxFileSize = 1024 * 1024 * 10,				// 10 MB
		maxRequestSize = 1024 * 1024 * 10
)
public class BoardController extends HttpServlet {
	public static final int LIST_PER_PAGE = 10;			// 한 페이지당 글 목록의 갯수
	public static final int PAGE_PER_SCREEN = 10;		// 한 화면에 표시되는 페이지 갯수
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		HttpSession session = request.getSession();
		String sessionUid = (String) session.getAttribute("uid");
		session.setAttribute("menu", "board");
		BoardDao bDao = new BoardDao();
		
		RequestDispatcher rd = null;
		int bid = 0, page = 0;
		String title = null, content = null, files = null;
		Board board = null;
		switch (action) {
		case "list":
			String page_ = request.getParameter("p");
			String field = request.getParameter("f");
			String query = request.getParameter("q");
			page = (page_ == null || page_.equals("")) ? 1 : Integer.parseInt(page_);
			field = (field == null || field.equals("")) ? "title" : field;
			query = (field == null || field.equals("")) ? "" : query;
			session.setAttribute("currentBoardPage", page);
			
			List<Board> list = bDao.listBoard(field, query, page);
			
			int totalBoardCount = bDao.getBoardCount();
			int totalPages = (int) Math.ceil(totalBoardCount / (double) LIST_PER_PAGE);
			int startPage = (int) Math.ceil((page-0.5)/PAGE_PER_SCREEN - 1) * PAGE_PER_SCREEN + 1;
			int endPage = Math.min(totalPages, startPage + PAGE_PER_SCREEN - 1);
			List<String> pageList = new ArrayList<>();
			for (int i = startPage; i <= endPage; i++) {
				pageList.add(String.valueOf(i));
			}
			
			request.setAttribute("boardList", list);
			request.setAttribute("field", field);
			request.setAttribute("query", query);
			request.setAttribute("today", LocalDate.now().toString());		// 현재 날짜
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("startPage", startPage);
			request.setAttribute("endPage", endPage);
			request.setAttribute("pageList", pageList);
			
			rd = request.getRequestDispatcher("/WEB-INF/view/board/list.jsp");
			rd.forward(request, response);
			break;
		case "write":
			if (request.getMethod().equals("GET")) {
				rd = request.getRequestDispatcher("/WEB-INF/view/board/write.jsp");
				rd.forward(request, response);
			} else {
				title = request.getParameter("title");
				content = request.getParameter("content");
				board = new Board(sessionUid, title, content, files);
				bDao.insertBoard(board);
				response.sendRedirect("/bbs/board/list?p=1&f=&q=");
			}
			break;
		}
	}
}

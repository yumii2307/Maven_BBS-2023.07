package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.servlet.http.Part;

import org.mindrot.jbcrypt.BCrypt;

import db.UserDao;
import entity.User;
import utility.AsideUtil;
import utility.UserService;

/**
 * Servlet implementation class UserController
 */
@WebServlet("/user/*")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1,		// 1 MB
		maxFileSize = 1024 * 1024 * 10,				// 10 MB
		maxRequestSize = 1024 * 1024 * 10
)
public class UserController extends HttpServlet {
	public static final String PROFILE_PATH = "c:/Temp/profile/";

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		HttpSession session = request.getSession();
		session.setAttribute("menu", "user");
		UserDao uDao = new UserDao();
		
		RequestDispatcher rd = null;
		User user = null;
		String uid = null, pwd = null, pwd2 = null, uname = null, email = null, addr = null;
		String filename = null;
		Part filePart = null;
		switch (action) {
		case "list":
			String page_ = request.getParameter("page");
			int page = Integer.parseInt(page_);
			List<User> list = uDao.getUserList(page);
			request.setAttribute("userList", list);
			int totalUsers = uDao.getUserCount();
			int totalPages = (int) Math.ceil(totalUsers / 10.);
			session.setAttribute("currentUserPage", page);
			List<String> pageList = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++)
				pageList.add(String.valueOf(i));
			request.setAttribute("pageList", pageList);
			
			rd = request.getRequestDispatcher("/WEB-INF/view/user/list.jsp");
			rd.forward(request, response);
			break;
		case "login":
			if (request.getMethod().equals("GET")) {
				rd = request.getRequestDispatcher("/WEB-INF/view/user/login.jsp");
				rd.forward(request, response);
			} else {
				uid = request.getParameter("uid");
				pwd = request.getParameter("pwd");
				UserService us = new UserService();
				int result = us.login(uid, pwd);
				if (result == UserService.CORRECT_LOGIN) {
					session.setAttribute("uid", uid);
					user = uDao.getUser(uid);
					session.setAttribute("uname", user.getUname());
					session.setAttribute("email", user.getEmail());
					session.setAttribute("addr", user.getAddr());
					session.setAttribute("profile", user.getProfile());
					
					// 상태 메세지
					// D:\JavaWorkspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\bbs\WEB-INF/data/todayQuote.txt
					String quoteFile = getServletContext().getRealPath("/") + "WEB-INF/data/todayQuote.txt";
					AsideUtil au = new AsideUtil();
					String stateMsg = au.getTodayQuote(quoteFile);
					session.setAttribute("stateMsg", stateMsg);
					
					// 환영 메세지
					request.setAttribute("msg", user.getUname() + "님 환영합니다.");
					request.setAttribute("url", "/bbs/board/list?p=1&f=&q=");
					rd = request.getRequestDispatcher("/WEB-INF/view/common/alertMsg.jsp");
					rd.forward(request, response);
				} else if (result == UserService.WRONG_PASSWORD) {
					request.setAttribute("msg", "잘못된 패스워드입니다. 다시 입력하세요.");
					request.setAttribute("url", "/bbs/user/login");
					rd = request.getRequestDispatcher("/WEB-INF/view/common/alertMsg.jsp");
					rd.forward(request, response);
				} else {		// UID_NOT_EXIST
					request.setAttribute("msg", "ID가 없습니다. 회원가입 페이지로 이동합니다.");
					request.setAttribute("url", "/bbs/user/register");
					rd = request.getRequestDispatcher("/WEB-INF/view/common/alertMsg.jsp");
					rd.forward(request, response);
				}
			}
			break;
		case "register":
			if (request.getMethod().equals("GET")) {
				rd = request.getRequestDispatcher("/WEB-INF/view/user/register.jsp");
				rd.forward(request, response);
			} else {
				uid = request.getParameter("uid");
				pwd = request.getParameter("pwd");
				pwd2 = request.getParameter("pwd2");
				uname = request.getParameter("uname");
				email = request.getParameter("email");
				filePart = request.getPart("profile");
				addr = request.getParameter("addr");
				
				try {
					filename = filePart.getSubmittedFileName();
					int dotPosition = filename.indexOf(".");
					String firstPart = filename.substring(0, dotPosition);
					filename = filename.replace(firstPart, uid);
					filePart.write(PROFILE_PATH + filename);
				} catch (Exception e) {
					System.out.println("프로필 사진을 입력하지 않았습니다.");
				}
				
				// uid가 중복 --> 등록 화면
				if (uDao.getUser(uid) != null) {
					request.setAttribute("msg", "사용자 ID가 중복되었습니다.");
					request.setAttribute("url", "/bbs/user/register");
					rd = request.getRequestDispatcher("/WEB-INF/view/common/alertMsg.jsp");
					rd.forward(request, response);
				} else if (!pwd.equals(pwd2)) { 	// pwd != pwd2 --> 등록 화면
					request.setAttribute("msg", "패스워드 입력이 잘못되었습니다.");
					request.setAttribute("url", "/bbs/user/register");
					rd = request.getRequestDispatcher("/WEB-INF/view/common/alertMsg.jsp");
					rd.forward(request, response);
				} else {
					String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
					// profile image를 정사각형으로 만들어 주는 메소드를 호출
					if (!(filename == null || filename.equals(""))) {
						AsideUtil au = new AsideUtil();
						filename = au.squareImage(filename);
					}
					user = new User(uid, hashedPwd, uname, email, filename, addr);
					uDao.insertUser(user);
					request.setAttribute("msg", "등록을 마쳤습니다. 로그인하세요.");
					request.setAttribute("url", "/bbs/user/login");
					rd = request.getRequestDispatcher("/WEB-INF/view/common/alertMsg.jsp");
					rd.forward(request, response);
				}
			}
			break;
		case "logout":
			session.invalidate();
			response.sendRedirect("/bbs/user/login");
			break;
		case "update":
			if (request.getMethod().equals("GET")) {
				uid = request.getParameter("uid");
				user = uDao.getUser(uid);
				request.setAttribute("user", user);
				rd = request.getRequestDispatcher("/WEB-INF/view/user/update.jsp");
				rd.forward(request, response);
			} else {
				uid = request.getParameter("uid");
				String hashedPwd = request.getParameter("hashedPwd");
				String oldFilename = request.getParameter("filename");
				pwd = request.getParameter("pwd");
				pwd2 = request.getParameter("pwd2");
				uname = request.getParameter("uname");
				email = request.getParameter("email");
				filePart = request.getPart("profile");
				addr = request.getParameter("addr");
				try {
					filename = filePart.getSubmittedFileName();
					int dotPosition = filename.indexOf(".");
					if (!(oldFilename == null || oldFilename.equals("")) && !(filename == null || filename.equals(""))) {
						File oldFile = new File(PROFILE_PATH + oldFilename);
						oldFile.delete();
					}
					String firstPart = filename.substring(0, dotPosition);
					filename = filename.replace(firstPart, uid);
					filePart.write(PROFILE_PATH + filename);
				} catch (Exception e) {
					System.out.println("프로필 사진을 변경하지 않았습니다.");
				}
//				filename = (filename == null || filename.equals("")) ? oldFilename : filename;
				if (!(filename == null || filename.equals(""))) {
					AsideUtil au = new AsideUtil();
					filename = au.squareImage(filename);
				} else
					filename = oldFilename;
				
				boolean pwdFlag = false;
				if (pwd != null && pwd.length() > 1 && pwd.equals(pwd2)) {
					hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
					pwdFlag = true;
				} 
				user = new User(uid, hashedPwd, uname, email, filename, addr);
				uDao.updateUser(user);
				session.setAttribute("uname", uname);
				session.setAttribute("email", email);
				session.setAttribute("addr", addr);
				session.setAttribute("profile", filename);
				if (pwdFlag) {
					request.setAttribute("msg", "패스워드가 변경이 되었습니다.");
					request.setAttribute("url", "/bbs/user/list?page=" + session.getAttribute("currentUserPage"));
					rd = request.getRequestDispatcher("/WEB-INF/view/common/alertMsg.jsp");
					rd.forward(request, response);
				} else
					response.sendRedirect("/bbs/user/list?page=" + session.getAttribute("currentUserPage"));
			}
			break;
		case "delete":
			uid = request.getParameter("uid");
			rd = request.getRequestDispatcher("/WEB-INF/view/user/delete.jsp?uid=" + uid);
			rd.forward(request, response);
			break;
		case "deleteConfirm":
			uid = request.getParameter("uid");
			uDao.deleteUser(uid);
			response.sendRedirect("/bbs/user/list?page=" + session.getAttribute("currentUserPage"));
			break;
		default:
			rd = request.getRequestDispatcher("/WEB-INF/view/error/error404.jsp");
			rd.forward(request, response);
		}
	}


}

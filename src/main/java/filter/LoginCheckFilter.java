package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class BbsFilter
 */
@WebFilter({"/board/*", "/reply/*", "/file/*", "/aside/*", 
			"/user/list", "/user/logout", "/user/update", "/user/delete", "/user/deleteConfirm"})
public class LoginCheckFilter extends HttpFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		
		// 로그인을 안했으면 로그인 화면으로 이동
		String sessionUid = (String) session.getAttribute("uid");
		if (sessionUid == null || sessionUid.equals("")) {
			httpResponse.sendRedirect("/bbs/user/login");
			return;
		}
		
		chain.doFilter(request, response);
	}

}

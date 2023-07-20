package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class FileController
 */
@WebServlet("/file/*")
public class FileController extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		
		String file = null, path = null;
		OutputStream os = null;
		FileInputStream fis = null;
		File f = null;
		byte[] buffer = new byte[1024 * 8];
		
		switch (action) {
		case "download":
			file = request.getParameter("file");
			path = BoardController.UPLOAD_PATH + file;
			os = response.getOutputStream();
			response.setContentType("text/html; charset=utf-8");
			response.setHeader("Cache-control", "no-cache");
			response.setHeader("Content-disposition", "attachment; fileName=" + URLEncoder.encode(file, "UTF-8"));
			
			f = new File(path);
			fis = new FileInputStream(f);
			while (true) {
				int count = fis.read(buffer);
				if (count == -1)
					break;
				os.write(buffer, 0, count);
			}
			fis.close(); os.close();
			break;
		case "profile":
			file = request.getParameter("file");
			path = UserController.PROFILE_PATH + file;
			os = response.getOutputStream();
			response.setContentType("text/html; charset=utf-8");
			response.setHeader("Cache-control", "no-cache");
			response.setHeader("Content-disposition", "attachment; fileName=" + URLEncoder.encode(file, "UTF-8"));
			
			f = new File(path);
			fis = new FileInputStream(f);
			while (true) {
				int count = fis.read(buffer);
				if (count == -1)
					break;
				os.write(buffer, 0, count);
			}
			fis.close(); os.close();
			break;
		}
		
	}

}

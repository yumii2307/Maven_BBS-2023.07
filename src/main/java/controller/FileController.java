package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024 * 1,		// 1 MB
		maxFileSize = 1024 * 1024 * 10,				// 10 MB
		maxRequestSize = 1024 * 1024 * 10
)
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
		case "imageUpload":
			String callback = request.getParameter("CKEditorFuncNum");
			System.out.println("callback: " + callback);
			String error = "";
			String url = null;
			
			List<Part> fileParts = (List<Part>) request.getParts();
			System.out.println(fileParts.size());
			for (Part part: fileParts) {
				String filename = part.getSubmittedFileName();
				if (filename == null || filename.equals(""))
					continue;
				String now = LocalDateTime.now().toString().substring(0, 22).replaceAll("[-T:.]", "");
				int idx = filename.lastIndexOf(".");
				filename = now + filename.substring(idx);				// 고유한 이름으로 변경
				System.out.println(filename);
				part.write(BoardController.UPLOAD_PATH + filename);
				url = "/bbs/file/download?file=" + filename;
			}
			
			String ajaxResponse = "<script>"
					+	 "window.parent.CKEDITOR.tools.callFunction("
					+      	  callback + ",'" + url + "','" + error + "'"
					+ 	 ");"
					+ "</script>";
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println(ajaxResponse);
			break;
		}
	}

}

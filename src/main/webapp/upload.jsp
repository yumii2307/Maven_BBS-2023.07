<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>
<body>
	<form action="/bbs/file/upload" method="post" enctype="multipart/form-data">
		<input type="text" name="id"><br><br>
		<input type="file" name="files" multiple><br><br> 			// multiple : 파일을 한꺼번에 여러개 올릴 수 있다
		<input type="submit" value="제출">
	</form>
</body>
</html>
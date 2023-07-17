<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
	<%@ include file="../common/head.jspf" %>
</head>
<body>
    <nav class="navbar navbar-expand-sm bg-dark navbar-dark fixed-top">
        <div class="container-fluid">
            <img src="/bbs/img/ck-logo.png" alt="Logo" style="height:60px;" class="rounded-3 mx-2">
            <div class="p-2 bg-dark justify-content-center rounded">
                <img src="https://picsum.photos/1500/200" width="100%">
            </div>
        </div>
    </nav>
    <div class="container" style="margin-top: 300px;">
        <div class="row">
            <div class="col-4"></div>
            <div class="col-4">
                <h3><strong>회원가입</strong></h3>
                <hr>
                <form action="#" method="post">
                    <table class="table table-borderless">
                        <tr>
                            <td><label for="uid" class="col-form-label">사용자 ID</label></td>
                            <td><input type="text" name="uid" id="uid" class="form-control"></td>
                        </tr>
                        <tr>
                            <td><label for="pwd" class="col-form-label">패스워드</label></td>
                            <td><input type="password" name="pwd" id="pwd" class="form-control"></td>
                        </tr>
                        <tr>
                            <td><label for="pwd2" class="col-form-label">패스워드확인</label></td>
                            <td><input type="password" name="pwd2" id="pwd2" class="form-control"></td>
                        </tr>
                        <tr>
                            <td><label for="uname" class="col-form-label">이름</label></td>
                            <td><input type="text" name="uname" id="uname" class="form-control"></td>
                        </tr>
                        <tr>
                            <td><label for="email" class="col-form-label">이메일</label></td>
                            <td><input type="text" name="email" id="email" class="form-control"></td>
                        </tr>
                        <tr>
                            <td><label for="profile" class="col-form-label">프로필 사진</label></td>
                            <td><input type="text" name="profile" id="profile" class="form-control"></td>
                        </tr>
                        <tr>
                            <td><label for="addr" class="col-form-label">주소</label></td>
                            <td><input type="text" name="addr" id="addr" class="form-control"></td>
                        </tr>
                        <tr>
                            <td colspan="2" style="text-align: center;">
                                <input class="btn btn-primary" type="submit" value="제출">
                                <input class="btn btn-secondary" type="reset" value="취소">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="col-4"></div>
        </div>
    </div>

	<%@ include file="../common/bottom.jspf" %>
</body>
</html>
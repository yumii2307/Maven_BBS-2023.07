package utility;

import org.mindrot.jbcrypt.BCrypt;

import db.UserDao;
import entity.User;

public class UserService {
	public static final int CORRECT_LOGIN = 0;
	public static final int WRONG_PASSWORD = 1;
	public static final int UID_NOT_EXIST = 2;
	
	public int login(String uid, String pwd) {
		UserDao uDao = new UserDao();
		User user = uDao.getUser(uid);
		if (user == null)
			return UID_NOT_EXIST;
		if (BCrypt.checkpw(pwd, user.getPwd()))
			return CORRECT_LOGIN;
		else
			return WRONG_PASSWORD;
	}
}

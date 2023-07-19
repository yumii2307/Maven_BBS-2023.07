package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import entity.Board;

public class BoardDao {
	public Connection getConnection() {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/bbs");
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public int getBoardCount() {
		int count = 0;
		Connection conn = getConnection();
		String sql = "select count(bid) from board where isDeleted=0";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close(); stmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public List<Board> listBoard(String field, String query, int page) {
		Connection conn = getConnection();
		List<Board> list = new ArrayList<>();
		int offset = (page - 1) * 10;
		String sql = "SELECT b.bid, b.title, b.modTime, b.viewCount, b.replyCount, u.uname FROM board AS b "
				+ "	JOIN users AS u"
				+ "	ON b.uid = u.uid"
				+ "	WHERE b.isDeleted=0 AND " + field + " LIKE ?"
				+ "	ORDER BY b.modTime DESC, b.bid DESC"
				+ "	LIMIT 10 OFFSET ?;";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+query+"%");
			pstmt.setInt(2, offset);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Board board = new Board();
				board.setBid(rs.getInt(1));
				board.setTitle(rs.getString(2));
				// 2023-07-19 10:40:55 ==> 2023-07-19T10:40:55
				board.setModTime(LocalDateTime.parse(rs.getString(3).replace(" ", "T")));
				board.setViewCount(rs.getInt(4));
				board.setReplyCount(rs.getInt(5));
				board.setUname(rs.getString(6));
				list.add(board);
				}
			rs.close(); pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void insertBoard(Board board) {
		Connection conn = getConnection();
		String sql = "insert into board values(default, ?, ?, ?, default, default, default, default, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getUid());
			pstmt.setString(2, board.getTitle());
			pstmt.setString(3, board.getContent());
			pstmt.setString(4, board.getFiles());
			
			pstmt.executeUpdate();
			pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

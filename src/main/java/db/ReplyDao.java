package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import entity.Reply;

public class ReplyDao {
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
	
	public List<Reply> getReplyList(int bid) {
		List<Reply> list = new ArrayList<Reply>();
		Connection conn = getConnection();
		String sql = "SELECT r.rid, r.comment, r.regTime, r.isMine, r.uid, r.bid, u.uname FROM reply AS r"
				+ "	JOIN users AS u"
				+ "	ON r.uid=u.uid"
				+ "	WHERE r.bid=?;";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bid);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Reply reply = new Reply(rs.getInt(1), rs.getString(2), 
						LocalDateTime.parse(rs.getString(3).replace(" ", "T")),
						rs.getInt(4), rs.getString(5), rs.getInt(6), rs.getString(7));
				list.add(reply);
			}
			rs.close(); pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void insertReply(Reply reply) {
		Connection conn = getConnection();
		String sql = "insert into reply values(default, ?, default, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reply.getComment());
			pstmt.setInt(2, reply.getIsMine());
			pstmt.setString(3, reply.getUid());
			pstmt.setInt(4, reply.getBid());
			
			pstmt.executeUpdate();
			pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void increaseReplyCount(int bid) {
		Connection conn = getConnection();
		String sql = "update board set replyCount=replyCount+1 where bid=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bid);
			
			pstmt.executeUpdate();
			pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

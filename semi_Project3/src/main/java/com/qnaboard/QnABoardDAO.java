package com.qnaboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class QnABoardDAO {
	private Connection conn = DBConn.getConnection();

	// 데이터 추가
	public void insertBoard(QnABoardDTO dto, String mode) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;

		try {
			sql = " SELECT QnAboard_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			seq = 0;
			if (rs.next()) {
				seq = rs.getInt(1);
			}
			dto.setBoardNum(seq);

			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;

			if (mode.equals("write")) {
				// 글쓰기일 때
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			} else if (mode.equals("reply")) {
				// 답변일 때
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());

				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNo(dto.getOrderNo() + 1);
			}

			sql = " INSERT INTO QnAboard(boardNum, userId, categoryType, subject, content, "
					+ " groupNum, depth, orderNo, parent, hitCount, reg_date) "
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0, SYSDATE) ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, dto.getBoardNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getCategoryType());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setInt(6, dto.getGroupNum());
			pstmt.setInt(7, dto.getDepth());
			pstmt.setInt(8, dto.getOrderNo());
			pstmt.setInt(9, dto.getParent());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	// 답변일 경우 orderNo 변경
	public void updateOrderNo(int groupNum, int orderNo) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE QnAboard SET orderNo = orderNo+1 WHERE groupNum = ? AND orderNo > ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	// 데이터 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {

			sql = "SELECT NVL(COUNT(*), 0) FROM QnAboard";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}

		return result;
	}

	// 검색에서의 데이터 개수
	public int dataCount(String condition, String keyword, String categoryType) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append("SELECT NVL(COUNT(*), 0) FROM QnAboard b JOIN member1 m ON b.userId = m.userId ");
			if (keyword.length() != 0) {
				if (condition.equals("all")) {
					sb.append("     WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 )  ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("     WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
				} else {
					sb.append("     WHERE INSTR(" + condition + ", ?) >= 1 ");
				}
			}
			if (keyword.length() != 0 && !categoryType.equals("all")) {
				sb.append(" AND categoryType='" + categoryType + "' ");
			} else if (keyword.length() == 0 && !categoryType.equals("all")) {
				sb.append(" WHERE categoryType='" + categoryType + "' ");
			}

			pstmt = conn.prepareStatement(sb.toString());

			if (keyword.length() != 0) {
				pstmt.setString(1, keyword);
				if (condition.equals("all")) {
					pstmt.setString(2, keyword);
				}
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return result;
	}

	// 게시물 리스트
	public List<QnABoardDTO> listBoard(int start, int end) {
		List<QnABoardDTO> list = new ArrayList<QnABoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("     SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("         SELECT boardNum, b.userId, userName, userNickName, categoryType, ");
			sb.append("               subject, groupNum, orderNo, depth, hitCount,");
			sb.append("               TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append("         FROM QnAboard b ");
			sb.append("         JOIN member1 m ON b.userId = m.userId ");
			sb.append("         ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("     ) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, end);
			pstmt.setInt(2, start);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				QnABoardDTO dto = new QnABoardDTO();

				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setCategoryType(rs.getString("categoryType"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));

				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}

		return list;
	}

	// 검색시 게시물 리스트
	public List<QnABoardDTO> listBoard(int start, int end, String condition, String keyword, String categoryType) {
		List<QnABoardDTO> list = new ArrayList<QnABoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT * FROM ( ");
			sb.append("     SELECT ROWNUM rnum, tb.* FROM ( ");
			sb.append("         SELECT boardNum, b.userId, userName, userNickName, categoryType, ");
			sb.append("               subject, groupNum, orderNo, depth, hitCount, ");
			sb.append("               TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append("         FROM QnAboard b ");
			sb.append("         JOIN member1 m ON b.userId = m.userId ");
			if (keyword.length() != 0) {
				if (condition.equals("all")) {
					sb.append("     WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("     WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
				} else {
					sb.append("     WHERE INSTR(" + condition + ", ?) >= 1 ");
				}
			}
			if (keyword.length() != 0 && !categoryType.equals("all")) {
				sb.append(" AND categoryType='" + categoryType + "' ");
			} else if (keyword.length() == 0 && !categoryType.equals("all")) {
				sb.append(" WHERE categoryType='" + categoryType + "' ");
			}
			sb.append("         ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("     ) tb WHERE ROWNUM <= ? ");
			sb.append(" ) WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			if (keyword.length() != 0 && condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, end);
				pstmt.setInt(4, start);
			} else if (keyword.length() != 0 && !condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, end);
				pstmt.setInt(3, start);
			} else if (keyword.length() == 0) {
				pstmt.setInt(1, end);
				pstmt.setInt(2, start);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				QnABoardDTO dto = new QnABoardDTO();

				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setCategoryType(rs.getString("categoryType"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}

		return list;
	}

	// 조회수 증가하기
	public void updateHitCount(int boardNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = " UPDATE QnAboard SET hitCount=hitCount+1 WHERE boardNum=? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNum);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	// 해당 게시물 보기
	public QnABoardDTO readBoard(int boardNum) {
		QnABoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT boardNum, b.userId, userName, userNickName, subject, content, reg_date, hitCount, "
					+ " groupNum, depth, orderNo, parent "
					+ " FROM QnAboard b "
					+ " JOIN member1 m ON b.userId=m.userId "
					+ " WHERE boardNum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new QnABoardDTO();
				
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserNickName(rs.getString("userNickName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setParent(rs.getInt("parent"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		
		return dto;
	}

	// 이전글
	public QnABoardDTO preReadBoard(int groupNum, int orderNo, String condition, String keyword, String categoryType) {
		QnABoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" SELECT * FROM ( ");
				sb.append("    SELECT boardNum, subject ");
				sb.append("    FROM QnAboard b ");
				sb.append("    JOIN member1 m ON b.userId = m.userId ");
				sb.append("    WHERE ( (groupNum = ? AND orderNo < ?) OR (groupNum > ?) ) ");
				if (condition.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + condition + ", ?) >= 1 ) ");
				}
				if(!categoryType.equals("all")) {
					sb.append(" AND categoryType='" + categoryType + "' ");
				}
				sb.append("     ORDER BY groupNum ASC, orderNo DESC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setInt(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setInt(3, groupNum);
				pstmt.setString(4, keyword);
				if (condition.equals("all")) {
					pstmt.setString(5, keyword);
				}
			} else {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT boardNum, subject FROM QnAboard ");
				sb.append("     WHERE (groupNum = ? AND orderNo < ?) OR (groupNum > ?) ");
				if(!categoryType.equals("all")) {
					sb.append(" AND categoryType='" + categoryType + "' ");
				}
				sb.append("     ORDER BY groupNum ASC, orderNo DESC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setInt(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setInt(3, groupNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new QnABoardDTO();

				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		
		return dto;
	}

	// 다음글
	public QnABoardDTO nextReadBoard(int groupNum, int orderNo, String condition, String keyword, String categoryType) {
		QnABoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if (keyword != null && keyword.length() != 0) {
				sb.append(" SELECT * FROM ( ");
				sb.append("    SELECT boardNum, subject ");
				sb.append("    FROM QnAboard b ");
				sb.append("    JOIN member1 m ON b.userId = m.userId ");
				sb.append("    WHERE ( (groupNum = ? AND orderNo > ?) OR (groupNum < ?) ) ");
				if (condition.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (condition.equals("reg_date")) {
					keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + condition + ", ?) >= 1 ) ");
				}
				if(!categoryType.equals("all")) {
					sb.append(" AND categoryType='" + categoryType + "' ");
				}
				sb.append("     ORDER BY groupNum DESC, orderNo ASC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setInt(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setInt(3, groupNum);
				pstmt.setString(4, keyword);
				if (condition.equals("all")) {
					pstmt.setString(5, keyword);
				}
			} else {
				sb.append(" SELECT * FROM ( ");
				sb.append("     SELECT boardNum, subject FROM QnAboard ");
				sb.append("     WHERE (groupNum = ? AND orderNo > ?) OR (groupNum < ?) ");
				if(!categoryType.equals("all")) {
					sb.append(" AND categoryType='" + categoryType + "' ");
				}
				sb.append("     ORDER BY groupNum DESC, orderNo ASC ");
				sb.append(" ) WHERE ROWNUM = 1 ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setInt(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setInt(3, groupNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new QnABoardDTO();

				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		
		return dto;
	}

	// 게시물 수정
	public void updateBoard(QnABoardDTO dto) throws SQLException {
	}

	// 게시물 삭제
	public void deleteBoard(int boardNum) throws SQLException {
	}
}

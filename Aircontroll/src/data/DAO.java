package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DAO {
	DataSource ds;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public DAO() {
		super();
		try {
			InitialContext ic = new InitialContext();
			ds = (DataSource) ic.lookup("java:comp/env/jdbc/orcl");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkID(String userName) {
		boolean res=false;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("SELECT userName FROM AirMemberJoin WHERE userName=?");
			pstmt.setString(1, userName);
			rs=pstmt.executeQuery();
			res=rs.next();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
		return res;
	}

	public void insertHumiTempDTO(HumiTempDTO dto) {
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("INSERT INTO AirHumiTemp(Humi, Temp, userName) VALUES (?,?,?)");
			pstmt.setInt(1, dto.getHumi());
			pstmt.setInt(2, dto.getTemp());
			pstmt.setString(3, dto.getUserName());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
	}

	public List<HumiTempDTO> selectHumiTempDTO(String userName) {
		List<HumiTempDTO> dtos = new ArrayList<HumiTempDTO>();
		try {
			String query="SELECT SaveDate,Humi,Temp FROM AirHumiTemp WHERE userName=? ORDER BY SaveDate ASC";
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				java.util.Date time=new java.util.Date(rs.getTimestamp("SaveDate").getTime());
				dtos.add(new HumiTempDTO(rs.getInt("Humi"), rs.getInt("Temp"), time));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
		return dtos;
	}
	
	public HumiTempDTO selectHumiTempOneDTO(String userName) {
		try {
			String query="SELECT * FROM (SELECT * FROM AirHumiTemp WHERE userName=? ORDER BY SaveDate DESC) WHERE ROWNUM=1";
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				java.util.Date time=new java.util.Date(rs.getTimestamp("SaveDate").getTime());
				return new HumiTempDTO(rs.getInt("Humi"), rs.getInt("Temp"), time);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
		return null;
	}
	
	/*public void deleteHumiTemp(String userName) {
		String query="DELETE FROM AirHumiTemp WHERE SaveDate<SYSDATE-1 AND userName=?";		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			resourceClear();
		}
	}*/

	public void insertMemberJoin(MemberJoinDTO dto) {
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("INSERT INTO AirMemberJoin(userName, psw) VALUES (?,?)");
			pstmt.setString(1, dto.getUserName());
			pstmt.setString(2, dto.getPsw());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
	}

	public MemberJoinDTO selectMemberJoin(MemberJoinDTO dto) {
		MemberJoinDTO res = null;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM AirMemberJoin WHERE userName=?");
			pstmt.setString(1, dto.getUserName());
			rs = pstmt.executeQuery();
			if (rs.next())
				res = new MemberJoinDTO(rs.getString("userName"), rs.getString("psw"), rs.getInt("blockCount"));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
		return res;
	}

	public void updateLogin(int count, String userName) {
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("UPDATE AirMemberJoin SET blockCount=? WHERE userName=?");
			pstmt.setInt(1, count);
			pstmt.setString(2, userName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
	}
	
	public void updateACState(String userName, String state) {
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("UPDATE AC_STATE SET state=? WHERE userName=?");
			pstmt.setString(1, state);
			pstmt.setString(2, userName);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
	}
	
	public ACStateDTO selectACState(String userName) {
		ACStateDTO dto=null;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM AC_STATE WHERE userName=?");
			pstmt.setString(1, userName);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto=new ACStateDTO(rs.getString("state"), rs.getInt("temp"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
		return dto;
	}
	
	public void insertLog(IRLogDTO dto) {
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("INSERT INTO IRLog(userName,act,ip,result) VALUES(?,?,?,?)");
			pstmt.setString(1, dto.getUserName());
			pstmt.setString(2, dto.getAct());
			pstmt.setString(3, dto.getIp());
			pstmt.setString(4, dto.getResult());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
	}
	
	public List<IRLogDTO> selectLog(String userName){
		List<IRLogDTO> dtos=new ArrayList<>();
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM IRLOG WHERE userName=? AND SaveDate>(SELECT SYSDATE-1 FROM DUAL) ORDER BY SaveDate DESC");
			pstmt.setString(1, userName);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				String act=rs.getString("act");
				String ip=rs.getString("ip");
				java.util.Date time=new java.util.Date(rs.getTimestamp("SaveDate").getTime());
				String result=rs.getString("result");
				IRLogDTO dto=new IRLogDTO(userName, act, ip, null, result);
				dto.setDate(time);
				dtos.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resourceClear();
		}
		return dtos;
	}

	private void resourceClear() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

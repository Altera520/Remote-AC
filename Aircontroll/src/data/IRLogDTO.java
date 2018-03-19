package data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IRLogDTO {
	private String userName;
	private String act;
	private String ip;
	private String date;
	private String result;
	/**
	 * @param userName
	 * @param act
	 * @param ip
	 * @param date
	 */
	public IRLogDTO(String userName, String act, String ip, String date, String result) {
		super();
		this.userName = userName;
		this.act = act;
		this.ip = ip;
		this.date = date;
		this.result=result;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDate() {
		return date;
	}
	public void setDate(Date date) {
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd hh:mm");
		this.date=sdf.format(date);
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}

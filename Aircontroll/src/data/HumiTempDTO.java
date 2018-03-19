package data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HumiTempDTO {
	private int humi;
	private int temp;
	private String userName;
	private Date date;
	private String dateFormat;
	/**
	 * @param humi
	 * @param temp
	 * @param userName
	 */
	public HumiTempDTO(int humi, int temp, String userName) {
		super();
		this.humi = humi;
		this.temp = temp;
		this.userName = userName;
	}
	
	public HumiTempDTO(int humi, int temp, Date date) {
		super();
		this.humi = humi;
		this.temp = temp;
		this.date=date;
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd hh:mm");
		dateFormat=sdf.format(date);
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setHumi(int humi) {
		this.humi = humi;
	}
	
	public void setTemp(int temp) {
		this.temp = temp;
	}
	
	public int getHumi() {
		return humi;
	}
	
	public int getTemp() {
		return temp;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}

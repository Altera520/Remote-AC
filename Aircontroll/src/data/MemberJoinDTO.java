package data;

public class MemberJoinDTO {
	private String userName;
	private String psw;
	private int blockingCount;
	/**
	 * @param userName
	 * @param psw
	 */
	public MemberJoinDTO(String userName) {
		this.userName = userName;
	}
	
	public MemberJoinDTO(String userName, String psw, int blockingCount) {
		super();
		this.userName = userName;
		this.psw = psw;
		this.blockingCount=blockingCount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPsw() {
		return psw;
	}
	public void setPsw(String psw) {
		this.psw = psw;
	}
	public int getBlockingCount() {
		return blockingCount;
	}
	public void setBlockingCount(int blockingCount) {
		this.blockingCount = blockingCount;
	}
}

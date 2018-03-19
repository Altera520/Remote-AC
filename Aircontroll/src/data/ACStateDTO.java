package data;

public class ACStateDTO {
	String state;
	int temp;
	
	/**
	 * @param state
	 * @param temp
	 */
	public ACStateDTO(String state, int temp) {
		super();
		this.state = state;
		this.temp = temp;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getTemp() {
		return temp;
	}
	public void setTemp(int temp) {
		this.temp = temp;
	}
	
}

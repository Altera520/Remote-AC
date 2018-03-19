package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.DAO;
import data.HumiTempDTO;

public class setHumiAndTempCommand implements ActionCommand{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String userName=request.getParameter("userName");
		String tempString=request.getParameter("temp");
		String humiString=request.getParameter("humi");
		
		if(userName!=null && tempString!=null && humiString !=null) {
			System.out.println("OK");
			int temp=0;
			int humi=0;
			try {
				temp = (int)Double.parseDouble(tempString);
				humi = (int)Double.parseDouble(humiString);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DAO dao=new DAO();
			dao.insertHumiTempDTO(new HumiTempDTO(humi,temp,userName));
		}
	}
}

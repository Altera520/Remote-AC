package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.DAO;

public class LoginFailCommand implements ActionCommand{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int blockCount=(int)request.getAttribute("blockCount");
		if(blockCount<5) {
			DAO dao=new DAO();
			dao.updateLogin(++blockCount, (String)request.getAttribute("userName"));
			request.setAttribute("blockCount", blockCount);
		}
	}

}

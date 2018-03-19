package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.DAO;

public class CheckIDCommand implements ActionCommand{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		DAO dao=new DAO();
		try {
			PrintWriter pw=response.getWriter();
			pw.print(dao.checkID(request.getParameter("userName")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.DAO;
import data.MemberJoinDTO;

public class MemberJoinCommand implements ActionCommand{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		DAO dao=new DAO();
		String id=request.getParameter("id");
		String psw=request.getParameter("psw");
		dao.insertMemberJoin(new MemberJoinDTO(id, psw, 0));
	}

}

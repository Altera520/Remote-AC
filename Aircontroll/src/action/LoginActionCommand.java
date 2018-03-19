package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.DAO;
import data.MemberJoinDTO;

public class LoginActionCommand implements ActionCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HttpSession f5=request.getSession(false);
		if(f5!=null) {
			if(f5.getAttribute("loginInfo")!=null) {
				request.setAttribute("viewPage", "loginSuccess.Action");
				return;
			}
		}
		String userName = request.getParameter("id");
		String psw = request.getParameter("psw");
		DAO dao = null;
		dao = new DAO();
		MemberJoinDTO in = new MemberJoinDTO(userName);
		MemberJoinDTO res = dao.selectMemberJoin(in);
		if (res != null) {
			if (res.getBlockingCount() < 5 && res.getPsw().equals(psw)) {
				HttpSession session = request.getSession();
				if (session.isNew() || session.getAttribute("loginInfo") == null) {
					res.setPsw(null);
					session.setAttribute("loginInfo", res);
				}
				request.setAttribute("viewPage", "loginSuccess.Action");
			}
			else
				request.setAttribute("viewPage", "loginFail.Action");
			request.setAttribute("userName", userName);
			request.setAttribute("blockCount", res.getBlockingCount());
		}
		else {
			request.setAttribute("viewPage", "LoginPage.jsp");
			request.setAttribute("loginFail", true);
		}
	}
}

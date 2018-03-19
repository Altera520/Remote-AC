package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.DAO;
import data.IRLogDTO;
import data.MemberJoinDTO;

public class IRRecvCommand implements ActionCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		DAO dao = new DAO();
		HttpSession session = request.getSession(false);
		if (session != null) {
			Object obj = session.getAttribute("loginInfo");
			if (obj != null) {
				MemberJoinDTO dto = (MemberJoinDTO) obj;
				String result = request.getParameter("result");
				String act = request.getParameter("act");
				String ip = request.getParameter("ip");
				System.out.println(result);
				System.out.println(act);
				System.out.println(ip);
				IRLogDTO irDto = new IRLogDTO(dto.getUserName(), act, ip, null, result);
				dao.insertLog(irDto);
				if (act.equals("켜기") && result.equals("성공"))
					dao.updateACState(dto.getUserName(), "on");
				else if (act.equals("끄기") && result.equals("성공"))
					dao.updateACState(dto.getUserName(), "off");

			}
		}
	}
}

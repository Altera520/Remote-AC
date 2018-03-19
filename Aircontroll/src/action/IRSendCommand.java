package action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IRSendCommand implements ActionCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String cmd = request.getParameter("cmd");
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("loginInfo");
		if (obj != null) {
			//MemberJoinDTO dto = (MemberJoinDTO) obj;
			try {
				response.sendRedirect("http://203.250.32.171:8000?cmd=" + cmd);
				switch (Integer.parseInt(cmd)) {
				case 1: //켜기
					
					//dao.insertLog(irDto);
					break;
				case 2: //끄기
					
					break;
				case 3: //온도증가
					break;
				case 4: //온도감소
					break;
				case 5: //팬가속
					break;
				}
				// response.setHeader(arg0, arg1);
				// response.sendRedirect("http://192.168.0.28:80?cmd="+cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

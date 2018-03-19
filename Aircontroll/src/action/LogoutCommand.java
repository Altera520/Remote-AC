package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutCommand implements ActionCommand{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session=request.getSession(false);
		if(session != null) {
			if(session.getAttribute("loginInfo")!=null) {
				session.invalidate();
			}
		}
	}
}

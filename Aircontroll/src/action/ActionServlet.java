package action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class actionServlet
 */
public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doAction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doAction(request, response);
	}

	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String commandString=commandExtraction(request);
		ActionCommand command=null;
		String viewPage=null;
		System.out.println(commandString);
		if(commandString.equals("/login.Action")) {
			command=new LoginActionCommand();
			command.execute(request, response);
			viewPage=(String)request.getAttribute("viewPage");
		}
		else if(commandString.equals("/loginSuccess.Action")) {
			command=new LoginSuccessCommand();
			command.execute(request, response);
			viewPage="MainPage.jsp";
		}
		else if(commandString.equals("/loginFail.Action")) {
			command=new LoginFailCommand();
			command.execute(request, response);
			viewPage="LoginPage.jsp";
		}
		else if(commandString.equals("/logout.Action")) {
			command=new LogoutCommand();
			command.execute(request, response);
			viewPage="LoginPage.jsp";
		}
		else if(commandString.equals("/setHumiAndTemp.Action")) {
			command=new setHumiAndTempCommand();
			command.execute(request, response);
		}
		else if(commandString.equals("/getData.Action")) {
			command=new getDataCommand();
			command.execute(request, response);
		}
		else if(commandString.equals("/memberJoin.Action")) {
			command=new MemberJoinCommand();
			command.execute(request, response);
			viewPage="LoginPage.jsp";
		}
		else if(commandString.equals("/checkID.Action")) {
			command=new CheckIDCommand();
			command.execute(request, response);
		}
		else if(commandString.equals("/irSend.Action")) {
			command=new IRSendCommand();
			command.execute(request, response);
		}
		else if(commandString.equals("/saveLog.Action")) {
			command=new IRRecvCommand();
			command.execute(request, response);
		}
		
		if(viewPage!=null) {
			RequestDispatcher rd=request.getRequestDispatcher(viewPage);
			rd.forward(request, response);
		}
	}
	
	private String commandExtraction(HttpServletRequest request) {
		String uri=request.getRequestURI().toString();
		String context=request.getContextPath();
		String command=uri.substring(context.length());
		return command;
	}
}

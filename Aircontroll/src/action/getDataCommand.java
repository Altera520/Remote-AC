package action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import data.DAO;
import data.HumiTempDTO;
import data.MemberJoinDTO;

public class getDataCommand implements ActionCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		DAO dao = new DAO();
		HttpSession session = request.getSession(false);
		if (session != null) {
			MemberJoinDTO dto = (MemberJoinDTO) session.getAttribute("loginInfo");
			HumiTempDTO data = dao.selectHumiTempOneDTO(dto.getUserName());
			try {
				response.getWriter().print(convertJSON(data));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private String convertJSON(HumiTempDTO dto) { // json string으로 변환
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("temp", dto.getTemp());
		jsonObject.put("humi", dto.getHumi());
		jsonObject.put("date", dto.getDate().getTime());
		return jsonObject.toJSONString();
	}

}

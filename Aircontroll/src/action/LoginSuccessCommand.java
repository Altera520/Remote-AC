package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import data.ACStateDTO;
import data.DAO;
import data.HumiTempDTO;
import data.IRLogDTO;
import data.MemberJoinDTO;

public class LoginSuccessCommand implements ActionCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Object blockCount = request.getAttribute("blockCount");
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("loginInfo");
		if (obj != null) {
			MemberJoinDTO dto = (MemberJoinDTO) obj;
			DAO dao = new DAO();
			if (dto != null) {
				List<HumiTempDTO> dtos = dao.selectHumiTempDTO(dto.getUserName());
				List<IRLogDTO> dtosLog = dao.selectLog(dto.getUserName());
				ACStateDTO acState = dao.selectACState(dto.getUserName());
				session.setAttribute("ACState", acState);
				if (!dtos.isEmpty()) {
					request.setAttribute("TempHumiJson", convertJSON(dtos));
					request.setAttribute("LatestHumiTempDTO", dtos.get(dtos.size() - 1));
				}
				if (!dtosLog.isEmpty()) {
					request.setAttribute("dtosLog", dtosLog);
				}
			}
			if (blockCount != null && (int) blockCount != 0) { // blockingCount가 0이 아닐 때, 0으로 초기화 시킨다
				dao.updateLogin(0, (String) request.getAttribute("userName"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private String convertJSON(List<? extends HumiTempDTO> dtos) { // json string으로 변환
		JSONObject jsonObject = new JSONObject();
		JSONArray tempJsonArray = new JSONArray();
		JSONArray humiJsonArray = new JSONArray();
		JSONArray dateJsonArray = new JSONArray();

		for (HumiTempDTO item : dtos) {
			tempJsonArray.add(item.getTemp());
			humiJsonArray.add(item.getHumi());
			// -hh-mm분
			// SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
			// dateJsonArray.add(sdf.format(item.getDate()));
			dateJsonArray.add(item.getDate().getTime());
		}
		jsonObject.put("temp", tempJsonArray);
		jsonObject.put("humi", humiJsonArray);
		jsonObject.put("date", dateJsonArray);
		return jsonObject.toJSONString();
	}
}

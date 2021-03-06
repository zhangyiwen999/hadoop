package ssh.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.model.Authority;
import ssh.model.HdfsUser;
import ssh.service.HdfsUserService;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * Hdfs 模块用户登录
 * 
 * @author Robin
 * 
 */
@Resource(name = "hdfsUserAction")
public class HdfsUserAction extends ActionSupport implements
		ModelDriven<HdfsUser> {
	// 登陆用户实体类
	HdfsUser hdfsUser = new HdfsUser();
	//
	private HdfsUserService hdfsUserService;

	private String hadoopUserName;
	private String hadoopPassword;

	private String sessionProperty;

	private Logger log = LoggerFactory.getLogger(HdfsUserAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public HdfsUser getModel() {
		return hdfsUser;
	}

	/**
	 * 登录函数，用户名，密码不能为空
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void login() throws ServletException, IOException {
		Map<String, Object> map = new HashMap<>();
		// 通过email从数据库中查询HdfsUser
		HdfsUser hUser = hdfsUserService.getByEmail(hdfsUser.getEmail());
		// 判断MySql是否存在数据，存在则放入session
		if (hUser == null) {
			map.put("flag", "false");
			map.put("msg", "登录失败，用户名不存在!");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		if (!hdfsUser.getPassword().equals(hUser.getPassword())) {
			map.put("flag", "false");
			map.put("msg", "登录失败,用户密码不正确!");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;

		} else {
			map.put("flag", "true");
			map.put("msg", "登录成功!");
			ActionContext context = ActionContext.getContext();
			Map session = context.getSession();
			session.put("user", hUser.getName());
			session.put("email", hUser.getEmail());// 用于更新
			session.put("hUser", HadoopUtils.getHadoopUserName());// 用于更新
			session.put("authority", hUser.getAuthority());
			log.info("用户：{}, email:{} 登录!", new Object[] { hUser.getName(),
					hUser.getEmail() });
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;

	}

	/**
	 * 注销
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	public void logout() throws ServletException, IOException {
		Map<String, Object> map = new HashMap<>();

		ActionContext context = ActionContext.getContext();
		Map session = context.getSession();

		if (session.get("user") != null || session.get("email") != null) {
			map.put("flag", "true");
			map.put("msg", "注销成功!");
			log.info("用户：{}, email:{} 注销!", new Object[] { session.get("user"),
					session.get("email") });
			// session.put("user", null);
			// session.put("email", null);// 用于更新
			session.clear();

		} else {
			map.put("flag", "false");
			map.put("msg", "注销失败!");
		}

		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;

	}

	/**
	 * 权限验证
	 */
	public void authCheck() {
		Map<String, Object> map = new HashMap<>();
		// 进行ssh权限验证
		boolean hasHdfsLoginAuth = Utils.canLogin(hadoopUserName,
				hadoopPassword);
		ActionContext context = ActionContext.getContext();
		Map session = context.getSession();
		if (!hasHdfsLoginAuth) {
			map.put("flag", "false");
			map.put("msg", "HDFS用户名或密码错误！");
			session.put("authCheck", "false");// 用于验证 ,更新数据库时

		} else {
			map.put("flag", "true");
			session.put("authCheck", "true");
			session.put("tmpHadoopUserName", hadoopUserName);// 临时存储，防止验证和更新使用的是不一样的用户名
			session.put("tmpHadoopPassword", hadoopPassword);// 临时存储，防止验证和更新使用的是不一样的密码
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 更新hdfsuser表数据
	 */
	public void authUpdate() {
		Map<String, Object> map = new HashMap<>();

		// 获得session中的email
		ActionContext context = ActionContext.getContext();
		Map session = context.getSession();
		if (session.get("authCheck") == null
				|| !"true".equals(session.get("authCheck"))) {
			map.put("flag", "false");
			map.put("msg", "权限验证没用通过！");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;

		}
		if (!session.get("tmpHadoopUserName").equals(hadoopUserName)
				|| !session.get("tmpHadoopPassword").equals(hadoopPassword)) {
			map.put("flag", "false");
			map.put("msg", "验证用户名或密码被修改！");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		String email = (String) session.get("email");
		// 更新常量值
		HadoopUtils
				.updateHadoopUserNamePassword(hadoopUserName, hadoopPassword);

		map.put("flag", "true");
		map.put("msg", "更新成功!");
		session.put("hUser", hadoopUserName);// 用于更新

		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;

	}

	/**
	 * 注册
	 */
	public void register() {
		log.info("进行注册过程......");
		Map<String, Object> map = new HashMap<>();
		// 注册用户权限全部设置为用户
		hdfsUser.setAuthority(Authority.USER.ordinal());
		Integer ret = hdfsUserService.save(hdfsUser);
		if (ret > 0) {
			log.info("注册成功");
			map.put("flag", "true");
		} else {
			log.info("注册失败");
			map.put("flag", "false");
			map.put("msg", "注册失败，请联系管理员!");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 注册前的检查，是否有重email
	 */
	public void registerCheck() {
		log.info("进行注册邮箱是否已被注册过");
		Map<String, Object> map = new HashMap<>();
		HdfsUser hUser = hdfsUserService.getByEmail(hdfsUser.getEmail());
		if (hUser != null) {
			log.info("该邮件名已经注册!");
			map.put("flag", "false");
			map.put("msg", "该邮件名已经注册!");
		} else {
			map.put("flag", "true");
		}

		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 获取session中的sessionProperty对应的值
	 */
	public void getSessionValue() {
		Map<String, Object> map = new HashMap<>();
		ActionContext context = ActionContext.getContext();
		Map session = context.getSession();

		int authority = -1;
		if (session.get(sessionProperty) != null) {
			authority = (Integer) session.get(sessionProperty);
		}
		map.put("authority", authority);
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	public HdfsUserService getHdfsUserService() {
		return hdfsUserService;
	}

	@Resource
	public void setHdfsUserService(HdfsUserService hdfsUserService) {
		this.hdfsUserService = hdfsUserService;
	}

	public String getHadoopUserName() {
		return hadoopUserName;
	}

	public void setHadoopUserName(String hadoopUserName) {
		this.hadoopUserName = hadoopUserName;
	}

	public String getHadoopPassword() {
		return hadoopPassword;
	}

	public void setHadoopPassword(String hadoopPassword) {
		this.hadoopPassword = hadoopPassword;
	}

	public String getSessionProperty() {
		return sessionProperty;
	}

	public void setSessionProperty(String sessionProperty) {
		this.sessionProperty = sessionProperty;
	}

}

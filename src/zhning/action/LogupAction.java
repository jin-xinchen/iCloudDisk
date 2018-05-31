package zhning.action;

import java.io.File;
import java.io.Serializable;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionSupport;

import zhning.dao.po.User;
import zhning.service.FileService;
import zhning.service.UserService;

public class LogupAction extends ActionSupport implements Serializable {

	private String usernamesignup;
	private String passwordsignup;
	private UserService service;
	private User user;

	public void setUser(User user) {
		this.user = user;
	}

	public void setService(UserService service) {
		this.service = service;
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

	// 当进行注册前，检查参数是否正确
	public void validateLogup() {

		if ("".equals(usernamesignup) || "".equals(passwordsignup)) {
			ServletActionContext.getRequest().setAttribute("usernameerror", "用户名必须6-20位");
			ServletActionContext.getRequest().setAttribute("passworderror", "密码必须6-20位");
			addFieldError("", "");
		} else if(	usernamesignup==null||passwordsignup==null){
			ServletActionContext.getRequest().setAttribute("usernameerror", "用户名或密码不能为空");
			ServletActionContext.getRequest().setAttribute("passworderror", "用户名或密码不能为空");
			addFieldError("", "");
		
		}
		else if (usernamesignup.length() > 20 || usernamesignup.length() < 6) {
			ServletActionContext.getRequest().setAttribute("usernameerror", "用户名必须6-20位");
			addFieldError("", "");
		} else if (passwordsignup.length() > 20 || passwordsignup.length() < 6) {
			ServletActionContext.getRequest().setAttribute("passworderror", "密码必须6-20位");
			addFieldError("", "");
		}
	}

	public String logup() {

		user.setUsername(usernamesignup);
		user.setPassword(passwordsignup);
		try {
			service.createUser(user); // 如果用户已注册 下层的service会抛出异常
			// 注册成功，就在upload下分配一个私人的文件夹
			String path = ServletActionContext.getServletContext().getRealPath("WEB-INF/upload");
			File file = new File(path + File.separator + usernamesignup);
			file.mkdir();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			ServletActionContext.getRequest().setAttribute("usernameerror", "该用户已注册");
			return ERROR;
		}
	}

	public String getUsernamesignup() {
		return usernamesignup;
	}

	public void setUsernamesignup(String usernamesignup) {
		this.usernamesignup = usernamesignup;
	}

	public String getPasswordsignup() {
		return passwordsignup;
	}

	public void setPasswordsignup(String passwordsignup) {
		this.passwordsignup = passwordsignup;
	}

}

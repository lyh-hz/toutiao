package com.nowcoder.toutiao.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nowcoder.toutiao.dao.LoginTicketDAO;
import com.nowcoder.toutiao.dao.UserDAO;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.LoginTicket;
import com.nowcoder.toutiao.model.User;

@Component
public class PassportInterceptor implements HandlerInterceptor{
	
	@Autowired
	private LoginTicketDAO loginTicketDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private HostHolder hostHolder;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		hostHolder.clear();
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView modelAndView)
			throws Exception {
		if(modelAndView!=null && hostHolder.getUser()!=null) {
			modelAndView.addObject("user",hostHolder.getUser());
		}	
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String ticket=null;
		if(request.getCookies()!=null) {
			for(Cookie cookie : request.getCookies()) {
				if(cookie.getName().equals("ticket")) {
					ticket = cookie.getValue();
					break;
				}
			}
		}
		if(ticket!=null) {
			LoginTicket loginTicket =loginTicketDAO.selectByTicket(ticket);
			if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0) {
				return true;
			}
			User user = userDAO.selectById(loginTicket.getUserId());
			hostHolder.setUser(user);
		}
		return true;
	}
	
}

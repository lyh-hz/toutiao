package com.nowcoder.toutiao.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nowcoder.toutiao.async.EventHandler;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.service.MessageService;
import com.nowcoder.toutiao.util.MailSender;

@Component
public class LoginExceptionHandler implements EventHandler{
	@Autowired 
	MessageService messageService;
	
	
	@Autowired
	MailSender mailsender;
	
	

	@Override
	public void doHandle(EventModel model) {
		Message message = new Message();
		//系统账户
		message.setFromId(3);
		message.setToId(model.getActorId());
		message.setContent("你上次的登陆异常");
		message.setCreatedDate(new Date());
		messageService.addMessage(message);
		System.out.println(model.toString());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", model.getExt("username"));
		mailsender.sendWithHTMLTemplantes(model.getExt("email"), "登陆异常", 
				"mails/welcome.html", map);
	}

	@Override
	public List<EventType> getSupportEventType() {
		return Arrays.asList(EventType.LOGIN);
	}

	
}

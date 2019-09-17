package com.nowcoder.toutiao.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nowcoder.toutiao.async.EventHandler;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.service.MessageService;
import com.nowcoder.toutiao.service.UserService;

@Component
public class LikeHandler implements EventHandler {
	@Autowired
	MessageService messageService;
	
	@Autowired
	UserService userService;

	@Override
	public void doHandle(EventModel model) {
		Message message = new Message();
		User user = userService.getUser(model.getActorId());
		message.setToId(model.getEntityOwnerId());
		message.setContent("用户"+user.getName()+"赞了你的资讯，http://127.0.0.1:8080/news/"
				+String.valueOf(model.getEntityId()));
		//系统账号
		message.setFromId(3);
		message.setCreatedDate(new Date());
		messageService.addMessage(message);
	}

	@Override
	public List<EventType> getSupportEventType() {
		return Arrays.asList(EventType.LIKE);
	}

}

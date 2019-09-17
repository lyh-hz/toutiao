package com.nowcoder.toutiao.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.model.ViewObject;
import com.nowcoder.toutiao.service.MessageService;
import com.nowcoder.toutiao.service.UserService;
import com.nowcoder.toutiao.util.ToutiaoUtil;

@Controller
public class MessageController {
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	MessageService messageService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HostHolder hostHolder;

	@RequestMapping(path = { "/msg/addMessage" }, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId, @RequestParam("toId") int toId,
			@RequestParam("content") String content) {
		try {
			Message msg = new Message();
			msg.setContent(content);
			msg.setCreatedDate(new Date());
			msg.setToId(toId);
			msg.setFromId(fromId);
//			msg.setConversationId(
//					fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
			messageService.addMessage(msg);
			return ToutiaoUtil.getJSONString(0);
		} catch (Exception e) {
			logger.error("增加评论失败"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "增加评论失败");
		}
	}
	
	@RequestMapping(path = { "/msg/detail" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String conversationDetail(Model model,@RequestParam("conversationId") String conversationId) {
		try {
			List<Message> conversationList =messageService.getConversationDetail(conversationId, 0, 10);
			List<ViewObject> messages = new ArrayList<ViewObject>();
			for(Message msg : conversationList) {
				ViewObject vo = new ViewObject();
				vo.set("message", msg);
				User user = userService.getUser(msg.getFromId());
				if(user == null)
					continue;
				vo.set("headUrl", user.getHeadUrl());
				vo.set("userId", user.getId());
				messages.add(vo);
			}
			model.addAttribute("messages", messages);
		} catch (Exception e) {
			logger.error("获取详情消息失败"+e.getMessage());
		}
		return "letterDetail";
	}

	@RequestMapping(path = { "/msg/list" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String conversationList(Model model) {
		try {
			int localUserId = hostHolder.getUser().getId();
			List<Message> conversationList =messageService.getConversationList(localUserId, 0, 10);
			List<ViewObject> conversations = new ArrayList<ViewObject>();
			for(Message msg : conversationList) {
				ViewObject vo = new ViewObject();
				vo.set("conversation", msg);
				int targetId = msg.getFromId()== localUserId? msg.getToId() : msg.getFromId();
				User user = userService.getUser(targetId);
				vo.set("user", user);
				vo.set("unread", messageService.getUnreadCount(localUserId, msg.getConversationId()));
				conversations.add(vo);
			}
			model.addAttribute("conversations", conversations);
		} catch (Exception e) {
			logger.error("获取站内信列表失败"+e.getMessage());
		}
		return "letter";
	}
	
}

package com.nowcoder.toutiao.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventProducer;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.EntityType;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.service.LikeService;
import com.nowcoder.toutiao.service.NewsService;
import com.nowcoder.toutiao.util.ToutiaoUtil;

@Controller
public class LikeController {
	private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

	@Autowired
	LikeService	likeService;
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
	NewsService newsService;
	
	@Autowired
	EventProducer eventProducer;
	
    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(Model model, @RequestParam("newsId") int newsId) {
    	int userId = hostHolder.getUser().getId();
    	long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
    	newsService.updateLikeCount(newsId, (int)likeCount);
    	eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(userId)
    			.setEntityType(EntityType.ENTITY_NEWS).setEntityId(newsId)
    			.setEntityOwnerId(newsService.getById(newsId).getUserId())    			);
    	return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
    
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String disLike(Model model, @RequestParam("newsId") int newsId) {
    	int userId = hostHolder.getUser().getId();
    	long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
    	newsService.updateLikeCount(newsId, (int)likeCount);
    	return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}

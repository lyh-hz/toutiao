package com.nowcoder.toutiao.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nowcoder.toutiao.model.Comment;
import com.nowcoder.toutiao.model.EntityType;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.model.ViewObject;
import com.nowcoder.toutiao.service.CommentService;
import com.nowcoder.toutiao.service.LikeService;
import com.nowcoder.toutiao.service.NewsService;
import com.nowcoder.toutiao.service.UserService;
import com.nowcoder.toutiao.util.ToutiaoUtil;



@Controller
public class NewsController {
	
	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
	
	@Autowired
	NewsService newsService;
	
	@Autowired
	UserService	userService;
	
	@Autowired
	HostHolder hostHolder;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	LikeService likeService;
	
    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
    	try {
			String fileUrl = newsService.saveImage(file);
			if(fileUrl==null) {
				return ToutiaoUtil.getJSONString(1, "上传图片失败");
			}
			return ToutiaoUtil.getJSONString(0, fileUrl);
		} catch (Exception e) {
			logger.error("上传图片失败"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "上传失败");
		}
    }
    
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
    		HttpServletResponse response) {
    	response.setContentType("image/jpg");
    	try {
			StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)), response.getOutputStream());
		} catch (Exception e) {
			logger.error("读取图片错误"+e.getMessage());
		} 
    }
    
    @RequestMapping(path = {"/user/addNews"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
    					@RequestParam("title") String title,
						@RequestParam("link") String link) {
    	try {
			News news = new News();
			if(hostHolder.getUser()!=null) {
				news.setUserId(hostHolder.getUser().getId());
			}else {
				//匿名ID
				news.setUserId(3);
			}
			news.setImage(image);
			news.setCreatedDate(new Date());
			news.setTitle(title);
			news.setLink(link);
			newsService.addNews(news);
			return ToutiaoUtil.getJSONString(0);
		} catch (Exception e) {
			logger.error("添加资讯错误"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "发布失败");
		}
    }
    
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
		News news = newsService.getById(newsId);
		if(news!=null) {
	        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                model.addAttribute("like", 0);
            }
	        List<Comment> comments = commentService.getCommentsByEntity(newsId, EntityType.ENTITY_NEWS);
			List<ViewObject> commentVOs = new ArrayList<ViewObject>();
			for(Comment comment : comments) {
				ViewObject vo = new ViewObject();
				vo.set("comment", comment);
				vo.set("user", userService.getUser(comment.getUserId()));
				commentVOs.add(vo);
			}
			model.addAttribute("comments",commentVOs);
		}
		model.addAttribute("news", news);
		model.addAttribute("owner", userService.getUser(news.getUserId()));
    	return "detail";
    }
    
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                         @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);

        } catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }
}

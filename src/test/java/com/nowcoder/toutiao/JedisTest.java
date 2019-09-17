package com.nowcoder.toutiao;

import com.nowcoder.toutiao.model.Comment;
import com.nowcoder.toutiao.model.EntityType;
import com.nowcoder.toutiao.dao.CommentDAO;
import com.nowcoder.toutiao.dao.LoginTicketDAO;
import com.nowcoder.toutiao.dao.NewsDAO;
import com.nowcoder.toutiao.dao.UserDAO;
import com.nowcoder.toutiao.model.LoginTicket;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.util.JedisAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest


public class JedisTest {

    @Autowired
    JedisAdapter jedisAdapter;
    
//    @Test
//    public void testJedis() {
//    	jedisAdapter.set("hello", "world");
//    	Assert.assertEquals("world", jedisAdapter.get("hello"));
//    }
    
    @Test
    public void testObject() {
    	User user = new User();
        user.setHeadUrl("http://images.nowcoder.com/head/100t.png");
        user.setName("user1");
        user.setPassword("abc");
        user.setSalt("def");
        jedisAdapter.setObject("user1", user);
        
        User u = jedisAdapter.getObject("user1", User.class);
        System.err.println(ToStringBuilder.reflectionToString(u));
        Math.min(1, 1);
    }

}

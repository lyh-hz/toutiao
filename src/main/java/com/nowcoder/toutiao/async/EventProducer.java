package com.nowcoder.toutiao.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.toutiao.util.JedisAdapter;
import com.nowcoder.toutiao.util.RedisKeyUtil;

@Service
public class EventProducer {
	@Autowired
	JedisAdapter jedisAdapter;

	public boolean fireEvent(EventModel eventModel) {
		try {
			String json = JSONObject.toJSONString(eventModel);
			String key = RedisKeyUtil.getEventQueueKey();
			jedisAdapter.lpush(key, json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

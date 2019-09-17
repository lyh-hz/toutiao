package com.nowcoder.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.toutiao.util.JedisAdapter;
import com.nowcoder.toutiao.util.RedisKeyUtil;

@Service
public class LikeService {
	@Autowired
	JedisAdapter jedisAdapter;
	

	/**
	 * 喜欢返回1，不喜欢返回-1，否则为0
	 */
	public int getLikeStatus(int userId,int entityType,int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		if(jedisAdapter.sismember(likeKey, String.valueOf(userId)))
			return 1;
		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
		if(jedisAdapter.sismember(disLikeKey, String.valueOf(userId)))
			return -1;
		return 0;
	}
	
	public long like(int userId,int entityType,int entityId) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		jedisAdapter.sadd(likeKey, String.valueOf(userId));
		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
		jedisAdapter.srem(disLikeKey, String.valueOf(userId));
		return jedisAdapter.scard(likeKey);
	}
	
	public long disLike(int userId,int entityType,int entityId) {
		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
		jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		jedisAdapter.srem(likeKey, String.valueOf(userId));
		return jedisAdapter.scard(likeKey);
	}
	
}

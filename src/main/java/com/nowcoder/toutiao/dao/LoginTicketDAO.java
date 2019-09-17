package com.nowcoder.toutiao.dao;

import com.nowcoder.toutiao.model.LoginTicket;
import com.nowcoder.toutiao.model.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String iNSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id," + iNSERT_FIELDS ;

    @Insert({"insert into ", TABLE_NAME, "(", iNSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

}

package com.example.totaldemo.mapper;

import com.example.totaldemo.pojo.entity.UserLogin;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserLoginMapper {
    /**
     * 用户登录
     * @param username
     * @return
     */
    UserLogin userLoginUser(@Param("username") String username);

    /**
     * 根据用户id查出所属权限昵称
     * @param userid
     * @return
     */
    List<String> selectUserRole(@Param("userid") Integer userid);

    /**
     * 查看当前用户所属id状态
     */
    Integer selectUserState(@Param("userid") Integer userid);

    /**
     * 冻结当前用户(当jwt与redisJWT不相同时)
     */
    int freezeUser(@Param("userid") Integer userid);
}

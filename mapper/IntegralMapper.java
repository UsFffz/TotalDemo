package com.example.totaldemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface IntegralMapper {

    /**
     * 给该用户添加积分
     * @param userid
     * @param integral
     */
    void userAddIntegral(Long userid,Integer integral);
}

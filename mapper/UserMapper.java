package com.example.totaldemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;

@Mapper
@Repository
public interface UserMapper {
    BigDecimal selectUserWallet(@Param("userid") Long userid);

    Integer updateUserBalance(@Param("balance")BigDecimal balance,@Param("userid") Long userid);

}

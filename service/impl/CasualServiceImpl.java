package com.example.totaldemo.service.impl;

import com.example.totaldemo.codenum.WarnEnum;
import com.example.totaldemo.codenum.WarnMessage;
import com.example.totaldemo.ex.ServiceCode;
import com.example.totaldemo.ex.ServiceException;
import com.example.totaldemo.mapper.CouponMapper;
import com.example.totaldemo.mapper.TestPlayMapper;
import com.example.totaldemo.mapper.UserMapper;
import com.example.totaldemo.pojo.entity.BookEntity;
import com.example.totaldemo.pojo.entity.BookTestRabbit;
import com.example.totaldemo.pojo.entity.CouponEntity;
import com.example.totaldemo.pojo.entity.KafKaWarn;
import com.example.totaldemo.service.CasualService;
import com.example.totaldemo.service.IntegralService;
import com.example.totaldemo.util.BookBuySuccess;
import com.example.totaldemo.util.GetAuthenticationInfo;
import com.example.totaldemo.web.JsonResult;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CasualServiceImpl implements CasualService {

    @Autowired
    private TestPlayMapper testPlayMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private BookBuySuccess bookBuySuccess;

    @Autowired
    private IntegralService integralService;

    @Autowired
    private GetAuthenticationInfo authenticationInfo;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional
    public Map<String, Object> buyBook(Integer bookId, Integer couponId) {
        BookEntity bookEntity = testPlayMapper.selectBookByIdEntity(bookId);
        Long userid = authenticationInfo.getUserID();
        Map<String, Object> map = new HashMap();
        Integer couponUpdate = 0;
        if (bookEntity == null) {
            map.put("message", "该书不存在");
            return map;
        }
        BigDecimal price = bookEntity.getSale();
        if (couponId != null) {
            CouponEntity couponEntity = couponMapper.selectCouponById(userid, couponId);
            if (couponEntity == null) {
                map.put("message", "您未拥有该优惠卷!");
                return map;
            }
            String couponEffect = couponEntity.getCouponEffect();
            Integer startPrice;
            Integer endPrice;
            if (couponEffect.contains("-")) {
                String[] split = couponEffect.split("-");
                startPrice = Integer.valueOf(split[0]);
                BigDecimal startBigDecimal = BigDecimal.valueOf(startPrice);
                endPrice = Integer.valueOf(split[1]);
                if (price.compareTo(startBigDecimal) != -1) {
                    price = price.subtract(new BigDecimal(endPrice));
                    couponUpdate++;
                }
            } else if (couponEffect.contains("%")) {
                String salePrice = "0." + couponEffect.split("%")[0];
                System.out.println(salePrice);
                BigDecimal salePriceDecimal = new BigDecimal(salePrice);
                price = price.multiply(salePriceDecimal);
                couponUpdate++;
            }else {
                BigDecimal bigDecimal1 = new BigDecimal(couponEffect);
                price = price.subtract(bigDecimal1);
                couponUpdate++;
            }
        }
        BigDecimal bigDecimal = userMapper.selectUserWallet(userid);
        if (bigDecimal.compareTo(price) == -1){
            map.put("message","您的余额不足,请充值后再试");
            return map;
        }
        BigDecimal dueAmount = bigDecimal.subtract(price);
        Integer updateColumn = userMapper.updateUserBalance(dueAmount,userid);
        if (updateColumn < 1){
            KafKaWarn kafKaWarn = new KafKaWarn();
            kafKaWarn.setBookId(bookEntity.getBookId())
                    .setWarnCode(WarnEnum.ERROR_CODE.getNum())
                    .setWarnMessage(WarnMessage.WARN_MESSAGE_ERROR)
                    .setBookName(bookEntity.getName())
                    .setInventory(bookEntity.getInventory());
            Gson gson = new Gson();
            String message = gson.toJson(kafKaWarn);
            kafkaTemplate.send("kafkawarn", message);
            throw new ServiceException(ServiceCode.ERR_UPDATE,"因未知原因购买失败,请手动查看失败原因!");
        }
        Integer i = testPlayMapper.buyBook(bookId);
        if (i <= 0) {
            KafKaWarn kafKaWarn = new KafKaWarn();
            kafKaWarn.setBookId(bookEntity.getBookId())
                    .setWarnCode(WarnEnum.ERROR_CODE.getNum())
                    .setWarnMessage(WarnMessage.WARN_MESSAGE_ERROR)
                    .setBookName(bookEntity.getName())
                    .setInventory(bookEntity.getInventory());
            Gson gson = new Gson();
            String message = gson.toJson(kafKaWarn);
            kafkaTemplate.send("kafkawarn", message);
            map.put("message", "因未知原因购买失败,请稍后再试.");
            return map;
        }
        bookBuySuccess.userSuccessLog(bookId, bookEntity.getName(), bookEntity.getInventory());
        if (couponUpdate == 1){
            Integer updateCoupon = couponMapper.updateCouponNum(userid,couponId);
            if (updateCoupon < 1) {
                KafKaWarn kafKaWarn = new KafKaWarn();
                kafKaWarn.setBookId(bookEntity.getBookId())
                        .setWarnCode(WarnEnum.ERROR_CODE.getNum())
                        .setWarnMessage(WarnMessage.WARN_UPDATE_COUPON)
                        .setBookName(bookEntity.getName())
                        .setInventory(bookEntity.getInventory());
                Gson gson = new Gson();
                String message = gson.toJson(kafKaWarn);
                kafkaTemplate.send("kafkawarn", message);
                map.put("message", "因未知原因购买失败,请稍后再试.");
                return map;
            }
        }

        integralService.addIntegralForUser(userid, bookEntity.getIntegral());
        map.put("message", "购买成功");
        return map;
    }


    @Override
    public Map<String, Object> bookList() {
        List<BookTestRabbit> list = testPlayMapper.selectAllBook();
        Map<String, Object> map = new HashMap<>();
        map.put("message", "查询成功");
        map.put("code", 200);
        map.put("data", list);
        return map;
    }


    /**
     * 查看该用户所拥有的优惠卷及数量
     *
     * @return
     */
    @Override
    public JsonResult<List<CouponEntity>> selectCoupon() {
        Long userid = authenticationInfo.getUserID();
        List<CouponEntity> list = couponMapper.selectCouponEntity(userid);
        JsonResult<List<CouponEntity>> jsonResult = new JsonResult<>();
        jsonResult.setCode(ServiceCode.OK.toString());
        jsonResult.setMessage("查询成功");
        jsonResult.setData(list);
        return jsonResult;
    }


}

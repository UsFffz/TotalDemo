package com.example.totaldemo.util;

import org.bson.types.Decimal128;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.math.BigDecimal;


@ReadingConverter
public class Decimal128ToBigDecimalConverter implements Converter<Decimal128,BigDecimal> {

    @Override
    public BigDecimal convert(Decimal128 decimal128) {
        BigDecimal bigDecimal = decimal128.bigDecimalValue();
        return bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP);
    }
}

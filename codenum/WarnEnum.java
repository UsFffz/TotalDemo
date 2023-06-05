package com.example.totaldemo.codenum;


public enum WarnEnum {
    INSUFFICIENT_CODE("worn",500),
    ERROR_CODE("error",400),
    SUCCESS_CODE("success",200),
    ERRORBUY_CODE("errorBuy",4000);

    private WarnEnum(String name,Integer num){
        this.name = name;
        this.num = num;
    }

    private String name;
    private Integer num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}

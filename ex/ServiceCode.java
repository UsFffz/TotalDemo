package com.example.totaldemo.ex;

public enum ServiceCode {
    OK(200),
    ERR_BindRequest(40000),
    ERR_NOTFOUND(40400),
    ERR_CONFLICT(40500),
    ERR_INSERT(40900),
    ERR_DELETE(50000),
    ERR_UPDATE(50200),
    ERR_NOTFOUNDID(164100),
    ERR_HTTPSDOS(473),
    ERR_BAD_REQUEST(40000),
    ERR_UNAUTHORIZED_DISABLED(40110),
    ERR_FORBIDDEN(40300),
    ERR_UNAUTHORIZED(40100),
    ERR_JWT_EXPIRED(60000),
    ERR_JWT_PARSE(60100),
    ERR_NO_LOGIN(410);

    private int value;

    ServiceCode(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public String toString(){
        return ""+value;
    }
}

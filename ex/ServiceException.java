package com.example.totaldemo.ex;


import lombok.Data;

@Data
public class ServiceException extends RuntimeException {
    private ServiceCode serviceCode ;



    public ServiceException (ServiceCode serviceCode,String message){
        super(message);
        this.serviceCode = serviceCode;
    }

}

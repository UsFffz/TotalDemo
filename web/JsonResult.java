package com.example.totaldemo.web;



import com.example.totaldemo.ex.ServiceCode;
import com.example.totaldemo.ex.ServiceException;
import lombok.Data;

@Data
public class JsonResult<T> {

    /**
     * 业务状态码
     */
    private String code;
    /**
     * 错误时的提示消息
     */
    private String message;
    /**
     * 成功时响应的数据
     */
    private T data;

    public JsonResult() {

    }

    private JsonResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static JsonResult<Void> ok() {
        return ok(null);
    }

    public static <T> JsonResult<T> ok(T data) {
        return new JsonResult(ServiceCode.OK.toString(),null, data);

    }

    public static <T> JsonResult<T> ok(T data,String message){
        return new JsonResult(ServiceCode.OK.toString(),message,data);
    }

    public static <T> JsonResult<T> ok(String message){
        return new JsonResult(ServiceCode.OK.toString(),message,null);
    }

    public static JsonResult<Void> fail(ServiceException e) {
        return fail(String.valueOf(e.getServiceCode().getValue()), e.getMessage());
    }

    public static JsonResult<Void> fail(String state, String message) {
        return new JsonResult(state, message, null);
    }

}

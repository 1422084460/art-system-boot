package com.art.artcommon.constant;


/**
 * description
 * 自定义异常类
 * @author lou
 * @create 2022/5/7
 */
public class CustomException extends RuntimeException{

    private String code;
    private String message;

    public CustomException(){}

    public CustomException(String message){
        super(message);
    }

    public CustomException(String code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

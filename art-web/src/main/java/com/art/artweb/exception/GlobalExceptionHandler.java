package com.art.artweb.exception;

import com.art.artcommon.constant.R;
import com.art.artcommon.entity.IResult;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public IResult handlerException(Exception e, HttpServletRequest request){
        log.error("访问接口:"+request.getRequestURI()+"失败===>>"+e.getMessage());
        return IResult.fail(null,"系统异常", R.CODE_FAIL);
    }

    @ExceptionHandler(NullPointerException.class)
    public IResult handlerNullPointerException(NullPointerException e, HttpServletRequest request){
        log.error("访问接口:"+request.getRequestURI()+"失败===>>"+e.getMessage());
        return IResult.fail(null,"空指针异常", R.CODE_FAIL);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public IResult handlerTokenExpiredException(TokenExpiredException e, HttpServletRequest request){
        log.error("访问接口:"+request.getRequestURI()+"失败===>>"+e.getMessage());
        return IResult.fail(null,"token过期", R.CODE_TOKEN_EXPIRE);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public IResult handlerSignatureVerificationException(SignatureVerificationException e, HttpServletRequest request){
        log.error("访问接口:"+request.getRequestURI()+"失败===>>"+e.getMessage());
        return IResult.fail(null,"token签名无效", R.CODE_TOKEN_SIGNATURE_INVALID);
    }

    @ExceptionHandler(AlgorithmMismatchException.class)
    public IResult handlerAlgorithmMismatchException(AlgorithmMismatchException e, HttpServletRequest request){
        log.error("访问接口:"+request.getRequestURI()+"失败===>>"+e.getMessage());
        return IResult.fail(null,"token算法不一致", R.CODE_TOKEN_ALGORITHM_MISMATCH);
    }

    @ExceptionHandler(ClassCastException.class)
    public IResult handlerClassCastException(ClassCastException e, HttpServletRequest request){
        log.error("访问接口:"+request.getRequestURI()+"失败===>>"+e.getMessage());
        return IResult.fail(null,"类型转换异常", R.CODE_FAIL);
    }

}
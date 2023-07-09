package com.zml.result;

import com.zml.exception.SsyxException;
import lombok.Data;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-09 15:20
 */
@Data
public class Result<T> {
    //状态码
    private Integer code;
    //信息
    private String message;
    //数据
    private T data;

    //    私有化构造器，防止创建实例
    private Result() {
    }

    //    设置数据返回对象
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        //创建Resullt对象，设置值，返回对象
        Result<T> result = new Result<>();
        //判断返回结果中是否需要数据
        if (data != null) {
            //设置数据到result对象
            result.setData(data);
        }
        //设置其他值
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        //返回设置值之后的对象
        return result;
    }

    public static <T> Result<T> build(ResultCodeEnum resultCodeEnum) {
        //创建Resullt对象，设置值，返回对象
        Result<T> result = new Result<>();
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        //返回设置值之后的对象
        return result;
    }

    public static <T> Result<T> build(T data, SsyxException exception) {
        //创建Resullt对象，设置值，返回对象
        Result<T> result = new Result<>();
        //判断返回结果中是否需要数据
        if (data != null) {
            //设置数据到result对象
            result.setData(data);
        }
        //设置其他值
        result.setCode(exception.getCode());
        result.setMessage(exception.getMessage());
        //返回设置值之后的对象
        return result;
    }

    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail(T data) {
        return build(data, ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> ok() {
        return build(ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail() {
        return build(ResultCodeEnum.FAIL);
    }


}

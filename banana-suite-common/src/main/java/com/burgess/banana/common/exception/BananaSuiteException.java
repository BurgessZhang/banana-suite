package com.burgess.banana.common.exception;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.exception
 * @file BananaSuiteException.java
 * @time 2018/05/16 上午11:24
 * @desc 自定义异常类
 */
public class BananaSuiteException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private int code;

    public BananaSuiteException(){
        super();
    }

    public BananaSuiteException(int code,String message){
        super(message);
        this.code = code;
    }

    public BananaSuiteException(int code,String message,Throwable cause){
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

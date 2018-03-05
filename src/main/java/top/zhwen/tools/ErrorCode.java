package top.zhwen.tools;


public enum ErrorCode {
    /**
     * 系统错误信息
     * */
    OK(200, "success", "成功"),
    PERMISSION_DENIED(400, "Permission denied", "没有权限"),
    SYSTEM_ERR(400, "System is busy", "系统繁忙"),
    INVALID_CLIENTID(400, "Invalid clientid", "无效的clientid"),
    INVALID_PASSWORD(400, "User name or password is incorrect", "用户名或密码不正确"),
    INVALID_CAPTCHA(400, "Invalid captcha or captcha overdue", "无效的验证码或验证码过期"),
    INVALID_TOKEN(400, "Invalid token", "无效token"),
    /**
     * 银行卡错误信息
     * */
    CARD_INFO_ERROR(400, "Bank card gets abnormal", "银行卡获取异常"),//银行卡获取异常
    CARD_NUMBER_ERROR(400, "The card number is wrong", "银行卡卡号有误"), //银行卡获取异常
    BANK_CARD_TYPE_ABNORMALITY(400,"Bank card type abnormality","银行卡类型异常"),    //银行卡类型异常
    BANK_NAME_ERROR(400,"Bank name error","银行名错误"), //银行名错误
    BANK_ERROR(400,"A bank error","开户行错误"),//开户行错误
    BANK_PROVINCES_AND_CITIES_ERROR(400,"Bank provinces and cities error","开户省市错误"), //开户省市错误
    INCORRECT_ACCOUNT_OPENING(400,"Incorrect account opening","开户支行错误"), //开户支行错误
    BANK_PHONE_NUMBER_ERROR(400,"Phone number error","手机号错误"), //手机号错误
    BANK_ABNORMAL_VALIDITY(400,"Abnormal validity","有效期异常"), //有效期异常
    BIND_TYPE_ERROR(400,"Bank card bind type error","绑定类型错误");//绑定类型错误
    // 成员变量
    private int httpStatus;
    private String code;
    private String message;
    private int res_code;

    // 构造方法
    private ErrorCode(int httpStatus, String code, String message) {
        this.setHttpStatus(200);
        this.setRes_code(httpStatus);
        this.setCode(code);
        this.setMessage(message);
    }

    private ErrorCode() {
        this.setHttpStatus(httpStatus);
        this.setCode(code);
        this.setMessage(message);
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRes_code() {
        return res_code;
    }

    public void setRes_code(int res_code) {
        this.res_code = res_code;
    }

}

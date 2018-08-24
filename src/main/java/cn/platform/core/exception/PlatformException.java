package cn.platform.core.exception;

import cn.platform.core.property.PropertyUtils;
import cn.platform.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 平台异常
 * @Package: cn.platform.core.exception
 * @ClassName: PlatformException
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:49
 * @Version: 1.0
 */
public class PlatformException extends RuntimeException {
    private static Logger logger = LoggerFactory.getLogger(PlatformException.class);

    private String errorCode;//错误码
    private String errorMessage;//错误消息


    public PlatformException() {
    }

    public PlatformException(Throwable e) {
        super(e);
        this.errorMessage = e.getMessage();
    }

    /**
     * @param errorCode 错误码
     */
    private PlatformException(String errorCode) {
        super(assemblyErrorMessage(errorCode));
        this.errorCode = errorCode;
        this.errorMessage = assemblyErrorMessage(errorCode);
    }

    /**
     * @param errorCode 错误码
     * @param params    错误消息组织参数
     */
    public PlatformException(String errorCode, String... params) {
        super(assemblyErrorMessage(errorCode, params));
        this.errorCode = errorCode;
        this.errorMessage = assemblyErrorMessage(errorCode, params);
    }

    /**
     * @param e         异常
     * @param errorCode 错误码
     * @param params    错误消息组织参数
     */
    public PlatformException(Throwable e, String errorCode, String... params) {
        super(assemblyErrorMessage(errorCode, params), e);
        this.errorCode = errorCode;
        this.errorMessage = assemblyErrorMessage(errorCode, params);
    }

    public String getErrorCode() {
        return errorCode;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }


    /**
     * 组织错误消息
     *
     * @param errorCode
     * @param params
     * @return
     */
    private static String assemblyErrorMessage(String errorCode,
                                               String... params) {
        //占位符匹配
        String propertyMessage = PropertyUtils.getStringValue(errorCode, null);

        if (StringUtils.isEmpty(propertyMessage)) {
            if (logger.isWarnEnabled()) {
                logger.warn("错误码:[{}]未定义", errorCode);
            }
            return errorCode;
        } else {
            return StringUtils.assemblyStringMessageByParam(propertyMessage, params);
        }
    }

}

package cn.platform.core.annotation.paramcheck;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description: 参数长度校验注解
 * @Package: cn.platform.core.annotation.paramcheck
 * @ClassName: LengthCheck
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:48
 * @Version: 1.0
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface LengthCheck {
    /**
     * 参数名中文描述
     *
     * @return
     */
    String fieldDescription();

    /**
     * 最大长度
     *
     * @return
     */
    int maxLength();

    /**
     * 最小长度
     *
     * @return
     */
    int minLength() default 0;
}

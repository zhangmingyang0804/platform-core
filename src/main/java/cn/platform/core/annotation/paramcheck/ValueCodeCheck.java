package cn.platform.core.annotation.paramcheck;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description: 参数码值校验注解
 * @Package: cn.platform.core.annotation.paramcheck
 * @ClassName: ValueCodeCheck
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:48
 * @Version: 1.0
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
public @interface ValueCodeCheck {
    /**
     * 码值校验
     *
     * @return
     */
    String codeValue();

    /**
     * 字段描述
     *
     * @return
     */
    String fieldDescription();
}

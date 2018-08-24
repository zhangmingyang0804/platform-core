package cn.platform.core.annotation.paramcheck;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description:
 * @Package: cn.platform.core.annotation.paramcheck
 * @ClassName: ParamCheck
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:48
 * @Version: 1.0
 */
@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Documented
public @interface ParamCheck {
    String value() default "";
}

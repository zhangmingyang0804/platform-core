package cn.platform.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 字符串工具类
 * @Package: cn.platform.core.util
 * @ClassName: StringUtils
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:52
 * @Version: 1.0
 */
public class StringUtils {
    private static final String SPACE_STR = "";//空串
    private static final String PARAM_PLACEHOLDER_SYM = "\\{}";//参数占位符

    /**
     * 字符串空判断
     *
     * @param val 待判断数据值
     * @return
     */
    public static boolean isEmpty(String val) {
        return null == val || SPACE_STR.equals(val.trim());
    }

    /**
     * 字符串非空判断
     *
     * @param val 待判断数据值
     * @return
     */
    public static boolean isNotEmpty(String val) {
        return !isEmpty(val);
    }

    /**
     * 根据指定的数据模板切割数据
     *
     * @param template     数据模板
     * @param strVal       待切割数据
     * @param splitType    数据分割符
     * @param isCompletion 当模板与数据分割完不一致时是否自动补全(true:自动补齐)
     * @return
     */
    public static Map<String, String> splitStrWithTemplate(String template,
                                                           String strVal,
                                                           String splitType,
                                                           boolean isCompletion) {
        Map<String, String> splitMap = new HashMap<>();

        //数据模板或者待切割数据或者切割符为空时,抛出运行时异常
        if (StringUtils.isEmpty(template) ||
                StringUtils.isEmpty(strVal) ||
                StringUtils.isEmpty(splitType)) {

            throw new NullPointerException();
        } else {
            //切割数据模板
            String[] templateArr = splitStr(template, splitType, false);

            //切割原数据
            String[] strValArr = splitStr(strVal, splitType, false);

            //判断是否自动补全
            if (isCompletion) {
                //自动补全
                for (int i = 0; i < templateArr.length; i++) {
                    String splitTemplate = templateArr[i];
                    String splitVal;

                    //防止索引越界
                    try {
                        splitVal = strValArr[i];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        splitVal = SPACE_STR;
                    }
                    splitMap.put(splitTemplate.trim(), splitVal.trim());
                }
            } else {
                //非自动补全
                if (templateArr.length != strValArr.length) {
                    //当切割模板与数据不一致时抛出异常

                    throw new RuntimeException("Value size not equal template size");
                } else {
                    //组装数据
                    for (int i = 0; i < templateArr.length; i++) {
                        splitMap.put(templateArr[i].trim(), strValArr[i].trim());
                    }
                }
            }
        }
        return splitMap;
    }

    /**
     * 根据给定的分割符切割数据
     *
     * @param strVal            待切割数据
     * @param splitType         切割数据分割符
     * @param isIgnoreSplitType 是否忽略分隔符(true:是;false:否)
     * @return
     */
    public static String[] splitStr(String strVal,
                                    String splitType,
                                    boolean isIgnoreSplitType) {
        strVal = strVal.trim();
        String[] splitArr;
        if (StringUtils.isEmpty(strVal) ||
                StringUtils.isEmpty(splitType)) {
            //待切割数据及分割符不能为空
            throw new NullPointerException();
        } else {
            //根据指定的规则切割数据
            if (isIgnoreSplitType) {
                splitArr = strVal.split(splitType);
            } else {
                splitArr = strVal.split(splitType, -1);
            }
        }
        return splitArr;
    }

    /**
     * 空串专空(null)
     *
     * @param val
     * @return
     */
    public static String space2Null(String val) {
        if (StringUtils.isEmpty(val)) {
            return null;
        } else {
            return val.trim();
        }
    }

    /**
     * null转换为空串
     *
     * @param val
     * @return
     */
    public static String null2Space(String val) {
        if (StringUtils.isEmpty(val)) {
            return SPACE_STR;
        } else {
            return val.trim();
        }
    }

    /**
     * 根据消息模板及参数组装消息
     *
     * @param messsgeTemplate
     * @param params
     * @return
     */
    public static String assemblyStringMessageByParam(String messsgeTemplate, String... params) {
        if (null == params) {
            return messsgeTemplate;
        } else {
            for (String param : params) {
                messsgeTemplate = messsgeTemplate.replaceFirst(PARAM_PLACEHOLDER_SYM, param);
            }
            return messsgeTemplate;
        }
    }
}

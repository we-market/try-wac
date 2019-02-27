package com.wemarket.wac.common.utils;

import com.wemarket.wac.common.dto.BizErrors;
import com.wemarket.wac.common.exception.SysException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringNotNullUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(StringNotNullUtil.class);

    public static boolean checkFieldNotNull(Object object, BizErrors bizErrors, String... fieldsName) {
        try {
            for (String key : fieldsName) {
                String value = BeanUtils.getProperty(object, key);
                if (StringUtils.isBlank(value)) {
                    bizErrors.reject("-1", null, key + "未输入");
                }
            }
        } catch (Exception e) {
            throw new SysException("beans check field error!");
        }
        if (bizErrors.hasErrors()) {
            return false;
        }
        return true;
    }


    public static String checkFieldNotNull(Object object, String... fieldsName) {
        StringBuffer stringBuffer = new StringBuffer();

        try {
            for (String key : fieldsName) {
                String value = BeanUtils.getProperty(object, key);
                if (StringUtils.isBlank(value)) {
                    stringBuffer.append("[").append(key).append("] cannot be empty! ");
                }
            }
        } catch (Exception e) {
            throw new SysException("beans check field error!");
        }
        return stringBuffer.toString();
    }


    public static boolean checkMultiFieldNotNull(String... fields) {
        for (String field : fields) {
            if (StringUtils.isBlank(field)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
    }
}

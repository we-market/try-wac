package com.wemarket.wac.common.utils;

import com.wemarket.wac.common.annotation.WacMaskData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class WacReflectionToStringBuilder extends ReflectionToStringBuilder {

    private static final String TYPE_STRING = "java.lang.String";

    public WacReflectionToStringBuilder(final Object object) {
        super(object);
    }

    public WacReflectionToStringBuilder(final Object object, final ToStringStyle style) {
        super(object, style);
    }

    public WacReflectionToStringBuilder(final Object object, final ToStringStyle style, final StringBuffer buffer) {
        super(object, style, buffer);
    }

    public <T> WacReflectionToStringBuilder(final T object, final ToStringStyle style, final StringBuffer buffer, final Class<? super T> reflectUpToClass, final boolean outputTransients, final boolean outputStatics) {
        super(object, style, buffer, reflectUpToClass, outputTransients, outputStatics);
    }

    public static String reflectionToString(final Object object) {
        return WacReflectionToStringBuilder.toString(object);
    }

    public static String toString(final Object object) {
        return toString(object, null, false, false, null);
    }

    /**
     * toString方法。
     *
     * @param object           对象
     * @param style            风格
     * @param outputTransients whether to include transient fields。
     * @param outputStatics    输出统计
     * @param reflectUpToClass the superclass to reflect up to (inclusive), may be <code>null</code>
     * @param <T>              业务对象类型
     * @return 格式化后的字符串
     */
    public static <T> String toString(final T object, final ToStringStyle style,
                                      final boolean outputTransients, final boolean outputStatics,
                                      final Class<? super T> reflectUpToClass) {
        return new WacReflectionToStringBuilder(object, style, null, reflectUpToClass,
                outputTransients, outputStatics).toString();
    }

    @Override
    protected void appendFieldsIn(final Class<?> clazz) {
        if (clazz.isArray()) {
            this.reflectionAppendArray(this.getObject());
            return;
        }
        final Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (final Field field : fields) {
            final String fieldName = field.getName();
            if (this.accept(field)) {
                try {
                    // mumble biz logic
                    if (field.isAnnotationPresent(WacMaskData.class) && this.getValue(field) != null) {
                        WacMaskData maskAnnotation = field.getAnnotation(WacMaskData.class);
                        if (StringUtils.equals(TYPE_STRING, field.getType().getName())) {
                            // Customized for String type field
                            if (maskAnnotation.cursors().length != 0
                                    && maskAnnotation.offsets().length != 0
                                    && maskAnnotation.cursors().length == maskAnnotation.offsets().length) {
                                String value = this.getValue(field).toString();
                                int valueLength = StringUtils.length(value);
                                int[] cursors = maskAnnotation.cursors();
                                int[] offsets = maskAnnotation.offsets();
                                char replacementChar = maskAnnotation.replacementChar();
                                int cursorLength = maskAnnotation.cursors().length;
                                boolean legal = true;
                                for (int i = 0; i < cursorLength; i++) {
                                    int cursor = cursors[i];
                                    int offset = offsets[i];
                                    if ((cursor + offset) > valueLength || cursor < 0 || offset <= 0) {
                                        legal = false;
                                        break;
                                    }
                                }

                                if (legal) {
                                    for (int i = 0; i < cursorLength; i++) {
                                        int cursor = cursors[i];
                                        int offset = offsets[i];
                                        value = StringUtils.overlay(value, StringUtils.repeat(replacementChar, offset), cursor, cursor + offset);
                                    }
                                    this.append(fieldName, value);
                                    continue;
                                }
                            }
                        }
                        this.append(fieldName, maskAnnotation.wholeReplacementString());
                    } else {
                        // Warning: Field.get(Object) creates wrappers objects
                        // for primitive types.
                        final Object fieldValue = this.getValue(field);
                        this.append(fieldName, fieldValue);
                    }
                } catch (final IllegalAccessException ex) {
                    // this can't happen. Would get a Security exception
                    // instead
                    // throw a runtime exception in case the impossible
                    // happens.
                    throw new InternalError(
                            "Unexpected IllegalAccessException: " + ex.getMessage());
                }
            }
        }

    }
}

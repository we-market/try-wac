package com.wemarket.wac.common.dto;

import com.wemarket.wac.common.utils.WacReflectionToStringBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BaseDTO {

    @Override
    public String toString() {
        return WacReflectionToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object baseDTO) {
        return EqualsBuilder.reflectionEquals(this, baseDTO);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}

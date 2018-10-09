package com.easyrpc.register;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 缓存key
 *
 * @author: guanjie
 */
@AllArgsConstructor
@Getter
public class ImplementKey {
    private String contract;
    private String implCode;


    public String path() {
        return contract + "/" + implCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImplementKey that = (ImplementKey) o;
        return Objects.equal(contract, that.contract) &&
                Objects.equal(implCode, that.implCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contract, implCode);
    }
}

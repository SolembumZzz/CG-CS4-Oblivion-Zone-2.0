package com.codegym.utility;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    public static final BigDecimal FIFTY = new BigDecimal("50");
    public static final BigDecimal ONE_BILLION = new BigDecimal("1000000000");

    public static BigDecimal percentage(BigDecimal base, BigDecimal percent) {
        return base.multiply(percent).scaleByPowerOfTen(-2);
    }
}

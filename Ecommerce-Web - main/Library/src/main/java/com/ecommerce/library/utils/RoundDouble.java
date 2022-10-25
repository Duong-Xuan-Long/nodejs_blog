package com.ecommerce.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class RoundDouble {
    public double roundAvoid(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }
}

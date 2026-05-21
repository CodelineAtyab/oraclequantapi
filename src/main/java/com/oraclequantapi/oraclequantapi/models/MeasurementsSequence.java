package com.oraclequantapi.oraclequantapi.models;

import static sun.security.krb5.internal.ktab.KeyTab.normalize;

public class MeasurementsSequence {
    
    public String value;
    
    public MeasurementsSequence(String value){
        setValue(value);
    }

    //  capital letters converted into lowercases
    //  Anything than letters and underscore will be converted into underscore
    private void setValue(String value) {
        if (value == null) {
            this.value = null;
        } else {
            this.value = normalize(value);
        }
    }
}

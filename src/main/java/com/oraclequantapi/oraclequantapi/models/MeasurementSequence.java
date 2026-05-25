package com.oraclequantapi.oraclequantapi.models;


import java.util.Locale;

public class MeasurementSequence {
    
    public String value;
    
    public MeasurementSequence(String value){
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

    public String getValueAsString() {
        return value;
    }

    // After normalization, the value is valid as long as it is not null.
    public boolean isValid() {
        return value != null;
    }


    private String normalize (String value){
        String lowerValue = value.toLowerCase(Locale.ROOT);
        StringBuilder normalizedValue = new StringBuilder();

        for (int index = 0; index < lowerValue.length(); index++) {
            char current = lowerValue.charAt(index);

            if ((current >= 'a' && current <= 'z') || current == '_') {
                normalizedValue.append(current);
            }else {
                normalizedValue.append('_');
            }
        }
        return normalizedValue.toString();
    }
}

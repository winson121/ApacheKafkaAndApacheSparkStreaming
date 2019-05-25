package com.iot.app.spark.util;

import java.lang.reflect.Field;

public class ShowFields {
    public String showFields(Object o) {
        Class<?> clazz = o.getClass();
        String fields = "";
        for(Field field : clazz.getDeclaredFields()) {
            //you can also use .toGenericString() instead of .getName(). This will
            //give you the type information as well.
     
            fields += field.getName();
        }
        return fields;
    }
}
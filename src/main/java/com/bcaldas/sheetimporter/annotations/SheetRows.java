package com.bcaldas.sheetimporter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SheetRows {

    int min();
    int max();
    String message() default "Number of rows is not valid";
}

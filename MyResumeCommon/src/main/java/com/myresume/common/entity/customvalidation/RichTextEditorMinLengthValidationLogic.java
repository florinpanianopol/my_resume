package com.myresume.common.entity.customvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RichTextEditorMinLengthValidationLogic implements ConstraintValidator<RichTextEditorMinLength, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String temp = value.replaceAll("\\<.*?\\>", "").replace("&nbsp;","");
        return temp.trim().length()>=3;
    }

    @Override
    public void initialize(RichTextEditorMinLength constraintAnnotation) {

    }
}

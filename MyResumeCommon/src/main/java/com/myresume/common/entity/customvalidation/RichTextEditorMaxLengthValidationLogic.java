package com.myresume.common.entity.customvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RichTextEditorMaxLengthValidationLogic implements ConstraintValidator<RichTextEditorMaxLength, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String temp = value.replaceAll("\\<.*?\\>", "").replace("&nbsp;","");
        return temp.trim().length()<=1000;
    }

    @Override
    public void initialize(RichTextEditorMaxLength constraintAnnotation) {

    }
}

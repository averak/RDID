package dev.abelab.rdid.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.abelab.rdid.api.request.BaseRequest;

public class RequestValidator implements ConstraintValidator<RequestValidation, BaseRequest> {

    @Override
    public void initialize(RequestValidation constraintAnnotation) {}

    @Override
    public boolean isValid(final BaseRequest baseRequest, final ConstraintValidatorContext constraintValidatorContext) {
        baseRequest.validate();
        return true;
    }

}

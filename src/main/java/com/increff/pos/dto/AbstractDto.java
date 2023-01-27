package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.Set;

@Service
public abstract class AbstractDto<T> {

    Class<T> clazz;

    public AbstractDto() {
        this.clazz = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void checkNull(Object object, String message) throws ApiException {
        if (Objects.isNull(object)) {
            throw new ApiException(message);
        }
    }

    protected void validate(T form) throws ApiException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        for (ConstraintViolation<T> violation : violations) {
            throw new ApiException(violation.getMessage());
        }
    }

    /**
     * Tried to create generic function for converting pojo to data
     */
//    public List<T> pojoToData() {
//        return api.getAll().stream().map(ProductHelper::convert).collect(Collectors.toList());
//    }


}

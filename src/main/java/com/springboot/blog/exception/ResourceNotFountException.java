package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFountException extends RuntimeException {
    private String resourceName;
    private String field_name;
    private long field_value;

    public ResourceNotFountException(String resourceName, String field_name, long field_value) {
        super(String.format("%s not found with %s : '%s'", resourceName, field_name, field_value));
        this.resourceName = resourceName;
        this.field_name = field_name;
        this.field_value = field_value;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getField_name() {
        return field_name;
    }

    public long getField_value() {
        return field_value;
    }
}

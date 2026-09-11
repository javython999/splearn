package com.errday.splearn.support.stereotype;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Adapter
@RestController
public @interface WebApiAdapter {
}

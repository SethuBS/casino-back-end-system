package com.rank.interactive.services;

import org.springframework.stereotype.Component;


@Component
public interface PassWordValidatorService {
    String validate(String passWord);
    String getPassword();
}

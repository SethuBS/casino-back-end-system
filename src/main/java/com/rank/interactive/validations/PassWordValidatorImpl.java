package com.rank.interactive.validations;

import com.rank.interactive.services.PassWordValidatorService;
import org.springframework.stereotype.Service;

@Service
public class PassWordValidatorImpl implements PassWordValidatorService {
    private final String validPassWord = "swordfish";
    @Override
    public String validate(String passWord) {
        if(!passWord.equals(validPassWord)){
            return "Incorrect password";
        } else {
            return validPassWord;
        }
    }

    @Override
    public String getPassword() {
        return validate(validPassWord);
    }
}

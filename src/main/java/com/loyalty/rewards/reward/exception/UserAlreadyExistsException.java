package com.loyalty.rewards.reward.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String userName){
        super("User Already exists with UserName: " + userName);
    }
}

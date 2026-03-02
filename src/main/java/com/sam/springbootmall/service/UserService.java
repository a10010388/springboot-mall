package com.sam.springbootmall.service;

import com.sam.springbootmall.dto.UserLoginRequest;
import com.sam.springbootmall.dto.UserRegisterRequest;
import com.sam.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);
}

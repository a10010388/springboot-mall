package com.sam.springbootmall.dao;

import com.sam.springbootmall.dto.UserRegisterRequest;
import com.sam.springbootmall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);
}

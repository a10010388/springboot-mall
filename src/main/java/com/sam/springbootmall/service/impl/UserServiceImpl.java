package com.sam.springbootmall.service.impl;

import com.sam.springbootmall.dao.UserDao;
import com.sam.springbootmall.dto.UserLoginRequest;
import com.sam.springbootmall.dto.UserRegisterRequest;
import com.sam.springbootmall.model.User;
import com.sam.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        if(user != null){
            logger.warn("already exist email{}:",user.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        logger.info(userLoginRequest.getEmail());
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if(user == null){
            logger.warn("not exist email{}:",userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(user.getPassword().equals(userLoginRequest.getPassword())){
            return user;
        }else {
            logger.warn("password not equals user,user email:{}",user.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
}

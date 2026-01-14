package com.hanxw.project.service;

import com.hanxw.project.entity.UserEntity;

public interface UserService {
    UserEntity getUserById(Long id);

    void updateUser(UserEntity userEntity);
}

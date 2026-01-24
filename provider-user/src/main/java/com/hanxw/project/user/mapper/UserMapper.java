package com.hanxw.project.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanxw.project.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper（复用原有）
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}

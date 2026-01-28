package com.hanxw.project.user.service;

import com.hanxw.project.api.UserRpcService;
import com.hanxw.project.api.dto.UserDTO;
import com.hanxw.project.common.constants.DubboConstant;
import com.hanxw.project.common.enums.ErrorCode;
import com.hanxw.project.common.result.Result;
import com.hanxw.project.common.service.CacheService;
import com.hanxw.project.user.entity.UserEntity;
import com.hanxw.project.user.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 用户服务 RPC 实现
 * 
 * 基于原有 UserServiceImpl 改造，添加 Dubbo 注解
 * 
 * @DubboService 配置说明：
 * - version: 服务版本
 * - group: 服务分组
 * - timeout: 超时时间
 * - retries: 重试次数
 */
@DubboService(
    version = DubboConstant.VERSION,
    group = DubboConstant.GROUP_USER,
    timeout = DubboConstant.TIMEOUT_DEFAULT,
    retries = DubboConstant.RETRIES_DEFAULT
)
public class UserRpcServiceImpl implements UserRpcService {

    private static final Logger log = LoggerFactory.getLogger(UserRpcServiceImpl.class);
    private static final String USER_CACHE_KEY = "user:detail:";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public Result<UserDTO> getUserById(Long id) {
        log.info("[UserRpcService] getUserById, id={}", id);

        if (id == null || id <= 0) {
            return Result.fail(ErrorCode.PARAM_ERROR);
        }

        // 先查缓存
        String cacheKey = USER_CACHE_KEY + id;
        UserDTO cached = cacheService.get(cacheKey,UserDTO.class);
        if (cached != null) {
            log.info("命中缓存, id={}", id);
            return Result.success(cached);
        }

        // 查数据库
        UserEntity entity = userMapper.selectById(id);
        if (entity == null) {
            return Result.fail(ErrorCode.USER_NOT_FOUND);
        }

        // 转换并缓存
        UserDTO dto = convertToDTO(entity);
        cacheService.set(cacheKey, dto, 1, TimeUnit.HOURS);

        return Result.success(dto);
    }

    @Override
    public Result<Boolean> updateUser(UserDTO userDTO) {
        log.info("[UserRpcService] updateUser, id={}", userDTO.getId());

        if (userDTO.getId() == null) {
            return Result.fail(ErrorCode.PARAM_ERROR);
        }

        UserEntity entity = userMapper.selectById(userDTO.getId());
        if (entity == null) {
            return Result.fail(ErrorCode.USER_NOT_FOUND);
        }

        // 更新
        BeanUtils.copyProperties(userDTO, entity);
        userMapper.updateById(entity);

        // 删除缓存
        String cacheKey = USER_CACHE_KEY + userDTO.getId();
        cacheService.delete(cacheKey);

        return Result.success(true);
    }

    @Override
    public Result<Boolean> existsById(Long id) {
        log.info("[UserRpcService] existsById, id={}", id);

        if (id == null || id <= 0) {
            return Result.success(false);
        }

        UserEntity entity = userMapper.selectById(id);
        return Result.success(entity != null);
    }

    /**
     * Entity 转 DTO
     */
    private UserDTO convertToDTO(UserEntity entity) {
        if (entity == null) return null;
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}

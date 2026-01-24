# Week 8 | Dubbo + RPC（基于现有工程拆分）

> **目标：真正理解"微服务不是 HTTP"**

## 一、工程拆分方案

### 原有单体结构
```
src/main/java/com/hanxw/project/
├── common/          # 公共组件
├── controller/      # 控制器（UserController、OrderController）
├── service/         # 服务层（UserService、OrderService）
├── mapper/          # 数据访问
└── entity/          # 实体类
```

### 拆分后的多模块结构
```
Java Project Study/
├── pom.xml                    # 父POM（聚合管理）
├── project-common/            # 公共模块
│   └── Result, ErrorCode, BizException, DubboConstant
├── project-api/               # API模块（RPC接口定义）
│   └── UserRpcService, OrderRpcService, DTO
├── provider-user/             # 用户服务提供者
│   └── UserRpcServiceImpl（基于原UserServiceImpl改造）
├── provider-order/            # 订单服务提供者
│   └── OrderRpcServiceImpl（基于原OrderServiceImpl改造）
├── project-web/               # Web网关（消费者）
│   └── UserController, OrderController（调用RPC服务）
└── src/                       # 保留原单体应用（对比参考）
```

## 二、服务调用关系

```
                    ┌─────────────────┐
                    │   project-web   │
                    │   (Web 网关)    │
                    │    :8080        │
                    └────────┬────────┘
                             │ Dubbo RPC
              ┌──────────────┴──────────────┐
              │                             │
              ▼                             ▼
    ┌─────────────────┐           ┌─────────────────┐
    │  provider-user  │◀──────────│ provider-order  │
    │     :8081       │  RPC调用   │     :8082       │
    │  Dubbo:20881    │           │  Dubbo:20882    │
    └────────┬────────┘           └────────┬────────┘
             │                             │
             ▼                             ▼
    ┌─────────────────────────────────────────────────┐
    │              MySQL: java_project_study          │
    │                    (同一数据库)                  │
    └─────────────────────────────────────────────────┘
```

## 三、核心改造点

### 1. 原 UserService → UserRpcService

**原有接口：**
```java
public interface UserService {
    UserEntity getUserById(Long id);
    void updateUser(UserEntity userEntity);
}
```

**改造后的 RPC 接口：**
```java
public interface UserRpcService {
    Result<UserDTO> getUserById(Long id);
    Result<Boolean> updateUser(UserDTO userDTO);
    Result<Boolean> existsById(Long id);
}
```

### 2. 服务暴露 (@DubboService)

```java
@DubboService(
    version = "1.0.0",
    group = "user-group",
    timeout = 3000,
    retries = 2
)
public class UserRpcServiceImpl implements UserRpcService {
    // 复用原有业务逻辑
}
```

### 3. 服务引用 (@DubboReference)

```java
@DubboReference(
    version = "1.0.0",
    group = "user-group",
    check = false,
    mock = "com.hanxw.project.web.mock.UserRpcServiceMock"
)
private UserRpcService userRpcService;
```

### 4. 服务降级 (Mock)

```java
public class UserRpcServiceMock implements UserRpcService {
    @Override
    public Result<Boolean> existsById(Long id) {
        log.warn("[降级] existsById");
        return Result.fail(ErrorCode.SERVICE_DEGRADED);
    }
}
```

## 四、快速启动

```bash
# 1. 启动基础设施
docker-compose up -d nacos mysql redis

# 2. 等待 Nacos 启动
# 访问 http://localhost:8848/nacos (nacos/nacos)

# 3. 编译项目
mvn clean install -DskipTests

# 4. 在 IDEA 中按顺序启动
#    UserProviderApplication  → :8081
#    OrderProviderApplication → :8082
#    WebApplication           → :8080

# 5. 测试接口
curl http://localhost:8080/user/1
curl http://localhost:8080/order/1
curl http://localhost:8080/order/user/1
```

## 五、Week 8 学习目标

| 目标 | 状态 | 说明 |
|------|------|------|
| Dubbo 架构 | ✅ | Provider-Consumer 模式 |
| 注册中心 | ✅ | Nacos :8848 |
| 服务暴露 | ✅ | @DubboService |
| 服务引用 | ✅ | @DubboReference |
| 负载均衡 | ✅ | roundrobin/random |
| 超时配置 | ✅ | 3000ms |
| 重试策略 | ✅ | 读2次，写0次 |
| 服务降级 | ✅ | Mock 实现 |
| 2-3个服务拆分 | ✅ | provider-user, provider-order |

## 六、模块说明

| 模块 | 端口 | 说明 |
|------|------|------|
| project-common | - | 公共组件 |
| project-api | - | RPC 接口定义 |
| provider-user | 8081/20881 | 用户服务 |
| provider-order | 8082/20882 | 订单服务 |
| project-web | 8080 | Web 网关 |
| Nacos | 8848 | 注册中心 |

## 七、与原单体应用的对比

| 对比项 | 单体应用 | 微服务拆分后 |
|--------|----------|--------------|
| 调用方式 | 本地方法调用 | Dubbo RPC |
| 部署 | 单一 JAR | 多个独立服务 |
| 扩展 | 整体扩展 | 按需扩展 |
| 故障隔离 | 全局影响 | 局部隔离 + 降级 |
| 原有代码 | src/ 目录保留 | 复用到各模块 |

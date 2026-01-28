# Mock 定义（Dubbo Mock / 服务降级）

## 1. Mock 是什么

在 Dubbo 里，**Mock** 通常用于“服务降级/兜底”：当远程服务不可用（超时、网络异常、注册中心异常、服务未启动等）时，让调用方自动走一个本地实现，返回可控结果，避免调用方直接报错或对外接口直接 500。

常见配置方式是在 **消费端** 的 `@DubboReference` 上配置：

```java
@DubboReference(
    check = false,
    timeout = 2000,
    retries = 2,
    mock = "com.xxx.UserRpcServiceMock"
)
private UserRpcService userRpcService;
```

> 结论：**谁写 `@DubboReference` 调用别人，谁是“消费端”，Mock 就应该放在谁的工程里。**

---

## 2. 为什么 Mock 类会分散在不同模块

一个工程既可能是“服务提供者”（`@DubboService`），也可能同时是“服务消费者”（`@DubboReference` 调用别的服务）。

因此：

- **对外提供 RPC 的模块（Provider）**：负责实现接口（`@DubboService`）
- **调用别人 RPC 的模块（Consumer）**：需要降级兜底（Mock）

当 `provider-order` 调用 `provider-user` 时，`provider-order` 也就成了 **UserRpcService 的消费端**，它就需要 `UserRpcServiceMock`（如果你配置了 `mock=...`）。

---

## 3. 三个模块的 Mock 分别能实现什么功能

### 3.1 `project-web` 的 Mock：网关兜底，让对外 REST 不崩

`project-web` 通过 Dubbo RPC 调用：

- `provider-user`（用户服务）
- `provider-order`（订单服务）

因此 `project-web` 的 Mock 通常用于 **对外接口降级**：

- **`UserRpcServiceMock`**
  - 作用：当用户服务不可用时，网关依然返回统一结构的降级结果（例如 `Result.fail(ErrorCode.SERVICE_DEGRADED)`），而不是直接抛异常导致 500。
  - 常见场景：查询用户、校验用户存在、用户信息接口降级。
- **`OrderRpcServiceMock`**
  - 作用：当订单服务不可用时，网关对订单相关接口返回可控降级结果（可选择返回失败/空列表/提示“系统繁忙”）。
  - 常见场景：查询订单、查询用户订单、订单详情接口降级。

一句话：**`project-web` 的 Mock 是“对外 API 的降级保护”。**

---

### 3.2 `provider-order` 的 Mock：服务间依赖兜底，防止链路级雪崩

如果 `provider-order` 在业务中通过 `@DubboReference` 调用 `provider-user`（比如下单前校验用户、查询用户信息），那么 `provider-order` 就是用户 RPC 的消费端。

此时 `provider-order` 的 Mock 用于 **内部依赖降级**，避免“用户服务挂了 → 订单服务也挂”：

- **`UserRpcServiceMock`（示例）**
  - 场景：创建订单时 `existsById(userId)` 超时/异常
  - 可选策略：
    - **严格策略**：直接返回失败（不允许下单，保证数据强一致）
    - **宽松策略**：假设用户存在 / 返回缓存值（保证核心链路可用，承担一定风险）

一句话：**`provider-order` 的 Mock 是“服务间调用的降级保护”。**

---

### 3.3 `provider-user` 的 Mock：只有当它也“消费别人服务”才有意义

如果 `provider-user` 没有通过 `@DubboReference` 去调用其他服务，那么它的 Mock 一般 **没有实际用途**（属于冗余文件）。

只有当 `provider-user` 也依赖别的服务（例如短信/风控/积分/订单聚合等）时：

- `provider-user` 才会需要对应的 Mock，用来在依赖不可用时做降级兜底。

一句话：**`provider-user` 的 Mock 用于“用户服务依赖其它服务时的降级”。不消费则无需 Mock。**

---

## 4. 如何判断某个 Mock 是否应该存在

**最简单的判断标准：有没有被 `@DubboReference(mock="...")` 引用。**

- **被引用**：应该保留（它确实承担了降级功能）
- **未被引用**：可以删除（避免项目里出现“看起来有，但实际没用”的 Mock）

---

## 5. 建议的实践

- **Mock 返回值要“可控”**：统一返回 `Result.fail(...)` / 合理的默认值，避免误导业务
- **日志要清晰**：Mock 被触发时打印降级日志，方便排查依赖故障
- **策略要明确**：不同接口可采用不同降级策略（失败、空列表、缓存兜底等）


com.hanxw.project.common
├── config
│   ├── JacksonConfig.java
│   └── RedisConfig.java
├── constants
│   └── DubboConstant.java
├── enums
│   └── ErrorCode.java
├── exception
│   ├── BizException.java
│   └── GlobalExceptionHandler.java
├── logging
│   └── ApiLogAspect.java
├── mybatis
│   ├── MybatisPlusConfig.java
│   └── handler
│       └── MyMetaObjectHandler.java
├── redis
│   ├── RateLimit.java
│   ├── cache
│   │   └── CacheService.java
│   ├── limiter
│   │   ├── RateLimiterInterceptor.java
│   │   └── RedisRateLimiter.java
│   ├── lock
│   │   ├── RedisDistributedLock.java
│   │   └── RedissonConfig.java
│   └── queue
│       └── RedisDelayQueue.java
├── result
│   └── Result.java
├── security
│   └── jwt
│       ├── JwtUtil.java
│       └── TokenService.java
└── web
    ├── annotation
    │   └── LoginRequired.java
    ├── context
    │   └── RequestContext.java
    └── interceptor
        ├── AuthInterceptor.java
        └── TraceInterceptor.java
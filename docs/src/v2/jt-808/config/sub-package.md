---
icon: config
---

# sub-package

::: info 提示

该章节介绍的是 `分包消息` 相关的配置。

:::

## 配置项总览

## request-sub-package-storage

请求分包消息暂存器相关配置。

```yaml
jt808:
  request-sub-package-storage:
    type: caffeine # caffeine || none
    ## 当且仅当 jt808.request-sub-package-storage.type = caffeine 时生效
    caffeine:
      # 最多缓存多少条消息
      maximum-size: 1024
      # 最大缓存时间
      ttl: 45s
```

## response-sub-package-storage

响应分包消息暂存器相关配置。

```yaml
jt808:
  response-sub-package-storage:
  type: caffeine # caffeine || redis || none
  ## 当且仅当 jt808.response-sub-package-storage.type = caffeine 时生效
  caffeine:
    # 最多缓存多少条消息
    maximum-size: 1024
    # 最大缓存时间
    ttl: 45s
  ## 当且仅当 jt808.response-sub-package-storage.type = redis 时生效
  redis:
    # 最大缓存时间
    ttl: 1m
```

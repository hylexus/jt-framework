---
icon: guide
---

# overview

## 配置项总览

```yaml
jt808:
  built-components:
  # ...
  features: # v2.1.1
  # ...
  protocol:
  # ...
  server:
  # ...
  msg-processor:
  # ...
  request-sub-package-storage:
  # ...
  response-sub-package-storage:
  # ...
```

## 默认配置

默认的配置可以在
[default-jt808-server-config.yml](https://github.com/hylexus/jt-framework/blob/master/jt-808-server-spring-boot-autoconfigure/src/main/resources/META-INF/default-jt808-server-config.yml)
中查看。 并且已经将默认的配置加入到了 `Spring` 的 `PropertySources` 中，并将其置于最后，名称为 `default-jt808-server-config` 。

<p class="">
    <img :src="$withBase('/img/v2/config/default-config-property-source.png')" alt="default-config-property-source">
</p> 

---
icon: proposal
---

# 苏标扩展(v2.1.4)

```yaml
jt808:
  ### ...(省略其他配置)
  ## 苏标附件服务器
  attachment-server:
    ## 是否启用(默认: false)
    enabled: true
    # 附件服务器端口(TCP)
    port: 6809
    # this.bossGroup=new NioEventLoopGroup(bossThreadCount);
    boss-thread-count: 2
    # this.workerGroup=new NioEventLoopGroup(workThreadCount);
    worker-thread-count: 4
    ## 0x30316364附件上传报文的最大长度(66560 = 1024 * 65)
    max-frame-length: 66560
    # 处理附件相关指令(0x1210,0x1211,0x1212,0x6364)的线程池线配置
    msg-processor:
      ## 业务线程池配置
      executor-group:
        max-pending-tasks: 64
        pool-name: 808-attachment-processor
        thread-count: 16
```

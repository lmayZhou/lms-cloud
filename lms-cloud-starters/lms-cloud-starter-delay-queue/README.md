# lms-cloud-starter-delay-queue

#### 介绍
  Delay Queue Starter 任务延迟队列，采用 Redis Sorted Set是一个有序的集合，元素的排序基于加入集合时指定的score。
通过ZRANGEBYSCORE命令，我们可以取得score在指定区间内的元素。将集合中的元素做为消息，score视为延迟的时间，这便是一个延迟队列的模型。
  消费者通过ZRANGEBYSCORE获取消息。如果时间未到，将得不到消息；当时间已到或已超时，都可以得到消息。
  使用ZRANGEBYSCORE取得消息后，消息并没有从集合中删出。消费者组合使用ZRANGEBYSCORE和ZREM的过程不是原子的，当有多个消费者时会存在竞争，
可能使得一条消息被消费多次。消息推送端使用了分布式同步锁，防止重复推送相同消息队列。消费端也需加相关幂等防止重复消费并执行相关操作。

  kafka是一个分布式、高吞吐量、高扩展性的消息队列系统。一个消息系统负责将数据从一个应用传递到另外一个应用，应用只需关注于数据， 
无需关注数据在两个或多个应用间是如何传递的。分布式消息传递基于可靠的消息队列，在客户端应用和消息系统之间异步传递消息。
  有两种主要的消息传递模式：点对点传递模式、发布-订阅模式。大部分的消息系统选用发布-订阅模式。Kafka就是一种发布-订阅模式。

  Guava-Retrying 任务处理异常或失败，重试的机制。

#### 软件架构
##### 版本依赖
```text
Java                    1.8
Spring Boot             2.3.3.RELEASE
Spring Cloud            Hoxton.SR8
Spring Cloud Alibaba    2.2.2.RELEASE
```

##### 项目结构

```text
├── lms-cloud										根目录
│   ├── ...
│   ├── lms-cloud-starters							Spring Boot Starter 自动配置服务
│   │   ├── ...
│   │   └── lms-cloud-starter-delay-queue           延迟队列自动配置服务
```

#### 服务使用

##### 依赖

1. Gradle 方式

```groovy
implementation 'com.lmaye:lms-cloud-starter-delay-queue:1.3.0'
```

2. Maven 方式

```xml
<dependency>
    <groupId>com.lmaye</groupId>
    <artifactId>lms-cloud-starter-delay-queue</artifactId>
    <version>1.3.0</version>
</dependency>
```

##### 配置说明

```yaml
# 延迟队列
delay-queue:
  # 是否启用
  enabled: true
  # 线程池大小
  core-pool-size: 10
  # 初始延迟时间(单位: s)，延迟第一次执行的时间
  initial-delay: 0
  # 线程延迟时间(单位: s)，一个执行的终止和下一个执行的开始之间的延迟
  delay: 30
  # 任务队列缓存Key
  queue-cache-key: DelayQueue
  # 分布式锁
  lock-key: Locked
  # 重试次数
  retry-nums: 3
  # 重试睡眠时间(单位: s)
  retry-sleep-time: 3
```

##### 使用示例
hosts

192.168.30.253 hts-kafka

1. 项目配置

```yaml
spring:
  redis:
    host: lms-redis
    port: 6379
    password: 123456
    database: 0
  kafka:
    bootstrap-servers: ${KAFKA_HOST:lms-kafka}:9092
    # 生产者配置
    producer:
      acks: 1
      batch-size: 16384
      buffer-memory: 33554432
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      retries: 0
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    # 消费者配置
    consumer:
      auto-commit-interval: 100
      auto-offset-reset: earliest
      enable-auto-commit: false
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      max-poll-records: 500
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    listener:
      ack-mode: manual_immediate
      poll-timeout: 500S

# 延迟队列
delay-queue:
  # 是否启用
  enabled: true
  # 线程池大小
  core-pool-size: 10
  # 初始延迟时间(单位: s)，延迟第一次执行的时间
  initial-delay: 0
  # 线程延迟时间(单位: s)，一个执行的终止和下一个执行的开始之间的延迟
  delay: 30
  # 队列缓存Key
  queue-cache-key: DelayQueue
  # 分布式锁
  lock-key: Locked
  # 重试次数
  retry-nums: 3
  # 重试睡眠时间(单位: s)
  retry-sleep-time: 3
```

2. 延迟任务放入缓存

```java
@SpringBootTest
class ExampleApplicationTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private DelayQueueProperties properties;

    @Test
    void contextLoads() {
        int x = 3;
        while(x > 0) {
            DelayQueueBody notice = DelayQueueBody.builder().serialNo(UUID.randomUUID().toString())
                    // 消息主题
                    .topic("topic.msg.delay.notice")
                    // 任务内容(msg)，这里仅供参考
                    .msg(String.format("%d分钟后通知", x)).build();
            // score 执行时间
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, x);
            // 放入Redis缓存ZSet中
            redisTemplate.opsForZSet().add(properties.getQueueCacheKey(), GsonUtils.toJson(notice), calendar.getTimeInMillis());

            DelayQueueBody invalidation = DelayQueueBody.builder().serialNo(UUID.randomUUID().toString())
                    .topic("topic.msg.delay.invalidation").msg(String.format("%d分钟后订单失效", x + 1)).build();
            calendar.add(Calendar.MINUTE, 1);
            redisTemplate.opsForZSet().add(properties.getQueueCacheKey(), GsonUtils.toJson(invalidation), calendar.getTimeInMillis());
            --x;
        }
    }
}
```

3. Kafka Consumer 任务执行

```java
@Slf4j
@Component
public class KafkaConsumer {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 消费
     *
     * @param record 消息记录
     * @param ack    消息确认
     * @throws InterruptedException 异常
     */
    @KafkaListener(topics = "topic.msg.delay.notice", groupId = "delay-queue-001")
    public void consumerNotice(ConsumerRecord<String, String> record, Acknowledgment ack) throws InterruptedException {
        DelayQueueBody body = GsonUtils.fromJson(record.value(), DelayQueueBody.class);
        // TODO 测试使用Redis做幂等，实际业务按实际需求
        String key = "ConsumeFlag:" + body.getSerialNo();
        Boolean flag = (Boolean) redisTemplate.opsForValue().get(key);
        if(Boolean.TRUE.equals(flag)) {
            // 避免消费者重复消费
            ack.acknowledge();
            return;
        }
        log.info("【{}】{} 接收到kafka消息: {}", record.topic(), record.offset(), record.value());
        // TODO body.getMsg() 获取消息，做相关业务处理
        TimeUnit.SECONDS.sleep(1);
        ack.acknowledge();
        redisTemplate.opsForValue().set(key, true, 60, TimeUnit.SECONDS);
    }

    /**
     * 消费
     * - 未做幂等
     *
     * @param record 消息记录
     * @param ack    消息确认
     * @throws InterruptedException 异常
     */
    @KafkaListener(topics = "topic.msg.delay.invalidation", groupId = "delay-queue-001")
    public void consumerInvalidation(ConsumerRecord<String, String> record, Acknowledgment ack) throws InterruptedException {
        log.info("【{}】{} 接收到kafka消息: {}", record.topic(), record.offset(), record.value());
        TimeUnit.SECONDS.sleep(1);
        ack.acknowledge();
    }
}
```

#### Git 格式规范

例如:
```text
feat: 新增XXX功能接口.
```

用于说明本次commit的类别，只允许使用下面7个标识
```text
1.  feat        新功能（feature）
2.  fix         修补bug
3.  docs        文档（documentation）
4.  style       格式（不影响代码运行的变动）
5.  refactor    重构（即不是新增功能，也不是修改bug的代码变动）
6.  test        增加测试
7.  chore       构建过程或辅助工具的变动
```

#### 发布版本

SNAPSHOT    ----  快照版本

RELEASE     ----  正式版本

发布 RELEASE 版本时需修改版本号，同一版本号发布会失败。

首先修改根目录下 build.gradle 配置文件中的版本号(eg: version = '1.0.23-RELEASE')，然后执行发布命令。

```text
# Gradle/Maven 发布命令
Gradle: gradle publish
Maven: mvn clean deploy
```


#### 版本说明

后续每次迭代一个版本，版本号需 +1及说明新增/修复/调整了些什么功能；
目前服务已新增test/dev分支，合并步骤顺序: 当前版本(V1.3.0) -> dev -> test -> master

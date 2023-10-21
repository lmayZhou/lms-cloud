# lms-cloud-starter-serial-no

#### 介绍

  基于 Spring Boot 自定义快速依赖集合，可快速集成于项目中，从而避免重复编写！ 通过自定义Spring Boot Starter(自动化配置)的特性，来实现快速的将各个自动配置服务快速引入我们的Spring Boot应用服务中。
如有更好的方案和idea，欢迎互相交流！如您觉得该项目对您有所帮助，欢迎点击右上方的Star标记，给予支持！！！谢谢 ~ ~

  Serial No Starter 分布式业务序列号，生成自由组合的业务编号。
```text
生成业务序号
   示例
   字节长度    4         6          8
   序号解析  业务标识 + 年月日    + Redis全局ID
            1001      220104     00000001
            
        未格式化
        eg: 00000001                  Redis全局ID(8)
        eg: 100100000001              业务标识 + Redis全局ID(12)
        eg: 22010400000001            日期 + Redis全局ID(14)
        eg: 100122010400000001        业务标识 + 日期 + Redis全局ID(18)
        
        已格式化
        eg: 1001-220104-00000001      业务标识 + 日期 + Redis全局ID(20)
        eg: 220104-00000001           日期 + Redis全局ID(15)
        eg: 1001-00000001             业务标识 + Redis全局ID(13)
```

采用 Redis 来生成ID。这主要依赖于Redis是单线程的，所以也可以用生成全局唯一的ID。使用Redis的原子操作 INCR 和 INCRBY 来实现。
比较适合使用Redis来生成日切流水号。比如(B0012201060000000008): 订单号 = 业务标识 + 日期 + 当日Redis全局自增ID。可以每天在Redis中生成一个Key，使用INCR进行累加。

优点：
  • 不依赖于数据库，灵活方便，且性能优于数据库。
  • 数字ID天然排序，对分页或者需要排序的结果很有帮助。

缺点：
  • 如果系统中没有Redis，还需要引入新的组件，增加系统复杂度。
  • 需要编码和配置的工作量比较大。
  • Redis单点故障，影响序列服务的可用性。

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
│   │   └── lms-cloud-starter-serial-no             分布式业务序列号
```

#### 服务使用

##### 依赖

1. Maven 方式

```xml
<dependency>
    <groupId>com.lmaye</groupId>
    <artifactId>lms-cloud-starter-serial-no</artifactId>
    <version>Latest Version</version>
</dependency>
```

2. Gradle 方式

```groovy
// api或compile 引用的包对于其他module是可见的
api 'com.lmaye:lms-cloud-starter-serial-no:1.2.11'
compile 'com.lmaye:lms-cloud-starter-serial-no:1.2.11'
// implementation 引用的包对于其他module是不可见的
implementation 'com.lmaye:lms-cloud-starter-serial-no:1.2.11'
```

##### 配置说明

```yaml
# Web配置
web:
  # 业务序列号
  serial-no:
    # Redis 全局ID长度，默认8位
    global-id-len: 8
    # 日期格式，默认6位(e.g: 220105)
    date-format: yyMMdd
```

##### 使用示例

```yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 0
# Web配置
web:
  # 业务序列号
  serial-no:
    # Redis 全局ID长度，默认8位
    global-id-len: 8
    # 日期格式，默认6位(e.g: 220105)
    date-format: yyMMdd
```

```java
// 生成
final String id = serialNoService.generate("B001", "", true);
// 格式化
final String fmtId = serialNoService.format("B001", id, "-");
```

##### 多线程测试结果

```text
Thread Id 59 -- 生成ID: B00122010600000009
Thread Id 63 -- 生成ID: B00122010600000005
Thread Id 66 -- 生成ID: B00122010600000008
Thread Id 61 -- 生成ID: B00122010600000010
Thread Id 65 -- 生成ID: B00122010600000007
Thread Id 58 -- 生成ID: B00122010600000006
Thread Id 62 -- 生成ID: B00122010600000004
Thread Id 64 -- 生成ID: B00122010600000003
Thread Id 57 -- 生成ID: B00122010600000001
Thread Id 60 -- 生成ID: B00122010600000002
Thread Id 62 -- 生成ID: B00122010600000016
Thread Id 61 -- 生成ID: B00122010600000017
...
```

#### 注意事项

Redis + Lua 解决高并发Redis计数越界值。

Lua脚本的好处：

- 减少网络开销，可以将多个请求通过脚本的形式一次发送，减少网络时延。
- 原子操作，redis 会将整个脚本作为一个整体执行，中间不会被其他命令插入。因此在编写脚本的过程中无需担心会出现竞态条件，无需使用事务。
- 复用，客户端发送的脚本会永久存在redis中，这样，其他客户端可以复用这一脚本而不需要使用代码完成相同的逻辑。
- redis会将整个脚本作为一个整体执行，中间不会被其他命令插入。因此在编写脚本的过程中无需担心会出现竞态条件，无需使用事务。

```java
// 高并发存在的问题，并发时incr的值分别为19998/19999/20000/20001/20002，然而到19999需重置，没生效。
final String incrStr = String.valueOf(atomicLong.incrementAndGet());
if (Objects.equals(Integer.parseInt(incrStr), 19999)) {
    atomicLong.set(10000);
}

// Redis + Lua 解决高并发
String getIncr = "local key = KEYS[1];" +
        "local incr = redis.call('incr', key);" +
        "if incr >= tonumber(ARGV[1]) then " +
        "    redis.call('set', key, tonumber(ARGV[2]));" +
        "    return incr;" +
        "end;" +
        "return incr;";
final Long incr = redissonClient.getScript(LongCodec.INSTANCE).eval(RScript.Mode.READ_WRITE, getIncr, RScript.ReturnType.INTEGER,
        Collections.singletonList(key), 19999, 10000);
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

首先修改根目录下 build.gradle 配置文件中的版本号(eg: version = '1.0.1')，然后执行发布命令。

```text
# Gradle/Maven 发布命令

Gradle: gradle publish
Maven: mvn clean deploy
```

#### 版本说明

后续每次迭代一个版本，版本号需 +1及说明新增/修复/调整了些什么功能；
目前服务已新增test/dev分支，合并步骤顺序: 当前版本(V1.0.1) -> dev -> test -> master
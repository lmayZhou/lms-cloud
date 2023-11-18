# lms-cloud

#### 介绍

  基于 Spring Boot 自定义快速依赖集合，可快速集成于项目中，从而避免重复编写！ 
如有更好的方案和idea，欢迎互相交流！如您觉得该项目对您有所帮助，欢迎点击右上方的Star标记，给予支持！！！谢谢 ~ ~

  lms-cloud 可作为微服务项目中的 parent 工程，使用它可直接默认使用 Spring Boot 2.5.0，Spring Cloud 2020.0.3 等以下各个组件。

#### 软件架构

##### 版本依赖关系

| 框架                   | 版本          |
| ---------------------- | :------------ |
| Spring Cloud           | 2020.0.3      |
| Spring Cloud Alibaba   | 2021.1        |
| Spring Boot            | 2.5.0         |
| Spring Security OAuth2 | 2.3.6.RELEASE |

##### 项目结构

```text
├── lms-cloud										根目录
│   ├── lms-cloud-common							公共资源
│   │   └── lms-cloud-core							Core 应用核心依赖包
│   ├── lms-cloud-plus                              服务插件
│   ├── lms-cloud-starters							Spring Boot Starter 自动配置服务
│   │   ├── lms-cloud-starter-canal					Canal 数据同步
│   │   ├── lms-cloud-starter-delay-queue           延迟队列自动配置服务
│   │   ├── lms-cloud-starter-drools				规则引擎
│   │   ├── lms-cloud-starter-elasticsearch			ES 搜索引擎
│   │   ├── lms-cloud-starter-email					Email 邮件服务
│   │   ├── lms-cloud-starter-logs					业务日志
│   │   ├── lms-cloud-starter-minio					MinIO 对象存储服务
│   │   ├── lms-cloud-starter-mybatis				MyBatis Puls MyBatis增强工具
│   │   ├── lms-cloud-starter-oauth2-resource		OAuth2 Resource Server 权限资源服务
│   │   ├── lms-cloud-starter-oss		            OSS文件存储服务
│   │   ├── lms-cloud-starter-rabbitmq				RabbitMQ 消息服务
│   │   ├── lms-cloud-starter-redis				    Redis 缓存
│   │   ├── lms-cloud-starter-serial-no				分布式业务序列号
│   │   ├── lms-cloud-starter-swagger				Swagger-UI 接口文档
│   │   └── lms-cloud-starter-web					Web 应用服务
```

#### 安装教程

  微服务可以直接以 lms-cloud 为 parent 工程。则该项目的 Spring 版本默认为 lms-cloud 引用的[版本](#版本依赖关系)，
如果想直接引用某个依赖则看各个依赖的文档，具体使用示例都有介绍。

1. ##### Maven

```xml
<!-- 项目引用 parent 工程 -->
<!-- https://mvnrepository.com/artifact/com.lmaye/lms-cloud -->
<parent>
    <groupId>com.lmaye</groupId>
    <artifactId>lms-cloud</artifactId>
    <version>1.2.23</version>
    <relativePath/>
</parent>
```

2. ##### Gradle

```groovy
// 项目引用 Parent 工程
// https://mvnrepository.com/artifact/com.lmaye/lms-cloud
dependencyManagement {
    imports {
        mavenBom 'com.lmaye:lms-cloud:1.2.23'
    }
}
```

#### 使用说明

##### 版本发布

SNAPSHOT    ----  快照版本
RELEASE     ----  正式版本

发布 RELEASE 版本时需修改版本号，同一版本号发布会失败。
首先修改根目录下 build.gradle/pom.xml 配置文件中的版本号(eg: version = '1.0.1')，然后执行发布命令。

```text
# Gradle/Maven 发布命令

Gradle: gradle publish
Maven: mvn clean deploy
```

```shell
# 1. 命令上传方式
# 生成秘钥
gpg --gen-key

# 查看秘钥
gpg --list-keys

# 上传秘钥至公钥服务器
gpg --keyserver keyserver.ubuntu.com --send-keys 秘钥ID

# 验证是否成功
gpg --keyserver keyserver.ubuntu.com --recv-keys 秘钥ID

# gpg: 发送至公钥服务器失败：No route to host
# 2. 手动上传方式

# 导出秘钥
gpg --export lmay@lmaye.com > ~/Downloads/gpg.pub

https://keys.openpgp.org/upload
```

指定发布 release
```bash
mvn clean deploy -P release
```

1.  版本发布报错 gpg: signing failed: Inappropriate ioctl for device

    原因是 GPG 在当前终端无法弹出密码输入页面。

```bash
# 执行
export GPG_TTY=$(tty)
```

##### git 规范

例如:

feat: 新增XXX功能接口.
用于说明本次commit的类别，只允许使用下面7个标识

```text
feat        新功能（feature）
fix         修补bug
docs        文档（documentation）
style       格式（不影响代码运行的变动）
refactor    重构（即不是新增功能，也不是修改bug的代码变动）
test        增加测试
chore       构建过程或辅助工具的变动
```

#### 版本说明

后续每次迭代一个版本，版本号需 +1及说明新增/修复/调整了些什么功能；
目前服务已新增test/dev分支，合并步骤顺序: 当前版本(V1.0.1) -> dev -> test -> master

##### V1.2.23

- 新增 lms-cloud-starter-mybatis 字段数据 加密/解密/脱敏(注解: @FieldEnDecrypt/@RetentionPolicy)
- 加密算法支持: AES, DES, SM4, SM2, RSA
- 脱敏类型支持: ALL, Name, EMAIL, MOBILE, ID_CARD, PASSWORD, BANK_CARD

#### 参与贡献

1.  示例地址: [lms-cloud-examples](https://gitee.com/lmay/spring-boot-examples/tree/master/lms-cloud-examples)

### 相关文章

#### 『 Spring Boot 2.x 快速教程 』
- [Spring Boot 整合 WebSocket](https://www.lmaye.com/2018/12/06/20181206163745/)
- [Spring Boot Sharding-JDBC 读写分离分表分库](https://www.lmaye.com/2021/01/29/20210129000510/)

#### 『 Gradle 快速教程 』
- [Gradle 多项目构建](https://www.lmaye.com/2021/01/29/20210129145644/)

#### 『 Centos 7 快速教程 』
- [Centos 7 静态IP设置](https://www.lmaye.com/2017/12/22/20180809103359/)
- [Linux增加bash脚本为service，开机自启服务脚本配置](https://www.lmaye.com/2017/12/23/20180809103413/)
- [Centos7 安装 Docker CE](hhttps://www.lmaye.com/2019/04/28/20190428183357/)
- [Centos7 安装 JDK1.8](https://www.lmaye.com/2019/04/29/20190429005630/)
- [Centos7 安装较高版本Ruby2.2+（RVM 安装）](https://www.lmaye.com/2019/01/24/20190124223042/)
- [Centos7 开启Docker远程API访问端口](https://www.lmaye.com/2019/06/04/20190604230713/)

#### 『 Docker 快速教程 』
- [Docker 安装 MongoDB](https://www.lmaye.com/2019/05/06/20190506232452/)
- [Docker 安装 MySQL 8.0](https://www.lmaye.com/2019/05/22/20190522162930/)
- [Dockerfile 部署MySql 8并初始化数据脚本](https://www.lmaye.com/2019/06/02/20190602133656/)

#### 『 Redis 快速教程 』
- [Redis 配置文件详解](https://www.lmaye.com/2018/09/06/20180906002632/)
- [Redis Cluster 集群](https://www.lmaye.com/2019/01/24/20190124212849/)
- [Redis 配置集群遇到问题及解决方法](https://www.lmaye.com/2019/01/24/20190124223656/)

### 联系我
    * QQ: 379839355
    * QQ群: [Æ┊Java✍交流┊Æ](https://jq.qq.com/?_wv=1027&k=5Dqlg2L)
    * QQ群: [Cute Python](https://jq.qq.com/?_wv=1027&k=58hW2jl)
    * Email: lmay@lmaye.com
    * Home: [lmaye.com](https://www.lmaye.com)
    * GitHub: [lmayZhou](https://github.com/lmayZhou)

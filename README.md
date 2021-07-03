# lms-cloud

#### 介绍
​		基于 Spring Boot 自定义快速依赖集合，可快速集成于项目中，从而避免重复编写！如有更好的方案和idea，欢迎互相交流！如您觉得该项目对您有所帮助，欢迎点击右上方的Star标记，给予支持！！！谢谢 ~ ~

​		通过自定义Spring Boot Starter(自动化配置)的特性，来实现快速的将各个自动配置服务快速引入我们的Spring Boot应用服务中。

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
├── lms-cloud																		根目录
│   ├── lms-cloud-common												公共资源
│   │   ├── lms-cloud-core											Core 应用核心依赖包
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── ...
│   │   └── pom.xml
│   ├── lms-cloud-examples											测试示例
│   │   ├── lms-cloud-example-elasticsearch			ES 搜索引擎测试
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-example-email							Emial 邮件测试
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── ...
│   │   └── pom.xml
│   ├── lms-cloud-starters											Spirng Boot Starter 自动配置服务
│   │   ├── lms-cloud-starter-canal							Canal 数据同步(TODO)
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-elasticsearch			ES 搜索引擎
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-email							Email 邮件服务
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-minio							MinIO 对象存储服务
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-mybatis						MyBatis Puls MyBatis增强工具
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-oauth2-resource		OAuth2 Resource Server 权限资源服务
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-rabbitmq					RabbitMQ 消息服务(TODO)
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-redis							Redis 缓存(TODO)
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-swagger						Swagger-UI 接口文档
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── lms-cloud-starter-web								Web 应用服务
│   │   │   ├── src
│   │   │   └── pom.xml
│   │   ├── ...
│   │   └── pom.xml
│   ├── ...
│   └── pom.xml
```

#### 安装教程

1. ##### Maven

   ```xml
   <!-- LMS Cloud Core -->
   <dependency>
       <groupId>com.lmaye</groupId>
       <artifactId>lms-cloud-core</artifactId>
       <version>1.0.1</version>
   </dependency>
   ```

2. ##### Gradle

   ```groovy
   // api或compile 引用的包对于其他module是可见的
   api 'com.lmaye:lms-cloud-core:1.0.1'
   compile 'com.lmaye:lms-cloud-core:1.0.1'
   // implementation 引用的包对于其他module是不可见的
   implementation 'com.lmaye:lms-cloud-core:1.0.1'
   ```

#### 使用说明

##### 版本发布

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
feat：新功能（feature）
fix：修补bug
docs：文档（documentation）
style： 格式（不影响代码运行的变动）
refactor：重构（即不是新增功能，也不是修改bug的代码变动）
test：增加测试
chore：构建过程或辅助工具的变动
```

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)

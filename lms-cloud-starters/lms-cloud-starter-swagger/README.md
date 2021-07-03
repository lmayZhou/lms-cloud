# lms-cloud-starter-swagger

#### 介绍
​		基于 Spring Boot 自定义快速依赖集合，可快速集成于项目中，从而避免重复编写！如有更好的方案和idea，欢迎互相交流！如您觉得该项目对您有所帮助，欢迎点击右上方的Star标记，给予支持！！！谢谢 ~ ~

​		通过自定义Spring Boot Starter(自动化配置)的特性，来实现快速的将各个自动配置服务快速引入我们的Spring Boot应用服务中。lms-cloud-starter-swagger是为了简化原生使用代码的方式整合生成API文档，而是直接通过配置文件实现整合。

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
       <artifactId>lms-cloud-starter-swagger</artifactId>
       <version>1.0.1</version>
   </dependency>
   ```

2. ##### Gradle

   ```groovy
   // api或compile 引用的包对于其他module是可见的
   api 'com.lmaye:lms-cloud-starter-swagger:1.0.1'
   compile 'com.lmaye:lms-cloud-starter-swagger:1.0.1'
   // implementation 引用的包对于其他module是不可见的
   implementation 'com.lmaye:lms-cloud-starter-swagger:1.0.1'
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

##### 参数配置

```yaml
# Swagger 配置
swagger:
  enabled: true
  title: XXX 服务API-在线接口文档
  description: Starter for swagger 2.x
  version: 1.0.1
  termsOfServiceUrl: https://www.lmaye.com
  basePackage: com.lmaye.cloud
  basePath: /**
  excludePath: /error
  contact:
    email: lmay@lmaye.com
    name: lmay Zhou
    url: https://www.lmaye.com
  globalRequestParameters:
    - description: 凭证
      modelType: string
      modelFormat:
      name: Authorization
      parameterType: header
      required: false
```

##### 配置说明

```properties
- swagger.enabled																		是否启用swagger，默认：true
- swagger.title																			标题
- swagger.description																描述
- swagger.version																		版本
- swagger.license																		许可证
- swagger.licenseUrl																许可证URL
- swagger.termsOfServiceUrl													服务条款URL
- swagger.contact.name															维护人名称
- swagger.contact.url																维护人URL
- swagger.contact.email															维护人email
- swagger.base-package															swagger扫描的基础包，默认：全扫描
- swagger.base-path																	需要处理的基础URL规则，默认：/**
- swagger.exclude-path															需要排除的URL规则，默认：空
- swagger.host																			文档的host信息，默认：空
- swagger.globalRequestParameters[0].name						参数名
- swagger.globalRequestParameters[0].description		描述信息
- swagger.globalRequestParameters[0].modelType			指定参数类型 string/integer/number(具体参考 ScalarType)
- swagger.globalRequestParameters[0].modelFormat		指定参数类型格式 string、integer[int32/int64](具体参考 ScalarType)
- swagger.globalRequestParameters[0].parameterType	指定参数存放位置 可选header,query,path,body.form
- swagger.globalRequestParameters[0].required				指定参数是否必传 true,false
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
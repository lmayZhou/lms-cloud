# lms-cloud-starter-swagger

#### 介绍

  基于 Spring Boot 自定义快速依赖集合，可快速集成于项目中，从而避免重复编写！ 通过自定义Spring Boot Starter(自动化配置)的特性，来实现快速的将各个自动配置服务快速引入我们的Spring Boot应用服务中。
如有更好的方案和idea，欢迎互相交流！如您觉得该项目对您有所帮助，欢迎点击右上方的Star标记，给予支持！！！谢谢 ~ ~

  Swagger Starter 是为了简化原生使用代码的方式整合配置生成API文档，而是直接通过配置文件来实现更方便快速的整合，无需再去写一个 swagger 配置类。

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
│   ├── ...
│   ├── lms-cloud-starters							Spring Boot Starter 自动配置服务
│   │   ├── ...
│   │   └── lms-cloud-starter-swagger				Swagger-UI 接口文档
```

#### 安装教程

1. ##### Maven

```xml
<dependency>
   <groupId>com.lmaye</groupId>
   <artifactId>lms-cloud-starter-swagger</artifactId>
   <version>1.2.24</version>
</dependency>
```

2. ##### Gradle

```groovy
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
# 接口文档V3.0访问地址: http://127.0.0.1:8080/swagger-ui/index.html
# Swagger 配置
springfox:
  documentation:
    enabled: true
swagger:
  title: XXX 服务API-在线接口文档
  description: Starter for swagger 3.0
  version: 1.0.1
  termsOfServiceUrl: https://www.lmaye.com
  basePackage: com.example.demo.controller
  basePath: /**
  excludePath: /error
  contact:
    email: lmay@lmaye.com
    name: lmay Zhou
    url: https://www.lmaye.com
  global-operation-parameters:
    - description: 凭证
      modelRef: string
      name: Authorization
      parameterType: header
      required: false
```

##### 配置说明

```text
- springfox.documentation.enabled						是否启用swagger，默认：true
- swagger.title											标题
- swagger.description									描述
- swagger.version										版本
- swagger.license										许可证
- swagger.licenseUrl									许可证URL
- swagger.termsOfServiceUrl								服务条款URL
- swagger.contact.name									维护人名称
- swagger.contact.url									维护人URL
- swagger.contact.email									维护人email
- swagger.base-package									swagger扫描的基础包，默认：全扫描
- swagger.base-path										需要处理的基础URL规则，默认：/**
- swagger.exclude-path									需要排除的URL规则，默认：空
- swagger.host											文档的host信息，默认：空
- swagger.globalOperationParameters[0].name				参数名
- swagger.globalOperationParameters[0].description		描述信息
- swagger.globalOperationParameters[0].modelRef			指定参数类型 string/integer/number(具体参考 ScalarType)
- swagger.globalOperationParameters[0].modelFormat		指定参数类型格式 string、integer[int32/int64](具体参考 ScalarType)
- swagger.globalOperationParameters[0].parameterType    指定参数存放位置 可选header,query,path,body.form
- swagger.globalOperationParameters[0].required			指定参数是否必传 true,false
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

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request

### 相关文章
#### 『 Spring Boot 2.x 快速教程 』
- [Spring Boot 整合 WebSocket](https://www.lmaye.com/2018/12/06/20181206163745/)

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

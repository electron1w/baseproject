# BasicPermissions —— 基础权限

## 项目介绍
**本系统包含基础权限操作**

### Spring boot
* <version>1.5.6.RELEASE</version>

### MyBatis
* mapper根据application.properties中mybatis.mapperLocations位置存放

### SpringMVC
Spring Boot为Spring MVC提供适用于多数应用的自动配置功能。
在Spring默认基础上，自动配置添加了以下特性：

    引入ContentNegotiatingViewResolver和BeanNameViewResolver beans。
    对静态资源的支持，包括对WebJars的支持。
    自动注册Converter，GenericConverter，Formatter beans。
    对HttpMessageConverters的支持。
    自动注册MessageCodeResolver。
    对静态index.html的支持。
    对自定义Favicon的支持。

如果想全面控制Spring MVC，你可以添加自己的@Configuration，
并使用@EnableWebMvc对其注解。如果想保留Spring Boot MVC的特性，
并只是添加其他的MVC配置(拦截器，formatters，视图控制器等)，
你可以添加自己的WebMvcConfigurerAdapter类型的@Bean（不使用@EnableWebMvc注解）。

### Thymeleaf
Thymeleaf是一款用于渲染XML/XHTML/HTML5内容的模板引擎。
类似JSP，Velocity，FreeMaker等，它也可以轻易的与Spring MVC等Web框架
进行集成作为Web应用的模板引擎。与其它模板引擎相比，Thymeleaf最大的特点
是能够直接在浏览器中打开并正确显示模板页面，而不需要启动整个Web应用。它的功能特性如下：

    Spring MVC中@Controller中的方法可以直接返回模板名称，接下来Thymeleaf模板引擎会自动进行渲染
    模板中的表达式支持Spring表达式语言（Spring EL)
    表单支持，并兼容Spring MVC的数据绑定与验证机制
    国际化支持 
### Apache shiro

### idea导入项目
* 菜单中选择File–>New–>Project from Existing Sources...
* 选择解压后的项目文件夹，点击OK
* 点击Import project from external model并选择Maven，点击Next到底为止。
* 若你的环境有多个版本的JDK，注意到选择Java SDK的时候请选择Java 8版本

### 项目关键字
* 轻量级，可扩展
* 基于Spring boot构建，配置文件能少则少

## 环境准备
* mysql，数据结构在baseProject.sql中定义了

## 项目配置
* 应用配置：application.properties
* 日志配置：logback-spring.xml

## 部署
系统默认采用jar打包和运行。

### 打包

	mvn clean install

### 运行
jdk 8

	java -jar xxx.jar
可以以下命令修改tomcat端口号运行：

    java -jar xxx.jar --server.port=9090
也可在application.properties中添加属性server.port=9090

### war包部署
如果需要tomcat等容器部署，也可将配置打包方式修改成war包方式，修改pom.xml

	<packaging>war</packaging>


## 系统后台入口
* 请求：http://localhost:8080
* 管理员 账号:admin 密码:123123

## TODO
* admin账号的相关功能
* 项目权限相关删除功能依赖数据库id
* 与页面交互数据未统一
* shiro登录功能未完善`记住密码,用户登录唯一等`

## 最后说明
此基础包只做对java的学习
本系统由本人学习过程中整理的
其中多含网上资料
项目中许多错误和代码的冗余,如果有大神看到可以指出,感激不尽

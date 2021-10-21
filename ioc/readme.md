## 目录
resources: 配置文件，目前只支持properties文件

com.hour.demo：测试用例
com.hour.ioc.annotation：自定义注解
com.hour.ioc.core：功能在此完成

## 注解功能
1. Component, Configuration 作用于类上
   1. 这两个注解都会使得此类被加入到ioc容器中
   
2. ConfigurationProperties 作用于类上
   1. 依赖于Configuration
   2. ConfigurationProperties 可以将配置文件中的值注入当前类中

3. Value作用于基本类型上，Bean作用于方法上
   1. 依赖于以上三个注解，如果没有以上三个类，此注解无效。
   
4.  Autowired作用于引用类型上
    1.  不需要依赖其他注解
### SB+dubbo整合的三种方式：

* #### 导入`dubbo-starter`；

  * 在application.properties配置属性，使用@Service[暴露服务]，使用@Reference【引用服务】

注意：开启EnableDubbo；开启基于注解的dubbo功能

或者—

在properties文件配置`dubbo.scan.base-packages=`这个来进行包扫描。

以上二者选其一

这是没有dubbo.xml文件的配置方式，在application.properties里面配置

* #### 保留dubbo xml配置文件，

  * 导入dubbo-starter，使用`@ImportResource()`导入dubbo的配置文件即可

这种方式不用开启EnableDubbo

而是使用另一种方式读取xml文件自动配置，`@ImportResource(locations="classpath:dubbo.xml")`

这种方式就不需要注解了

* #### 使用注解API方式；

将每一个组件手动创建到容器中


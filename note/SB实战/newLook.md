### BeanUtils工具

---

```java
/*快速把v1的值赋值给v2*/
BeanUtils.copyProperties(question,questionDTO);
```

### MyBatis配置

---

**开启Mybatis的驼峰规则

```xml
mybatis.configuration.map-underscore-to-camel-case=true
```

### 一些小BUG

---

github头像显示不出来

​	`+` github官网头像格式不正确

***

### 热部署

***

Development tools

---

### FlyWay

将sql文件按照V1__***.sql存入resources/db/migration中

执行mvn flyway:migrate

flyway自动执行SQL文件并且记录日志

---

### MBG

---

在resources目录下写MBG的配置文件

写完之后在控制台执行：

`mvn -Dmybatis.generator.overwrite=ture mybatis-generator:generate`

---

### Localstorage

---

百度一下********

description:**在B页面点击功能-->提示登录-->点击确定-->跳转登录页面-->然后自动跳转回B页面并且显示之前在B页面显示的东西**

```js
 window.open("https://github.com/login/oauth/authorize?client_id=3211eea11af93835b682&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
```

```html
 window.onload=function () {
        var closable = window.localStorage.getItem("closable");
        if (closable == "true") {
            window.close();
            window.localStorage.removeItem("closable");
        }
    }
```



---

### 前端调试

---

在js中加一个`debugger`

---

### commons-lang

---

 
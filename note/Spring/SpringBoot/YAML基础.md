**基本语法**

**K:空格v：表示一对键值对**

**以空格的缩进控制层级关系；只要是左对齐的一列数据都是同一个层级；跟python一样**

**属性和值大小写敏感；**

**值的写法**

**字面量；普通的值（数字，字符串，boolean）**

​	**k:空格v：字面直接写；**

​		**字符串默认不用加上引号；**

​		**“”：双引号**

​			**不会转义字符串里面的特殊字符；**

​			**特殊字符会作为本身意义；**

​				**name: "zhangshan \n lisi"-->输出：zhangshan 换行 lisi**

​		**''：单引号**

​			**会转义特殊字符，特殊字符最终只会作为普通的字符串输出**

​				**name: "zhangshan \n lisi"-->输出：zhangshan \n lisi**

**对象、Map(属性和值)(键值对)：**

​	**k:空格v-->在下一行写对象和属性的关系，注意缩进**

​		**对象还是k:v方式**

```yaml
friends:
    lastName: zhangshan
    age: 20
    
    对象的行内写法
friends: {lastName: zhangsan,age: 20}

/*Map也一样，同上*/
```

---

**数组（List、Set)：**

​	**用-空格 值表示数组中的一个元素：**

```yaml
pets:
    - cat
    - dog
    - pig
行内写法
pets: [cat,dog,pig]
```

----

### 配置文件怎么写

​		**Javaben**

```java
@Component
@ConfigurationProperties(prefix = "person")
public class Person {

    private String lastName;
    private String age;
    private Boolean boss;
    private Date birth;

    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
```

---

​		**pom.xml**

```xml
<!--导入配置文件处理器，配置文件进行绑定就会有提示-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

​	**application.yml**

```yacas
person:
  lastName: zhangsan
  age: 18
  boss: false
  birth: 2017/12/12
  maps: {k1: v1,k2: v2}
  lists:
    - lisi
    - zhaoliu
  dog:
    name: 小狗
    age: 2
```


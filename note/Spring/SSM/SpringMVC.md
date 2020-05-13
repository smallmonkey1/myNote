## 校验：JSR303

- ### 导包

```java
classmate-1.3.4.jar

hibernate-validator-6.1.0.Final.jar

hibernate-validator-annotation-processor-6.1.0.Final.jar

hibernate-validator-cdi-6.1.0.Final.jar

jakarta.validation-api-2.0.2.jar

jboss-logging-3.3.2.Final.jar

```

- ### 给bean加注解

```java
@NotEmpty
@Length(min=6,max = 18)
private String lastName;
```

- ### 给处理方法加注解

```java
public String addEmp(@Validated Employee employee,BindingResult result) {
		System.out.println("要添加的员工："+employee);
		//获取是否有校验错误
		boolean hasErrors = result.hasErrors();
		if(hasErrors) {
			System.out.println("有校验错误");
			return "add";
		}else {
			employeeDao.save(employee);
			//直接重定向到查询所有员工请求；
			return "redirect:/emps";
		}
	}
```

---

- ### 给表单加提示

```java
<form:errors path="lastName"></form:errors>
    
<form:form action="${ctp }/emp" method="POST" modelAttribute="employee">
	<!-- path就是原来html-input标签的name：需要些 -->
	lastName:<form:input path="lastName" />
	<form:errors path="lastName"></form:errors>
	<hr>
	email:<form:input path="email"/>
	<form:errors path="email"></form:errors>
	<hr>
	gender:男：<form:radiobutton path="gender" value="1" />++女：
		<form:radiobutton path="gender" value="0"/>
	<hr>
	dept:
	<form:select path="department.id" items="${depts }"
	itemLabel="departmentName" itemValue="id"
	>
	
	</form:select>
	<hr>
	<input type="submit" value="submit"/>
</form:form>
```

---

- jsp文件头部需要加上

```jsp
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
```

---

## 第二种校验

- ### 将错误信息加到Map,然后在表单拿

```java
public String addEmp(@Validated Employee employee,BindingResult result,Model model) {
Map<String,Object> errorsMap=new HashMap<String,Object>();
boolean hasErrors = result.hasErrors();
		if(hasErrors) {
			List<FieldError> allErrors = result.getFieldErrors();
			for(FieldError e:allErrors) {
				errorsMap.put(e.getField(), e.getDefaultMessage());
			}
			model.addAttribute("errorInfo", errorsMap);
```

---

- ### 在jsp拿出

```java
${errorInfo.lastName }
```

---

​		文件下载用原生API

## 文件上传

- ### 文件上传表单准备；enctype="multipart/form-data"

```jsp
<form action="${ctp }/upload" enctype="multipart/form-data" method="post">
```

- ### 导包

```java
commons-fileupload-1.4.jar
commons-io-2.6.jar
```

- ### 在springmvc.xml写配置

```xml
<!-- 配置文件上传解析器 -->
	<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<property name="maxUploadSize" value="#{1024*1024*24}"></property>
		<property name="defaultEncoding" value="utf-8"></property>
	</bean>
```

* ### 再写处理器

```java
@RequestMapping(value = "/upload")
	public String upload(
			@RequestParam(value="username",required = false)String username,
			@RequestParam("headerimg")MultipartFile file,
			Model model
			) {
       
		System.out.println("input的name字段："+file.getName());
		System.out.println("文件的名字："+file.getOriginalFilename());
		//文件保存
		try {
			file.transferTo(
   new File("E:\\haha\\"+file.getOriginalFilename()));
			model.addAttribute("msg","文件上传成功了！");
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			model.addAttribute("msg","文件上传失败了！"+e.getMessage());
			e.printStackTrace();
		}
		return "forward:/index.jsp";
	}
```

---

* ### 多文件上传

```java
	public String mulUpload(
			@RequestParam(value="username",required = false)String username,
			@RequestParam("headerimg")MultipartFile[] file,
			Model model
			) {
		for (MultipartFile multipartFile : file) {
			if(!multipartFile.isEmpty()) {
				//文件保存
				try {
					multipartFile.transferTo(
							new File("E:\\haha\\"+multipartFile.getOriginalFilename())
							);	
				} catch (Exception e) {
				}
			}
		}
		return "forward:/index.jsp";
	}
```

---

* ## 拦截器--（过滤器的升级版）

##### SpringMVC提供了拦截器机制；允许目标方法运行之前进行一些拦截工作，或者目标方法运行之后进行一些其他处理；

```java
/*
 * 1.实现接口 HandlerInterceptor
 * 2.在SpringMVC配置文件注册拦截器
 * 		配置这个拦截器来拦截哪些请求的目标方法
 * */
public class MyFirstInterceptor implements HandlerInterceptor {
```

* ### 在SpringMVC.xml配置拦截器

```xml
<!-- 配置拦截器 -->
	<mvc:interceptors>
		<!-- 配置某一个拦截器：默认拦截所有请求； -->
		<bean class="com.z.controller.MyFirstInterceptor"></bean>
		<!-- 配置某个拦截器更详细的信息 -->
		<mvc:interceptor >
			<!-- 只拦截test01 -->
			<mvc:mapping path="/test01"/>
			<bean class="com.z.controller.MySecondInterceptor"></bean>
		</mvc:interceptor>
	</mvc:interceptors> 
```

---

---

---

#运行流程

---

1. 所有请求，前端控制器（dispatcherServlet）收到请求，调用doDispatch进行处理

2. 根据HandlerMapping中保存的请求映射信息，找到能处理当前请求的，处理执行链（包含拦截器）

3. 根据当前处理器找到它的HandlerAdapter（适配器）

4. 拦截器的preHandle先执行

5. 适配器执行目标方法，并返回ModelAndView

6. 1. ModelAttribute注解标注的方法提前运行

   2. 执行目标方法的时候（确定目标方法用的参数）

   3. 1. 有注解

      2. 没注解

      3. 1. 是否Model、Map以及其他的

         2. 如果是自定义类型

         3. 1. 从隐含模型中看有没有，如果有就从隐含模型中拿
            2. 如果没有，再看是否SessionAttributes标注的属性，如果是：从Session中拿，如果拿不到抛异常
            3. 都不是，利用反射创建对象

7. 拦截器的postHandle执行

8. 处理结果；（页面渲染流程）

9. 1. 如果有异常，使用异常解析器处理异常，处理完后还会返回ModelAndView

   2. 调用render进行页面渲染

   3. 1. 视图解析器根据视图名得到视图对象
      2. 视图对象调用rander方法；

   4. 执行拦截器的afterCompletion方法

---

---

---



# 注意事项

```xml
<!-- 扫静态资源 -->
	<mvc:default-servlet-handler/>
<!-- 扫动态资源 -->
<mvc:annotation-driven></mvc:annotation-driven>
```

* 两个注解要同时在springmvc的配置文件加上，如果只加annotation-driven，就会导致静态文件加载不出来，
* 另外@EnableWebMvc可以代替annotation-driven

---
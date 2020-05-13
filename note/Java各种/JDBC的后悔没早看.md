### 获取连接的正常操作

* 将driver、user、password、url写入配置文件，而不是写死在代码中

  ```java
  //获取与构造器同路径的jdbc.properties配置文件，转为流
  InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("jdbc.properties");
  		Properties pro = new Properties();
  		pro.load(is);
  		String NAME=pro.getProperty("jdbc.user");
  		String URL=pro.getProperty("jdbc.url");
  		String PASSWORD=pro.getProperty("jdbc.password");
  		String DRIVER=pro.getProperty("jdbc.driver");
  		Class.forName(DRIVER);
  		Connection conn=DriverManager.getConnection(URL,NAME,PASSWORD);
  ```

  

### 写一个基础通用的修改方法

* 通过传入sql语句和可变参数，达到一个调用一个方法，完成大部分增删改操作的功能….后悔没早看系列
* 斗宗强者恐怖如斯

```java
public static int update(String sql,Object ...args){
		Connection conn=null;
		PreparedStatement ps =null;
		int tag=0;
		try {
			conn = DBConnection.getConnection();
			ps= conn.prepareStatement(sql);
			
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i+1, args[i]);
			}
			tag = ps.executeUpdate();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			DBConnection.release(ps, conn);
		}
		return tag;
	}
```



---

### 写一个规定某个对象的通用查询方法，适应各种表，多个查询参数--返回一个对象；

* 运用了反射机制，哎
* 先写一个单个表的通用查询方法
* 注意事项
  * 建议不用`getColumnName()`方法获取表的列名，而是用`getColumnLabel()`方法获取别名
  * 因为表字段名和类属姓名不一定一致
  * 不一致时，只要在sql语句中使用别名就行了
  * `getColumnLabel()`方法，没有别名会自动获取列名

```java
public User find(String sql,Object ...arg){
    //获取连接--返回conn
    //预编译--conn.prepareStatement(sql);--返回ps
    //填充占位符，如下for循环
    for (int i = 0; i < args.length; i++) {
				ps.setObject(i+1, args[i]);
	}
    ResultSet rs=ps.executeQuery();
    //获取结果集的元数据，元数据作用是保存着返回结果集的列名啊，列数啊什么什么的
    ResultSetMetaData md=rs.getMetaData();
    //通过元数据-md-获取结果集中的列数
    int columnCount = md.getColumnCount();
    if(rs.next()){
        User user = new User();
        //接下来就是处理结果集中的每一个列，*重点*通过反射，将结果集中获取的列名，跟对象中的set方法一一对应，并且附上对应的值，好难解释，其实也不是很懂
        for(int i=0;i<columnCount;i++){
            Object columnValue=rs.getObject(i+1);
            //通过*元数据*获取每一列的列名
            //String columnName=md.getColumnName(i+1);
            //但是一般不推荐获取列名，因为数据库列名不一定跟对象的属性名一致，所以需要获取别名才对
            String columnName=md.getColumnLabel(i+1);
            
            //*重中之重*来了，通过反射，给user对象中指定的columnName属性，赋值为columnValue；牛批，没学
            //调用指定运行时类的指定属性
            Field field=User.class.getDeclaredField(columnName);
            field.setAccessible(true);
            field.set(user,columnValue);
        }
        //上面的for就是用反射机制将查询到的值封装进对象的过程  哎
        return user;
    }
    //关闭资源--价格try-catch-finally
    //返回
    return null;
}
```



### 再写个通用表的通用查询方法–获取一个对象



```java
public static <T>T query(Class<T> clazz,String sql,Object ...arg) {
		Connection conn=null;
		PreparedStatement ps =null;
		ResultSet rs=null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < arg.length; i++) {
				ps.setObject(i+1, arg[i+1]);
			}
			
			rs=ps.executeQuery();
			//获取结果集的元数据
			ResultSetMetaData md = rs.getMetaData();
			//通过元数据获取结果集中的列数
			int columnCount = md.getColumnCount();
			if(rs.next()) {
				T obj = clazz.newInstance();
				//处理结果集中的每一列
				for(int i=0;i<columnCount;i++) {
					//获取列的别名
					String columnLabel = md.getColumnLabel(i+1);
					//获取列的值
					Object columnValue = rs.getObject(i+1);
					
					//给obj对象指定的columnLabel属性，赋值为columnValue
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(obj, columnValue);
				}
				return obj;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBConnection.release(rs, ps, conn);
		}
		return null;
		
	}
```



---

### 写一个通用查询方法，获取多个对象，返回List\<T\>



```java 
public static <T> List<T> queryList(Class<T> clazz,String sql,Object ...arg) throws InstantiationException, IllegalAccessException {
		Connection conn=null;
		PreparedStatement ps =null;
		ResultSet rs=null;
		try {
			conn = DBConnection.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < arg.length; i++) {
				ps.setObject(i+1, arg[i]);
			}
			rs=ps.executeQuery();
			//获取结果集的元数据
			ResultSetMetaData md = rs.getMetaData();
			//通过元数据获取结果集中的列数
			int columnCount = md.getColumnCount();
			
			ArrayList<T> list = new ArrayList<T>();
			while(rs.next()) {
					T t = clazz.newInstance();
					for (int i = 0; i < columnCount; i++) {
						String columnLabel = md.getColumnLabel(i+1);
						
						Object columnValue = rs.getObject(i+1);
						
						Field field = clazz.getDeclaredField(columnLabel);
						field.setAccessible(true);
						field.set(t, columnValue);
					}
				list.add(t);
			}
			return list;
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBConnection.release(rs, ps, conn);
		}
		return null;
	}
```



### 写一个返回单个任意数据类型的通用查询方法



```java
public static Object query(String sql,Object ...arg) {
		Connection conn=null;
		PreparedStatement ps =null;
		ResultSet rs=null;
		try {
			conn = DBConnection.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < arg.length; i++) {
				ps.setObject(i+1, arg[i]);
			}
			rs=ps.executeQuery();
			//获取结果集的元数据
			ResultSetMetaData md = rs.getMetaData();
			//通过元数据获取结果集中的列数
			int columnCount = md.getColumnCount();
			if(rs.next()) {
					Object columnValue = rs.getObject(1);
				
				return columnValue;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			DBConnection.release(rs, ps, conn);
		}
		return null;
	}
```



---

### 解决回滚的问题

```java
//这是一个包含两个持久层操作的方法，如何把它变成事务？
public void Transation(){
    //首先，修改持久层方法，持久层的数据库连接操作放在事务层做
    Connection conn=JDBCUtils.getConnection();
    //关闭自动提交操作，conn断开连接的时候会自动提交。
    conn.setAutoCommit(false);
    //2.把连接作为参数传入持久层中
    try{
        类名.方法名(conn,sql,args);
    
		//模拟两个原子操作之间出现异常状况
        System.out.println(10/0);
    	类名.方法名(conn,sql,args);
        
        //前面的更新操作都没有异常的话，手动提交
        conn.commit();
    }catch(){
        //如果有任何异常，回滚
        conn.rollback();
    }finally{
        //关闭连接
        JDBCUtils.close(conn);
    }
    
}
```

* 总结：将数据库连接操作放到事务层--关闭自动提交--如果程序执行正常，则手动提交--如果捕获异常，则回滚--关闭连接



---

### QueryRunner()类封装了通用的增删改查方法，提供使用

* ResultSetHandler()为了应对各种返回类型，是一个接口，旗下的实现类能满足大部分需求

```java
QueryRunner runner=new QueryRunner();
Connection conn=JDBCUtils.getConnection();//
String sql="";
//BeanHander很明显就是返回一个User对象的意思，还有BeanListHandler等等
BeanHander<User> handler=new BeanHandler<>(User.class);
User user=runner.query(conn,sql,handler,Integer);
```


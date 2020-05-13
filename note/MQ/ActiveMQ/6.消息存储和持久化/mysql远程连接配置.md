$ /usr/local/mysql/bin/mysql -u root -p
--输入密码
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| seckill            |
| sys                |
| test               |
+--------------------+
6 rows in set (0.00 sec)
mysql> use mysql;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
--查询当前数据库相关信息
mysql> select host,user,authentication_string,plugin from user;
+-----------+------------------+------------------------------------------------------------------------+-----------------------+
| host      | user             | authentication_string                                                  | plugin                |
+-----------+------------------+------------------------------------------------------------------------+-----------------------+
| localhost | mysql.infoschema | $A$005$THISISACOMBINATIONOFINVALIDSALTANDPASSWORDTHATMUSTNEVERBRBEUSED | caching_sha2_password |
| localhost | mysql.session    | $A$005$THISISACOMBINATIONOFINVALIDSALTANDPASSWORDTHATMUSTNEVERBRBEUSED | caching_sha2_password |
| localhost | mysql.sys        | $A$005$THISISACOMBINATIONOFINVALIDSALTANDPASSWORDTHATMUSTNEVERBRBEUSED | caching_sha2_password |
| localhost | root             | *84AAC12F54AB666ECFC2A83C676908C8BBC381B1                              | mysql_native_password |
+-----------+------------------+------------------------------------------------------------------------+-----------------------+
4 rows in set (0.00 sec)

--将root用户设置为所有地址可登录，原来是localhost表示只用本机可登录
mysql> update user set host='%' where user='root';
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

--刷新权限
mysql> flush privileges;
Query OK, 0 rows affected (0.00 sec)

--将用户root密码设置为永不过期
mysql> alter user 'root'@'%' identified by '12345678' password expire never;
Query OK, 0 rows affected (0.01 sec)

--将root用户密码加密方式改为mysql_native_password ，上面查到root用户密码的加密方式为caching_sha2_password 
mysql> alter user 'root'@'%' identified with mysql_native_password by '12345678';
Query OK, 0 rows affected (0.00 sec)

--刷新权限，在别的机器上即可登录
mysql> flush privileges;
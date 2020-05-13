第一次修改 `nginx` 的配置文件，手写的配置

启动`nginx`报错，用提示的 `journalctl -xe` 查看错误日志。。。看不出什么东西

算了，还是复制一下吧

第二次修改配置文件，成功启动`nginx`服务，但是网站访问报错 403，

查看一下80端口，打开了，

猜测9000端口没打开，打开之后还是没能访问

重新来了一遍，发现配置都对了，但是 php 还是没能被解析，后来灵光一现，发现 php-fpm 服务不知道什么时候关了。崩溃！

打开 `nginx php-fpm` 服务之后，能够正常 `ip/index.php` ，

---

现在开始搭建 `wordpress`

，在默认的 `/etc/nginx/conf.d/default.conf` 中访问 PHP 成功，但是改了配置文件就访问不到，下面是我修改了的配置文件

```shell
location / {
root   /mnt/wordpress;
index  index.php index.html index.htm;
}

location ~ \.php$ {
root           /mnt/wordpress;
fastcgi_pass   127.0.0.1:9000;
fastcgi_index  index.php;
fastcgi_param  SCRIPT_FILENAME  /mnt/wordpress$fastcgi_script_name;
include        fastcgi_params;
}

```

搭建 `wordpress` 失败.
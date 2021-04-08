记一下别人踩过的坑：

在idea的maven项目中，部署的项目要看清楚，今天就遇到一个想要部署`mavwn_web`的却部署到`maven_web`项目去，很无聊的一个错误。

然后他还说了Maven自带的tomcat，那就百度弄吧，这点都搞不定有什么用：20-3-31



# springboot项目读取Resources下的文件乱码问题



打包出现乱码说明是打包中间出了问题，据说是缺少了一个maven插件，或者是配置

```xml
 <!-- 避免font文件的二进制文件格式压缩破坏 -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <configuration>
        <nonFilteredFileExtensions>
            <nonFilteredFileExtension>woff</nonFilteredFileExtension>
            <nonFilteredFileExtension>woff2</nonFilteredFileExtension>
            <nonFilteredFileExtension>eot</nonFilteredFileExtension>
            <nonFilteredFileExtension>ttf</nonFilteredFileExtension>
            <nonFilteredFileExtension>svg</nonFilteredFileExtension>
            <nonFilteredFileExtension>docx</nonFilteredFileExtension>
        </nonFilteredFileExtensions>
    </configuration>
</plugin>
```


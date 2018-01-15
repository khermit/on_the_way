 
 - 功能：基于ubuntu16.04安装jdk环境
 - 准备：在主机同一目录下的文件：jdk-8u131-linux-x64.tar.gz Dockerfile
 - Dockerfile文件内容：  
 - ```txt
   #ubuntu with jdk8
    FROM ubuntu

    MAINTAINER Duke quandk@foxmail.com

    ADD jdk-8u131-linux-x64.tar.gz /usr/java/

    ENV JAVA_HOME /usr/java/jdk1.8.0_131
    ENV JRE_HOME ${JAVA_HOME}/jre
    ENV CLASSPATH .:${JAVA_HOME}/lib:${JRE_HOME}/lib
    ENV PATH ${JAVA_HOME}/bin:$PATH
    ```
 - 构建： docker built -t khermit/ubuntu_jdk .
 - 运行： docker run -it --name=ubuntu_jdk khermit/ubuntu_jdk /bin/bash
 - 推送： docker push khermit/ubunut_jdk
 - 显示运行：
    - docker run ubuntu echo 'Hello World!' 在ubuntu镜像上运行
    - docker run -i -t --name=container01 ubuntu /bin/bash 其中：-i interactive -t tty
    - docker ps -a / -l 其中：-a all 无参时查看正在运行的容器
    - docker inspect container01 查看容器的详细信息
    - docker start -i container01 交互式地启动该容器
    - docker rm container01 删除
    - docker stop container01
    - docker kill container01
 - 守护运行：
    - 方式一：
        - docker run -i -t IMAGE /bin/bash 启动容器
        - Ctrl+P Ctrl+Q 后台运行
        - docker attach dockername 返回dockername容器
    - 方式二：
        - docker run -d IMAGE [COMMAND] [ARG...] 
 - 查看容器日志：
    - docker logs [-f] [-t] [--tail] container01
        - -f --fellows=false
        - -t --timestamps=false
        - --tail="all" 
        - 例：docker logs -ft --tail 0 container01
 - 查看容器进程：docker top containers01
 - 在运行中的容器内启动新进程：
    - docker exec [-d] [-i] [-t] container01 [COMMAND [ARG...] 
#### 设置容器的端口映射
 - docker port container01 查看端口映射情况（docker ps也可以）
 - run [-P] [-p] 
    - -P --publish-all=false 为容器暴露所有端口进行映射
    - -p --publish=[] 指定端口。
        - docker run -p 80 -i -t ubuntu /bin/bash 宿主机随机映射到容器80端口
        - docker run -p 8080:80 -i -t ubuntu /bin/bash 对应端口映射 
        - docker run -p 0.0.0.0:80 -i -t ubuntu /bin/bash
        - docker run -p 0.0.0.0:8080:80 -i -t ubuntu /bin/bash
#### docker
 - 镜像存储位置：/var/lib/docker (通过docker info 查看)
 - docker images查看镜像
 - 查看镜像：docker inspect [OPTIONS] CONTAINER|IMAGE [CONTAINER|IMAGE]
 - 删除镜像：docker rmi [OPTIONS] IMAGE [IMAGE...]
    - -f, --force=false Force removal of the image
    - --no-prune=false Do not delete untagged patents
 - Image id 可以对应于多个tag。
    - docker rmi ubuntu:12.04 删除ubuntu仓库中标签为12.04的镜像（如果该镜像对应多以标签，则只删除标签）
    - docker rmi $(docker images -q ubuntu) 删除ubuntu中的所有镜像
####获取和推送镜像
 - 搜索：Docker Hub：https://registry.hub.docker.com  或者（docker search ubuntu）
 - 拉取：
    - pull ubuntu:16.04
    - 使用--registry-mirrot选项
        - 1、修改/etc/default/docker
        - 2、添加DOCKER_OPTS="--registry-mirror=http://MIRROR_ADDR（DAOCLOUD加速器地址）" 
 - 推送：docker push container01 （需要Docker Hub账号）
#### 构建镜像
 - 以软件的形式打包并分发服务及其运行环境
 - 1、docker commit -a 'Author' -m 'message' container01 new_container02 
 - 2、使用Dockerfile构建镜像
    - a、在当前目录创建一个Dockerfile，df_test1
        ```txt
        From ubuntu:16.04
        MAINTAINER dormancypress "dormancypress@outlook.com"
        RUN apt update
        RUN apt install -y nginx
        EXPOSE 80   
        ```  
    - b、docker build -t='dormancypress/df_test1' .
 - 启动配置文件：/etc/default/docker
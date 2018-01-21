Docker Hub是一个由Docker公司负责维护的公共注册中心，它包含了超过15,000个可用来下载和构建容器的镜像，并且还提供认证、工作组结构、工作流工具（比如webhooks）、构建触发器以及私有工具（比如私有仓库可用于存储你并不想公开分享的镜像）。  
### Docker命令和Docker Hub  
Docker通过docer search、pull、login和push等命令提供了连接Docker Hub服务的功能，本页将展示这些命令如何工作的。
#### 账号注册和登陆
一般，你需要先在docker中心创建一个账户（如果您尚未有）。你可以直接在Docker Hub创建你的账户，或通过运行：

    $ sudo docker login
这将提示您输入用户名，这个用户名将成为你的公共存储库的命名空间名称。如果你的名字可用，docker会提示您输入一个密码和你的邮箱，然后会自动登录到Docker Hub，你现在可以提交和推送镜像到Docker Hub的你的存储库。

注：你的身份验证凭证将被存储在你本地目录的.dockercfg文件中。

#### 搜索镜像
你可以通过使用搜索接口或者通过使用命令行接口在Docker Hub中搜索，可对镜像名称、用户名或者描述等进行搜索：

    $ sudo docker search centos
    NAME           DESCRIPTION                                     STARS     OFFICIAL   TRUSTED
    centos         Official CentOS 6 Image as of 12 April 2014     88
    tianon/centos  CentOS 5 and 6, created using rinse instea...   21
    ...
这里你可以看到两个搜索的示例结果：centos和tianon/centos。第二个结果是从名为tianon/的用户仓储库搜索到的，而第一个结果centos没有用户空间这就意味着它是可信的顶级命名空间。/字符分割用户镜像和存储库的名称。

当你发现你想要的镜像时，便可以用docker pull <imagename>来下载它。

    $ sudo docker pull centos
    Pulling repository centos
    0b443ba03958: Download complete
    539c0211cd76: Download complete
    511136ea3c5a: Download complete
    7064731afe90: Download complete
现在你有一个镜像，基于它你可以运行容器。

#### 向Docker Hub贡献
任何人都可以从Docker Hub仓库下载镜像，但是如果你想要分享你的镜像，你就必须先注册，就像你在第一部分的docker用户指南看到的一样。

#### 推送镜像到Docker Hub
为了推送到仓库的公共注册库中，你需要一个命名的镜像或者将你的容器提到为一个命名的镜像，正像这里我们所看到的。

你可以将此仓库推送到公共注册库中，并以镜像名字或者标签来对其进行标记。
推送镜像的规范是：  
docker push 注册用户名/镜像名  
如果不是规范命名，则需通过tag进行规范：  

    $ docker tag myimages khermit/myimages

    $ sudo docker push yourname/newimage:v1
镜像上传之后你的团队或者社区的人都可以使用它。




















  
参考自：http://wiki.jikexueyuan.com/project/docker/userguide/dockerrepos.html
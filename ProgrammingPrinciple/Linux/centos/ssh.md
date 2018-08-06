多台机器之间免密访问

1、在每台服务器上都执行ssh-keygen -t rsa生成密钥对:

ssh-keygen -t rsa

2、在每台服务器上生成密钥对后，将公钥复制到需要无密码登陆的服务器上：

ssh-copy-id -i  ~/.ssh/id_rsa.pub [root@192.168.15.241](mailto:root@192.168.15.241)

ssh-copy-id -i  ~/.ssh/id_rsa.pub [root@192.168.15.242](mailto:root@192.168.15.242)

说明：ssh-copy-id可以自动将公钥添加到名为authorized_keys的文件中，在每台服务器都执行完以上步骤后就可以实现多台服务器相互无密码登陆了，默认访问端口为22。如果端口不是22，则改为：

ssh-copy-id -i ~/.ssh/id_rsa.pub “-p 10022 [user@server](mailto:user@server)”



pac manager集群模式可以方便管理集群。
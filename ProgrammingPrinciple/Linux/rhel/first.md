RHEL7系统用systemd代替原来 的inittab。效率高，但变化大。

[网页版阅读地址](http://www.linuxprobe.com/book)

top  
uptime  
free -h/m  
ps -aux:所有用户，额外信息，不包括终端  
pstree  
netstat -ap | grep 5500 查看5500端口占用程序  
crtl + z ：暂停当前终端的任务，然后可以去执行别的任务。  
jobs :可以查看后端暂定的服务。然后通过 fg 1 来重新运行。  

uname -a  
who / w / whoami /id
last: 登录信息
history -c清空 通过查看历史命令的编号，可以通过!7来调用第7个历史命令  
sosreport 把系统信息打包  

init 3(文字界面ctrl+alt+F1切换)、 init 5（图形界面）  
cd - 返回上一次目录  
ls -al 所有、长格式  
cat -n（行号） 短文件    
more 长文件  
head -n 10 前十行  
tail -n 10  
cat my.txt | tr [a-z] [A-Z] 将小写替换为大写  
wc -l/w/c my.txt 行数/单词数/字节数  
stat 文件的详细信息  
cut -d : -f 1,2 my.txt 在文件中，：作为分隔符，输出第1、2列。 cut -d : -f 1 /etc/passwd | grep -l 用户数量。  
diff a.txt b.txt  -c具体不同  
file 查看文件类型  
touch -a(access) -m(modify) -d(-am) 时间 文件 （修改文件的时间）  
mkdir -p 递归常见目录  
rm -f 不提醒  
dd if=/dev/zero of=myfile count=2 bs=100M 将/dev/zero中大小为100M的两个数据块拷贝到myfile。可以用来1、数据备份。2、测试硬盘读写速度。3、制作光盘镜像。  
dd if=/dev/zeros of=/boot/file count=1 ns=3M /dev/zeros有无穷多的数据，但不占用空间  
tar -zcvf a.tar.gz /etc 用Gzip算法、打包、显示、目标文件  
tar -zxvf a.tar.gz -C /etc 解压 到 /etc目录  
grep -n word file.txt 在文本中搜索word，并显示行号。 -v 反向选择（列出没有关键词 的行） -i 忽略大小写  
cp -pdr 保留权限，所有者，所有组 相当于-a   
find / -name file.txt  
find / -user root  
find / -user quan -exec cp -rf {} /home/quan2 \; find的结果通过-exec匹配{}，然后对{}进行操作。  

grep "/sbin/nologin" /etc/passwd | wc -l  
echo ubuntu | passwd --stdin root 将root用户的密码改为ubuntu。--stdin接收管道的输出  
ls >> file 2>&1 等同于 ls &> file  
ls -d 只有目录  
双引号：可以提取字符串内部的变量。  
单引号：纯字符  
alias ysh="ch /home" 命令别名，重启无效。除非写入/etc/profile  
为什么不能在$PATH中添加当前目录？（黑客上传ls，root执行ls）  
su - quan 切换用户（整个环境和用户变量）  
set：系统所有变量 snv：系统环境变量  
unset:取消环境变量
echo $HISTSIZE 历史命令的行数  
`ls`等同于$(ls)  
echo "$(ifconfig)"  保留换行符号和空格  
[ $USER = root ] && echo "root" || echo "user"  
read -p "Enter your score（0-100）：" GRADE  
$# $* $? $0 $1 参数个数 所有参数 上一条命令执行成功与否 文件名 第一个参数  
开始的时候，输入:set autoindent设置自动缩进  
hostlist=$(cat ip.txt)  
for IP in $hostlist  
do  ping -c 3 -i 0.2 -w 3 $IP &> /dev/null  
case "$KEY" in  
[a-z])  
echo "a-z"  
;;  
esac  
一次性计划任务：  at 7:00 reboot (ctrl+d结束并保存) at -l;atq;atrm 1  
service crond start  
crontab -e/l/r [-u user] 创建、查询、删除。 分 时 日 月 星期 命令. 分不能为空和*，日和星期不能同时使用（冲突）。  
25 3 * * 1,3,5 /usr/bin/tar -vczf backup.tar.gz /home/project  
0 23-8/2,9 * * * 23至8点之间每两个小时 和 9点  

useradd -d /home/linux -u 8888 -s /sbin/nologin linuxuser (1000开始)  
passwd -l/u/e/S username 锁定、解锁、强制修改密码、查看锁状态  
用户的信息保存在/etc/passwd中  用户名称、是否有密码、uid、gid、描述信息、家目录、解释器
groupadd -g 8888 gname 创建用户组  groupdel  gname  。对应于/etc/group文件
usermod -u/g/G/L/U/s username uid、groupid、扩展用户组、锁定、解锁、变更默认终端  
usermod -G root username 加入root组  
userdel -f/r username 强制/同删家目录  
-/d/l/b/c/p 普通、目录、连接、块、字符设备、管道文件  
文件特殊权限： SUID 4 SGID 2 SBIT 1 文件权限：0777
- SUID：让执行者临时拥有属主的权限。/bin/passwd文件拥有该权限s。即rws。chmod u+s;chmodu-s  
- SGID：chmod g+t; chmod g-t
    - 让执行者临时拥有属组的权限 ps: -r-xr-sr-x
    - 在某个目录中创建的文件自动继承该目录的用户组
 - SBIT:sticky bit.粘滞位。例如/tmp目录，目录中的文件只能被所有这删除。其他人的权限被替换（x:t, -:T）.chmod o+t； chmod o-t

chmod -Rf 777 dir 递归、强制  
chown -R user:group /tmp 递归改变目录的所有者和所有组  
lsattr -a/l file 查看文件隐藏权限  
chattr +i file;chattr -a file; 无法修改、仅允许追加(日志，防黑客)  
当删除一个文件时，取决于user对该文件的上级目录有没有写入权限  
vim /etc/sudoers user ALL=(ALL) ALL 允许使用的主机 以谁的身份 可执行的命令  
文件的ACL提供的是在所有者、所属组、其他人的读/写/执行权限之外的特殊权限控制，使用setfacl命令可以针对单一用户或用户组、单一文件或目录来进行读/写/执行权限的控制。其中，针对目录文件需要使用-R递归参数；针对普通文件则使用-m参数；如果想要删除某个文件的ACL，则可以使用-b参数。  
setfacl -Rm u:username:rwx /root 单独给username用户在/root目录的rwx权限。  
可以看到文件的权限最后一个点（.）变成了加号（+）,这就意味着该文件已经设置了ACL了  
getfacl /root  
facl > 特殊权限 > 隐藏权限 > 普通权限 ?  

HDF协定：  
主分区编号从1开始至4结束，也可指定分配数字。  
逻辑分区从编号5开始，也可指定分配数字。  
SCSI/SATA/U盘设备：/dev/sd[a-p]  
sda5: sd代表存储设备，a:第一个被识别到，5：分区编号为5（逻辑分区）  
挂载：在使用硬件设备之前，需要将该设备与一个已存在的目录相关联，即可通过该目录访问设备数据。  
mount /dev/cdrom /media/cdrom 将/dev/cdrom设备挂载到/media/cdrom。 umount /dev/cdrom  
df -h查看设备挂载信息  
fdisk /dev/sdb 交互式查看磁盘设备。 p 分区信息；n分区;  
mkfs.ext4 /dev/sdb3 将设备格式化为ext4.然后挂载，就可以使用了。  
mkswap /dev/sdb5 && swapon /dev/sdb5 设置交换分区。重启失效。
在/etc/fstab文件中写入挂载信息，以永久挂载  
/dev/sdb3 /backup ext4 defaults 0 0 
/dev/sdb5 swap swap defaults 0 0  
quota 为用户设置文件个数，磁盘配额  
sfs_quota -x -c 'limit bsoft=3m bhard=6m isoft=3 ihard=6 username' /boot 专家 参数  
sfs_quota -x -c report /boot 查看/boot目录的一些限制  
edquota -u username 查看用户磁盘配额（root用户可以修改）  

硬链接（hard link）：可以将它理解为一个“指向原始文件inode的指针”，系统不为它分配独立的inode和文件。所以，硬链接文件与原始文件其实是同一个文件，只是名字不同。我们每添加一个硬链接，该文件的inode连接数就会增加1；而且只有当该文件的inode连接数为0时，才算彻底将它删除。换言之，由于硬链接实际上是指向原文件inode的指针，因此即便原始文件被删除，依然可以通过硬链接文件来访问。需要注意的是，由于技术的局限性，我们不能跨分区对目录文件进行链接。相当于复制一个inode文件。

软链接（也称为符号链接[symbolic link]）：仅仅包含所链接文件的路径名，因此能链接目录文件，也可以跨越文件系统进行链接。但是，当原始文件被删除后，链接文件也将失效，从这一点上来说与Windows系统中的“快捷方式”具有一样的性质。  
ln -s file file_ln 给file创建软链接，没有s则为硬链接。  
du 命令就是用来查看一个或多个文件占用了多大的硬盘空间. du -sh /*命令来查看在Linux系统根目录下所有一级目录分别占用的空间大小。  

在企业里，先做raid，再装系统？
mdadm -Cv /dev/md0 -a yes -n 4 -l 10 /dev/sdb /dev/sdc /dev/sdd /dev/sde  其中，-C参数代表创建一个RAID阵列卡；-v参数显示创建的过程，同时在后面追加一个设备名称/dev/md0，这样/dev/md0就是创建后的RAID磁盘阵列的名称；-a yes参数代表自动创建设备文件；-n 4参数代表使用4块硬盘来部署这个RAID磁盘阵列；而-l 10参数则代表RAID 10方案；最后再加上4块硬盘设备的名称就搞定了。  
mkfs.ext4 /dev/md0  
mount /dev/md0 /RAID  
mdadm -D /dev/md0  

mdadm -Cv /dev/md0 -n 3 -l 5 -x 1 /dev/sdb /dev/sdc /dev/sdd /dev/sde  现在创建一个RAID 5磁盘阵列+备份盘。在下面的命令中，参数-n 3代表创建这个RAID 5磁盘阵列所需的硬盘数，参数-l 5代表RAID的级别，而参数-x 1则代表有一块备份盘。当查看/dev/md0（即RAID 5磁盘阵列的名称）磁盘阵列的时候就能看到有一块备份盘在等待中了。
mkfs.ext4 /dev/md0 && mkdir /RAID    
mount /dev/md0 /RAID  
df -h && mdadm -D /dev/md0
  
LVM逻辑卷管理器：技术是在硬盘分区和文件系统之间添加了一个逻辑层，它提供了一个抽象的卷组，可以把多块硬盘进行卷组合并。这样一来，用户不必关心物理硬盘设备的底层架构和布局，就可以实现对硬盘分区的动态调整。优点：
 - 灵活调整分区大小
 - 屏蔽磁盘底层技术（可随意切分大小，而不是按照磁盘的个数切分）

*LVM的核心理念*：物理卷处于LVM中的最底层，可以将其理解为物理硬盘、硬盘分区或者RAID磁盘阵列，这都可以。卷组建立在物理卷之上，一个卷组可以包含多个物理卷，而且在卷组创建之后也可以继续向其中添加新的物理卷。逻辑卷是用卷组中空闲的资源建立的，并且逻辑卷在建立后可以动态地扩展或缩小空间。  

accept、log、reject、drop  
iptables -L 查看
iptables -F && service iptables save 清空、保存  
iptables -I INPUT -s 192.168.10.1 -p icmp -j REJECT 往INPUT链头部中插入规则：源地址为192.168.10.1的icmp协议 拒绝  
iptables -I INPUT -p tcp --dport 12345 -j REJECT 不允许任何人访问tcp的12345端口  
将INPUT规则链设置为只允许指定网段的主机访问本机的22端口，拒绝来自其他所有主机的流量.再次重申，防火墙策略规则是按照从上到下的顺序匹配的，因此一定要把允许动作放到拒绝动作前面，否则所有的流量就将被拒绝掉，从而导致任何主机都无法访问我们的服务：
 - iptables -I INPUT -s 192.168.10.0/24 -p tcp --dport 22 -j ACCEPT 在链头部添加规则
 - iptables -A INPUT -p tcp --dport 22 -j REJECT 在INPUT链末端添加规则  

iptables -P INPUT DROP 设置INPUT默认策略为拒绝（不能为REJECT），则必须添加允许的规则：iptables -I INPUT -s 192.168.10.1 -p tcp --dport 22 -j ACCEPT  
iptables -D INPUR 1 删除INPUT中的第一条规则  

先保存并关闭iptables，然后使用firewall  
firewall-cmd --get-default-zone 获取默认区域  
firewall-cmd get-zones  
firewall-cmd --set-default=trusted  
firewall-cmd --reload 立即生效  （永久生效vs当前生效）
firewall-cmd --zone=public --query-service=ssh 查询ssh服务是否被允许  
firewall-cmd --panic-on/off 紧急关闭  
firewall-cmd --permanent --zone=public --add-service=http 永久开启http服务，并要reload  
firewall-cmd --permanent --zone=public --add-port=8080-8088/tcp  
firewall-cmd --zone=public --list-ports  
firewall-cmd --permanent --zone=public --add-rich-rule="rule family="ipv4" source address="192.168.10.0/24" service name="ssh" reject"  
firewall-config 图形化界面  

基于应用服务的配置：  
/etc/host.allow： sshd:192.168.10.1/24 优先匹配允许规则     
/etc/host.deny:  sshd:192.168.10.1/24  保存之后直接生效  







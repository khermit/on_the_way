RHEL7系统用systemd代替原来 的inittab。效率高，但变化大。

[网页版阅读地址](http://www.linuxprobe.com/book)

top  
uptime  
free -h/m  
ps -aux:所有用户，额外信息，不包括终端  
pstree  
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


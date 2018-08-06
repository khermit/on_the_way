# 文件处理



### 刻录

```
首先在Linux系统中打开终端，使用dd命令，格式如下：
　　sudo dd if=xxx.iso of=/dev/sdb
　　命令中xxx.iso是你的ISO镜像文件的路径，of=后面的你的U盘路径，一般就是/dev/sdb或者/dev/sdc（后面不要带1或者2的数字）。
　　如何确认U盘路径：终端中输入：
　　sudo fdisk -l
　　完整实例：
　　sudo dd if=/home/mtoou/下载/xubunut.iso of=/dev/sdb
　　涵义：向sdb磁盘写入位于/home/mtoou/下载/目录下的xubuntu.iso镜像文件。输完上述dd命令后回车执行，系统就开始制作启动盘了，期间终端命令窗口不会有任何反馈，但能通过U盘运行指示灯看到U盘在进行读写操作，这个过程可能持续5、6分钟才完成。当看到终端命令窗口有返回消息即制作完成。
```


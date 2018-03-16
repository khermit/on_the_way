##  区分(type command)

- 内部命令built-in：echo, cd, pwd. 像cd，pwd这些内置命令是属于Shell的一部分，当Shell一运行起来就随Shell加载入内存，因此，当我们在命令行上输入这些命令就可以像调用函数一样直接使用，效率非常高。

- 外部命令：ls, cat, mkdir. 当我们在命令行输入cat，当前的Shell会fork一个子进程，然后调用exec载入这个命令的可执行文件，比如bin/cat，因此效率上稍微低了点。

- ./test.sh 是子bash执行，而 . test.sh是当前shell执行（或者source）。

  ​


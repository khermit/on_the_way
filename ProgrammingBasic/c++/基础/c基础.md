## 基础

[TOC]

### １．gcc编译

分步编译：(-o为指定生成文件名)

1. 预处理： gcc -E hello.c -o hello.i  　宏定义展开、头文件展开、条件编译、删除注释。不检查语法。
2. 编　译： gcc -S hello.i -o hello.s　检查语法，将预处理后的文件编译生成汇编文件
3. 汇　编： gcc -c hello.s -o hello.o　将汇编文件生成目标文件(二进制文件)，但不可执行(例如图像)。
4. 链　接： gcc     hello.o -o hello_elf　将需要的库链接到最终的可执行文件中去。

查看软件需要的库：　ldd命令、Depends.exe



### ２．关键字

#### extren

extern int a; 声明变量a，但是没有申请分配空间。

#### define

\#开头的为预处理语句，无需分好结束

在预处理阶段，只要出现MAX出现的地方，都由100来替代。

为常量，不允许修改。

```c
#define MAX 100　//
```

#### const

修饰变量为只读，不允许修改。但在c语言中可以通过指针来修改：（c++不可以）

```c
const int b = 10;
int* p = &b;
*p = 20;//修改成功。
```



#### goto

只能跳转到统一作用域

#### break

只跳出最内层循环



### 3. 数组

不初始化的局部数组为随机数。

数组名是首元素地址。

内层中只有一维。

```c
int a[10];
int a[3] = {1,2,3}; //其余元素补０
int a[10] = {0}; //全部初始化为０
int n = sizeof(a)/sizeof(a[0]); //数组大小
int a[2][2] = {0}
```

 C原因中没有字符串类型，用字符数组模拟，以'\0'来结尾。'\0'与0等价。

\0后面最好别跟数字，因为有可能跟后面的数字组成一个转移字符。'\012'就时'\n'.

```c
char a[] = {'a','b','\0'}; //字符串
char a[] = {'a','b',0};　//字符串
char a[] = {'a','c'};　//字符数组,长度为2
char a[] = "abc";//自动在末尾添加'\0'，长度为４
printf("%s\n", a);//以字符串输出时，找到'\0'就提前或延后结束。
```

sizeof(a)测数据类型大小，不会因为中间的'\0'而影响，跟'\0'无关。

unsigned int strlen(const char *s); 计算第一个结束符'\0'之前的字符长度; \<string.h>



### 4.字符串处理函数

scanf("%s", arr); //将输入放入缓冲区，并以**空格**分割。linux下不做越界检查，**不安全**。

char *gets(char *s); //从标准输入读入字符，并保存到s空间，直到出现**换行符号或者文件末尾**。失败返回NULL。也不做越界检查，**不安全**。

char *fgets(char *s, int size, FILE *stream); //从stream中(stdin为标准输入)读入字符，保存到ｓ空间，直到出现换行、文件尾部或已经读了size-1个字符为止，如果新行可读，则会存到buffer中。最后自动加上'\0'作为字符串结束。fgets(buf, sizeof(buf), stdin).

**安全**。

输出：

int puts(const char *s); 标准设备输出ｓ字符串，自动　在后面添加换行符。

int fputs(const char *str, FILE * stream); 将str的字符串写到stream(stdout)中，字符串结束符'\0'不写入文件。

strcpy(des, src); 从src拷贝，遇到'\0'结束

strncpy(des, src, 8);拷贝指定的８个，若遇到'\0'提前结束　memcpy()可以拷贝'\0'

int strcmp(const char *s1, const char *s2);

int strncmp(const char *s1, const char *s2, int n);//指定比较前n个字符

strcat(dst,src); 将src追加到dst

sprintf(dst, "a = %d\n", a); 将格式化的字符串输出到dst数组

sscanf(src, "%d, %d", &a, &b); 将src中的数字以src的格式提取出来给a,b。　提取字符串，默认以空格分割，其它不行。

char *strchr(const char *s, int c);返回c在ｓ中的第一次的地址

char *strstr(const char *s, const char *newstr)；返回newstr第一次出现的地址

char *strtok(char *str, const char *delim);将切割字符换成结束符。会破坏原来的字符换。第二次调用时，第一个参数需要写NULL，p = strtok(NULL, ','); 用循环写比较合适。

int atoi(const char *nptr);跳过前面的空格字符，转换数字字符，直到遇到非数字字符或者结尾。

atof, atol

将数字转为字符串： sprintf(buf, "%d", 123);

函数中的exit(1);会结束整个进程。

**strlen("abc");大小为３,即字符的有效长度**，不包括后面的'\0'，但是后面有’\0‘；

**sizeof("abcdef")；大小为４，因为sizeof求的是数据类似，"abcd"的数据类型为char*。**



### 头文件

调用函数时，只会往前找定义和声明，没有的化，ｃ编译器会警告、c++会出错。

声明告诉编译器，这个函数是有定义的，只是放在了别的地方。

在用之前必须有定义，如果之前没有定义，必须在调用前声明。

一个函数只能定义一次，但可以声明多次。

声明的形参名可以不一样，甚至可以不写形参变量名。

函数定义放在a.c中，声明放在a.h中，在使用时，只需要include "a.h"即可，告诉编译器在别的地方有函数定义。

为解决include多次同一个.h文件，可以采用两种方式：

```c
#pragma once

#ifndef _A_H
#define _A_H
extern int my_strlen(char buf[]);
#endid;          
```

\#ifndef只能解决在一个文件中多次include，只有一次有效。而不是在多个文件中。

所以头文件中只能有声明，把所有定义放在c文件中，保证所有的定义只定义一次。

#### 宏定义

只要定义了，不管是不是在函数里面，其作用域都在定义以后。宏定义只是**简单的替换**：

```c
#define test(a，b) a*b　
int a = test(1+1,2); //替换为： 1+1*2　所以写宏定义要加括号：#define test(a,b) (a)*(b)
```

用宏定义实现比较大小：

```c
#define MAX2(a,b) (a) > (b) ? (a) : (b)
#define MAX3(a,b,c) (a) > MAX2(b,c) ? (a) : MAX(b,c) //错误
#define MAX3(a,b,c) (a) > (MAX2(b,c)) ? (a) : (MAX(b,c)) //正确，要加括号，不然就可能会出错
```

#### 条件编译

```c
#ifndef A  #ifdef A   #if 表达式
#else
#endif
```





### 指针

%p以１６进制打印指针。

＆a: 对变量a取地址

*a：对变量a解地址，取其地址上的内容。

**操作野指针变量本身p=0x123没有问题，但是操作野指针指向的内存*p=100就会导致内存段错误。**

```c
*p == *(p+０．) == p[0]　
```

万能指针：void* 可以指向任何类型(类型匹配)，使用时要转化为它本身的指针类型，要确定解析多少字节地址。

```
void* p = &a;
*( (int *)p ) = 100;
```

指针步长：为指针指向的数据类型大小。void*不能确定步长。

```c
const int* p; //指针所指向的内存只读
int const* p;　//指针所指向的内存只读
int * const p; //指针p只读。
```

指针数组：指针的数组，它是数组，每个元素都是指针　int* p[3];

数组指针：数组的指针，它是指针，指向数组的指针

swap(a,b) 不管变量是什么类型，只要是变量本身传递，就是值传递。

swap(&a, &b) 地址传递，变量的地址。

形参中的数组： fun(int a[]),  fun(int* a), fun(int a[100])都当做 int* a处理。所以sizeof(a)的大小只是指针大小。

但是：fun(int b\[][]) 不等价于 fun(int **p)　即二位数组不是二级指针。

```c
char *p[] = {"abc", "mike"};//正确
char** p2 = {"abc", "mike"};//错误， 因为char**只能指向一个char*，不能有多个。但是char**可以指向数组p数组元素首地址
char** p3=&p[0];
```



fun(char * p[10])，fun(char * p[])，　fun(char * *p)即指针数组。

```c
int main(int argc, char *argv[]){
    int i = 0;
    for(i = 0; i < argc; i++)
        printf("%s ", argv[i]); //
    return 0;
}
```



int* fun(); 返回值为指针类型

在linux 64下不允许返回局部变量的地址。

printf("%s", str); 打印整个字符串

printf("%s", *str); 打印第一个字符	*(str+1)



全局变量：

```c
int a; //普通全局变量，在任何地方都可以使用，整个程序结束后才释放。
int main(){
    
}
```



字符串常量放在data区，**文字常量区**。每个字符代表字符串的首元素地址。"abc"，"abc"+1代表"b"的地址。

企图通过指针来修改字符串常量，则会出现段错误：char *p = "hello"; strcpy(p, "abc");

字符串常量区与程序声明周期一样。

字符数组不用于文字常量：char buf[]="abc"; char *p="abc";　两者不同。后者在字符常量区，前者则可以改,因为数组申请的内存不在常量区。



局部变量：auto int a; auto可省略。执行到时才分配内存。　作用域在当前{}，{int a; {int a;}} 就近原则。

static局部变量：在data段，在编译阶段就已经分配空间，函数在没有调用之前就已经存在，程序结束时才释放，默认初始化为0；初始化语句只会执行一次，只能用常量初始化，不能用变量初始化，因为static初始化时，变量还不存在。但是可以赋值多次。作用域在{}内。

extern int a = 10;//错误，只有声明，没有定义，不能赋值

普通全局变量：（外部链接）

在编译阶段就已经分配空间。在{}函数外面定义的变量为全局变量。任何地方都可以使用该变量。使用前找不到定义，则需要声明，声明时不要赋值extern int a。可以声明多次。默认初始化为０；在多个文件中，全局变量只能定义一次，不能重复定义。

注意：针对全局变量，int b; int b; int b;　在c中合法，其中一次为定义，两次为声明，但c++不合法。无法确定哪一次是定义，哪一次是声明。但是 int b = 0; 这肯定是定义。

static 全局变量：(内部链接)

与普通全局变量的区别就是文件作用域不一样。static全局变量只能在本文件使用，不同文件的static全局变量没有影响。而普通全局变量可以在全部文件中使用，前提是需要声明。

extern只适用于普通全局变量。

普通函数和static函数的区别：文件作用域不一样，static函数只能在定义的文件中使用。



### 内存分区

在程序没有执行前，已经确定的分区：通过size a.out查看：

- text代码区：只读，函数，可执行文件的二进制代码。
- data：初始化的数据，全部变量，static变量，文字常量区(只读)
- bss：没有初始化的数据，全局变量，static变量（用０初始化）

ulimit -a查看系统支持的栈、文件描述符大小等

加载内存：

前加载text, data, bss，然后加载stack栈区、heap堆区

- stack栈区：普通局部变量，函数参数，返回值，自动管理内存
- heap堆区：手动申请空间，手动释放



```c
void* memset(void* s, int c, size_t n);//将ｓ区域的前ｎ个字节填充为ｃ,c按照ASSIC处理（字符）
memset(a, 0, sizeof(a));
void* memcpy(void* dest, const void *src, size_t n);//将src的前n个字节拷贝到dest地址上
memcpy(a,b,sizeof(b));// dest和src不要有内存重叠。如果出现内存重叠，则使用memmove:
mommove($a[2], a, 5*sizeof(int));
memcmp(a, b, 9*siezof(int));//比较a与b的前9*siezof(int)个大小。

```

分配内存：

```c
int a;//栈区
int* p = (int*)malloc( sizeof(int) );//返回的是void*，需要转换。
free(p);//释放p所指向的内存，而不是释放p，p在栈上分配。只能释放一次。
p = NULL;//释放完之后置为NULL
```

内存污染：gcc编译器不能检查出来，能够成功运行，不代表没有错误。

```c
char *p = (char*)malloc(0);
if( NULL == p ){
    printf("failed\n");
    return 0;
}
strcpy(p, "abc");
prinf("%s\n", p);
free(p);
return 0;
```



### 结构体

```c
//1.1 先定义类型，再定义变量
// struct Teacher合在一起才是类型，定义类似时是不分配空间的。
struct Teacher{
    int age;
};
//定义变量
struct Teacher t1;

//1.2 定义类型同时定义变量
struct Teacher２{
    int age;
}t2,t3;

//1.3 匿名
struct {
    char name[50];
    int age;
}t4,t5;
```

```c
//初始化
struct Teacher t1 = {"Tom", 18};
```

```c
//typedef改名字
typedef struct Teacher3{
    int age;
}Teacher3;
Teacher3 t9;
t9.age = 10;
Teacher3 *p = &t9;
p->age = 11; // 通过指针方式
```

相同类似的结构体可以直接复制拷贝：t1 = t2;



字节对齐：linux默认#pragma pack(4)

- 原则1：**数据成员**对齐规则：结构(struct)(或联合(union))的数据成员，第一个数据成员放在offset为0的地方，以后每个数据成员的对齐按照#pragma pack指定的数值和这个数据成员自身长度中，比**较小**的那个进行。
- 原则2：**结构(或联合)的整体**对齐规则：在数据成员完成各自对齐之后，结构(或联合)本身也要进行对齐，对齐将按照#pragma pack指定的数值和结构(或联合)最大数据成员长度中，比**较小**的那个进行。
- 原则3：**结构体作为成员**：如果一个结构里有某些结构体成员，则结构体成员要从其内部最大元素大小的整数倍地址开始存储。


对齐的字节数 = min { #pragma pack(n) , 最大成员大小 }.即以最小的为准。要注意不同结构CPU数据通信可能对齐方式不同，为避免错误：

```c
1 void Func(struct B *p){
2     struct B tData;
3     memmove(&tData, p, sizeof(struct B));
4     //此后可安全访问tData.a，因为编译器已将tData分配在正确的起始地址上
5 }
```

或者用#pragma pack (1)将STRUCT_T定义为1字节对齐方式。

可以通过stddef.h库中的offsetof宏来查看对应结构体元素的偏移量。

嵌套：#pragma pack (8)

```c
struct B {
    char e[2];      //长度1 < 8 按2对齐；偏移量为0；存放位置区间[0,1]
    short h;        //长度2 < 8 按2对齐；偏移量为2；存放位置区间[2,3]
    //结构体内部最大元素为double,偏移量为4，提升到8，所以从8开始存放接下来的struct A
    struct A {
        int a;      //长度4 < 8 按4对齐；偏移量为8；存放位置区间[8,11]
        double b;   //长度8 = 8 按8对齐；偏移量为12，提升到16；存放位置区间16,23]
        float c;    //长度4 < 8,按4对齐；偏移量为24，存放位置区间[24,27]
    };
    //整体对齐系数 = min((max(int,double,float), 8) = 8，将内存大小由28补齐到8的整数倍32
};
```

为什么要对齐：若cpu的访问粒度为4，也就是一次性可以读取内存中的四个字节内容；当我们不采用内存对齐策略，如果需要访问A中的b元素，cpu需要先取出0~3四个字节的内容，发现没有读取完，还需要再次读取，一共需要进行两次访问内存的操作；而有了内存对齐，参考左图，可一次性取出4~7四个字节的元素也即是b，这样就只需要进行一次访问内存的操作。所以操作系统这样做的原因也就是所谓的拿空间换时间，提高效率。

可以人工调整结构体元素之间的先后顺序来减小内存空间。



### 文件操作

文件操作API:

- fgetc fputc  按照字符读写文件
- fputs fgets　按照行读写文件（读写配置文件）
- fread fwrite　按照块读写文件（大数据块前移）
- fprintf fscanf　按照格式化进行读写文件



feof(f)：判断f是否到文件末尾。



### 额外功能

#### 产生随机数

```c
#include <time.h>
#include <stdlib.h>
int main(){
    srand( (unsigned int)time(NULL) );//以时间作为种子
    srand(100);
    int num = rand();
}
```

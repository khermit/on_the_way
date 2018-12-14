## new

```c++
int *p = new int;
*p = 10;
delete p;
int a(10);//等价于int a = 10;
int *arr = new int[10];
delete[] arr;
```

malloc free是函数，标准库，stdlib.h。malloc不会调动类的构造函数，free不会调用析构。

new delete是c++中的关键字。new会调用类的构造函数，delete会调用析构。
## STL 标准模板库

[TOC]

序列式容器：位置根据进入时机和地点决定。

关联式容器：根据容器自身规则决定。



迭代器：迭代器是一个类对象，封装了一个指针。遍历容器中元素的指针。

容器与算法分离。通过迭代器关联起来。迭代器begin为第一个元素，end为最后一个元素的下一个。

### 容器

#### vector 动态数组／可变数组

单口容器。迭代器v.begin(),v.end()，反向迭代器：v.rend(), v.rbegin()。

在数组尾部进程插入push_back和删除pop_back

默认两倍增长。

vector构造函数：

```c++
vector<T> v;
voctor(v.begin(), v.end()); //[v.begin(), v.end())
vector(n,elem);
vector(const vector &vec);//拷贝构造
int arr[] = {1,3,4};
vecot<int> v(arr, arr+sizeof(arr)/sizeof(int)); //将数组的开始和结尾作为迭代器传进去　
```

赋值：

```c++
v2.assign();
v2 = v1;
v2.swap(v1);将指针交换。
```



```c++
v.resize(1);
v.resize(1,2)
v.at(i);
v[i];
v.front();返回第一个元素
v.back();返回最后一个元素
insert(const_iterator pos, int count,ele);//在迭代器的地方插入count个ele元素。
erase(const_ietrator start, const_iterator end);
erase(const_iterator pos);//删除迭代器指向的元素,迭代器可+6
clear();//清空
```



```c++
vector<int> v;
v.push_back(10);
vector<int>::iterator pBegin = v.begin();//拿到迭代器的类型。不同类型的迭代器++时处理不一样。
vector<int>::iterator pEnd = v.end();
//容器可以放基础类型和自定类型。默认可以打印基础类型，但需要传入回调函数来打印自定义类型。
for_each(pBegin, pEnd, MyPrintVector);

vector<Person> v;
for(vector<Person>::iterator it = v.begin(); it != v.end; it++){
    cout << (*it).age << ' ';
}
```

收缩容量：即收缩申请的数组空间。

```c++
vector<int>(v).swap(v);//创建一个匿名对象，然后v中的元素去初始化，之后将v与匿名对象交换。最后匿名对象销毁。v中的容量缩减为刚好容纳v中的元素，达到收缩容量的目的。
```

v.reserve(100)；//指定容器容量。

#### deque双端队列

两端访问效率高，支持随机存取。

push_front, pop_front, push_back, pop_back。front(), back(), insert()

原理：分段存储，由中控器负责调度，内部复杂。如果要排序的化，会申请一块大的缓冲区，效率低。

![creenshot from 2018-12-19 17-05-2](/home/quandk/Pictures/Screenshot from 2018-12-19 17-05-23.png)

#### stack栈

不能遍历，不支持随机存储。s.push(10),s.pop(), s.top(), s.size(), s.empty()

#### queue队列

先进先出，一端插入，另一端删除。不支持随机访问，不支持遍历(没有迭代器)d.push(10), d.pop(), p.front(), p.back()

#### list双向链表

push_front(), pop_front(), begin(), end(), push_back(), pop_back()

clear, erase, insert,size(), empty(), resize(1),remove(elem)删除所有值为elem的元素

reverse()反转列表，sort(mycompare)排序

#### String

char*是一个指针，String 是一个类，封装了char\*，封装了find, copy, delete, replace, insert不用考虑内存申请和释放。	

```c++
string s;
string s(10,'a');
string s("abc");
string s2(s);//拷贝构造
s2 = "abc";//重载了赋值操作符
s2.assign("abcd");
s2.at(i);//若越界，抛异常out_of_range
s2[i];//若越界，挂
string& operaotr+=(const string& str);
string& operaotr+=(const char* str);
string& operaotr+=(const cahr c);

string& append(const char *s);
string& append(const char *s, int n);//只作用s的前n个字符串
string& append(const string &s);
string& append(const string &s, int pos, int n);//s中从pos开始的n个字符
string& append(int n, char c);//追加n个c字符

int find(const string& str, int pos = 0) const;
int find(const char *s, int pos = 0) const;
int find(const char *s, int pos, int n) const;//从pos开始找s的前n个字符的第一次位置
int find(const char c, int pos = 0) const;
rfind//最后一次出现的位置。

string& replace(int pos, int n, const string& str);//替换从pos开始n个字符为字符串str
string& replace(int pos, int n, const char* s);//

int compare(const string &s) const; //大于返回１，区分大小写。
int compare(const char *s) const;

string substr(int pos = 0; in n = npos) const;//返回由pos开始的n个字符组成的字符串。

string& insert(int pos, const char* s);//插入
string& insert(int pos, const string& str);
string& insert(int pos, int n, char c);
string& erase(int pos, int n = npos);//删除

```

#### set

底层红黑树实现。set不允许重复元素，multiset允许重复元素。

不能通过迭代器修改值，因为这样会破坏红黑树的结构。

=, size(), empty(), swap(), insert(10),clear(), erase(pos), erase(begin,end), erase(elem)

find(key), lower_bound(keyElem)第一个key>=keyElem的迭代器, upper_bound(keyElem)

s.begin(),s.end()默认排序

```c++

```





### 算法 algorithm

```c++
bool mycompare(Person& p1, Person& p2){
    return p1.age < p2.age;
}
sort(v.begin(), v.end(),mycompare);//支持可随机访问的容器
```


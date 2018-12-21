## STL 标准模板库

[TOC]

序列式容器：位置根据进入时机和地点决定。

关联式容器：根据容器自身规则决定。



迭代器：迭代器是一个类对象，封装了一个指针。遍历容器中元素的指针。

容器与算法分离。通过迭代器关联起来。迭代器begin为第一个元素，end为最后一个元素的下一个。



### 容器

容器是值语义的，元素必须能够被拷贝。拷贝构造，重载=。

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

两端访问效率高，支持随机存取。在中间插入会引起数组的移动。用于排队场景（头部删除，尾部添加）

push_front, pop_front, push_back, pop_back。front(), back(), insert()

原理：分段存储，由中控器负责调度，内部复杂。如果要排序的化，会申请一块大的缓冲区，效率低。

![creenshot from 2018-12-19 17-05-2](/home/quandk/Pictures/Screenshot from 2018-12-19 17-05-23.png)

#### stack栈

不能遍历，不支持随机存储。s.push(10),s.pop(), s.top(), s.size(), s.empty()。

#### queue队列

先进先出，一端插入，另一端删除。不支持随机访问，不支持遍历(没有迭代器)d.push(10), d.pop(), p.front(), p.back()

#### list双向链表

push_front(), pop_front(), begin(), end(), push_back(), pop_back()

clear, erase, insert,size(), empty(), resize(1),remove(elem)删除所有值为elem的元素

reverse()反转列表，sort(mycompare)自己提供排序。find()查找

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

#### set/multiset

底层红黑树实现。set不允许重复元素，multiset允许重复元素。需要排序。自定义对象要自定义排序规则。key就是排序的对象。

不能通过迭代器修改值，因为这样会破坏红黑树的结构。默认从小到达排序。

=, size(), empty(), swap(), insert(10),clear(), erase(pos), erase(begin,end), erase(elem)

find(key), lower_bound(keyElem)第一个key>=keyElem的迭代器, upper_bound(keyElem)找第一个大于keyElem的值。

s.begin(),s.end()默认排序

```c++
set<int>::iterator ret = s1.find(key);//若找到，则返回key位置的迭代器，若未找到则返回s1.end()
if( ret != s1.end() ){
    cout << *ret;
}
//分别返回lower_bound，　upper_bound
pair<set<int>::iterator, set<int>::iterator> myret = s.equal_range(1);
if( myret.first != s.end() ){}
if( myret.second != s.end() ){}
```

```c++
//仿函数
class mycompare{
    public:
    bool operator()(int v1, int v2){
        return v1 > v2;
    }
}
set<int, mycompare> s2;
struct mycompare{
    bool operator()(int i1, int i2){
        return i1 < i2;
    }
}
map<int, int, mycompare> mymap;
```



#### pair

可将两种独立的数据类型合成一个对，通过first, secondtianjia来访问。

```c++
pair<int,int> pair1(10,20);
pair<int, string> pair2 = make_pair(10, "Duke");
pair3 = pair4;
```

#### map/multimap

红黑树实现。不能通过迭代器改变key值。count(key)返回key的个数。

```c++
map<int,int> mymap;
//前三种方法，如果Key已存在，则插入失败。
pair<map<int,int::iterator, bool> ret = mymap.insert(pair<int,int>(10,10));
if( ret.second ) cout << "插入成功";
mymap.insert(make_pair(10,10));
mymap.insert(map<int,int>::value_type(10,10));

mymap[10] = 10;//如果key不存在，则创建pair插入，如果key存在，则修改其value
//如果通过mymap[i]来访问，且key不存在，则会创建key和默认value。
(*it).first, it->second;
```





### 算法 algorithm

```c++
bool mycompare(Person& p1, Person& p2){
    return p1.age < p2.age;
}
sort(v.begin(), v.end(),mycompare);//支持可随机访问的容器
find(iterator beg, iterator end, value);
iterator adjacent_find(iterator beg, iterator end, _callback);//查找相邻重复的元素
bool binary_search(iterator beg, iterator end, value);//
find_if(iterator beg, iterator end, _callback);//根据__callback(int val)返回true，则找到
count(iterator beg, iterator end, value);//出现的次数
for_each(iterator beg, iterator end, _callback);
transform(iterator beg1, iterator end1, iterator beg2, _callback);//将容器1元素通过_callback处理后放到到容器2，空间要足够，resize(10)开辟空间.
random_shuffle(iterator beg, iter end);//乱序，容器必须支持随机访问。list不行。
reverse(beg, end);//反转，容器必须支持随机访问。list不行。
merge(beg1, end1, beg2, end2,dest,_callback);//合并两个有序容器的数到dest中。如果不是默认排序，则加callback规则。
```

拷贝和替换

```;/c++
copy(beg1, end1, dest);
swap(container c1, container c2);
replace(beg, end, oldValue, newValue);
replace_if(beg, end, myCompare,newValue);//只要myCompare为true，则替换
```

```c++
accumulate(beg, end, value);//累加和+value
fill(beg, end, value);//填充
```

集合算法：

```c++
set_intersection 两个有序集合的交集。
set_union 两个有序集合的并集
set_difference 差集
```



### 函数对象

重载函数调用操作符的类，其对象称为函数对象。即使重载()，使得类对象可以像函数那样调用。

因为是类，所以可以用成员来保存每次调用的状态。

```c++
struct MyPrint{
    MyPrint(){
        num = 0;
    }
    int num;
    void operator()(int val){
        num++;
        cout << cal;
    }
};
MyPrint print;
print(10);
MyPrint print2 = for_each(v.begin(), v.end(), print);//print传参为拷贝，所以返回的为真正调用的
```

#### 谓词

返回值为bool类型的普通函数或者函数对象。

#### 內建函数对象（functional库提供）

#### 函数对象适配器

```c++
struct MyPrint{
    void operator()(int val){
        cout << val << " ";
    }
};
for_each(v.begin(), v.end(), MyPrint());
```

如果要在打印val的时候，每个值都加100，如何操作？函数对象适配器来解决。

绑定适配器：将一个二元函数对象转变为一元函数对象

```c++
struct MyPrint : public binary_function<int, int, void>{
    void operator()(int v, int val) const {
        cout << v + val << " ";
    }
};
//for_each只能用一元函数对象。
for_each(v.begin(), v.end(), bind2nd(MyPrint(),100));//将100绑定到函数中。
//bind1st, 将100绑定为第一个参数。即v。
```

not1, not2对一元/二元谓词取反适配器。

ptr_fun：防函数适配器。ptr_fun(MyPrint2) 将普通函数 适配为 函数对象

mem_fun mem_fun_ref：成员函数适配器。for_each调用类的成员函数进程打印。如果是对象指针，用mem_fun。

```c++
for_each(v.begin(), v.end(), mem_fun_ref(&Person::show));
```




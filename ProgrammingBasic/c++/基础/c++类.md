## C++类

[TOC]

默认private

析构与构造都没返回值，析构没有形参。

**显示提供构造(包括拷贝构造)，默认无参构造消失**。析构不能重载。

有显示的析构时，会有默认的析构。可以手动调用，但要保证多次调用没有影响。

拷贝构造函数：

会有默认的拷贝构造函数.

```c++
Test t2(t1); //初始化时调用拷贝构造
Test t3 = t1;//初始化时调动拷贝构造
Test t4;　　
t4 = t1;　　//不是在初始化的时候，就是调用赋值
```

拷贝构造：**当没有显示的拷贝构造时，就会有默认的拷贝构造**

```c++
Ani(const A &another){
    m = another.m;
}
```

先构造的，后析构。

```c++
void fun(Ani ani){　//　Ani ani = ani1; 调用ani的拷贝构造。
}
Ani ani1;
fun(ani1);
```

若函数返回一个对象，但没有参数接收，则立即回收掉。func();

```c++
Test fun(){
    Test temp(10,20);
    return temp;
}// 返回时，构造一个匿名对象 = temp　执行匿名对象的拷贝构造
fun();//没有参数接收该匿名对象，则直接回收
Test t = fun();//有参数接收，则将匿名对象转正直接起名为t，而不是执行t的拷贝构造。并没有再次拷贝。
Test t1;
t1 = fun();//执行t1的赋值操作，然后析构掉匿名对象。
```

**默认拷贝为浅拷贝**，若有malloc申请空间，则多个指针指向同一空间，一旦有一个对象析构释放空间，别的对象会发送段错误。

构造对象成员的顺序跟初始化列表的顺序无关，跟成员对象的定义顺序有关。

构造函数参数列表：

```c++
public:
	B(A &a1, A &a2, int b):ma1(a1),ma2(a2) //调用ma1的拷贝构造，先于构造函数执行
    {
        mb = b;
    }
```

类中定义成员变量时，不要初始化，有的编译器不通过。所以在构造函数中初始化。常量的赋值只能写到初始化列表中。

```c++
void func(Test t){} //调用t的拷贝构造
Test t1;
func(t1);
```

不能构造函数中再调用构造函数。

类的static成员，属于类，在类外赋值：int AA::m_c = 0;　通过AA::m_c访问。该变量在**静态区**。

类的static方法也属于类。

- static成员类外存储，求类的大小并不包含在内。只有类的普通成员变量占用对象空间。
- static成员的命名空间属于类的全局变量，存储在data区。



this：代表当前对象的地址。常量指针：Test * const this类型。对象本身：*this

类的成员函数尾部出现const，修饰的是this指针，变成 Test const * const this。也不允许通过this改变指向的值。

静态成员函数只能访问静态数据成员。因为普通方法在调用时，将this参数传递，而this属于对象，不属于类。所以静态成员函数没有this指针。

```c++
Test& testAdd(Test &another){
    this->a += another.a;
    return *this;
}
t1.testAdd(t2).testAdd(t2);
```



### 友元

在类A中声明：friend void func(A & a)；则该函数中可以直接访问类A的私有成员，不用再去调用set和get，避免了频繁的压栈和出栈。但破坏了类的封装性和隐藏性。单向性。**友元关系不能被继承**、不具有传递性。

友元可以是函数，也可以是类。friend class B;



### 操作符重载

只能对已有的c++云算法进行重载，大部分可以被重载。不能重载的：. .* (a.*p -> *(a.p)) :: ?:不能改变操作符个数、优先级、结合性、默认参数。

new new[] delete 也是操作符，也可以被重载。

```c++
//全局
A operator+(A& a1, A& a2){
    A temp();
    return A;
}
operator+(a1,a2);//调用
//写在类中
a1.operator(a2);//调用

A a3 = a1 + a2;//这种写法能匹配上面两种，若都实现，则会出现二义性
```

```c++
Complex &operator++(){//类中重载前置++，要支持++++a，所以要返回本身
    this->a++;
    return *this;
}
const Complex operator++(int){//亚元，后置++，只支持a++。亚元用来占位，防止二义性。
    Complex temp(this->a);
    return temp;
}
```

<<操作符只能写在全局，不能够写在成员方法，否则方向反了。

```c++
ostream& operator<<(ostream & os, Complex &c){
    os << "complex:" << c.a;
    return os;
}
cout << c;
```

=操作符，与拷贝构造类型，只能是浅拷贝。=支持a=b=c;

```c++
Student & operator=(const Student & another){
    if( this == &another )
        return *this;
    if( this->name != NULL ){
        delets[] this->name;
        this->name = NULL;
    }
    this->id = another.id;
    int len = strlen(another.name);
    this->name = new char[len+1];
    strcpy(this-name, another.name);
    return *this;
}
```

()：该对象为防函数、伪函数

```c++
int operaotr()(int value){
    return value+1;
}
Student(1);
```

new重载：通过malloc实现，但会调用构造函数。 

```c++
void * operator new(size_t size){
    return malloc(size);
}
void operator delete(void * p){//也会出发析构
    if(NULL != p){
        free(p);
        p = NULL;
    }
}
stu->operator new(sizeof(A));
stu->operator new[](sizeof(A)); //当重载new[]时的调用
```

重载&&、||要注意短路现象。

智能指针：自动回收。由一个类实现。需要传入类、申请的地址、重载->等

```c++
#include <memory>
auto_ptr<int> ptr(new int);
*ptr = 20;
auto_ptr<A> ptr(new A(10));
ptr->func();
(*ptr)->fun();
```

字符串类\<string>，内部已经实现了深拷贝。

输入时，先输入到临时空间，再将临时空间拷贝到内存中。

### 继承和派生

```c++
//B类中有　成员变量类A;  B has A; B依赖A
class B{
    A a;
}v
//C的方法中需要A；　C use A;
class C{
    void fun(A * a)
}
//D继承A;　D is A;
class D : public A{
}
```

对于单个类：private和protected都只在类内部可见。

public继承：子类内部可以访问protected成员。不可访问父类的private成员。

protected继承：min(原来，protected)

private继承：都变为private。　	



父类的空间　< 　子类的空间，所以可以用子类去填满父类，转化后能正常使用。所以：

- 父类(指针，引用)　＝　子类(指针，引用)



父类的数据需要由父类构造函数进行构造。父类先构造。

父类和子类有同名变量a，在子类中访问：this->a, Parent::a。

子类也可以访问父类的static变量。

多继承:

```c++
public SofaBed :public Bed, public Sofa{
    
}
```

虚继承：virtual public Bed

防止菱形继承。。。。如何实现》？



多态：发出同一消息，不同的对象执行不同的行为。

编译器是保守的：(正常情况下)

父类指针　＝　父类／子类；　然后通过父类指针调用方法，编译器会统一调用父类的。

多态：父类的virtual函数，子类重写。(子类重写时也可写上virtual，加强可读性，不写也可以)

父类指针　＝　父类／子类；　然后通过父类指针调用对应的父类／子类方法，

要发生多态，就让父类函数为virtual。

```c++
class A {
    A();
    print(){}
    ~A();
}
class B :public A{
    B();
    print(){}
    ~B();
}
B b; //先构造父类，再构造子类
A a;
void func(A *ap){
    ap->print(); //此时，若print为virtual，则发生多态。
}
void func2(A *ap){
    delete ap; //调用delete会自动调用析构函数。
} 
func2(&a);//自动调用a对象的析构
func2(&b);//因为形参：ap = &b;所以执行父类a的方法，而且父类析构函数没有virtual，不会发生多态。即只会执行Ａ的析构函数，并不会执行Ｂ的析构。若要执行B的析构，则需要将父类的析构函数写为virtual,让析构函数也发生多态。子类的析构执行后，会自动触发父类的析构。子类构构造执行前，先执行父类构造。
virtual ~A();
virtual ~B();
```

重写：只发生在virtual函数中。否则叫重定义。

虚函数表，存储虚函数的入口地址，只读（text 符号表中）。每个类有自己的虚函数表，表中放着每个虚函数的地址。子类重写了父类的virtual，不管子类函数有没有写virtual，都为虚函数。

只要类中有virtual函数，编译器在给这个对象开辟空间的时候，会默认增加一个指针vptr，指向该类的虚函数表。

A * ap = &b；让父类类型指针ap指向子类b的首地址，当通过ap调用函数时，会首先在指向的地址（即子类地址）前面的虚函数表中去查找是够有满足的函数，有则直接调用。所以调用的是子类的虚函数。

注意：通过虚函数表指针vptr调用重写函数是在程序运行时进行的，因此要通过寻址操作才能确定真正应调用的函数。而普通函数是在编译时就确定了调用的函数，在效率上，虚函数的效率要低很多。

vptr指针的分布初始化（避免在构造器中写业务）：当new一个子类时，会先执行父类构造，当执行父类构造时，按照父类的方式构造，即vptr指针指向父类的虚函数表。故父类构造器中调用的方法按照父类的vptr指针进行。父类构造好后，按照子类的方式构造子类，此时vptr指针指向子类的虚函数表。

指针步长：

A * ap = &b；父类指针++步长sizeof(A)，子类指针++步长sizeof(B)；当父类指针指向子类，并加加时，会因为步长不同导致段错误。

纯虚函数和抽象类：只要类中有纯虚函数就是抽象类(可以定义成员)。子类没有重写完父类的纯虚函数，子类依然是抽象类，依然不能实例化。

```c++
virtual void fun(int i) = 0;//用virtual和=0来声明一个纯虚函数，没有实现。
```


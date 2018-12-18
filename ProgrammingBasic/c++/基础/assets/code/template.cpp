#include <iostream>

using namespace std;

/*
 * 1.模板、友元
*/
//先声明类模板，再声明函数模板
template<class T> class Person; 
template<class T> void myPrint(Person<T>& p);

template<class T>
class Person{
public:
    T age;
    friend void myPrint<T>(Person<T>& p);//在类中声明友元函数
};
//在类外部定义该友元函数
template<class T>
void myPrint(Person<T>& p){
    cout << p.age << endl;
}

/**
 * 2.类模板,static
 */
template<class T>
class A{
    T age;
    static int s;
};
//类外初始化；
template<class T> int A<T>::s = 0; 

int main(){
    Person<int> p;
    p.age = 10;
    myPrint(p);

    // a1, a2属于不同的类，所有拥有各自类的static变量s
    A<int> a1;
    A<char> a2;

    cout << "hello" << endl;
    return 0;
}
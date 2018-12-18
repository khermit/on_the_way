#include <iostream>
using namespace std;

template<class T>
class MyArray{
public:
    int mCapacity;
    int mSize;
    T* pAddr;
public:
    MyArray(int capacity){
        this->mCapacity = capacity;
        this->mSize = 0;
        this->pAddr = new T[this->mCapacity];
    }
    //拷贝构造
    MyArray(const MyArray<T>& arr){
        this->mCapacity = arr.mCapacity;
        this->mSize = arr.mSize;
        this->pAddr = new T[this->mCapacity];
        for( int i = 0; i < this->mSize; i++ ){ 
            this->pAddr[i] = arr.pAddr[i];
        }
    }
    T& operator[](int index){
        return this->pAddr[index];
    }
    MyArray<T>& operator=(const MyArray<T>& arr){
        if( this->pAddr != NULL ){
            delete[] this->pAddr;
        }
        this->mCapacity = arr.mCapacity;
        this->mSize = arr.mSize;
        this->pAddr = new T[this->mCapacity];
        for( int i = 0; i < this->mSize; i++ ){ 
            this->pAddr[i] = arr.pAddr[i];
        }
        return *this;
    }
    //只能对左值取引用
    void PushBack(T& data){
        if( this->mSize >= this->mCapacity )
            return;
        //使用等号赋值，调用拷贝构造,=号操作符。　对象必须能够拷贝(深拷贝)、所有容器中只是拷贝(值寓意)
        this->pAddr[this->mSize] = data;
        this->mSize++;
    }
    //c++11,可以对右值引用。 -std=c++11
    void PushBack(T&& data){
        if( this->mSize >= this->mCapacity )
            return;
        this->pAddr[this->mSize] = data;
        this->mSize++;
    }
    void Print(){
        for( int i = 0; i < this->mSize; i++ ){
            cout << this->pAddr[i] << " ";
        } 
        cout << endl;
    }
    ~MyArray(){
        if( this->pAddr != NULL ){
            delete[] this->pAddr;
        }
    }
};

void test01(){
    MyArray<int> marray(20);
    int a = 10, b  =20;
    //只能对左值取引用，不能对右值取引用。
    //左值：可以在多行使用。　右值：临时变量，只能在当前行使用。
    marray.PushBack(a);
    marray.PushBack(b);
    marray.Print();
    marray.PushBack(30);
    marray.Print();
}

int main(){
    test01();
    return 0;
}
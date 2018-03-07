#include <stdio.h>

//队列
//rear指向队尾， front指向队头

typedef int DataType;
//节点定义
typedef struct QNode{
    DataType data;
    struct QNode *next;
}QNode;

//队列
typedef struct LinkQueue{
    QNode *front;
    QNode *rear;
}LinkQueue;


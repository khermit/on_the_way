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

//1、初始化
void initQueue(LinkQueue *LQ)
{
    LQ->front = NULL;
    LQ->rear = NULL;
}

//2、清空队列 从头开始
void clearQueue(LinkQueue *LQ)
{
    QNode *tmp = NULL;
    while(LQ->front != LQ->rear)
    {
        tmp = LQ->front;
        LQ->front = tmp->next;
        free(tmp);
    }
    free(LQ->front);
    LQ->front = NULL;
    LQ->rear = NULL;
}

//3、读取队头元素
DataType getTop(LinkQueue *LQ)
{
    if(LQ->front == NULL)
    {
        exit(1);
    }
    return LQ->front->data;
}

//4、是否为空
int idEmpty(LinkQueue *LQ)
{
    return (LQ->front == NULL);
}

//5、入队
void insertQueue(LinkQueue *LQ, DataType elem)
{
    QNode *tmp = NULL;
    tmp = (QNode*)malloc(sizeof(QNode));
    if(NULL == tmp) //未申请到内存
    {
        exit(1);
    }
    tmp->data = elem;
    tmp->next = NULL;
    if(LQ->rear == NULL)//原队列为空
    {
        LQ->front = tmp;
        LQ->rear = tmp;
    }
    else
    {
        LQ->rear->next = tmp;
        LQ->rear = tmp;
    }
}

//6、出队
DataType deleteQueue(LinkQueue *LQ)
{
    QNode *tmp = NULL;
    DataType elem;
    if(LQ->front == NULL)
    {
        exit(1);
    }
    tmp = LQ->front;
    elem = tmp->data;
    LQ->front = LQ->front->next;
    if(LQ->front == NULL)
    {
        LQ->rear = NULL;
    }
    free(tmp);
    return elem;
}

 



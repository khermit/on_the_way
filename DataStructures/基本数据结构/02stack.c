#include <stdio.h>

//链栈
// top -> an ... -> a2 -> a1

typedef int DataType;
//节点定义
typedef struct SNode{
    DataType data;
    struct SNode *next;
}SNode;
//栈顶
typedef struct{
    SNode* top;
}LinkStack;

//1 初始化
void initStack(LinkStack *LS)
{
    LS->top = NULL;
}

//2、清空
void clearStack(LinkStack *LS)
{
    SNode *cur = LS->top;
    SNode *tmp = NULL;
    while(cur != NULL)
    {
        tmp = cur;
        cur = cur->next;
        free(tmp);
    }
    LS->top = NULL;
}

//3、读取栈顶元素,不出栈
DataType getTop(LinkStack *LS)
{
    if(LS->top == NULL)
    {
        exit(1);
    }
    return LS->top->data;
}

//4、判断是否为空
int isEmpty(LinkStack *LS)
{
    return (LS->top == NULL);
}

//5、入栈
void push(LinkStack *LS, DataType elem)
{
    SNode *tmp = NULL;
    tmp = (SNode*)malloc(sizeof(SNode));
    if(tmp == NULL)
    {
        exit(1);
    }
    tmp->data = elem;
    tmp->next = LS->top;
    LS->top = tmp;
}

//6、出栈
DataType pop(LinkStack *LS)
{
    DataType elem;
    SNode *tmp = NULL;
    if(LS->top == NULL)
    {
        exit(1);
    }
    tmp = LS->top;
    LS->top  = LS->top->next;
    elem = tmp->data;
    free(tmp);
    return elem;

}
#include <stdio.h>

//一、单向链表

typedef int DataType;
typedef struct Node{
    DataType data;
    struct node *next;
}Node;

//1、链表初始化, 初始化头结点，头结点不存数据
Node* initList()
{
    Node *head = (Node*)malloc(sizeof(Node));
    head->next = NULL;
    return head;
}

//2、尾插法建立链表
Node* createListTail(DataType a[], int num)
{
    Node *head = (Node*)malloc(sizeof(Node));
    Node *cur = NULL;
    Node *tmp = NULL;
    cur = head;
    for (int i=0; i<num; i++)
    {
        tmp = (Node*)malloc(sizeof(Node));//申请空间并赋值
        tmp->data = a[i];
        tmp->next = NULL;
        
        cur->next = tmp;//连接到cur后面
        cur = cur->next;//cur后移
    }
    return head;
}

//3、头插法建立链表
Node* createListHead(DataType a[], int num)
{
    Node *head = (Node*)malloc(sizeof(Node));
    head->next = NULL;
    Node *tmp = NULL;
    for (int i=0; i<num; i++)
    {
        tmp = (Node*)malloc(sizeof(Node));//申请空间并赋值
        tmp->data = a[i];

        tmp->next = head->next;//tmp指向头节点指向的位置
        head->next = tmp;//头节点指向tmp
    }
}

//4、清空链表
void clearList(Node* head)
{
    Node* tmp;
    while(head->next != NULL)
    {
        tmp = head->next; //释放head指向的节点
        head->next = tmp->next;//将head指向下下个节点
        free(tmp);//释放空间
    }
}

//5、遍历
void outputList(Node* head)
{
    Node *cur = NULL;
    cur = head->next;
    while(cur != NULL)
    {
        pringf("%d", cur->data);
        cur = cur->next;
    }
}

//6、判断是否为空
int isEmpty(Node *head)
{
    return (head->next == NULL);
}

//7、在p前插入一个元素
int insertBefore(Node *head, Node *p, DataType elem)
{
    Node *q = head;
    Node *tmp = NULL;
    while(q->next != p && q->next != NULL)
    {
        q = q->next;
    }
    if(q->next != NULL)
    {
        tmp = (Node*)malloc(sizeof(Node));
        tmp->data = elem;
        tmp->next = p;
        q->next = tmp;
        return 1;
    }
    else{
        return 0;
    }
}

//8、删除一个节点
int deleteNode(Node *head, DataType elem)
{
    Node *p = head->next;
    Node *before = p;
    while(p->data!=elem && p->next!=NULL)
    {
        before = p;
        p = p->next;
    }
    if(p->next != NULL)
    {
        before->next = p->next;
        free(p);
        return 1;
    }
    else{
        return 0;
    }
}

//9、查找
Node* searchList(Node* head, DataType elem)
{
    Node *cur = head->next;
    while(cur != NULL)
    {
        if(cur->data == elem)
        {
            return cur;
        }
        else
        {
            cur = cur->next;
        }
    }
    return NULL;
}

//10、将一个从小到大的有序链表拆分为 逆序 的 奇、偶 链表 （顺序遍历、头插法）
void splitList(Node *head, Node *he, Node *ho)
{
    Node *cur = head->next;
    Node *tmp = NULL;
    while(cur != NULL)
    {
        tmp = cur;
        cur = cur->next;
        if(1 == tmp->data % 2)
        {
            tmp->next = ho->next;
            ho->next = tmp;
        }
        else
        {
            tmp->next = he->data;
            he->next = tmp;
        }
    }
    free(head);
}

//11、合并 逆序
void mergeList(Node *head, Node *hp, Node *hq)
{
    Node *p = hp->next;
    Node *q = hq->next;
    Node *tmp = NULL;
    while(p != NULL && q != NULL)
    {
        if(p->data <= q->data)
        {
            tmp = p;
            p = p->next;
            tmp->next = head->next;
            head->next = tmp;
        }
        else{
            tmp = q;
            q = q->next;
            tmp->next = head->next;
            head->next = tmp;
        }
    }
    while(p != NULL)
    {
        tmp = p;
        p = p->next;
        tmp->next = head->next;
        head->next = tmp; 
    }
    while(q != NULL)
    {
        tmp = q;
        q = q->next;
        tmp->next = head->next;
        head->next = tmp;
    }
}


//二、循环链表

//2.1、初始化
Node* initRList()
{
    Node *head = (Node*)malloc(sizeof(Node));
    head->next = head;
    return head;
}

//2.2、判断为空
int isREmpty(Node* head)
{
    return (head = head->next);
}

//2.3、求循环链表的长度
int getLengthRlist(Node* head)
{
    int len = 0;
    Node *cur = head->next;
    while(cur != head)
    {
        len++;
        cur = cur->next;
    }
    return len;
}


//三、双向链表

typedef struct DNode{
    DataType data;
    struct DNode *prev;
    struct DNode *next;
}DNode;

//3.1 在p节点前插入元素 4步操作
void insertDList(DNode* p, DataType elem)
{
    DNode *tmp = (DNode*)malloc(sizeof(DNode));
    p->prev->next = tmp;
    tmp->prev = p->prev;
    tmp->next = p;
    p->prev = tmp;
}

//3.2 删除p节点
void deleteDList(DNode* p)
{
    p->prev->next =  p->next;
    p->next->prev = p->prev;
}
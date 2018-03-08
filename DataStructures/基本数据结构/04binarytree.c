#include <stdio.h>

//二叉树
/**
 * 
二叉树性质：

1、满二叉树，深度k，结点数为 2^k - 1
2、二叉树，第i层，最多有 2^(k-1)
3、n个结点的完全二叉树，其深度为 [lgn] + 1 (地板除)

4、完全二叉树(n个结点)：
    (1)、2i <= n, 则i的 左孩子：2i, 否则 i无左孩子
    (2)、2i + 1 <= n，则i的 右孩子：2i+1, 否则 i无右孩子
    (3)、i != 1时，i的双亲结点为 [i/2] （地板除）
    (4)、结点i所在的层次为 [lgn] + 1 (地板除)

5、2个指针域的链表二叉树，n个结点共2n个指针，但除了根结点外，每个结点都有一个指针指向自己(双亲)，
    即用了 n-1 个指针，还空了 2n-(n-1),即n+1个指针。三叉链表的话，空n+2个指针。

*/

//结点
typedef int DataType;
typedef struct BTNode{
    struct BTNode *LChild;
    DataType data;
    struct BTNode *RChild;
}BTNode;

//1、建立二叉树, 根结点 -> 左子树 -> 右子树
BTNode * create()
{
    BTNode *bt = NULL;
    int elem;
    scanf("%d", &elem);
    if(-1 != elem)
    {
        bt = (BTNode*)malloc(sizeof(BTNode));
        if(NULL == bt)
        {
            exit(1);
        }
        bt->data = elem;
        bt->LChild = create();
        bt->RChild = create();
    }
    return bt;
}

//2、遍历二叉树

//2.1 层次遍历 (类似 序遍历)
void layerOrder(BTNode *root)
{
    BTNode *tmp;
    LinkQueue LQ;//建立顺序队列
    initQueue(&LQ);//初始化队列
    if(NULL != root)
    {
        insertQueue(&LQ, *root);
    }
    while( !isEmpty(LQ) )
    {
        tmp = deleteQueue( &LQ ); //取出队头元素
        print("%d ", tmp->data); 
        if(NULL != tmp->LChild)  //若当前结点有左孩子，则入队
        {
            insertQueue(&LQ, tmp->LChild);
        }
        if(NULL != tmp->RChild) //若当前结点有右孩子，则入队
        {
            insertQueue(&LQ, tmp->RChild);
        }
    }
}

//2.2、递归方式

//2.2.1 先序遍历
void preOrder(BTNode *root)
{
    if(NULL != root)
    {
        printf("%d ", root->data);
    }
    preOrder(root->LChild);
    preOrder(root->RChild);
}

//2.2.2 中序遍历
void midOrder(BTNode *root)
{
    if(NULL != root)
    {
        midOrder(root->LChild);
        printf("%d ", root->data);
        midOrder(root->RChild);
    }
}

//2.2.3 后序遍历
void postOrder(BTNode *root)
{
    if(NULL != root)
    {
        postOrder(root->LChild);
        postOrder(root->RChild);
        printf("%d ", root->data);
    }
}

//3、查找树 O(n) O(n)
BTNode* searchBTree(BTNode *root, DataType elem)
{
    BTNode *tmp = NULL;
    if(NULL == root)
    {
        return NULL;
    }
    if(root->data == elem)
    {
        return root->data;
    }
    else
    {
        tmp = searchBTree(root->LChild, elem); //搜左子树
        if(NULL != tmp) 
        {
            return tmp;   //如果搜到，就返回，否则继续搜右子树
        }
        else
        {
            return searchBTree(root->RChild, elem); //不管是否搜到，都返回右子树的结果
        }
    }
}

//4、求二叉树的深度. 总深度 = max(左深度，右深度) + 1. 故可以递归实现. O(n) O(n)
int getDepth(BTNode *root)
{
    int LDep, RDep;
    if(NULL == root)
    {
        return 0;
    }
    else
    {
        LDep = getDepth(root->LChild);
        RDep = getDepth(root->RChild);
        return (LDep > RDep) ? LDep+1 : RDep+1;
    }
}

//5、线索二叉树
//利用二叉树的空指针，将结构信息存储在空指针中，根据这些信息就可以遍历二叉树，不需要递归。

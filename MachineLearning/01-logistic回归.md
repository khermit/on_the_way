#### 一、前言

​        本文从Logistic回归的原理开始讲起，补充了书上省略的数学推导。

#### 二、Logistic回归与梯度上升算法

​        Logistic回归是众多回归算法中的一员。回归算法有很多，比如：线性回归、Logistic回归、多项式回归、逐步回归、令回归、Lasso回归等。我们常用Logistic回归模型做预测。通常，Logistic回归用于二分类问题，例如预测明天是否会下雨。当然它也可以用于多分类问题，不过为了简单起见，本文暂先讨论二分类问题。首先，让我们来了解一下，什么是Logistic回归。

**1、Logistic回归**

​       假设现在有一些数据点，我们利用一条直线对这些点进行拟合(该线称为最佳拟合直线)，这个拟合过程就称作为回归，如下图所示：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMxrkNODnIQx03b9ZnBibed9WCRnXcfDrdmRib3CTYYhlrxJflw5HibcDEg/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        Logistic回归是回归的一种方法，它利用的是Sigmoid函数阈值在[0,1]这个特性。Logistic回归进行分类的主要思想是：根据现有数据对分类边界线建立回归公式，以此进行分类。其实，Logistic本质上是一个基于条件概率的判别模型(Discriminative Model)。

​        所以要想了解Logistic回归，我们必须先看一看Sigmoid函数 ，我们也可以称它为Logistic函数。它的公式如下：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMIql34BYdA3z364UGttibkvm8icw8nfAtLXHay4KjQpNQSD0tN5Rcj0Nw/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrM01Ey07XOAicuSYboDIXT2RsTX8C1ltjsAAaCSS8HXEALbHa9OgmicSSw/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrM9iaick09oJw7AveVbjP1LtXtPuU2rGsx9hiahvTW6WiadY8INmdN4Gsr9g/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        整合成一个公式，就变成了如下公式：

![img](http://mmbiz.qpic.cn/mmbiz_jpg/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMB8E1z9wFsjibEFdcNEEqjCKxEEibeB0WeOoUSacR9W9AdSup4OQibiaz4Q/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1)

​        下面这张图片，为我们展示了Sigmoid函数的样子。

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMqGbwKXm7sMHFfWczCulpfveDe4DnibXT6iafNM1exv3o2CPuhBcnjVibg/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        z是一个矩阵，θ是参数列向量(要求解的)，x是样本列向量(给定的数据集)。θ^T表示θ的转置。g(z)函数实现了任意实数到[0,1]的映射，这样我们的数据集([x0,x1,…,xn])，不管是大于1或者小于0，都可以**映射到[0,1]区间**进行分类。hθ(x)给出了输出为1的概率。比如当hθ(x)=0.7，那么说明有70%的概率输出为1。输出为0的概率是输出为1的补集，也就是30%。

​        如果我们有合适的参数列向量θ([θ0,θ1,…θn]^T)，以及样本列向量x([x0,x1,…,xn])，那么我们对样本x分类就可以通过上述公式计算出一个概率，如果这个概率大于0.5，我们就可以说样本是正样本，否则样本是负样本。

​        

​        **那么问题来了！如何得到合适的参数向量θ?**

​        根据sigmoid函数的特性，我们可以做出如下的假设：

![img](http://mmbiz.qpic.cn/mmbiz_jpg/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMgJLPiaflJpurw4coZgBtDbY6WNuInCLWoHKiaibaYXuOlDLicbEfNGCDpw/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1)

​        上式即为在已知样本x和参数θ的情况下，样本x属性正样本(y=1)和负样本(y=0)的条件概率。理想状态下，根据上述公式，求出各个点的概率均为1，也就是完全分类都正确。但是考虑到实际情况，样本点的概率越接近于1，其分类效果越好。比如一个样本属于正样本的概率为0.51，那么我们就可以说明这个样本属于正样本。另一个样本属于正样本的概率为0.99，那么我们也可以说明这个样本属于正样本。但是显然，第二个样本概率更高，更具说服力。我们可以把上述两个概率公式合二为一：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMtgbhZ332S2F5r3b7ZCZsot6CR1Vqp4f0m6h6P53QP9W1kPedicia2Z9w/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        合并出来的Cost，我们称之为代价函数(Cost Function)。当y等于1时，(1-y)项(第二项)为0；当y等于0时，y项(第一项)为0。为了简化问题，我们对整个表达式求对数，(将指数问题对数化是处理数学问题常见的方法)：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMnCrEDqDtt6ibAlTExNibMg27ThD1dib6stjbuK7ica2bDzglvcZjzJxV7g/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        这个代价函数，是对于一个样本而言的。给定一个样本，我们就可以通过这个代价函数求出，样本所属类别的概率，而这个概率越大越好，所以也就是求解这个代价函数的最大值。既然概率出来了，那么最大似然估计也该出场了。假定样本与样本之间相互独立，那么整个样本集生成的概率即为所有样本生成概率的乘积，再将公式对数化，便可得到如下公式：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrM7DN7l3a08CEvWZ0dIdbyLNIPIEkvmEESsCef3ZdIbibV3VSR5bpf5dw/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        其中，m为样本的总数，y(i)表示第i个样本的类别，x(i)表示第i个样本，需要注意的是θ是多维向量，x(i)也是多维向量。

补充：常见的损失函数形式如下：

1.0-1损失函数 （0-1 loss function） 

​	L(Y,f(X))={1,0,Y ≠ f(X)Y = f(X)

2.平方损失函数（quadratic loss function) 

​	L(Y,f(X))=(Y−f(x))2

3.绝对值损失函数(absolute loss function) 

​	L(Y,f(x))=|Y−f(X)|

4.对数损失函数（logarithmic loss function) 或对数似然损失函数(log-likehood loss function) 

​	L(Y,P(Y|X))=−logP(Y|X)　( 注意有个负号，求最小(最小化损失函数)，与本身求最大是一样的(极大似然估计) )

逻辑回归中，采用的则是对数损失函数。如果损失函数越小，表示模型越好。

​        **综上所述，满足J(θ)的最大的θ值即是我们需要求解的模型。**

​        怎么求解使J(θ)最大的θ值呢？因为是求最大值，所以我们需要使用梯度上升算法。如果面对的问题是求解使J(θ)最小的θ值，那么我们就需要使用梯度下降算法。面对我们这个问题，如果使J(θ) := -J(θ)，那么问题就从求极大值转换成求极小值了，使用的算法就从梯度上升算法变成了梯度下降算法，它们的思想都是相同的，学会其一，就也会了另一个。本文使用梯度上升算法进行求解。

**2、梯度上升算法**

​        说了半天，梯度上升算法又是啥？J(θ)太复杂，我们先看个简单的求极大值的例子。一个看了就会想到高中生活的函数：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMwwgruTCZIahfHQpvCXSJD4ibCFWgA4U8YG0QLqofVdTl6EOx6StJN2g/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        来吧，做高中题。这个函数的极值怎么求？显然这个函数开口向下，存在极大值，它的函数图像为：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMLbSFrk8CiacRDa8h8dKgnEgh4UIx8vX5gibBZicWada6brk8SDKcVspeQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        求极值，先求函数的导数：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMceKicwEJPmFqiaZ0DkDdKkcadR6KKHqIxJKZUJuFnBwCzfT9st0t7AXw/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        令导数为0，可求出x=2即取得函数f(x)的极大值。极大值等于f(2)=4

​        但是真实环境中的函数不会像上面这么简单，就算求出了函数的导数，也很难精确计算出函数的极值。此时我们就可以用迭代的方法来做。就像爬坡一样，一点一点逼近极值。这种寻找最佳拟合参数的方法，就是最优化算法。爬坡这个动作用数学公式表达即为：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMBiazyD7YXCXMnqTNicEfmAxDB7cCVXz7cN9UFurd5NL7Ee3DEZGACw7w/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        其中，α为步长，也就是学习速率，控制更新的幅度。效果如下图所示：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMetNBeXUkzvfQDozsXsUsGia4uuJhO7D8VLiacRwxcUaWibJ0rGoOkdAbQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        比如从(0,0)开始，迭代路径就是1->2->3->4->…->n，直到求出的x为函数极大值的近似值，停止迭代。我们可以编写Python3代码，来实现这一过程

```python
# -*- coding:UTF-8 -*-
""" 
函数说明:梯度上升算法测试函数
求函数f(x) = -x^2 + 4x的极大值
Parameters:    无
Returns:    无
Author:    Jack Cui
Blog:    http://blog.csdn.net/c406495762
Zhihu:    https://www.zhihu.com/people/Jack--Cui/
Modify:    2017-08-28
"""
def Gradient_Ascent_test():
    def f_prime(x_old):           #f(x)的导数
        return -2 * x_old + 4
    x_old = -1         #初始值，给一个小于x_new的值
    x_new = 0        #梯度上升算法初始值，即从(0,0)开始
    alpha = 0.01    #步长，也就是学习速率，控制更新的幅度
    presision = 0.00000001           #精度，也就是更新阈值 
    while abs(x_new - x_old) > presision:
        x_old = x_new
        x_new = x_old + alpha * f_prime(x_old)
                                             #上面提到的公式
        print(x_new)                 #打印最终求解的极值近似值
if __name__ == '__main__':
     Gradient_Ascent_test()
```

​        代码运行结果如下：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMPo20pZ2a6fibQQicDVfOCicsy99hDDGdibdKK6VUwfuRrBZcel1tK2qiaeg/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        结果很显然，已经非常接近我们的真实极值了。这一过程，就是梯度上升算法。那么同理，J(θ)这个函数的极值，也可以这么求解。公式可以这么写：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMxFLOaT0YMUrkwYfV2WznzrLguibdUibZJXHvaiccpIS3ickzIekCs10anQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        由上小节可知J(θ)为：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMcBiaay4C28oFYDs7Cz8emJTrxiab8wczbbbfy9Bql1kYIWDOPa24sstQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        sigmoid函数为：

![img](http://mmbiz.qpic.cn/mmbiz_jpg/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMB8E1z9wFsjibEFdcNEEqjCKxEEibeB0WeOoUSacR9W9AdSup4OQibiaz4Q/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1)

**那么，现在我只要求出J(θ)的偏导，就可以利用梯度上升算法，求解J(θ)的极大值了。**

那么现在开始求解J(θ)对θ的偏导，求解如下(**数学推导**)：

![img](http://mmbiz.qpic.cn/mmbiz_jpg/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMlWxOY4lMGSFeDJQN8M1kEX141rlfg6RAOe7u3iay8CbYUQjeVN1y0Jw/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1)

![img](http://mmbiz.qpic.cn/mmbiz_jpg/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrM6VfibYIutqYbGSOs7zQGnKKXO1LfLHkCxbGiaMweSlETzt7NMupehT7g/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1)

​        知道了，梯度上升迭代公式，我们就可以自己编写代码，计算最佳拟合参数了。

#### 三、Python3实战

**1、数据准备**

​        数据集已经为大家准备好，下载地址：

https://github.com/Jack-Cherish/Machine-Learning/blob/master/Logistic/testSet.txt

​        这就是一个简单的数据集，没什么实际意义。让我们先从这个简单的数据集开始学习。先看下数据集有哪些数据：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMZFUwQv3VBB6nHXCiaNhsDqia36zLtqRSAictyHymjbPaRMUeLkj7zweyg/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​         这个数据有两维特征，因此可以将数据在一个二维平面上展示出来。我们可以将第一列数据(X1)看作x轴上的值，第二列数据(X2)看作y轴上的值。而最后一列数据即为分类标签。根据标签的不同，对这些点进行分类。

​        那么，先让我们编写代码，看下数据集的分布情况：

```python
# -*- coding:UTF-8 -*-
import matplotlib.pyplot as plt
import numpy as np
"""
函数说明:加载数据
Parameters:    无
Returns:    dataMat - 数据列表
                  labelMat - 标签列表
Author:    Jack Cui 
Blog:    http://blog.csdn.net/c406495762
Zhihu:    https://www.zhihu.com/people/Jack--Cui/ 
Modify:    2017-08-28
"""
def loadDataSet():
    dataMat = []                                 #创建数据列表
    labelMat = []                                 #创建标签列表
    fr = open('testSet.txt')                  #打开文件
    for line in fr.readlines():               #逐行读取
    lineArr = line.strip().split()            #去回车，放入列表
    dataMat.append([1.0, float(lineArr[0]),
                                 float(lineArr[1])])       #添加数据
    labelMat.append(int(lineArr[2]))            #添加标签
    fr.close()                                                #关闭文件
    return dataMat, labelMat         
"""
函数说明:绘制数据集
Parameters:    无
Returns:    无
Author:    Jack Cui
Blog:    http://blog.csdn.net/c406495762
Zhihu:    https://www.zhihu.com/people/Jack--Cui/
Modify:    2017-08-28
"""
def plotDataSet():
    dataMat, labelMat = loadDataSet()       #加载数据集
    dataArr = np.array(dataMat)                                                                       #转换成numpy的array数组
    n = np.shape(dataMat)[0]       #数据个数
    xcord1 = []
    ycord1 = []                     #正样本
    xcord2 = []
    ycord2 = []                    #负样本
    for i in range(n):            #根据数据集标签进行分类
        if int(labelMat[i]) == 1:            
            xcord1.append(dataArr[i,1])
            ycord1.append(dataArr[i,2])    #1为正样本
        else:
            xcord2.append(dataArr[i,1])
            ycord2.append(dataArr[i,2])    #0为负样本
    fig = plt.figure()
    ax = fig.add_subplot(111)                #添加subplot
    ax.scatter(xcord1, ycord1, s = 20, c = 'red', 
                        marker = 's',alpha=.5)     #绘制正样本
    ax.scatter(xcord2, ycord2, s = 20,
                     c = 'green',alpha=.5)          #绘制负样本
    plt.title('DataSet')                                #绘制title
    plt.xlabel('x'); plt.ylabel('y')                  #绘制label
    plt.show()                                            #显示
if __name__ == '__main__': 
    plotDataSet()
```



​        运行结果如下：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMHCpW7zGIZRYT7pC6HL1S1oUbMYtDeqVOAYh1mQe5fPKW2twOOLR6cw/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        从上图可以看出数据的分布情况。假设Sigmoid函数的输入记为z，那么z=w0x0 + w1x1 + w2x2，即可将数据分割开。其中，x0为全是1的向量，x1为数据集的第一列数据，x2为数据集的第二列数据。另z=0，则0=w0 + w1x1 + w2x2。横坐标为x1，纵坐标为x2。这个方程未知的参数为w0，w1，w2，也就是我们需要求的回归系数(最优参数)。

**2、训练算法**

​        在编写代码之前，让我们回顾下梯度上升迭代公式：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMQgSib8lOPWjY4HUaacJib0xiafWoJEYeKibCia2cFg0N2QlzwatIjianq7Ew/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        将上述公式矢量化：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMNziaiceNG72ED95iaT4qNy7mWqNwHUEb9ZgB13PFXY1Zakice9HxicPLRnQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        根据矢量化的公式，编写代码如下：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrM4uDY73qibBEZn87v22B4tuIWhcyeAMm43rhkyr1lL5eSQDPRgKEUJgA/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        运行结果如图所示：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMkqUCNlKa6bxjWT4T6IQxAftGFZKqvAhYA5ofTibBwaqMibNRdyib4h4Nw/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​         可以看出，我们已经求解出回归系数[w0,w1,w2]。

​         通过求解出的参数，我们就可以确定不同类别数据之间的分隔线，画出决策边界。

**3、绘制决策边界**

​        我们已经解出了一组回归系数，它确定了不同类别数据之间的分隔线。现在开始绘制这个分隔线，编写代码如下：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMxg6aFriadbNTemJSckFZplWjOUSLQkch6K1j1wl8adRcAVQDSibejgJQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMOevj5cupjSOZDw84coOz5piaIgIlmLrT2mLRAbjY3TcG6pEjWzdETibA/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMGZXJruG4S5NKDmQTz5icIt6OE6TnVMovzWtaOtIjebUNabR4EC9UaaA/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMxn8n5Ybl5icLmric2xA2IZRLGlf8HsPkLnt7yduibiaIZ3lQKMKCK2nwGA/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMWJeAQwY8iagmAaiaqmyYWKFLLmDRCYvXRPahngklrQAyF6YGrpyb7zjA/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​        运行结果如下：

![img](http://mmbiz.qpic.cn/mmbiz_png/iaTa8ut6HiawCvFkTkL5zYsBgSxkqlQFrMpBI1tUE4qApgh5a2YpBmV6gvIT3EWcxYah6eosjgTTGhABtm4ic7iatA/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

​       这个分类结果相当不错，从上图可以看出，只分错了几个点而已。但是，尽管例子简单切数据集很小，但是这个方法却需要大量的计算(300次乘法)。因此下篇文章将对改算法稍作改进，从而减少计算量，使其可以应用于大数据集上。

#### 四、总结

**Logistic回归的一般过程：**

​    收集数据：采用任意方法收集数据。

​    准备数据：由于需要进行距离计算，因此要求数据类型为数值型。另外，结构化数据格式则最佳。

​    分析数据：采用任意方法对数据进行分析。

​    训练算法：大部分时间将用于训练，训练的目的是为了找到最佳的分类回归系数。

​    测试算法：一旦训练步骤完成，分类将会很快。

​    使用算法：首先，我们需要输入一些数据，并将其转换成对应的结构化数值；接着，基于训练好的回归系数，就可以对这些数值进行简单的回归计算，判定它们属于哪个类别；在这之后，我们就可以在输出的类别上做一些其他分析工作。

**其他：**

​    Logistic回归的目的是寻找一个非线性函数Sigmoid的最佳拟合参数，求解过程可以由最优化算法完成。

**参考文献：**

1. 斯坦福大学的吴恩达《机器学习》：https://www.coursera.org/learn/machine-learning
2. 《机器学习实战》第五章内容

声明：本文参考自https://mp.weixin.qq.com/s/Abn_WM4X7DsnR2ev2YjpLQ，并做了部分修改(侵必删)。
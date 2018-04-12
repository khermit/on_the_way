#### List

快速创建指定长度的list: a = [0]*1000 比 a = list(range(1000))快近3倍（？反正要快）。

创建指定长度list后，在根据索引追加值要比append块。
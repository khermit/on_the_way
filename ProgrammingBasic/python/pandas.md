矩阵合并：

如果多次合并，使用连接的方式，每次都会创建新的矩阵，所以性能会下降。

解决方案：一次性创建好新的矩阵，然后直接通过复制的到，性能会得到很大的提升。

## 设置索引

`df_new = df.set_index('new_index_col')`



## 筛选

```python
df[(df['PCTL']<0.95) & (df['PCTL']>0.05)]
```



## 查找

1. []
   1. data[1:5]　多行选择　选择1-4
   2. data[['column1', 'column2']]　多列选择
   3. data[:7][['column1', 'column2'\]\] 区块选择
2. loc：按照索引选取。建议使用loc，而不是[].[原因](http://pandas.pydata.org/pandas-docs/stable/indexing.html#indexing-view-versus-copy)
   1. data.locp[1:5] 多行选择，选择1-5
   2. data.loc[2:4, ['column1', 'column2']] 区块选择
   3. data.loc[date1:date2] date=dt.datetime(2013,1,1)　选择连个特定日期之间的数据。
3. iloc: 按照位置选择
   1. data.iloc[1:5]
   2. data.iloc[:,[1,2]]
   3. data.iloc[[1,12,22],[5,6]]
4. at:与loc类似，速度更快，但只能访问单个元素。
   1. data.at['index2', 'column3']
5. iat:与iloc类似，速度更快，但只能访问单个元素。
   1. data.iat[1][2\]
6. ix:允许不在dataframe索引中的数据。既可以通过位置，也可以通过标签。效率较低。
   1. data.ix[date1:date2]　查找这之间的数据，但date1和date2可以不在dataframe中。


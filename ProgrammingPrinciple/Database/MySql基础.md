# MySql基础

## 1.基础知识

主键：不更新主键列中的值，不在主键中使用可能会更改的值。

默认端口：3306



#### select

select语句：

```sql
select id, name from table;
select distinct id，name from table;只返回不同的值，该关键字应用于所有列，而不仅是前置它的列。
select * from table limit 3，5;返回从行3开始的行数不多于5的行。第一行为0。在order by之后
select products.name from data.products;使用完全限定的列名
select * from table order by name;也可以用非检索的列排序数据。在where之后
select * from table order by name DESC, id DESC;DESC只直接应用到前面的列，都需要排序的化，每一列都需要指定，先排的列在前面，前面的列值相同时，才按照后面的列排序。
```

#### where

where支持的操作复活： =等于、!=不等于、<>不等于、< <= > >= between and(闭区间)、or、and（比or 优先处理，可以使用圆括号）。

where name = 'fuses' 默认不区分大小写,通过单引号来限定与字符串类型比较，数值类型不需要单引号。

where name is null;检查是否为null，null与0、空字符串、空格不同。

在过滤出不具有特定值的行时，不会返回null值的行。

where id in (12,13);多值匹配，与or类型，但清单执行更快，可以包含其它select语句，使得能够更动态地建立where子句。

not否定之后的条件。mysql支持使用not对in，between，exists子句取反。

#### 通配符

通过LIKE实现通配符操作，注意是否区分大小写。

where name like 'jet%'; %代表任何字符出现任意次，不能匹配null.

注意：‘%jet’不会匹配jet后面的尾空格，要么使用函数将尾空格去掉，要么使用'%jet%'匹配。

where name like '_aaa'; _只能匹配一个字符

注意：通配符搜索的处理一般要花更长的时间。所以：

- 不能过度使用通配符，如果其它操作能代替，则用其它操作代替。
- 如果通配符位于搜索模式的开始处，搜索起来是最慢的。



#### 正则表达式

通过正则表达式进行文本匹配，mysql只支持一小部分正则表达式。

where name regexp '.oin'

like与regexp的重要差别：

- where name like '1000';like匹配整个行，如果‘1000’在列值中出现，则like不会匹配和返回，除非使用通配符
- where name regexp '1000';regexp在列值内进行匹配，如果‘1000’在列值中出现，则regexp会匹配和返回。regexp可以通过^和$来只匹配整个行（与like一样）。

where name regexp binary 'jack';通过binary来区分大小写，默认不区分大小写。

‘1000|2000’、'[123]0'、'^[123]'、‘[1-5]’、'[a-z]'

特殊字符通过两个\来转义,因为mysql自己解释一个，正则表达式解释一个。'\ \ .' 特殊字符包括.|[])( 

\ \ f 换页， \ \ n换行，\ \ r回车，\ \t制表符，\ \v纵向制表，\ \ \匹配‘\’

+{1,}、?{0,1}、

^文本开始，$文本结尾，[[:<:]]词的开始，[[:>:]]词的结尾



#### 计算字段

计算字段是运行时在select语句内创建。在数据库上的计算字段要比在应用程序上转化快的多。

select Concat(Rtrim(vend_name),' (',Ltrim(id),' )') as new_title from table;将两列拼接为新的一列，

select id, price, quantity, quantity*price as new_price from table;支持+-×/,可用圆括号区分优先级。



#### 数据处理函数

##### 文本处理函数

Left(), Length(), Locate(), Lower(), LTrim(), Right(), RTrim(), Soundex(), SubString(), Upper().

select name from table where Soundex(name) = Soundex('Y lie');找出发音类似'Y lie'的结果。

##### 日期和时间处理函数

AddDate(), AddTime(), CurDate(), CurTime(), Date(), DateDiff(), Date_Add(), Date_Format(), Day(), DayOfWeek(), Hour(), Minute(), Month(), Now(), Second(), Time(), Year().

日期格式为：xxxx-09-11(首选)，使用完整的日期，mysql就不必做出任何假设

##### 数值处理函数

Abs(), Cos(), Exp(), Mod(), Pi(), Rand(), Sin(), Sqrt(), Tan()



#### 汇总数据

##### 聚集函数：运行在行组上，计算和返回单个值的函数。

AVG(), COUNT(), MAX(), MIN(), SUM()

select AVG(price) as avg_price from table;忽略NULL。

COUNT(*)：统计行，包括所有值（null）

COUNT(column):对指定列中有值的行进行计数，忽略null。

MAX()：可以是文本行排列的最后一行，忽略NULL。MIN()类似

SUM():忽略null

DISTINCT参数，只能在列名前指定该参数，只考虑不同的值。AVG(distinct price) as avgprice;该参数不能用于count(*)。



#### 分组数据

group by:

- select 分组依据的列，聚集函数 from table group by 分组依据的列(与前面绝对一致，不能有别名)
- 如果分组列中有null,则单独将null分为一组
- where ...  group by ... order by..
- rollup?

where过滤行，但having过滤分组。having支持所有where的操作。即先用where过滤行，再进行分组，再用having过滤分组。

要对分组进行排序，则必须使用order by，而不是依赖group by的分组结果顺序。





常见命令：

```sql
show columns from customer; 等同于 describe customer; 
show status;显示服务器状态
show create database/table; 显示创建语句
show grants;显示授权用户的权限
show errors/warnings;显示错误或警告
```


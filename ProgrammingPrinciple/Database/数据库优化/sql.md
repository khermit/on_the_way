sql

```sql
limit 5
select * from xx order by price desc, name desc limit 5;
//where ... order by ... limit
where id <> 100; id != 100;
where xx is nll; 
and 优先级比 or高，最好使用()
where xx not in (100,200)
%：任何字符出现任意次数,不匹配null
where xx like '%dsf%';
_:只匹配单个字符
where xx regexp '100|200';匹配正则表达式
特殊字符前加\\，mysql解释一个，正则表达式解释一个
select concat(Rtrim(name),' (', RTrim(country), ')') as new_column；拼接，别名
select price*id as new_column...; 支持+-*/

```

文本处理函数：

```q
Left(), Length(), Locate()找子串, Lower(), LTrim(), Right(), RTrim(), Soundex()匹配发音, SubString(), Upper()
```

日期处理函数：

```sql
AddDate, AddTime, CurDate, curtime, date, datediff, date_add, date_format, day, dayOfweek, hour, minute, month, now, second, time, year
```

数值处理函数：

```sql
abs, cos, exp, mod, pi, rand, sin, sqrt, tan
```

聚集函数：

```sql
avg, count, max, min, sum
select vag(distinct price) as new_price...;
```



命令：

```sql
show status;
show create database xx;
show create create table;
show grants
show errors/warnings;
```


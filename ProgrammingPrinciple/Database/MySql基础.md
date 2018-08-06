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



#### 使用子查询

##### 利用子查询进行过滤

SELECT id FROM tables WHERE name IN ( SELECT order_name FROM orders WHERE prod_id = 'INT2' ); 处理时，先处理括号内的子查询，将查询结果以逗号分隔符的形式给IN。应当保证子select语句与where语句有相同数目的列。子查询还可与=,<>不等于结合。

##### 作为计算字段使用子查询

SELECT cust_name, cust_state,(SELECT COUNT(*) FROM orders WHERE orders.cust_id=customers.cust_id) AS orders   FROM customers;

该子查询对检索出的每个客户执行一次，共检索出了5个客户，该子查询共执行5次。

##### 相关子查询 涉及外部查询的子查询



#### 联结

SELECT vend_name,prod_price FROM vendors, products WHERE vendors.id=products.id;默认为笛卡尔积，表一的每一行都将与表二的每一行进行匹配。

##### 内部联结（等值联结，基于两个表之间的相等测试）

SELECT vend_name, prod_name, prod_price FROM vendors INNER JOIN products ON vendors.vend_id = products.prod_id;



#### 高级联结

SELECT name,price FROM customer AS c, orders AS o WHERE c.id = o.id;表别名只在查询中使用，可用于select，order by, where子句中，不返回到客户机（与列别名不同）。

##### 自联结

SELECT p1.prod_id,p1.prod_name FROM product AS p1, product AS p2 WHERE p1.vend_id = p2.vend_id AND p2.prod_id='DINTR';多次用到用一个表，可以通过表别名使用自联结，防止同一个表名出现两次产生的二义性。**通常情况，处理联结要比处理子查询快**。

##### 自然联结

通过对表使用通配符(select *),对所有其它表的列使用明确的子集来完成。排除多次出现。

SELECT c.*,o.num FROM customers AS c, ordersn AS o WHERE c.id = o.id;

##### 外部联结 联结包含了那些在相关表中没有关联行的行

SELECT customer.id, orders.num FROM customers LEFT OUTER JOIN orders ON customer.id=orders.id 

#### 组合查询

select .... UNION select ... 将过个查询结果合并成一个结果集。这里面的每个查询必须包含相同的列、表达式或聚集函数，而且列的数据类型必须兼容。UNION自动去除重复的行，如果要返回所有匹配的行，则使用UNION ALL。结果集排序只能在最后一条select语句中使用order by。



#### 全文本搜索（MyISAM支持） 

必须索引被搜索的列，并且随着数据的改变不断地重新索引。通过在创建表时FULLTEXT(note_text)来说明该列需要全文本索引。在该列增加、更新、删除行时，mysql会自动更新索引。在导入数据时，不应该启动fulltext，在导入完毕之后，在修改表，定义fulltext。因为如果启用，则在导入每行时需要分别进行索引。

SELECT note_text FROM productnotes WHERE Match(note_text) Against('rabbit');match指定被索引的列，against指定要使用的所有方式。match()中的值必须与fulltext定义的值相同。如果指定多个列，则必须按照次序列出。除非使用binary，否则不区分大小写。对于结果来说，全文本搜索将具有较高等级的行先返回。

SELECT note_text Match(note_text) Against('rabbit') AS rank FROM table;选出note_text列和匹配等级两列数据。

##### 扩展查询

扩展查询：用来放宽所返回的全文本搜索结果的范围。

SELECT note_text FROM table WHERE Match(note_text) Against('Tom' WITH QUERY EXPANSION);该查询对数据和索引进行两边扫描：

- 第一次全文本搜索，找出匹配的行；
- 根据匹配的行选出所有有用的词
- 第二次全文本搜索，找出匹配有用词的行，并返回。（行中的文本越多，查询扩展的返回结果越好）

##### 布尔文本搜索

如果行中即有要排斥的词，也有要匹配的词，则不返回。即使没有fulltext索引也可以使用。但很慢。

SELECT note_text FROM table WHERE Match(note_text) Against('heavy -rope*' IN BOOLEAN MODE);匹配heavy,排除rope开始的词的行。

支持的布尔操作：+包含，-排除，>包含并增加等级值，<包含并减小等级值，()把词组变为子表达式，~取消一个词的排序值，*词尾的通配符，""定义一个短语并称为整体。没有指定操作符，则至少匹配一个。

全文本搜索注意事项：

- 3个即以下字符的短词被忽略且从索引中排除
- mysql中內建的听此表中的词总是被忽略，可以自定义
- 如果一个词出现50%以上，则视为停词。该规则不适用于IN BOOLEAN MODE
- 如果表中的行数少于3行，则不返回结果。因为没个词或者不出现，或者至少出现在50%的行中。
- 忽略单引号，don't => dont
- 不具有词分隔符的语言，汉语和日语，不能恰当地返回全文本搜索结果。



#### 插入数据

INSERT INTO table VALUES(null, '12', 3, null);插入完整的行。

INSERT INTO table(id, name) VALUES(1, 'Duke');

INSERT LOW_PRIORITY INTO 降低insert操作的优先级，适用于update，delete.

INSERT INTO table(id) VALUES(1);INSERT INTO table(id) VALUES(2);

INSERT INTO table(id) VALUES(1),(2);一次性插入多行。

INSERT INTO table(id) SELECT c_id FROM C_table;将查询结果插入，支持空结果，主要按照对应次序插入。



#### 更新和删除数据

UPDATE table SET name='Duke' WHERE id = 1;

UPDATE table SET name='Duke', age=13 WHERE id = 1;可以使用子查询

更新多行时，若发生错误，曾错误之前所有的值恢复为原来的值。即使错误，也要继续更新，则使用update IGNORE table...

DELETE FROM table WHERE id=1;删除行。

更快地删除所有行：TRUNCATE table。删除原表并重新建表。



每个表只有一个AUTO_INCREMENT列，且必须被索引。

外键不能跨引擎。

ALTER TABLE vendors ADD vend_phone CHAR(20);给表增加列

ALTER TABLE vendors DROP COLUMN vend_phone;删除列。

ALTER TABLE orders ADD CONSTRAINT fk_orders_customers FOREIGN KEY(cust_id) REFERENCES customers (cust_id) 



#### 视图

视图不包含数据，相当于封装了sql语句。每次使用视图，都必须处理查询执行时所需的任一个检索。

规则：

- 如果从视图中检索数据select中含有order by,则该视图中的order by将被覆盖。
- 视图不能索引，也不能有关联的触发器或默认值。
- 视图可以和表一起使用。

```sql
create view 创建视图
show create view viewname 查看创建语句
drop view viewname
create or replace view 更新视图，不存在则创建。
```

CREATE VIEW productView AS SELECT cust_name FROM customer WHERE id=1;创建视图

视图主要用于检索数据，一般不对其进行更新，因为有些视图的操作不支持更新。



#### 存储过程

为以后的使用而保存的一条或者多条sql语句的集合。使用存储过程要比单独的sql语句更快。（简单、安全、高性能）

```sql
DELIMITER // 更改分隔符为//
...
END //
```

```sql
创建存储过程
CREATE PROCEDURE productpricing(
	IN innumber INT,
  OUT ototal DECIMAL(8,2)
)
BEGIN
	SELECT Avg(price) AS avg_price FROM products WHERE id=innumber INTO ototal;
END
DROP PROCEDURE productpricing;删除存储过程
```

CALL productpricing(200, @total) 调用存储过程

SELECT @total 得到结果

show procedure status

show procedure status like 'producpricing'                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              

show create procedure producpricing

在存储过程中，可以声明局部变量，使用IF x THEN ... ELSEIF THEN ... ELSE ...



#### 触发器

触发器可以应用于delete， insert， update.只有表支持，视图和临时表都不支持。不支持call，即不能在触发器内调用存储语句。前面的语句失败，后面的语句也不执行。

CREATE TRIGGER newproduct AFTER INSERT ON products FOR EACH ROW SELECT 'product added';每插入一行后，显示一次'product added'.

DROP TRIGGER newproduct;触发器不能修改和更新。只能删除并重建。

INSERT触发器：

- 在insert触发器代码内，可引用一个名为NEW的虚拟表，访问被插入的行。
- before insert中，NEW的值页可以被更新
- 对于AUTO_INCREMENT列，NEW在INSERT执行之前包含0，在INSERT执行之后包含新的自动生成的值。

CREATE TRIGGER neworder AFTER INSERT ON orders FOR EACH ROW SELECT NEW.order_num;

DELETE触发器：

- 可引用一个OLD的虚拟表，访问被删除的行
- OLD中的值只读。

```sql
CREATE TRIGGER deleteorder BEFORE DELETE ON orders FOR EACH ROW
BEGIN
	INSERT INTO archive_orders values(OLD.order_num, OLD.cust_id)
END;
```

UPDATE触发器：

- 引用OLD访问以前的值(只读)，引用NEW访问更新后的值
- 在BEFORE UPDATE中，，NEW中的值可能也被更新，允许更改将要用于update的值

CREATE TRIGGER updatevendor BEFORE UPDATE ON vendors FOR EACH ROW SET NEW.vend_state=Upper(NEW.vend_state); 将要更新的数据转为大写。



#### 管理事务处理

事务处理用来管理insert，update，delete语句。

```sql
SET autocommit=0; 设置不自动提交
start transaction;
savepoint delete1; 保留点在事务处理完成（执行一条rollback或者commit后自动释放，也可以用release savepoint来明确释放）
rollback to delete1;
commit;
```



#### 字符集和校对顺序

show character set;查看字符集

show collation；查看所支持校对的完整列表。_cs区分大小写，—ci不区分大小写

create table ... DEFAULT CHARACTER SET hebrew COLLATE hebrew_general_ci;指定字符集和校对。也可以对单独的列指定字符集和校对，校对将影响order by的顺序。

select ... order by lastname COLLATE latin1_general_cs;在不区分大小写的字符集上临时区分大小写排序。串可以在字符集之间转换（Cast(),Convert()函数）。



#### 管理用户

use mysql; select user from user;查看账户，账户信息存储在user表中

create user ylab identified by 'passwd';创建新账户和密码

SET PASSWORD FOR ylab = Password('mima');

SET PASSWORD = Password('mima');更新当前用户密码

rename user ylab to yylab;

drop user ylan;

show grants for yalb; 查看用户权限。

GRANT SELECT ON product.* TO ylab;授予ylan在product上所有的表只读select权限

REVOKE SELECT ON product.* TO ylab;撤销，如果权限不存在，则出错。但表可以不存在。

GRANT和REVOKE用用于：

- 整个服务器：GRANT ALL
- 整个数据库：ON database.*
- 特定的表：ON database.table
- 特定的列
- 特定的存储过程



#### 备份数据



​                                                                                                                                                                                                                                                                                            























常见命令：

```sql
show columns from customer; 等同于 describe customer; 
show status;显示服务器状态
show create database/table; 显示创建语句
show grants;显示授权用户的权限
show errors/warnings;显示错误或警告
rename table name1 to name2
```


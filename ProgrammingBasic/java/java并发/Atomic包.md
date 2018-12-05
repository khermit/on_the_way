## Atomic包

  在Atomic包里一共提供了13个类，属于4种类型的原子更新方式，分别是原子更新基本类型、原子更新数组、原子更新引用和原子更新属性（字段）。

  Atomic包里的类基本都是使用Unsafe实现的包装类。

基本类型：

- AtomicBoolean：原子更新布尔类型，内部实现转为整形(0,1)
- AtomicInteger：原子更新整型
- AtomicLong：原子更新长整型
  - int addAndGet（int delta）：以原子方式将输入的数值与实例中的（AtomicInteger里的value）相加，并返回结果
  - boolean compareAndSet（int expect，int update）：如果输入的数值等于预期值，则以原子方式将该值设置为输入的值。
  - int getAndIncrement()：以原子方式将当前值加1，注意，这里返回的是自增前的值
  - void lazySet（int newValue）：最终会设置成newValue，使用lazySet设置值后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。
  - int getAndSet（int newValue）：以原子方式设置为newValue的值，并返回旧值

数组类型：数组value通过构造方法传递进去，然后AtomicIntegerArray会将当前数组复制一份，所以当AtomicIntegerArray对内部的数组元素进行修改时，不会影响传入的数组。

- AtomicIntegerArray：原子更新整型数组里的元素。
- AtomicLongArray：原子更新长整型数组里的元素。
- AtomicReferenceArray：原子更新引用类型数组里的元素。
- AtomicIntegerArray类主要是提供原子的方式更新数组里的整型，其常用方法如下
  - int addAndGet（int i，int delta）：以原子方式将输入值与数组中索引i的元素相加
  - boolean compareAndSet（int i，int expect，int update）：如果当前值等于预期值，则以原子方式将数组位置i的元素设置成update值

原子更新引用类型：首先构建一个user对象，然后把user对象设置进AtomicReferenc中，最后调用compareAndSet方法进行原子更新操作：

- AtomicReference：原子更新引用类型。
- AtomicReferenceFieldUpdater：原子更新引用类型里的字
- AtomicMarkableReference：原子更新带有标记位的引用类型。可以原子更新一个布尔类型的标记位和引用类型。构造方法是AtomicMarkableReference（V initialRef，boolean initialMark）。

原子更新字段类：

- AtomicIntegerFieldUpdater：原子更新整型的字段的更新器
- AtomicLongFieldUpdater：原子更新长整型字段的更新器。
- AtomicStampedReference：原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于原子的更新数据和数据的版本号，可以解决使用CAS进行原子更新时可能出现的ABA问题。

要想原子地更新字段类需要两步。

第一步，因为原子更新字段类都是抽象类，每次使用的时候必须使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。

第二步，更新类的字段（属性）必须使用public volatile修饰符。

```java
// 创建原子更新器，并设置需要更新的对象类和对象的属性
private static AtomicIntegerFieldUpdater<User> a = AtomicIntegerFieldUpdater.
newUpdater(User.class， "old");
public static void main(String[] args) {
    // 设置柯南的年龄是10岁
    User conan = new User("conan"， 10);
    // 柯南长了一岁，但是仍然会输出旧的年龄
    System.out.println(a.getAndIncrement(conan));
    // 输出柯南现在的年龄
    System.out.println(a.get(conan));
}
```


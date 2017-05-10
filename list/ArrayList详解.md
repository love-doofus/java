#### ArrayList详解

我们知道ArrayList底层是就数组实现的，具有数组的一些特性，比如说按照下标访问很方便，但是如果执行插入或者删除操作的话，性能不好，因为执行操作的时候需要看是否需要扩容，如果需要扩容的话，就会生成一个新的数组来存储数组，而且调用Arrays.copy()将原来的数组复制到新的数组中，并且原来数组的引用指向新的数组。这样是很消耗性能的。而且删除操作的时候，是需要将删除位置之后的元素进行移位操作，所以性能也不太好，如果是插入或者删除操作比较频繁的时候，不建议使用ArrayList。具体来看看我自己的一个简单实现：

~~~java
package com.wang.day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 主要实现以下增删改查
 * @author wxe
 * @since 1.0.0
 */
public class ArrayDemo<E> {
	//储存数据数组
	private Object[] elementData;
	
	private static final int DEFAULT_CAPACITY = 10;
	
	private static final Object[] EMPTY_ELEMENTDATA = {};
	
	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
	
	 private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;//数组最大长度
	 
	//数据容量
	private int size;
	/**
	 * 指定构造多大容量的大小的数组
	 * @param initialCapacity
	 */
	public ArrayDemo(int initialCapacity){
		if (initialCapacity > 0) {
			elementData = new Object[initialCapacity];
		} else if (initialCapacity == 0) {
			elementData = EMPTY_ELEMENTDATA;//如果初始化容量为0，则为一个空数组
		} else {
			throw new IllegalArgumentException();
		}
	}
	/**
	 * 默认构造器
	 */
	public ArrayDemo(){
		elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;//默认构造器为空数组
	}
	
	//---------------------------------------------------------------添加元素----------------------------
	/**
	 * 添加元素
	 * @param e
	 * @return
	 */
	public E add(E e){
		//1.先要确保我的容量是否够,size+1
		ensureCapacityInternal(size + 1);
		//2.添加元素
		elementData[size ++] = e;
		return e;
	}
	
	public void ensureCapacityInternal(int minCapacity){
		if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
			//如果是无参构造
			minCapacity =  Math.max(DEFAULT_CAPACITY, minCapacity);//默认数组大小为10
		}
		//判断是否需要扩容
		ensureExplicitCapacity(minCapacity);
	}
	/**
	 * 判断是否需要扩容
	 * @param minCapacity
	 */
	 private void ensureExplicitCapacity(int minCapacity){
		 //超过数据长度，需要扩容
		 if (minCapacity - elementData.length > 0) {
			grow(minCapacity);
		}
	 }
	 /**
	  * 进行扩容，原来的1.5倍
	  * @param minCapacity
	  */
	 private void grow(int minCapacity){
		 int oldCapacity = elementData.length;//原来的容量就是数组的长度
		 int newCapacity = oldCapacity + (oldCapacity >> 1);//新的容量就是原来容量的扩容1.5倍
		 if (newCapacity - minCapacity < 0) {
			//如果扩容之后的容量还是比实际数组装在元素之后的容量小，那么新的容量就增加到minCapacity
			 newCapacity = minCapacity;
		} else if (minCapacity - MAX_ARRAY_SIZE > 0) {
			//如果minCapacity比数组能够容纳的最大值都大，那就需要
			newCapacity = hugeCapacity(minCapacity);
		}
		 
		 elementData = Arrays.copyOf(elementData, newCapacity);//复制原来的数组到一个新的数组，并将elementData引用指向新的数组
	 }
	 
	 private static int hugeCapacity(int minCapacity) {
		 if (minCapacity < 0) // overflow
	            throw new OutOfMemoryError();
	        return (minCapacity > MAX_ARRAY_SIZE) ?
	            Integer.MAX_VALUE :
	            MAX_ARRAY_SIZE;
	 }
	 
	//---------------------------------------------------------------获取元素----------------------------
	 /**
	  * 根据数组下标获取元素
	  * @param index
	  * @return
	  */
	public E get(int index){
		 rangeCheck(index);
		 return getElementData(index);
	 }
	 /**
	  * 检查数组下标是否合法
	  * @param index
	  */
	 public void rangeCheck(int index){
		 if (index > size) {
			throw new IndexOutOfBoundsException();
		}
	 }
	 
	//---------------------------------------------------------------修改元素----------------------------
	 public E set(int index,E e){
		 //1.先检查index是否合法
		 rangeCheck(index);
		 E oldElement = getElementData(index);
		 elementData[index] = e;
		 return oldElement;//返回原来的元素
	 }
	 
	 @SuppressWarnings("unchecked")
	public E getElementData(int index){
		 return (E) elementData[index];
	 }
	//---------------------------------------------------------------删除元素----------------------------
	/**
	 * 删除指定位置的元素
	 * @param index
	 * @return
	 */
	 public E remove(int index){
		 rangeCheck(index);
		 E oldElement = getElementData(index);
		 //1.index之后的元素需要向前移动一位
		 int movedLength = size - index -1;//需要移动数组的长度
		 if (movedLength >0) {
			 System.arraycopy(elementData, index+1, elementData, index, movedLength);
		}
		//最后一个元素置，以便垃圾回收器回收
		 elementData[size --] = null;
		 return oldElement;
	}
	/**
	 * 删除指定元素 
	 * @param e
	 * @return
	 */
	public boolean remove(Object o){
		//1.先要找到这个元素的位置,null
		if (o == null ) {
			for (int index = 0; index < size; index ++) {
				if (elementData[index] == o) {
					//查到确定位置的i，但是不立即停止循环，因为list可以装在重复数据，所以需要继续执行将数组中所有元素都移除
					fastRemove(index);
					return true;
				}
			}
		} else {
			for (int index = 0; index < size; index ++) {
				if (o.equals(elementData[index])) {
					fastRemove(index);
					return true;
				}
			}
		}
		
		return false;
	}
	/**
	 * 快速删除，主要体现在不需要验证index的合法性
	 * @param index
	 */
	private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }
	/**
	 * 清除所有元素
	 */
	public void clear(){
		for (int index = 0; index < size; index ++) {
			elementData[index] = null;
		}
		
		size = 0;
	}
	
	
	public static void main(String[] args){
		
		List<Integer> list = new ArrayList<Integer>();
		Thread thread1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				list.add(1);
				System.out.println(1);
				list.add(2);
				System.out.println(2);
				list.add(3);
				list.remove(3);
				System.out.println(3);
			}
		});
		
		Thread thread2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (Integer integer : list) {
					System.out.println(integer);
				}
				
			}
		});
		
		thread1.start();
		thread2.start();
		
	}
}
~~~

我只写了简单的增删改查操作，根据我的add()方法，我们知道**list是允许加入重复数据，而且允许null值的,能够保持顺序，根据下标访问比较方便。同时它不是线程安全的，所以在速度上性能要好点**。

里面有一个main()方法，这个是用来测试ArrayList提供的一个fail-fast机制的。所谓的fail-fast机制就是如果多线程情况下，一个线程迭代list，另一个线程修改list，会抛出异常ConcurrentModificationException。这一策略在源码中实现是通过checkForComodification()方法来实现的。具体代码如下：

~~~
final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
~~~

main()方法执行后的结果：

~~~java
1
2
Exception in thread "Thread-0" Exception in thread "Thread-1" 1
java.lang.IndexOutOfBoundsException: Index: 3, Size: 3
	at java.util.ArrayList.rangeCheck(ArrayList.java:653)
	at java.util.ArrayList.remove(ArrayList.java:492)
	at com.wang.day.ArrayDemo$1.run(ArrayDemo.java:217)
	at java.lang.Thread.run(Thread.java:745)
java.util.ConcurrentModificationException
	at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:901)
	at java.util.ArrayList$Itr.next(ArrayList.java:851)
	at com.wang.day.ArrayDemo$2.run(ArrayDemo.java:226)
	at java.lang.Thread.run(Thread.java:745)
~~~



也就是说这里比较modCount 和expectedModCount。那么这两个具体指什么呢？

~~~java
protected transient int modCount = 0;//ArryaList中定义
int expectedModCount = modCount;//迭代器中定义
~~~

我们知道，modCount在删除，添加，clear元素的时候，均会修改modCount，而我们在使用迭代器迭代的时候，expectedModCount是不变的，这个时候多线程环境下就有可能导致两者不相等，所以会抛出ConcurrentModificationException异常。

**总的来说**，ArrayList源码特点主要有三点：

1. 扩容机制，在添加元素时检测需要扩容否，需要则扩容为原数组的1.5倍之后再判断真实的扩容大小。扩容之后生成一个新的数组，并将原来的数组元素复制到新数组，原来数组引用指向新数组。

2. fast-fail机制。这个主要是迭代器形成的。在多线程环境下，如果一个线程通过迭代器读取元素，一个线程修改list中元素，就会抛出ConcurrentModificationException异常。这就是fast-fail机制。

3. 线程不安全。

   ​
#### LinkedList详解

##### LinkedList数据结构

LinkedList底层是基于环形链表实现的，如图所示：

![双向链表](E:\日常总结md文档\java\双向链表.png)

具体的数据结构体现在结点，结点包括三个要素：

对上一个结点的引用，对下一个结点的引用，保存的值。

##### 实现分析

~~~java
package com.wang.day;

import java.util.NoSuchElementException;



/**
 * 最重要的是双向链表
 * @author wxe
 * @since 1.0.0
 */
public class LinkedDemo<E> {
	
	/**
	 * 设计成静态类，为了方便使用，不依赖外部类对象来创建
	 * @author wxe
	 * @since 1.0.0
	 */
	public static class Node<E>{
		//上一个结点
		Node<E> preNode;
		//下一个结点
		Node<E> nextNode;
		//结点中保存的值
		E item;
		
		public Node(Node<E> preNode,Node<E> nextNode,E item){
			this.preNode = preNode;
			this.nextNode = nextNode;
			this.item = item;
		}
	}
	
	//------------------------------------------------------------------------------------------------------------------------------
	
	
	transient Node<E> first;//头结点
	
	transient Node<E> last;//尾结点
	
	transient int size;
	
	public LinkedDemo(){
		
	}
	
	//--------------------------------------------------------------------添加操作-----------------------------------------------------------
	/**
	 * 添加结点，向尾部添加
	 * @param e
	 * @return
	 */
	public boolean add(E item) {
        linkLast(item);
        return true;
    }
	/**
	 * 添加到尾结点
	 * @param item
	 */
	void linkLast(E item) {
		final Node<E> l = last;//获取当前尾结点
		final Node<E> newNode = new Node<E>(l, null, item);//构造待插入的结点
		last = newNode;
		if (l == null) {
			first = newNode;//原来链表为空，插入第一个元素
		} else {
			l.nextNode = newNode;
		}
		size++;
	}
	/**
	 * 添加到头结点
	 * @param item
	 */
	void linkFirst(E item){
		final Node<E> f = first;//当前头结点
		final Node<E> newNode = new Node<E>(null,f,item);//下一个结点指向原来头结点
		first = newNode;//头结点执行该新添加的结点
		if (f == null) {
			//如果原来没有头结点，更新尾结点
			last = newNode;
		} else {
			//如果原来没有头结点，更新头结点执行新结点
			f.preNode = newNode;
		}
		size++;
	}
	/**
	 * 指定结点之前插入
	 * @param item
	 * @param succ
	 */
	void linkBefore(E item, Node<E> succ) {
		final Node<E> pred = succ.preNode;
		final Node<E> newNode = new Node<E>(succ.preNode, succ, item);//新结点，前一个结点指向succ的前一个结点，后一个结点指向succ
		succ.preNode = newNode;
		if (pred == null) {
			first = newNode;//如果succ没有前一个结点，头结点指向新结点
		} else {
			pred.nextNode = newNode;//succ的后一个结点指向新节点
		}
		size++;
	}
	
	//-----------------------------------------------------------------删除结点------------------------------------------------------------
	/**
	 * 从表头删除结点
	 * @param f
	 */
	E removeFirst(Node<E> f){
		final E item = f.item;
		final Node<E> next = f.nextNode;//第二个结点，即将充当为头结点
		f.preNode = null;
		f.nextNode = null;//删除头结点
		
		first = next;//将第二个结点升级为头结点
		if (first == null) {
			first = null;
		} else {
			next.preNode = null;//新头结点第一个结点指向为null
		}
		return item;
	}
	/**
	 * 从表尾删除结点
	 * @param l
	 */
	E removeLast(Node<E> l){
		final E item = l.item;
		final Node<E> prev = l.preNode;//被删除的前一个结点，即将作为尾结点
		l.preNode = null;
		l.nextNode = null;
		last = prev;//尾结点指向新的尾结点
		if (last == null) {
			last = null;
		} else {
			last.nextNode = null;//新的尾结点下一个结点指向null
		}
		return item;		
	}
	/**
	 * 删除指定结点
	 * @param n
	 * @return
	 */
	E removeNode(Node<E> n){
		final E item = n.item;//防止被修改
		//遍历整个结点？
		final Node<E> preNode = n.preNode;
		final Node<E> nextNode = n.nextNode;
		if (preNode == null) {
			//头结点
			first = nextNode;
		} else {
			preNode.nextNode = nextNode;//n结点前一个结点的后结点指向n结点的后结点
			n.preNode = null;
		}
		
		if (nextNode == null) {
			last = preNode;
		} else {
			nextNode.preNode = preNode;//n结点后一个结点的前一个结点指向n结点的前结点
			n.nextNode = null;
		}
		
		size++;
		return item;
	}
	//-------------------------------------------------------------------------获取元素-------------------------------------------------------------
	/**
	 * 获取表头元素
	 * @return
	 */
	public E getFirst() {
		final Node<E> f = first;//防止头结点被修改
		if (first == null) {
			throw new NoSuchElementException();
		}
		return f.item;
	}
	/**
	 * 获取尾结点元素
	 * @return
	 */
	public E getLast(){
		final Node<E> l = last;
		if (last == null) {
			throw new NoSuchElementException();
		}
		return l.item;
	}
	/**
	 * 获取指定下标的结点
	 * @param index
	 * @return
	 */
	public Node<E> getNode(int index){
		//1.检查index的合法性
		checkElementIndex(index);
		//2.根据index和size/2进行比较，看是从表头还是表尾遍历查找
		if (index < (size >> 1)) {
			Node<E> x = first;//表头遍历
			for (int i = 0; i < index; i++) {
				x = x.nextNode;
			}
			return x;
		} else {
			Node<E> x = last;
			for (int i = 0; i < index; i++) {
				 x = x.preNode;
			}
			return x;
		}
	}
	
	private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }
	
	public void checkElementIndex(int index){
		if (!isElementIndex(index)) {
			throw new IndexOutOfBoundsException();
		}
	}
}

~~~

这个源码只是我仿照源码写的，我们主要看几个public修饰的增删改查的方法，

~~~java
 public boolean add(E e) {
        linkLast(e);
        return true;
    }
~~~

添加元素的时候，是默认从尾部添加，且允许元素为null，这个时候如果遍历的时候，就保证了顺序输入，顺序输出。而且添加的时间是一个常数时间。所以，如果添加元素需求占比大的时候，可以选择LinkedList.

~~~java
 public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }
~~~

获取元素的时候，我们需要遍历链表找到指定下标下的元素，虽然说在遍历的时候根据指定下标来区分了从头部还是尾部遍历，但是最坏的情况下也是需要遍历n次的。所以，如果是随机访问元素，LnkedList的性能比不上ArrayList的。

~~~java
 public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }
~~~

删除指定元素的时候，其中unlink(x);的时间也是一个常数，而且不需要向ArrayList那样移动数组了，性能好了很多。

##### 总结

1. 内部结点定义使用静态内部类，这种方式创建对象不依赖外部对象，所以用起来更方便。
2. 添加元素的时间为时间常数，删除元素，不需要移动数据，单纯删除操作也是时间常数，所以删除，添加性能优于ArrayList。
3. 随机访问指定下标的时间常数是线性的，所以性能没有ArrayList的好。








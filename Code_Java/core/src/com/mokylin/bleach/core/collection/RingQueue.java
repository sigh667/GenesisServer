package com.mokylin.bleach.core.collection;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.mokylin.bleach.core.annotation.NotThreadSafe;


/**
 * 固定大小的队列<p>
 * 在末尾塞入数据时，如果队列已满，会把头部的数据弹出并通过返回值传出
 * @author baoliang.shen
 *
 */
@NotThreadSafe
public class RingQueue<E> implements Iterable<E>{

	/**The array in which the elements of the queue are stored.*/
	private E[] elements;
	/**The size of the queue (the number of elements it contains).*/
	private int size;

	/**The index of the element at the head of the queue*/
	private int head;
	/**The index at which the next element would be added to the tail of the queue*/
	private int tail;

	/**
	 * 唯一的构造函数，旨在规定必须在构造时指定容量
	 * @param capacity
	 */
	@SuppressWarnings("unchecked")
	public RingQueue(int capacity) {
		elements = (E[]) new Object[capacity];
	}

	/**
	 * 在尾部添加一个元素<p>
	 * 如果队列已满，会把头部的元素弹出；<p>
	 * 如果队列未满，则返回值为null
	 * @param e 要添加的元素，如果为null，会抛出NullPointerException
	 * @return
	 */
	public E add(E e) {
		if (e == null)
			throw new NullPointerException();
		E ret = null;
		if (size==elements.length) {
			ret = elements[head];
			increaseHead();
			size -=1;
		}

		elements[tail] = e;
		increaseTail();
		size +=1;

		return ret;
	}

	/**
	 * 队列中实际含有多少个元素
	 *
	 * @return the number of elements in this queue
	 */
	public int size() {
		return size;
	}

	/**
	 * 将尾结点游标向后移。如果超出末尾，就从头开始
	 */
	private void increaseTail() {
		tail += 1;
		tail = tail > elements.length-1 ? 0 : tail;
	}
	/**
	 * 将头结点游标向后移。如果超出末尾，就从头开始
	 */
	private void increaseHead() {
		head += 1;
		head = head > elements.length-1 ? 0 : head;
	}


	/**
	 * Returns an iterator over the elements in this queue.  The elements
	 * will be ordered from first (head) to last (tail). 
	 * @return an iterator over the elements in this queue
	 */
	@Override
	public Iterator<E> iterator() {
		return new QueueIterator();
	}

	private class QueueIterator implements Iterator<E> {
		/**
		 * Index of element to be returned by subsequent call to next.
		 */
		private int cursor = head;

		/**
		 * Tail recorded at construction, to stop iterator
		 * and also to check for comodification.
		 */
		private int fence = tail;

		public boolean hasNext() {
			return cursor != fence;
		}

		public E next() {
			if (cursor == fence)
				throw new NoSuchElementException();
			E result = elements[cursor];
			// This check doesn't catch all possible comodifications,
			// but does catch the ones that corrupt traversal
			if (tail != fence || result == null)
				throw new ConcurrentModificationException();
			cursor += 1;
			cursor = cursor > elements.length-1 ? 0 : cursor;
			return result;
		}

		/**
		 * 目前不支持删除操作，以后有需求再加
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}

package com.mokylin.bleach.test.core.collection;

import com.genesis.core.collection.RingQueue;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * {@link RingQueue}的单元测试
 * @author Joey
 *
 */
public class RingQueueTest {

    @Test
    public void test_RingQueue_add_and_pop() {

        final int capacity = 10;
        RingQueue<Long> que = new RingQueue<Long>(capacity);
        //知道塞满
        for (int i = 0; i < capacity; i++) {
            Long ret = que.add((long) i);
            assertThat(que.size(), is(i + 1));
            assertThat(ret == null, is(true));
        }
        //让其溢出
        for (int i = 0; i < capacity; i++) {
            Long ret = que.add((long) i);
            assertThat(que.size(), is(10));
            assertThat(ret != null, is(true));
            assertThat(ret.intValue() == i, is(true));
        }
    }
}

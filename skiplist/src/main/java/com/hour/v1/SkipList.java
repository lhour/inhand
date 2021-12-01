package com.hour.v1;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class SkipList<K extends Comparable<K>, V> {

    // 节点数量
    public int size;
    // 节点最大层数
    public int height;
    // 第一个节点
    SkipListNode<K, V> head;
    // 最后一个节点
    SkipListNode<K, V> tail;

    HashSet<SkipListNode<K, V>> tails = new HashSet<>();
    HashSet<SkipListNode<K, V>> heads = new HashSet<>();

    public Random random;

    public SkipList() {
        // 创建 head 节点
        this.head = new SkipListNode<K, V>(null, null);
        // 创建 tail 节点
        this.tail = new SkipListNode<K, V>(null, null);

        // 将 head 节点的右指针指向 tail 节点
        this.head.right = tail;
        // 将 tail 节点的左指针指向 head 节点
        this.tail.left = head;

        this.heads.add(head);
        this.tails.add(tail);

        this.height = 0;
        this.size = 0;
        this.random = new Random();
    }

    private void addEmptyLevel() {
        // 一组新的头和尾
        SkipListNode<K, V> newHead = new SkipListNode<K, V>(null, null);
        SkipListNode<K, V> newTail = new SkipListNode<K, V>(null, null);
        // 头尾相连
        newHead.right = newTail;
        newTail.left = newHead;
        // 头接入
        newHead.down = head;
        head.up = newHead;
        // 尾接入
        newTail.down = tail;
        tail.up = newTail;
        // 指针更新
        head = newHead;
        tail = newTail;

        this.heads.add(head);
        this.tails.add(tail);

        height++;
    }

    private SkipListNode<K, V> findEntry(K key) {
        // 从head头节点开始查找
        SkipListNode<K, V> res = head;

        while (true) {
            // 从左向右查找，直到右节点的key值大于要查找的key值
            while (!tails.contains(res.right) && res.right.key.compareTo(key) <= 0) {
                res = res.right;
            }
            // 如果有更低层的节点，则向低层移动
            if (res.down != null) {
                res = res.down;
            } else {
                break;
            }
        }
        // 返回p，！注意这里p的key值是小于等于传入key的值的（p.key <= key）
        return res;
    }

    public V get(K key) {
        SkipListNode<K, V> p = findEntry(key);

        if (p.key == key) {
            return p.value;
        }

        return null;
    }

    public V put(K key, V value) {

        // 目标节点的左边节点，或者就是目标节点
        SkipListNode<K, V> leftOrTargetNode;
        // 如果不存在 key，新建要插入的节点
        SkipListNode<K, V> newNode;
        // 当前层高
        int i = 0;

        // 查找适合插入的位子
        leftOrTargetNode = findEntry(key);

        // 如果跳跃表中存在含有key值的节点，则进行value的修改操作即可完成
        if (leftOrTargetNode.key == key) {
            V oldValue = leftOrTargetNode.value;
            leftOrTargetNode.value = value;
            return oldValue;
        }

        // 如果跳跃表中不存在含有key值的节点，则进行新增操作
        newNode = new SkipListNode<K, V>(key, value);
        newNode.left = leftOrTargetNode;
        newNode.right = leftOrTargetNode.right;
        leftOrTargetNode.right.left = newNode;
        leftOrTargetNode.right = newNode;

        // 再使用随机数决定是否要向更高level攀升
        while (random.nextDouble() < 0.5) {

            // 如果要增加高度，单高度太高，则不再增加，直接返回
            if (i >= height && height > size) {
                break;
            }
            // 如果新元素的级别已经达到跳跃表的最大高度，则新建空白层
            if (i >= height) {
                addEmptyLevel();
            }

            // 从leftOrTargetNode向左扫描含有高层节点的节点
            while (leftOrTargetNode.up == null) {
                leftOrTargetNode = leftOrTargetNode.left;
            }
            leftOrTargetNode = leftOrTargetNode.up;

            // 新增和newNode指针指向的节点含有相同key值的节点对象
            // 这里需要注意的是除底层节点之外的节点对象是不需要value值的
            SkipListNode<K,V> leftNodeHigher = new SkipListNode<K,V>(key, null);

            leftNodeHigher.left = leftOrTargetNode;
            leftNodeHigher.right = leftOrTargetNode.right;
            leftOrTargetNode.right.left = leftNodeHigher;
            leftOrTargetNode.right = leftNodeHigher;

            leftNodeHigher.down = newNode;
            newNode.up = leftNodeHigher;

            newNode = leftNodeHigher;
            i = i + 1;
        }

        size = size + 1;

        // 返回null，没有旧节点的value值
        return null;
    }

    public V remove(K key) {
        SkipListNode<K,V> leftOrTargetNode, newNode;

        leftOrTargetNode = findEntry(key);

        if (leftOrTargetNode.key != key) {
            return null;
        }

        V oldValue = leftOrTargetNode.value;
        while (leftOrTargetNode != null) {
            newNode = leftOrTargetNode.up;
            leftOrTargetNode.left.right = leftOrTargetNode.right;
            leftOrTargetNode.right.left = leftOrTargetNode.left;
            leftOrTargetNode = newNode;
        }
        return oldValue;
    }

    public int getSize(){
        return size;
    }

    @Override
    public String toString() {
        SkipListNode<K,V> lowHead = head; // 最底层的头节点
        SkipListNode<K,V> nowHead = new SkipListNode<K,V>(null, null); // 当前层的头节点

        // 初始化
        nowHead.down = head;
        StringBuilder sb = new StringBuilder();
        while (lowHead.down != null) {
            lowHead = lowHead.down;
        }
        // 开始遍历打印
        while (nowHead.down != null) { // 向下一层，遍历输出
            nowHead = nowHead.down;
            sb.append("head -> ");
            SkipListNode<K,V> nowTemp = nowHead; // 临时当前层点，方便遍历
            SkipListNode<K,V> lowTemp = lowHead.right; // 临时底层节点，同步遍历
            while (Objects.nonNull(nowTemp.right.right)) {
                nowTemp = nowTemp.right;
                while (lowTemp.key.compareTo(nowTemp.key) != 0) {
                    lowTemp = lowTemp.right;
                    sb.append("  -> "); 
                }
                lowTemp = lowTemp.right;
                if (!tails.contains(lowTemp)) {
                    sb.append(lowTemp + " -> ");
                }
            }
            sb.append("tail\n");
        }
        return sb.toString();
    }
}

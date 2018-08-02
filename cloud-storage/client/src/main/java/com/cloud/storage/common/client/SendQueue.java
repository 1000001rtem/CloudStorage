package com.cloud.storage.common.client;

public class SendQueue {
    private int maxSize;
    private byte [][] queue;
    private int head;
    private int tail;
    private int items;


    public SendQueue() {
        this.maxSize = 10;
        this.queue = new byte [this.maxSize][];
        this.head = 0;
        this.tail = -1;
        this.items = 0;
    }

    public boolean isEmpty() { return items == 0; }
    public boolean isFull() { return items == maxSize; }
    public int size() { return items; }

    public void insert(byte [] bytes) {
        if (isFull()) {
            maxSize *= 2;
            byte [][] temp = new byte[maxSize][];
            if (tail >= head) {
                System.arraycopy(queue, 0, temp, 0, queue.length);
            } else {
                System.arraycopy(queue, 0, temp, 0, tail + 1);
                System.arraycopy(queue, head, temp,
                        maxSize - (queue.length - head),
                        queue.length - head);
                head = maxSize - head - 1;
            }
            queue = temp;
        }

        if (tail == maxSize - 1)
            tail = -1;
        queue[++tail] = bytes;
        items++;
    }

    public byte[] remove() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        byte [] temp = queue[head++];
        if (head == maxSize)
            head = 0;
        items--;
        return temp;
    }

    public byte [] peekFront() {
        return queue[head];
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getTail() {
        return tail;
    }

    public void setTail(int tail) {
        this.tail = tail;
    }


}

package monitors;

public class RingBuffer {
    public Object[] elements;

    private int capacity;
    private int writePos = 0;
    private int available = 0;

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.elements = new Object[capacity];
    }

    public void reset() {
        this.writePos = 0;
        this.available = 0;
    }

    public int capacity() {
        return this.capacity;
    }

    public int available() {
        return this.available;
    }

    public int remainingCapacity() {
        return this.capacity - this.available;
    }

    public boolean put(Object element) {

        if (available < capacity) {
            if (writePos >= capacity) {
                writePos = 0;
            }
            elements[writePos] = element;
            writePos++;
            available++;
            return true;
        }

        return false;
    }

    public boolean put(Object[] elements) {
        if (elements.length <= remainingCapacity()) {
            for (Object element : elements){
                put(element);
            }
        }
        return false;
    }

    public Object take() {
        if (available == 0) {
            return null;
        }
        int nextSlot = writePos - available;
        if (nextSlot < 0) {
            nextSlot += capacity;
        }
        Object nextObj = elements[nextSlot];
        available--;
        return nextObj;
    }

    public Object[] take(int n) {
        if (available < n) {
            return null;
        }
        Object[] objects = new Object[n];
        for (int i = 0; i < n; i++) {
            objects[i] = take();
        }
        return objects;
    }

}

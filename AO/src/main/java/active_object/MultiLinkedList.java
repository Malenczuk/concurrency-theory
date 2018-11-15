package active_object;

import java.util.Map;
import java.util.HashMap;

public class MultiLinkedList<E>{

    private Map<Integer, Integer> size = new HashMap<>();
    private Map<Integer, MLNode<E>> first = new HashMap<>();
    private Map<Integer, MLNode<E>> last = new HashMap<>();

    public MultiLinkedList(){
    }

    private void linkLast(E e){
        linkLast(e, 0);
    }

    private void linkLast(E e, int type){
        final MLNode<E> newNode = new MLNode<>(e, type);
        linkLastNode(newNode, type);
        if(type != 0)
            linkLastNode(newNode, 0);
    }

    private void linkLastNode(MLNode<E> newNode, int type){
        final MLNode<E> l = last.get(type);
        last.put(type, newNode);
        if (l == null)
            first.put(type, newNode);
        else{
            l.next.put(type, newNode);
            newNode.prev.put(type, l);
        }
        size.put(type, size.getOrDefault(type, 0) + 1);
    }

    private E unlinkFirst(MLNode<E> f, int type){
        final E element = f.item;
        final MLNode<E> next = f.next.get(type);
        first.put(type,next);
        if (next == null)
            last.put(type, null);
        else
            next.prev.put(type,null);
        size.put(type, size.get(type) - 1);
        return element;
    }

    private E unlink(MLNode<E> x){
        final E element = x.item;
        final MLNode<E> next = x.next.get(0);
        final MLNode<E> prev = x.prev.get(0);

        if (prev == null) {
            first.put(0, next);
        } else {
            prev.next.put(0, next);
            x.prev.put(0, null);
        }

        if (next == null) {
            last.put(0, prev);
        } else {
            next.prev.put(0, prev);
            x.next.put(0, null);
        }

        x.item = null;
        size.put(0, size.get(0) - 1);
        return element;
    }

    public int size(){
        return size(0);
    }

    public int size(int type){
        return size.getOrDefault(type, 0);
    }

    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    public boolean add(E e, int type) {
        linkLast(e, type);
        return true;
    }

    public E peek(){
        return peek(0);
    }

    public E peek(int type){
        final MLNode<E> f = first.get(type);
        return (f == null) ? null : f.item;
    }

    public E poll(){
        return poll(0);
    }

    public E poll(int type) {
        final MLNode<E> f = first.get(type);
        if(f == null) return null;
        final E element = unlinkFirst(f, f.type);
        if(f.type != 0) unlink(f);
        return element;
    }

    private static class MLNode<E> {
        E item;
        int type;
        Map<Integer, MLNode<E>> next = new HashMap<>();
        Map<Integer, MLNode<E>> prev = new HashMap<>();

        MLNode(E element) {
            this.item = element;
            this.type = 0;
        }
        MLNode(E element, int type) {
            this.item = element;
            this.type = type;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        var x = first.entrySet();
        for(var y: x){
            stringBuilder.append(y.getKey()).append(" (").append(size(y.getKey())).append(") : ");
            var z = y.getValue();
            while (z != null){
                stringBuilder.append(z.item).append(" => ");
                z = z.next.get(y.getKey());
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}

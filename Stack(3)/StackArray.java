


public class StackArray<T> implements MyStack<T> {

    private Object[] array;
    private int count; 

    public StackArray() {
        this.array = new Object[10]; 
        this.count = 0;
    }

    
    private void resize() {
        int newCapacity = array.length * 2; 
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(array, 0, newArray, 0, count);
        this.array = newArray;
    }

    @Override
    public void push(T x) {
        if (count == array.length) {
            resize();  
        }
        array[count++] = x;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) return null;
        T element = (T) array[--count];
        array[count] = null; 
        return element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) return null;
        return (T) array[count - 1];
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void delete(T n) {
        for (int i = 0; i < count; i++) {
            if (array[i].equals(n)) {
                
                for (int j = i; j < count - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[--count] = null;
                return; 
            }
        }
    }
}
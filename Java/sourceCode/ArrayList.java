package java.util;  
/* 
    1.ArrayList实现了List接口，允许包含null 
    2.ArrayList和Vector基本一致，除了ArrayList是非同步的（unsynchronized） 
      要想使ArrayList多线程同步，可以封装ArrayList,如 
      List list = Collections.synchronizedList(new ArrayList(...)); 
*/  
public class ArrayList<E> extends AbstractList<E>  
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable  
{  
    //序列号  
    private static final long serialVersionUID = 8683452581122892189L;  
  
    //默认的capacity为10，和vector一样大小  
    private static final int DEFAULT_CAPACITY = 10;  
      
    //ArrayList类共享的数组对象，  
    private static final Object[] EMPTY_ELEMENTDATA = {};  
      
    /* 
        1.ArrayList存储元素的数组,Arraylist的capacity等于数组的length 
        2.对于所有一开始为空的ArrayList(就是指elementData == EMPTY_ELEMENTDATA), 
          当第一个元素被加入时，ArrayList扩充为DEFAULT_CAPACITY 
    */  
    private transient Object[] elementData;  
      
    //包含元素的数目  
    private int size;  
      
    //初始化大小为initialCapacity的ArrayList  
    public ArrayList(int initialCapacity) {  
        super();  
        if (initialCapacity < 0)  
            throw new IllegalArgumentException("Illegal Capacity: "+  
                                               initialCapacity);  
        this.elementData = new Object[initialCapacity];  
    }  
      
    //无参构造函数  
    public ArrayList() {  
        super();  
        //注意这里：默认使用静态数组EMPTY_ELEMENTDATA  
        this.elementData = EMPTY_ELEMENTDATA;  
    }  
      
    //和vector类似，使用集合c初始化ArrayList  
    public ArrayList(Collection<? extends E> c) {  
        elementData = c.toArray();  
        size = elementData.length;  
        // c.toArray might (incorrectly) not return Object[] (see 6260652)  
        //函数使用说明参见Vector分析  
        if (elementData.getClass() != Object[].class)  
            elementData = Arrays.copyOf(elementData, size, Object[].class);  
    }  
      
    /* 
        如果ArrayList当前实际元素数目小于capacity，将vector缩小。 
        常用于减少ArrayList的存储空间 
    */  
    public void trimToSize() {  
        modCount++;  
        if (size < elementData.length) {  
            elementData = Arrays.copyOf(elementData, size);  
        }  
    }  
      
    /* 
        1.增大ArrayList的大小，确保能存放至少minCapacity个元素 
    */  
    public void ensureCapacity(int minCapacity) {  
      
        /* 
          如果elementData等于EMPTY_ELEMENTDATA，表示ArrayList对象只初始化， 
          还未包含任何元素，那么将ArrayList扩展为DEFAULT_CAPACITY   
        */  
        int minExpand = (elementData != EMPTY_ELEMENTDATA)  
            // any size if real element table  
            ? 0  
            // larger than default for empty table. It's already supposed to be  
            // at default size.  
            : DEFAULT_CAPACITY;  
  
        if (minCapacity > minExpand) {  
            ensureExplicitCapacity(minCapacity);  
        }  
    }  
      
    //内部函数，功能和vector类相似  
    private void ensureCapacityInternal(int minCapacity) {  
        if (elementData == EMPTY_ELEMENTDATA) {  
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);  
        }  
  
        ensureExplicitCapacity(minCapacity);  
    }  
      
    private void ensureExplicitCapacity(int minCapacity) {  
        modCount++;  
  
        // overflow-conscious code  
        if (minCapacity - elementData.length > 0)  
            grow(minCapacity);  
    }  
      
    /* 
        The maximum size of array to allocate. 
        有些VM需要在数组前加些头信息（header words ） 
    */  
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;  
      
    //Increases the capacity to ensure that it can hold at least the  
    // number of elements specified by the minimum capacity argument.  
    private void grow(int minCapacity) {  
        // overflow-conscious code  
        int oldCapacity = elementData.length;  
          
        //默认是增加原来oldcapacity的一半（重点）       
        int newCapacity = oldCapacity + (oldCapacity >> 1);  
          
        if (newCapacity - minCapacity < 0)  
            newCapacity = minCapacity;  
        if (newCapacity - MAX_ARRAY_SIZE > 0)  
            newCapacity = hugeCapacity(minCapacity);  
        // minCapacity is usually close to size, so this is a win:  
        elementData = Arrays.copyOf(elementData, newCapacity);  
    }  
      
    private static int hugeCapacity(int minCapacity) {  
        if (minCapacity < 0) // overflow  
            throw new OutOfMemoryError();  
        return (minCapacity > MAX_ARRAY_SIZE) ?  
            Integer.MAX_VALUE :  
            MAX_ARRAY_SIZE;  
    }  
      
    //实际元素数目，和vector方法类似  
    public int size() {  
        return size;  
    }  
      
    //是否为空  
    public boolean isEmpty() {  
        return size == 0;  
    }  
      
    //是否包含o对象  
    public boolean contains(Object o) {  
        return indexOf(o) >= 0;  
    }  
      
    ////index为起始位置,返回-1表示不包含o对象  
    public int indexOf(Object o) {  
        if (o == null) {  
            for (int i = 0; i < size; i++)  
                if (elementData[i]==null)  
                    return i;  
        } else {  
            for (int i = 0; i < size; i++)  
                if (o.equals(elementData[i]))  
                    return i;  
        }  
        return -1;  
    }  
      
    //反向查找  
    public int lastIndexOf(Object o) {  
        if (o == null) {  
            for (int i = size-1; i >= 0; i--)  
                if (elementData[i]==null)  
                    return i;  
        } else {  
            for (int i = size-1; i >= 0; i--)  
                if (o.equals(elementData[i]))  
                    return i;  
        }  
        return -1;  
    }  
      
    public Object clone() {  
        try {  
            @SuppressWarnings("unchecked")  
                ArrayList<E> v = (ArrayList<E>) super.clone();  
            v.elementData = Arrays.copyOf(elementData, size);  
            v.modCount = 0;  
            return v;  
        } catch (CloneNotSupportedException e) {  
            // this shouldn't happen, since we are Cloneable  
            throw new InternalError();  
        }  
    }  
      
    public Object[] toArray() {  
        return Arrays.copyOf(elementData, size);  
    }  
      
    //通过将对象置为null,来删除对象内存空间  
    @SuppressWarnings("unchecked")  
    public <T> T[] toArray(T[] a) {  
        if (a.length < size)  
         
  
       // Make a new array of a's runtime type, but my contents:  
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());  
        System.arraycopy(elementData, 0, a, 0, size);  
        if (a.length > size)  
            a[size] = null;  
        return a;  
    }  
      
      
    @SuppressWarnings("unchecked")  
    E elementData(int index) {  
        return (E) elementData[index];  
    }  
      
    public E get(int index) {  
        rangeCheck(index);  
  
        return elementData(index);  
    }  
      
      
      
    public E set(int index, E element) {  
        rangeCheck(index);  
  
        E oldValue = elementData(index);  
        elementData[index] = element;  
        return oldValue;  
    }  
      
    public boolean add(E e) {  
        ensureCapacityInternal(size + 1);  // Increments modCount!!  
        elementData[size++] = e;  
        return true;  
    }  
      
    public void add(int index, E element) {  
        rangeCheckForAdd(index);  
  
        ensureCapacityInternal(size + 1);  // Increments modCount!!  
        System.arraycopy(elementData, index, elementData, index + 1,  
                         size - index);  
        elementData[index] = element;  
        size++;  
    }  
      
    public E remove(int index) {  
        rangeCheck(index);  
  
        modCount++;  
        E oldValue = elementData(index);  
  
        int numMoved = size - index - 1;  
        if (numMoved > 0)  
            System.arraycopy(elementData, index+1, elementData, index,  
                             numMoved);  
        elementData[--size] = null; // clear to let GC do its work  
  
        return oldValue;  
    }  
      
    public boolean remove(Object o) {  
        if (o == null) {  
            for (int index = 0; index < size; index++)  
                if (elementData[index] == null) {  
                    fastRemove(index);  
                    return true;  
                }  
        } else {  
            for (int index = 0; index < size; index++)  
                if (o.equals(elementData[index])) {  
                    fastRemove(index);  
                    return true;  
                }  
        }  
        return false;  
    }  
      
    /* 
     * Private remove method that skips bounds checking and does not 
     * return the value removed. 
     */  
    private void fastRemove(int index) {  
        modCount++;  
        int numMoved = size - index - 1;  
        if (numMoved > 0)  
            System.arraycopy(elementData, index+1, elementData, index,  
                             numMoved);  
        elementData[--size] = null; // clear to let GC do its work  
    }  
      
    public void clear() {  
        modCount++;  
  
        // clear to let GC do its work  
        for (int i = 0; i < size; i++)  
            elementData[i] = null;  
  
        size = 0;  
    }  
      
    public boolean addAll(Collection<? extends E> c) {  
        Object[] a = c.toArray();  
        int numNew = a.length;  
        ensureCapacityInternal(size + numNew);  // Increments modCount  
        System.arraycopy(a, 0, elementData, size, numNew);  
        size += numNew;  
        return numNew != 0;  
    }  
      
    public boolean addAll(int index, Collection<? extends E> c) {  
        rangeCheckForAdd(index);  
  
        Object[] a = c.toArray();  
        int numNew = a.length;  
        ensureCapacityInternal(size + numNew);  // Increments modCount  
  
        int numMoved = size - index;  
        if (numMoved > 0)  
            System.arraycopy(elementData, index, elementData, index + numNew,  
                             numMoved);  
  
        System.arraycopy(a, 0, elementData, index, numNew);  
        size += numNew;  
        return numNew != 0;  
    }  
      
    protected void removeRange(int fromIndex, int toIndex) {  
        modCount++;  
        int numMoved = size - toIndex;  
        System.arraycopy(elementData, toIndex, elementData, fromIndex,  
                         numMoved);  
  
        // clear to let GC do its work  
        int newSize = size - (toIndex-fromIndex);  
        for (int i = newSize; i < size; i++) {  
            elementData[i] = null;  
        }  
        size = newSize;  
    }  
      
      
    private void rangeCheck(int index) {  
        if (index >= size)  
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));  
    }  
      
      
    private void rangeCheckForAdd(int index) {  
        if (index > size || index < 0)  
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));  
    }  
      
    private String outOfBoundsMsg(int index) {  
        return "Index: "+index+", Size: "+size;  
    }  
      
    public boolean removeAll(Collection<?> c) {  
        return batchRemove(c, false);  
    }  
      
    public boolean retainAll(Collection<?> c) {  
        return batchRemove(c, true);  
    }  
      
    //通过控制complement,来表示是删除还是保留  
    private boolean batchRemove(Collection<?> c, boolean complement) {  
        final Object[] elementData = this.elementData;  
        int r = 0, w = 0;  
        boolean modified = false;  
        try {  
            for (; r < size; r++)  
            //通过控制complement,来表示是删除还是保留，具体实现  
                if (c.contains(elementData[r]) == complement)  
                    elementData[w++] = elementData[r];  
        } finally {  
            // Preserve behavioral compatibility with AbstractCollection,  
            // even if c.contains() throws.  
            if (r != size) {  
                System.arraycopy(elementData, r,  
                                 elementData, w,  
                                 size - r);  
                w += size - r;  
            }  
            if (w != size) {  
                // clear to let GC do its work  
                for (int i = w; i < size; i++)  
                    elementData[i] = null;  
                modCount += size - w;  
                size = w;  
                modified = true;  
            }  
        }  
        return modified;  
    }  
      
      
    private void writeObject(java.io.ObjectOutputStream s)  
        throws java.io.IOException{  
        // Write out element count, and any hidden stuff  
        int expectedModCount = modCount;  
        s.defaultWriteObject();  
  
        // Write out size as capacity for behavioural compatibility with clone()  
        s.writeInt(size);  
  
        // Write out all elements in the proper order.  
        for (int i=0; i<size; i++) {  
            s.writeObject(elementData[i]);  
        }  
  
        if (modCount != expectedModCount) {  
            throw new ConcurrentModificationException();  
        }  
    }  
  
    /** 
     * Reconstitute the <tt>ArrayList</tt> instance from a stream (that is, 
     * deserialize it). 
     */  
    private void readObject(java.io.ObjectInputStream s)  
        throws java.io.IOException, ClassNotFoundException {  
        elementData = EMPTY_ELEMENTDATA;  
  
        // Read in size, and any hidden stuff  
        s.defaultReadObject();  
  
        // Read in capacity  
        s.readInt(); // ignored  
  
        if (size > 0) {  
            // be like clone(), allocate array based upon size not capacity  
            ensureCapacityInternal(size);  
  
            Object[] a = elementData;  
            // Read in all elements in the proper order.  
            for (int i=0; i<size; i++) {  
                a[i] = s.readObject();  
            }  
        }  
    }  
      
    /* 
        1.将ListIterator放到ArrayList的内部，既实现了ArrayList数据封装, 
          又可以使迭代器能访问ArrayList内部数据 
        2.返回从index开始的迭代器 
    */  
    public ListIterator<E> listIterator(int index) {  
        if (index < 0 || index > size)  
            throw new IndexOutOfBoundsException("Index: "+index);  
        return new ListItr(index);  
    }  
      
    //默认返回从0开始的迭代器  
    public ListIterator<E> listIterator() {  
        return new ListItr(0);  
    }  
      
    public Iterator<E> iterator() {  
        return new Itr();  
    }  
      
    //类似于vector  
    private class Itr implements Iterator<E> {  
        int cursor;       // index of next element to return  
        int lastRet = -1; // index of last element returned; -1 if no such  
        //用于检查线程是否同步，如果线程不同步，它们两个的值不一样  
        int expectedModCount = modCount;  
  
        public boolean hasNext() {  
            return cursor != size;  
        }  
  
        @SuppressWarnings("unchecked")  
        public E next() {  
            checkForComodification();  
            int i = cursor;  
            if (i >= size)  
                throw new NoSuchElementException();  
            Object[] elementData = ArrayList.this.elementData;  
            if (i >= elementData.length)  
                throw new ConcurrentModificationException();  
            cursor = i + 1;  
            return (E) elementData[lastRet = i];  
        }  
  
        public void remove() {  
            if (lastRet < 0)  
                throw new IllegalStateException();  
            checkForComodification();  
  
            try {  
                ArrayList.this.remove(lastRet);  
                cursor = lastRet;  
                lastRet = -1;  
                expectedModCount = modCount;  
            } catch (IndexOutOfBoundsException ex) {  
                throw new ConcurrentModificationException();  
            }  
        }  
  
        //线程安全检查  
        final void checkForComodification() {  
            if (modCount != expectedModCount)  
                throw new ConcurrentModificationException();  
        }  
    }  
  
    //类似于Itr  
    private class ListItr extends Itr implements ListIterator<E> {  
        ListItr(int index) {  
            super();  
            cursor = index;  
        }  
  
        public boolean hasPrevious() {  
            return cursor != 0;  
        }  
  
        public int nextIndex() {  
            return cursor;  
        }  
  
        public int previousIndex() {  
            return cursor - 1;  
        }  
  
        @SuppressWarnings("unchecked")  
        public E previous() {  
            checkForComodification();  
            int i = cursor - 1;  
            if (i < 0)  
                throw new NoSuchElementException();  
            Object[] elementData = ArrayList.this.elementData;  
            if (i >= elementData.length)  
                throw new ConcurrentModificationException();  
            cursor = i;  
            return (E) elementData[lastRet = i];  
        }  
  
        public void set(E e) {  
            if (lastRet < 0)  
                throw new IllegalStateException();  
            checkForComodification();  
  
            try {  
                ArrayList.this.set(lastRet, e);  
            } catch (IndexOutOfBoundsException ex) {  
                throw new ConcurrentModificationException();  
            }  
        }  
  
        public void add(E e) {  
            checkForComodification();  
  
            try {  
                int i = cursor;  
                ArrayList.this.add(i, e);  
                cursor = i + 1;  
                lastRet = -1;  
                expectedModCount = modCount;  
            } catch (IndexOutOfBoundsException ex) {  
                throw new ConcurrentModificationException();  
            }  
        }  
    }  
      
     public List<E> subList(int fromIndex, int toIndex) {  
        //类型检查  
        subListRangeCheck(fromIndex, toIndex, size);  
        return new SubList(this, 0, fromIndex, toIndex);  
    }  
  
    static void subListRangeCheck(int fromIndex, int toIndex, int size) {  
        if (fromIndex < 0)  
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);  
        if (toIndex > size)  
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);  
        if (fromIndex > toIndex)  
            throw new IllegalArgumentException("fromIndex(" + fromIndex +  
                                               ") > toIndex(" + toIndex + ")");  
    }  
  
    //实现了AbstractList的随机访问方法  
    private class SubList extends AbstractList<E> implements RandomAccess {  
        private final AbstractList<E> parent;  
        private final int parentOffset;  
        private final int offset;  
        int size;  
  
        SubList(AbstractList<E> parent,  
                int offset, int fromIndex, int toIndex) {  
            this.parent = parent;  
            this.parentOffset = fromIndex;  
            this.offset = offset + fromIndex;  
            this.size = toIndex - fromIndex;  
            this.modCount = ArrayList.this.modCount;  
        }  
  
        public E set(int index, E e) {  
            rangeCheck(index);  
            checkForComodification();  
            E oldValue = ArrayList.this.elementData(offset + index);  
            ArrayList.this.elementData[offset + index] = e;  
            return oldValue;  
        }  
  
        public E get(int index) {  
            rangeCheck(index);  
            checkForComodification();  
            return ArrayList.this.elementData(offset + index);  
        }  
  
        public int size() {  
            checkForComodification();  
            return this.size;  
        }  
  
        public void add(int index, E e) {  
            rangeCheckForAdd(index);  
            checkForComodification();  
            parent.add(parentOffset + index, e);  
            this.modCount = parent.modCount;  
            this.size++;  
        }  
  
        public E remove(int index) {  
            rangeCheck(index);  
            checkForComodification();  
            E result = parent.remove(parentOffset + index);  
            this.modCount = parent.modCount;  
            this.size--;  
            return result;  
        }  
  
        protected void removeRange(int fromIndex, int toIndex) {  
            checkForComodification();  
            parent.removeRange(parentOffset + fromIndex,  
                               parentOffset + toIndex);  
            this.modCount = parent.modCount;  
            this.size -= toIndex - fromIndex;  
        }  
  
        public boolean addAll(Collection<? extends E> c) {  
            return addAll(this.size, c);  
        }  
  
        public boolean addAll(int index, Collection<? extends E> c) {  
            rangeCheckForAdd(index);  
            int cSize = c.size();  
            if (cSize==0)  
                return false;  
  
            checkForComodification();  
            parent.addAll(parentOffset + index, c);  
            this.modCount = parent.modCount;  
            this.size += cSize;  
            return true;  
        }  
  
        public Iterator<E> iterator() {  
            return listIterator();  
        }  
  
        public ListIterator<E> listIterator(final int index) {  
            checkForComodification();  
            rangeCheckForAdd(index);  
            final int offset = this.offset;  
  
            return new ListIterator<E>() {  
                int cursor = index;  
                int lastRet = -1;  
                int expectedModCount = ArrayList.this.modCount;  
  
                public boolean hasNext() {  
                    return cursor != SubList.this.size;  
                }  
  
                @SuppressWarnings("unchecked")  
                public E next() {  
                    checkForComodification();  
                    int i = cursor;  
                    if (i >= SubList.this.size)  
                        throw new NoSuchElementException();  
                    Object[] elementData = ArrayList.this.elementData;  
                    if (offset + i >= elementData.length)  
                        throw new ConcurrentModificationException();  
                    cursor = i + 1;  
                    return (E) elementData[offset + (lastRet = i)];  
                }  
  
                public boolean hasPrevious() {  
                    return cursor != 0;  
                }  
  
                @SuppressWarnings("unchecked")  
                public E previous() {  
                    checkForComodification();  
                    int i = cursor - 1;  
                    if (i < 0)  
                        throw new NoSuchElementException();  
                    Object[] elementData = ArrayList.this.elementData;  
                    if (offset + i >= elementData.length)  
                        throw new ConcurrentModificationException();  
                    cursor = i;  
                    return (E) elementData[offset + (lastRet = i)];  
                }  
  
                public int nextIndex() {  
                    return cursor;  
                }  
  
                public int previousIndex() {  
                    return cursor - 1;  
                }  
  
                public void remove() {  
                    if (lastRet < 0)  
                        throw new IllegalStateException();  
                    checkForComodification();  
  
                    try {  
                        SubList.this.remove(lastRet);  
                        cursor = lastRet;  
                        lastRet = -1;  
                        expectedModCount = ArrayList.this.modCount;  
                    } catch (IndexOutOfBoundsException ex) {  
                        throw new ConcurrentModificationException();  
                    }  
                }  
  
                public void set(E e) {  
                    if (lastRet < 0)  
                        throw new IllegalStateException();  
                    checkForComodification();  
  
                    try {  
                        ArrayList.this.set(offset + lastRet, e);  
                    } catch (IndexOutOfBoundsException ex) {  
                        throw new ConcurrentModificationException();  
                    }  
                }  
  
                public void add(E e) {  
                    checkForComodification();  
  
                    try {  
                        int i = cursor;  
                        SubList.this.add(i, e);  
                        cursor = i + 1;  
                        lastRet = -1;  
                        expectedModCount = ArrayList.this.modCount;  
                    } catch (IndexOutOfBoundsException ex) {  
                        throw new ConcurrentModificationException();  
                    }  
                }  
  
                final void checkForComodification() {  
                    if (expectedModCount != ArrayList.this.modCount)  
                        throw new ConcurrentModificationException();  
                }  
            };  
        }  
  
        public List<E> subList(int fromIndex, int toIndex) {  
            subListRangeCheck(fromIndex, toIndex, size);  
            return new SubList(this, offset, fromIndex, toIndex);  
        }  
  
        private void rangeCheck(int index) {  
            if (index < 0 || index >= this.size)  
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));  
        }  
  
        private void rangeCheckForAdd(int index) {  
            if (index < 0 || index > this.size)  
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));  
        }  
  
        private String outOfBoundsMsg(int index) {  
            return "Index: "+index+", Size: "+this.size;  
        }  
  
        private void checkForComodification() {  
            if (ArrayList.this.modCount != this.modCount)  
                throw new ConcurrentModificationException();  
        }  
    }  
}  

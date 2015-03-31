/* 
    1.Vector可以随着用户插入或删除元素来改变自己的大小。 
    2.Vector类通过维护capacity（函数）和capacityIncrement（变量）来优化存储。 
    3.capacity总是至少和vector的size一般大（capacity>=size）。 
    4.通过在向vector插入元素之前增大capacity，可以减少很大的内存分配时间。 
*/  
package java.util;  
  
public class Vector<E>  
    extends AbstractList<E>  
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable  
{  
    //用来存储vector元素的数组，vector的captcity等于数组的length  
    protected Object[] elementData;  
      
    //vector中实际存在元素的数目  
    protected int elementCount;  
      
    /* 
    1.capacity表示当需要存储空间大于capacity时，vector存储空间增大的数目 
    2.当capacity<=0时，vector的capacity每次需要增长时，大小翻倍 
    */  
    protected int capacityIncrement;  
      
    //使用JDK 1.0.2得到的序列号  
    private static final long serialVersionUID = -2767605614048989439L;  
      
    /* 
    1.初始化一个空的Vector 
    2.initialCapacity表示vector的初始大小。 
    3.capacityIncrement表示当vector溢出（overflow）时，capacity需要增加的数目 
    */  
    public Vector(int initialCapacity, int capacityIncrement) {  
          
        //Vector的直接父类AbstractList的构造函数为空，没有什么特别的意义  
        super();  
        //如果初始大小为负，抛出参数异常  
        if (initialCapacity < 0)  
            throw new IllegalArgumentException("Illegal Capacity: "+  
                                               initialCapacity);  
        //初始化成员变量  
        this.elementData = new Object[initialCapacity];  
        this.capacityIncrement = capacityIncrement;  
    }  
      
    //默认的capacityIncrement为0，表示vector大小需要增加时，采用double策略  
    public Vector(int initialCapacity) {  
        this(initialCapacity, 0);  
    }  
      
    //默认Vector的大小为10  
    public Vector() {  
        this(10);  
    }  
      
      
    public Vector(Collection<? extends E> c) {  
        //调用Collection子类的toArray方法  
        elementData = c.toArray();  
        elementCount = elementData.length;  
        // c.toArray might (incorrectly) not return Object[] (see 6260652)  
          
        /* 
        1.Arrays.copyOf(U[] original, int newLength, Class<? extends T[]> newType)  
        复制指定的数组，截取或用 null 填充（如有必要），以使副本具有指定的长度。 
        2.就是通过一系列方法将Colleciton的元素变成数组存到elementData中 
        */  
        if (elementData.getClass() != Object[].class)  
            elementData = Arrays.copyOf(elementData, elementCount, Object[].class);  
    }  
      
    /* 
    1.sysnchronized：从这里可以看出：vector线程安全，ArrayList不是线程同步的 
    2.将elemetntData中的元素复制到anArray中 
    */  
    public synchronized void copyInto(Object[] anArray) {  
        //从指定源数组中复制一个数组，复制从指定的位置开始，到目标数组的指定位置结束。  
       System.arraycopy(elementData, 0, anArray, 0, elementCount);  
    }  
    /* 
        如果vector当前实际元素数目小于capacity，将vector缩小。 
        常用于减少vector的存储空间 
    */  
    public synchronized void trimToSize() {  
        /* 
        1.在Vector和ArrayList的直接父类AbstractList中声明，表示集合容器结构上被修改的次数 
          通常用于线程并发中。（结构修改通常指改变容器size，以及使迭代器产生错误结果的情况） 
        */  
        modCount++;  
        int oldCapacity = elementData.length;  
        if (elementCount < oldCapacity) {  
            elementData = Arrays.copyOf(elementData, elementCount);  
        }  
    }  
      
    /* 
        1.增大vector的大小，确保能存放至少minCapacity个元素 
    */  
    public synchronized void ensureCapacity(int minCapacity) {  
        if (minCapacity > 0) {  
            modCount++;  
            ensureCapacityHelper(minCapacity);//函数在下面  
        }  
    }  
      
    private void ensureCapacityHelper(int minCapacity) {  
        // overflow-conscious code  
        //如果当前的elementData.length(即capacity)小于参数mincapacity  
        if (minCapacity - elementData.length > 0)  
            grow(minCapacity);//函数在下面  
    }  
      
    /* 
        The maximum size of array to allocate. 
        有些VM需要在数组前加些头信息（header words ） 
    */  
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;  
  
    private void grow(int minCapacity) {  
        // overflow-conscious code  
        int oldCapacity = elementData.length;  
        /* 
        如果capacityIncrement>0，则新的capacity = 旧的capacity+capacityIncrement 
        否则double 
        */  
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?  
                                         capacityIncrement : oldCapacity);  
        if (newCapacity - minCapacity < 0)  
            newCapacity = minCapacity;  
        //如果容量过大，进行异常处理  
        if (newCapacity - MAX_ARRAY_SIZE > 0)  
            newCapacity = hugeCapacity(minCapacity);//函数在下面  
        elementData = Arrays.copyOf(elementData, newCapacity);  
    }  
      
    private static int hugeCapacity(int minCapacity) {  
        if (minCapacity < 0) // overflow  
            throw new OutOfMemoryError();  
        return (minCapacity > MAX_ARRAY_SIZE) ?  
            Integer.MAX_VALUE :  
            MAX_ARRAY_SIZE;  
    }  
    /* 
      1.如果小了，增大空间，用null填充 
      2.如果大了，减少空间，用null填充 
    */  
    public synchronized void setSize(int newSize) {  
        modCount++;  
        if (newSize > elementCount) {  
            ensureCapacityHelper(newSize);  
        } else {  
            for (int i = newSize ; i < elementCount ; i++) {  
                elementData[i] = null;  
            }  
        }  
        elementCount = newSize;  
    }  
      
    //capacity = elementData.length ,数据的容量大小（不是实际大小）  
    public synchronized int capacity() {  
        return elementData.length;  
    }  
      
    //vector的实际大小  
    public synchronized int size() {  
        return elementCount;  
    }  
      
    //是否为空  
    public synchronized boolean isEmpty() {  
        return elementCount == 0;  
    }  
      
    //  返回此vector的组件的枚举。  
    public Enumeration<E> elements() {  
        //内部类  
        return new Enumeration<E>() {  
            int count = 0;  
              
            public boolean hasMoreElements() {  
                return count < elementCount;  
            }  
  
            public E nextElement() {  
                //锁机制  
                synchronized (Vector.this) {  
                    if (count < elementCount) {  
                        return elementData(count++);  
                    }  
                }  
                throw new NoSuchElementException("Vector Enumeration");  
            }  
        };  
    }  
      
    //是否包含o对象  
    public boolean contains(Object o) {  
        return indexOf(o, 0) >= 0;  
    }  
      
    //o对象的位置  
    public int indexOf(Object o) {  
        return indexOf(o, 0);  
    }  
      
    //index为起始位置,返回-1表示不包含o对象  
    public synchronized int indexOf(Object o, int index) {  
        if (o == null) {  
            for (int i = index ; i < elementCount ; i++)  
                if (elementData[i]==null)  
                    return i;  
        } else {  
            for (int i = index ; i < elementCount ; i++)  
                if (o.equals(elementData[i]))  
                    return i;  
        }  
        return -1;  
    }  
      
    //o对象的最后位置  
    public synchronized int lastIndexOf(Object o) {  
        return lastIndexOf(o, elementCount-1);  
    }  
      
    //反向查找，就是lastIndexOf  
    public synchronized int lastIndexOf(Object o, int index) {  
        if (index >= elementCount)  
            throw new IndexOutOfBoundsException(index + " >= "+ elementCount);  
  
        if (o == null) {  
            for (int i = index; i >= 0; i--)  
                if (elementData[i]==null)  
                    return i;  
        } else {  
            for (int i = index; i >= 0; i--)  
                if (o.equals(elementData[i]))  
                    return i;  
        }  
        return -1;  
    }  
      
    //返回指定位置对象  
    public synchronized E elementAt(int index) {  
        if (index >= elementCount) {  
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);  
        }  
  
        return elementData(index);//还可以这样调用?!，这是个函数，在下面有  
    }  
      
    //返回vector中第一个对象  
    public synchronized E firstElement() {  
        if (elementCount == 0) {  
            throw new NoSuchElementException();  
        }  
        return elementData(0);  
    }  
      
    //返回vector中第二个对象  
    public synchronized E lastElement() {  
        if (elementCount == 0) {  
            throw new NoSuchElementException();  
        }  
        return elementData(elementCount - 1);  
    }  
      
    //设置指定位置对象的值  
    public synchronized void setElementAt(E obj, int index) {  
        if (index >= elementCount) {  
            throw new ArrayIndexOutOfBoundsException(index + " >= " +  
                                                     elementCount);  
        }  
        elementData[index] = obj;  
    }  
      
    //删除指定位置对象  
    public synchronized void removeElementAt(int index) {  
        modCount++;  
        if (index >= elementCount) {  
            throw new ArrayIndexOutOfBoundsException(index + " >= " +  
                                                     elementCount);  
        }  
        else if (index < 0) {  
            throw new ArrayIndexOutOfBoundsException(index);  
        }  
        //得到删除位置到结尾之间的距离  
        int j = elementCount - index - 1;  
        if (j > 0) {  
            System.arraycopy(elementData, index + 1, elementData, index, j);  
        }  
        elementCount--;  
        //java中不必自己删除对象（用delete用习惯了），将对象置为null即可  
        elementData[elementCount] = null; /* to let gc do its work */  
    }  
      
    public synchronized void insertElementAt(E obj, int index) {  
        modCount++;  
        if (index > elementCount) {  
            throw new ArrayIndexOutOfBoundsException(index  
                                                     + " > " + elementCount);  
        }  
        //扩大vector的存储空间  
        ensureCapacityHelper(elementCount + 1);  
        //多了好多的拷贝时间呀  
        System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);  
        elementData[index] = obj;  
        elementCount++;  
    }  
      
    //直接在末尾添加  
    public synchronized void addElement(E obj) {  
        modCount++;  
        ensureCapacityHelper(elementCount + 1);  
        elementData[elementCount++] = obj;  
    }  
      
    //先找到位置，在删除对象  
    public synchronized boolean removeElement(Object obj) {  
        modCount++;  
        int i = indexOf(obj);  
        if (i >= 0) {  
            removeElementAt(i);  
            return true;  
        }  
        return false;  
    }  
      
    //将对象置为null，就可以删除对象了  
    public synchronized void removeAllElements() {  
        modCount++;  
        // Let gc do its work  
        for (int i = 0; i < elementCount; i++)  
            elementData[i] = null;  
  
        elementCount = 0;  
    }  
      
    //创建并返回此对象的一个副本（不是同一个对象了）  
    public synchronized Object clone() {  
        try {  
            @SuppressWarnings("unchecked")  
                Vector<E> v = (Vector<E>) super.clone();  
            v.elementData = Arrays.copyOf(elementData, elementCount);  
            v.modCount = 0;  
            return v;  
        } catch (CloneNotSupportedException e) {  
            // this shouldn't happen, since we are Cloneable  
            throw new InternalError();  
        }  
    }  
      
    //得到数组的表现形式  
    public synchronized Object[] toArray() {  
        return Arrays.copyOf(elementData, elementCount);  
    }  
      
    //返回一个数组，包含此向量中以恰当顺序存放的所有元素；返回数组的运行时类型为指定数组的类型。  
    @SuppressWarnings("unchecked")  
    public synchronized <T> T[] toArray(T[] a) {  
        if (a.length < elementCount)  
            return (T[]) Arrays.copyOf(elementData, elementCount, a.getClass());  
  
        System.arraycopy(elementData, 0, a, 0, elementCount);  
  
        if (a.length > elementCount)  
            a[elementCount] = null;  
  
        return a;  
    }  
      
    @SuppressWarnings("unchecked")  
    E elementData(int index) {  
        return (E) elementData[index];  
    }  
      
    //直接获取  
    public synchronized E get(int index) {  
        if (index >= elementCount)  
            throw new ArrayIndexOutOfBoundsException(index);  
  
        return elementData(index);  
    }  
      
    //直接设置  
    public synchronized E set(int index, E element) {  
        if (index >= elementCount)  
            throw new ArrayIndexOutOfBoundsException(index);  
  
        E oldValue = elementData(index);  
        elementData[index] = element;  
        return oldValue;  
    }  
      
    //直接增加  
    public synchronized boolean add(E e) {  
        modCount++;  
        ensureCapacityHelper(elementCount + 1);  
        elementData[elementCount++] = e;  
        return true;  
    }  
      
    //移除第一个匹配项  
    public boolean remove(Object o) {  
        return removeElement(o);  
    }  
      
    public void add(int index, E element) {  
        insertElementAt(element, index);  
    }  
      
      
    public synchronized E remove(int index) {  
        modCount++;  
        if (index >= elementCount)  
            throw new ArrayIndexOutOfBoundsException(index);  
        //保存旧的对象  
        E oldValue = elementData(index);  
  
        //将后面的对象往前移动  
        int numMoved = elementCount - index - 1;  
        if (numMoved > 0)  
            System.arraycopy(elementData, index+1, elementData, index,  
                             numMoved);  
        elementData[--elementCount] = null; // Let gc do its work  
  
        return oldValue;  
    }  
      
      
    public void clear() {  
        removeAllElements();  
    }  
      
      
    public synchronized boolean containsAll(Collection<?> c) {  
        return super.containsAll(c);  
    }  
      
    //将指定 Collection 中的所有元素添加到此向量的末尾，按照指定 collection 的迭代器所返回的顺序添加这些元素。  
    public synchronized boolean addAll(Collection<? extends E> c) {  
        modCount++;  
        Object[] a = c.toArray();  
        int numNew = a.length;  
        ensureCapacityHelper(elementCount + numNew);  
          
        System.arraycopy(a, 0, elementData, elementCount, numNew);  
        elementCount += numNew;  
        return numNew != 0;  
    }  
      
    public synchronized boolean removeAll(Collection<?> c) {  
        return super.removeAll(c);  
    }  
      
    public synchronized boolean retainAll(Collection<?> c) {  
        return super.retainAll(c);  
    }  
      
    //在指定位置将指定 Collection 中的所有元素插入到此向量中。  
    public synchronized boolean addAll(int index, Collection<? extends E> c) {  
        modCount++;  
        if (index < 0 || index > elementCount)  
            throw new ArrayIndexOutOfBoundsException(index);  
  
        Object[] a = c.toArray();  
        int numNew = a.length;  
        //首先扩大容量  
        ensureCapacityHelper(elementCount + numNew);  
  
        int numMoved = elementCount - index;  
        if (numMoved > 0)  
            System.arraycopy(elementData, index, elementData, index + numNew,  
                             numMoved);  
  
        System.arraycopy(a, 0, elementData, index, numNew);  
        elementCount += numNew;  
        return numNew != 0;  
    }  
      
    public synchronized boolean equals(Object o) {  
        return super.equals(o);  
    }  
      
    public synchronized int hashCode() {  
        return super.hashCode();  
    }  
      
    public synchronized String toString() {  
        return super.toString();  
    }  
      
    public synchronized List<E> subList(int fromIndex, int toIndex) {  
        return Collections.synchronizedList(super.subList(fromIndex, toIndex),this);  
    }  
      
    //从此 List 中移除其索引位于 fromIndex（包括）与 toIndex（不包括）之间的所有元素。  
    protected synchronized void removeRange(int fromIndex, int toIndex) {  
        modCount++;  
        int numMoved = elementCount - toIndex;  
        //现将后面的对象移到前面来  
        System.arraycopy(elementData, toIndex, elementData, fromIndex,  
                         numMoved);  
  
        // Let gc do its work  
        int newElementCount = elementCount - (toIndex-fromIndex);  
        while (elementCount != newElementCount)  
            elementData[--elementCount] = null;  
    }  
      
      
    private void writeObject(java.io.ObjectOutputStream s)  
            throws java.io.IOException {  
        final java.io.ObjectOutputStream.PutField fields = s.putFields();  
        final Object[] data;  
        synchronized (this) {  
            fields.put("capacityIncrement", capacityIncrement);  
            fields.put("elementCount", elementCount);  
            data = elementData.clone();  
        }  
        fields.put("elementData", data);  
        s.writeFields();  
    }  
      
    public synchronized ListIterator<E> listIterator(int index) {  
        if (index < 0 || index > elementCount)  
            throw new IndexOutOfBoundsException("Index: "+index);  
        return new ListItr(index);//ListItr类在后面  
    }  
      
    public synchronized ListIterator<E> listIterator() {  
        return new ListItr(0);  
    }  
      
    public synchronized Iterator<E> iterator() {  
        return new Itr();//在后面  
    }  
      
    //将迭代器类定义到Vector类的里面,这样迭代器就可以访问vector类的内部变量  
    private class Itr implements Iterator<E> {  
        int cursor;       // index of next element to return  
        int lastRet = -1; // index of last element returned; -1 if no such  
        //用于检查线程是否同步，如果线程不同步，它们两个的值不一样  
        int expectedModCount = modCount;  
  
        public boolean hasNext() {  
            // Racy but within spec, since modifications are checked  
            // within or after synchronization in next/previous  
            return cursor != elementCount;  
        }  
  
        public E next() {  
            synchronized (Vector.this) {  
            //检查线程安全  
                checkForComodification();  
                int i = cursor;  
                if (i >= elementCount)  
                    throw new NoSuchElementException();  
                    //cursor保存下次要访问的位置  
                cursor = i + 1;  
                //将最后依次访问的地址赋给lastRet(用于恢复)  
                return elementData(lastRet = i);  
            }  
        }  
  
        public void remove() {  
            if (lastRet == -1)  
                throw new IllegalStateException();  
            synchronized (Vector.this) {  
                checkForComodification();  
                //实质是调用vector自己的remove方法  
                Vector.this.remove(lastRet);  
                expectedModCount = modCount;  
            }  
            cursor = lastRet;  
            lastRet = -1;  
        }  
  
        final void checkForComodification() {  
            if (modCount != expectedModCount)  
                throw new ConcurrentModificationException();  
        }  
    }  
  
    //ListItr和Itr很像，基本上都是调用vector的方法      
    final class ListItr extends Itr implements ListIterator<E> {  
        ListItr(int index) {  
            super();  
            cursor = index;  
        }  
  
        public boolean hasPrevious() {  
            return cursor != 0;//第二个元素之后的元素都有previous  
        }  
  
        public int nextIndex() {  
            return cursor;  
        }  
  
        public int previousIndex() {  
            return cursor - 1;  
        }  
  
        public E previous() {  
            synchronized (Vector.this) {  
                checkForComodification();  
                int i = cursor - 1;  
                if (i < 0)  
                    throw new NoSuchElementException();  
                cursor = i;  
                return elementData(lastRet = i);  
            }  
        }  
  
        public void set(E e) {  
            if (lastRet == -1)  
                throw new IllegalStateException();  
            synchronized (Vector.this) {  
                checkForComodification();  
                Vector.this.set(lastRet, e);  
            }  
        }  
  
        public void add(E e) {  
            int i = cursor;  
            synchronized (Vector.this) {  
                checkForComodification();  
                Vector.this.add(i, e);  
                expectedModCount = modCount;  
            }  
            cursor = i + 1;  
            lastRet = -1;  
        }  
    }  
      

// https://docs.oracle.com/javase/8/docs/api/java/util/List.html

import java.util.*;
import java.lang.reflect.*;
import java.util.ArrayList;

public class SkipList<E> implements List<E>
{
    private static final int MAX_LEVELS = 30;

    private int count;    // list size
    private ArrayList<Node<E>> heads;

    /* the list constructor - starts with an empty list */
    public SkipList() {
        count = 0;
        heads = new ArrayList<Node<E>>(MAX_LEVELS);
        // initialize with null since ArrayLists start empty
        for (int i = 0; i < MAX_LEVELS; i++) {
            heads.add(i,null);
        }
    }

    // Group 1

    // Written as a group with Dr. Albert
    public boolean add(E e) {
        //Cast e to Comparable to use the CompareTo method
        @SuppressWarnings("unchecked")
        Comparable<E> ce = (Comparable<E>)e;

        Node<E> newNode = new Node<E>(e);
        int newNodeLevels = newNode.levels();

        // filling addPath with all the nodes to potentially be updated
        ArrayList<Node<E>> addPath = new ArrayList<Node<E>>(MAX_LEVELS);
        for (int i = 0; i < MAX_LEVELS; i++) {
            addPath.add(i,null);
        }

        Node<E> finger = null; // which means starting at the head
        for (int lvl = MAX_LEVELS - 1; lvl >= 0; lvl--) {
            if (finger == null) { // when finger is at the head
                if (heads.get(lvl) == null || ce.compareTo(heads.get(lvl).value()) < 0) {
                    addPath.set(lvl, null); // we'll have to update the head
                } else { // traveling on the level
                    finger = heads.get(lvl);
                    while ( (finger.next(lvl) != null)
                        && ce.compareTo(finger.next(lvl).value()) > 0 ) {
                        finger = finger.next(lvl);
                    }
                    addPath.set(lvl, finger);
                }
            } else { // finger is at a node
                while ( (finger.next(lvl) != null)
                    && ce.compareTo(finger.next(lvl).value()) > 0 ) {
                    finger = finger.next(lvl);
                }
                addPath.set(lvl,finger);
            }
        }

        // insert the new node in the skiplist
        for (int lvl = 0; lvl < newNodeLevels; lvl++) {
            if (addPath.get(lvl) == null) { // flag for the heads pointers
                newNode.setNext(lvl,heads.get(lvl));
                heads.set(lvl,newNode);
            } else { // affects a node
                newNode.setNext(lvl,addPath.get(lvl).next(lvl));
                addPath.get(lvl).setNext(lvl, newNode);
            }
        }

        count++;
        return true;
    }

    // initial version written by Dr. Albert
    public boolean testAdd(boolean verbose)  {
        //creates a new SkipList called testList
        List<Integer> testList = new SkipList<Integer>();

        testList.add(7);
        testList.add(4);
        testList.add(10);
        testList.add(7);

        //System.out.println(testList.get(0));
        //System.out.println(testList.get(1));
        //System.out.println(testList.get(2));
        //System.out.println(testList.get(3));

        return (
            (testList.get(0) == 4) &&
            (testList.get(1) == 7) &&
            (testList.get(2) == 7) &&
            (testList.get(3) == 10) &&
            (testList.size() == 4)
            );

        //return compareList.equals(testList);
    }


    public boolean addAll(Collection c)
    {
        return true;
    }

    public int indexOf(Object o)
    {
        int index = 0;
        return index;
    }

    public int lastIndexOf(Object o)
    {
        int index = 0;
        return index;
    }

    // Group 2
    public boolean contains(Object o)
    {
        return true;
    }

    public boolean containsAll(Collection c)
    {
        return true;
    }

    public boolean equals(Object o)
    {
        return true;
    }


    public List<E> subList(int fromIndex, int toIndex)
    {
        throw new IndexOutOfBoundsException();
    }

    // Group 3
    public boolean isEmpty()
    {
        return true;
    }

    public int size()
    {
        return this.count;
    }

    public void clear()
    {

    }

    // currently a bad, O(n) get function by Dr. Albert. Fix to be O(log n)
    public E get(int index) {
        //sets pointer equal to head (start of the collection)
        Node<E> pointer = heads.get(0);
        /* if the requested index is less than zero, or if the requested index is
         * greater than the size of collection an exception is thrown and the ouput prints
         * "chosen index is out of bounds"*/
        if ( index < 0 || index >= this.count ) {
            throw new IndexOutOfBoundsException ("chosen index is out of bounds");
        } else {
            //uses a for loop to move the pointer to the desired index in the location
            for (int i = 0; i < index; i++){
                pointer = pointer.next(0);
            }
            /*return the value of the pointer after it has been moved to the derired index in
             *the collection*/
            return pointer.value();
        }
    }

    public E getQuantile(double quantile) // e.g. 0 = minimum, 0.5 = median, 1 = max
    {
        throw new IndexOutOfBoundsException();
    }

    // Group 4
    public Iterator<E> iterator()
    {
        throw new IndexOutOfBoundsException();
    }

    public ListIterator<E> listIterator()
    {
        throw new IndexOutOfBoundsException();
    }

    public ListIterator<E> listIterator(int index)
    {
        throw new IndexOutOfBoundsException();
    }

    // Group 5
    public E remove(int index)
    {
        throw new IndexOutOfBoundsException();
    }

    public boolean remove(Object o)
    {
        return true;
    }

    public boolean removeAll(Collection c)
    {
        return true;
    }

    public boolean retainAll(Collection c)
    {
        return true;
    }

    // Group 6
    public int hashCode()
    {
        int hashCode = 0;
        return hashCode;
    }

    public Object[] toArray()
    {
        Object[] arr = new Object[0];
        return arr;
    }

    public <T> T[] toArray(T[] a)
    {
        return a;
    }


        //-----------------------------------------------------------------------------------------------
    // functions to get the compiler to agree to implement the list interface
    // these functions don't make sense in a SkipList implementation
    // and they are techincally (optional)

    public void add(int index, E e) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public E set(int index, E e) {
        throw new UnsupportedOperationException();
    }

    //--------------------------------------------------------------------------------------------

    /** superTest runs the test methods of the program and displays if they worked
     * @author: Mark Albert
     *
     * Note: this method uses java reflection to run only the methods
     * with 'test' as the first four letters of the method.
     */
    public static void superTest() {
        Class<?> c;
        Object retobj;

        // setup for running the test methods using reflection
        try {
            c = Class.forName("SkipList");
        } catch (ClassNotFoundException e) {
            return;
        }
        Method m[] = c.getDeclaredMethods();
        SkipList<Integer> myList = new SkipList<Integer>();

        boolean didWork = false;
        Object [] falseObjArray = new Object[1];
        falseObjArray[0] = new Boolean(false);

        System.out.println("\nTESTING THE SKIPLIST...\n");

        // iterate through the methods of SkipList
        for (int i = 0; i < m.length; i++) {
            String methodName = m[i].toString().substring(m[i].toString().indexOf("SkipList.") + 9);
            // are the first 4 letters 'test'?
            if (methodName.indexOf("test") > -1) {
                // then execute the test method
                Class<?> pvec[] = m[i].getParameterTypes();
                try {
                    // if the test method has no parameters
                    if (pvec.length == 0) {
                        retobj = m[i].invoke(myList);
                        // if the test method has one, it's the verbose flag - give it false
                    } else {
                        retobj = m[i].invoke(myList, falseObjArray);
                    }
                } catch (IllegalAccessException e) {
                    System.out.println("IllegalAccessException");
                    return;
                    // this exception happens if the method ran throws an exception during execution
                } catch (InvocationTargetException e) {
                    System.out.println("EXCEPTION THROWN from: " + methodName);
                    retobj = (Object) new Boolean(false);
                }

                didWork = (Boolean) retobj;

                // show if the test method worked or failed
                if (didWork) {
                    System.out.println(" Passed: " + methodName);
                } else {
                    System.out.println("FAILED: " + methodName);
                }
            }
        }
    }

    public static void main(String[] args) {
        superTest();
    }

}  // end SkipList definition

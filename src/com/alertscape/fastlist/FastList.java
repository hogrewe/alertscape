/*
 * Created on Sep 30, 2006
 */
package com.alertscape.fastlist;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author josh
 * @version $Version: $
 */
public class FastList<E> implements List<E>
{
  private List<E> list;
  private Map<E, Integer> objectToIndexMap;

  public boolean add(E e)
  {
    int i = indexOf(e);
    if (i == -1)
    {
      list.add(e);
      objectToIndexMap.put(e, list.size( ));
    }
    else
    {
      list.set(i, e);
    }
    return true;
  }

  public void add(int index, E element)
  {
    add(element);
  }

  public boolean addAll(Collection<? extends E> c)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean addAll(int index, Collection<? extends E> c)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public void clear( )
  {
    // TODO Auto-generated method stub

  }

  public boolean contains(Object o)
  {
    return objectToIndexMap.get(o) != -1;
  }

  public boolean containsAll(Collection<?> c)
  {
    for (Object object : c)
    {
      if (!contains(object))
      {
        return false;
      }
    }
    return true;
  }

  public E get(int index)
  {
    return list.get(index);
  }

  public int indexOf(Object o)
  {
    int index = -1;
    // Check for the object in the map
    Integer fatIndex = objectToIndexMap.get(o);
    if (fatIndex != null)
    {
      index = fatIndex;
    }
    return index;
  }

  public boolean isEmpty( )
  {
    return list.isEmpty( );
  }

  public Iterator<E> iterator( )
  {
    return list.iterator( );
  }

  public int lastIndexOf(Object o)
  {
    return indexOf(o);
  }

  public ListIterator<E> listIterator( )
  {
    return list.listIterator( );
  }

  public ListIterator<E> listIterator(int index)
  {
    return list.listIterator(index);
  }

  public boolean remove(Object o)
  {
    int index = indexOf(o);
    if (index >= 0)
    {
      list.remove(index);
      objectToIndexMap.remove(o);
      return true;
    }
    else
    {
      return false;
    }
  }

  public E remove(int index)
  {
    E e = list.remove(index);
    objectToIndexMap.remove(e);
    return e;
  }

  public boolean removeAll(Collection<?> c)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean retainAll(Collection<?> c)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public E set(int index, E element)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public int size( )
  {
    // TODO Auto-generated method stub
    return 0;
  }

  public List<E> subList(int fromIndex, int toIndex)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public E[] toArray( )
  {
    // TODO Auto-generated method stub
    return null;
  }

  public <T> T[] toArray(T[] a)
  {
    // TODO Auto-generated method stub
    return null;
  }
}

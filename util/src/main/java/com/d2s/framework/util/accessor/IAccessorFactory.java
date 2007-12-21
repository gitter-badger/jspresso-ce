/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.accessor;

/**
 * This interface specify the contract of property accessor factories.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IAccessorFactory {

  /**
   * Creates a new java bean collection property accessor.
   * 
   * @param property
   *            the property to be accessed.
   * @param beanClass
   *            the java bean class.
   * @param elementClass
   *            the collection element class.
   * @return the collection property accessor.
   */
  ICollectionAccessor createCollectionPropertyAccessor(String property,
      Class<?> beanClass, Class<?> elementClass);

  /**
   * Creates a new java bean property accessor .
   * 
   * @param property
   *            the property to be accessed.
   * @param beanClass
   *            the java bean class.
   * @return the property accessor.
   */
  IAccessor createPropertyAccessor(String property, Class<?> beanClass);
}

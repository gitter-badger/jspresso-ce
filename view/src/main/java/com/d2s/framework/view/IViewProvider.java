/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view;

/**
 * A simple interface to implement an indirection on a view.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            The root class of the view peers.
 */
public interface IViewProvider<E> {

  /**
   * Get the referenced view.
   * 
   * @return the referenced view.
   */
  IView<E> getView();
}

/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.flow;

import com.d2s.framework.action.IAction;

/**
 * Action to present a message to the user.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class InfoAction extends AbstractFlowAction {

  /**
   * Constructs a new <code>InfoAction</code> instance.
   */
  protected InfoAction() {
    super(OK_OPTION, null, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IAction getNextAction(@SuppressWarnings("unused")
  String selectedOption) {
    return getNextAction();
  }

}

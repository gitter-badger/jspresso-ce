/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ui.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import org.apache.commons.lang.StringUtils;

import com.d2s.framework.view.swing.components.JActionField;

/**
 * JActionFieldConnector connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JActionFieldConnector extends JComponentConnector<JActionField> {

  /**
   * Constructs a new <code>JActionFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param actionField
   *          the connected JActionField.
   */
  public JActionFieldConnector(String id, JActionField actionField) {
    super(id, actionField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {

    getConnectedJComponent().addTextFieldFocusListener(new FocusAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void focusLost(FocusEvent e) {
        if (!e.isTemporary()) {
          performActionIfNeeded();
        }
      }
    });
  }

  /**
   * Performs the action field action if the action field is not synchronized.
   */
  public void performActionIfNeeded() {
    if (!getConnectedJComponent().isSynchronized()) {
      if (StringUtils.isEmpty(getConnectedJComponent().getActionText())) {
        setConnectorValue(null);
      } else {
        getConnectedJComponent().performAction();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    getConnectedJComponent().setValue(aValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedJComponent().getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedUpdateState() {
    super.protectedUpdateState();
    getConnectedJComponent().setEditable(isWritable());
  }
}

/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.Collection;

import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.util.gate.IGate;
import com.d2s.framework.view.descriptor.ISubViewDescriptor;

/**
 * Default implementation of a sub-view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicSubviewDescriptor extends DefaultIconDescriptor implements
    ISubViewDescriptor {

  private IModelDescriptor  modelDescriptor;
  private Collection<IGate> readabilityGates;
  private boolean           readOnly;
  private Collection<IGate> writabilityGates;

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  public IModelDescriptor getModelDescriptor() {
    return modelDescriptor;
  }

  /**
   * Gets the readabilityGates.
   * 
   * @return the readabilityGates.
   */
  public Collection<IGate> getReadabilityGates() {
    return readabilityGates;
  }

  /**
   * Gets the writabilityGates.
   * 
   * @return the writabilityGates.
   */
  public Collection<IGate> getWritabilityGates() {
    return writabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * Sets the modelDescriptor.
   * 
   * @param modelDescriptor
   *            the modelDescriptor to set.
   */
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }

  /**
   * Sets the readabilityGates.
   * 
   * @param readabilityGates
   *            the readabilityGates to set.
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  /**
   * Sets the readOnly.
   * 
   * @param readOnly
   *            the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * Sets the writabilityGates.
   * 
   * @param writabilityGates
   *            the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }
}

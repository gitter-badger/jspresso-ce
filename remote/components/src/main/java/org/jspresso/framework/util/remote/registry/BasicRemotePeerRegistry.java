/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.remote.registry;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.AbstractReferenceMap;
import org.apache.commons.collections.map.ReferenceMap;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The basic implementation of a remote peer registry. It is stored by a
 * reference map so that it is memory neutral.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicRemotePeerRegistry implements IRemotePeerRegistry {

  private Map<String, String>              automationBackingStore;
  private Map<String, Integer>             automationIndices;
  private Map<String, IRemotePeer>         backingStore;

  private Set<IRemotePeerRegistryListener> rprListeners;

  private boolean                          automationEnabled;

  /**
   * Constructs a new <code>BasicRemotePeerRegistry</code> instance.
   */
  @SuppressWarnings("unchecked")
  public BasicRemotePeerRegistry() {
    backingStore = new RemotePeerReferenceMap(AbstractReferenceMap.WEAK,
        AbstractReferenceMap.WEAK, true);
    automationBackingStore = new ReferenceMap(AbstractReferenceMap.WEAK,
        AbstractReferenceMap.WEAK, true);
    automationIndices = new HashMap<String, Integer>();
    setAutomationEnabled(false);
  }

  /**
   * {@inheritDoc}
   */
  public void clear() {
    backingStore.clear();
    automationBackingStore.clear();
    automationIndices.clear();
  }

  /**
   * {@inheritDoc}
   */
  public IRemotePeer getRegistered(String guid) {
    return backingStore.get(guid);
  }

  /**
   * {@inheritDoc}
   */
  public IRemotePeer getRegisteredForPermId(String permId) {
    if (permId != null) {
      return getRegistered(automationBackingStore.get(permId));
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRegistered(String guid) {
    return backingStore.containsKey(guid);
  }

  /**
   * {@inheritDoc}
   */
  public void register(IRemotePeer remotePeer) {
    if (!backingStore.containsKey(remotePeer.getGuid())) {
      backingStore.put(remotePeer.getGuid(), remotePeer);
    }
    if (remotePeer instanceof IPermIdSource) {
      String permId = ((IPermIdSource) remotePeer).getPermId();
      if (permId != null) {
        automationBackingStore.put(permId, remotePeer.getGuid());
      }
    }
    fireRemotePeerAdded(remotePeer);
  }

  /**
   * {@inheritDoc}
   */
  public String registerPermId(String automationsSeed, String guid) {
    if (automationEnabled) {
      String seed = automationsSeed;
      if (seed == null) {
        seed = "generic";
      }
      String permId = computeNextPermId(seed);
      automationBackingStore.put(permId, guid);
      return permId;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public void unregister(String guid) {
    IRemotePeer remotePeer = backingStore.remove(guid);
    if (remotePeer instanceof IPermIdSource) {
      String permId = ((IPermIdSource) remotePeer).getPermId();
      if (permId != null) {
        automationBackingStore.remove(permId);
      }
    }
    fireRemotePeerRemoved(guid);
  }

  private synchronized String computeNextPermId(String seed) {
    if (seed == null) {
      return null;
    }
    Integer currentIndex = automationIndices.get(seed);
    int idIndex = 0;
    if (currentIndex != null) {
      idIndex = currentIndex.intValue() + 1;
    }
    automationIndices.put(seed, new Integer(idIndex));
    return new StringBuffer(seed).append("#").append(idIndex).toString();
  }

  private class RemotePeerReferenceMap extends ReferenceMap {

    private static final long                     serialVersionUID = 1494465151770293403L;

    private transient ReferenceQueue<IRemotePeer> remotePeerQueue;

    public RemotePeerReferenceMap(int keyType, int valueType,
        boolean purgeValues) {
      super(keyType, valueType, purgeValues);
    }

    @Override
    protected void init() {
      remotePeerQueue = new ReferenceQueue<IRemotePeer>();
      super.init();
    }

    @Override
    public void clear() {
      while (remotePeerQueue.poll() != null) {
        // drain the queue.
      }
      super.clear();
    }

    @Override
    protected void purge() {
      Reference<? extends IRemotePeer> ref = remotePeerQueue.poll();
      while (ref != null) {
        purge(ref);
        ref = remotePeerQueue.poll();
      }
      super.purge();
    }

    @Override
    protected HashEntry createEntry(HashEntry next, int hashCode, Object key,
        Object value) {
      if (value instanceof IRemotePeer) {
        return new RemotePeerReferenceEntry(this, next, hashCode, key, value);
      }
      return super.createEntry(next, hashCode, key, value);
    }

    private class RemotePeerReferenceEntry extends ReferenceEntry {

      public RemotePeerReferenceEntry(RemotePeerReferenceMap parent,
          HashEntry next, int hashCode, Object key, Object value) {
        super(parent, next, hashCode, key, value);
      }

      @Override
      protected Object toReference(int type, Object referent, int hash) {
        if (referent instanceof IRemotePeer) {
          switch (type) {
            case SOFT:
              return new RemotePeerSoftRef(hash, (IRemotePeer) referent,
                  ((RemotePeerReferenceMap) parent).remotePeerQueue);
            case WEAK:
              return new RemotePeerWeakRef(hash, (IRemotePeer) referent,
                  ((RemotePeerReferenceMap) parent).remotePeerQueue);
            default:
              break;
          }
        }
        return super.toReference(type, referent, hash);
      }
    }
  }

  private class RemotePeerSoftRef extends SoftReference<IRemotePeer> implements
      IRemotePeer {

    private int    hash;
    private String guid;

    public RemotePeerSoftRef(int hash, IRemotePeer r,
        ReferenceQueue<IRemotePeer> q) {
      super(r, q);
      this.hash = hash;
      this.guid = r.getGuid();
    }

    @Override
    public int hashCode() {
      return hash;
    }

    public String getGuid() {
      return guid;
    }

    @Override
    public void clear() {
      fireRemotePeerRemoved(guid);
      super.clear();
    }
  }

  private class RemotePeerWeakRef extends WeakReference<IRemotePeer> implements
      IRemotePeer {

    private int    hash;
    private String guid;

    public RemotePeerWeakRef(int hash, IRemotePeer r,
        ReferenceQueue<IRemotePeer> q) {
      super(r, q);
      this.hash = hash;
      this.guid = r.getGuid();
    }

    @Override
    public int hashCode() {
      return hash;
    }

    public String getGuid() {
      return guid;
    }

    @Override
    public void clear() {
      fireRemotePeerRemoved(guid);
      super.clear();
    }
  }

  /**
   * {@inheritDoc}
   */
  public void addRemotePeerRegistryListener(IRemotePeerRegistryListener listener) {
    if (rprListeners == null && listener != null) {
      rprListeners = new LinkedHashSet<IRemotePeerRegistryListener>();
    }
    rprListeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void removeRemotePeerRegistryListener(
      IRemotePeerRegistryListener listener) {
    if (rprListeners == null || listener == null) {
      return;
    }
    rprListeners.remove(listener);
  }

  /**
   * Notifies the listeners that a remote peer has been added.
   * 
   * @param peer
   *          the added remote peer.
   */
  protected void fireRemotePeerAdded(IRemotePeer peer) {
    if (rprListeners != null) {
      for (IRemotePeerRegistryListener listener : rprListeners) {
        listener.remotePeerAdded(peer);
      }
    }
  }

  /**
   * Notifies the listeners that a remote peer has been removed.
   * 
   * @param guid
   *          the removed remote peer guid.
   */
  protected void fireRemotePeerRemoved(String guid) {
    if (rprListeners != null) {
      for (IRemotePeerRegistryListener listener : rprListeners) {
        listener.remotePeerRemoved(guid);
      }
    }
  }

  /**
   * Sets the automationEnabled.
   * 
   * @param automationEnabled
   *          the automationEnabled to set.
   */
  public void setAutomationEnabled(boolean automationEnabled) {
    this.automationEnabled = automationEnabled;
  }
}

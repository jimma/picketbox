/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.picketbox.core.session;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.picketbox.core.PicketBoxMessages;
import org.picketbox.core.UserContext;
import org.picketbox.core.event.PicketBoxEventManager;
import org.picketbox.core.exceptions.PicketBoxSessionException;
import org.picketbox.core.session.event.SessionExpiredEvent;
import org.picketbox.core.session.event.SessionGetAttributeEvent;
import org.picketbox.core.session.event.SessionInvalidatedEvent;
import org.picketbox.core.session.event.SessionSetAttributeEvent;
import org.picketbox.core.session.event.SessionTouchedEvent;

/**
 * A session that is capable of storing attributes
 *
 * @author anil saldhana
 * @since Jul 16, 2012
 */
public class PicketBoxSession implements Serializable {

    private static final long serialVersionUID = 2149908831443524877L;

    protected ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    protected ConcurrentMap<String, Object> applicationAttributes = new ConcurrentHashMap<String, Object>();

    protected SessionId<? extends Serializable> id;
    protected boolean invalid = false;
    protected UserContext userContext;
    private Date creationDate = new Date();

    private transient PicketBoxEventManager eventManager;

    public PicketBoxSession() {
        this(new DefaultSessionId());
    }

    public PicketBoxSession(UserContext subject, SessionId<? extends Serializable> id) {
        this(id);
        this.userContext = subject;
    }

    /**
     * Usable by {@link PicketBoxSessionManager#create()}
     */
    public PicketBoxSession(SessionId<? extends Serializable> id) {
        this.id = id;
    }

    /**
     * Get the session id
     *
     * @return
     */
    public SessionId<? extends Serializable> getId() {
        return this.id;
    }

    /**
     *
     * @param key
     * @throws PicketBoxSessionException
     */
    public void removeAttribute(String key) throws PicketBoxSessionException {
        checkIfIsInvalid();
        this.attributes.remove(key);
    }

    /**
     * Add an attribute
     *
     * @param key
     * @param val
     * @throws PicketBoxSessionException
     */
    public void setAttribute(final String key, final Object val) throws PicketBoxSessionException {
        checkIfIsInvalid();

        this.attributes.put(key, val);

        this.eventManager.raiseEvent(new SessionSetAttributeEvent(this, key, val));
    }

    /**
     * Get a read only copy of the attributes.
     *
     * @return
     * @throws PicketBoxSessionException
     */
    public Map<String, Object> getAttributes() throws PicketBoxSessionException {
        checkIfIsInvalid();
        return Collections.unmodifiableMap(this.attributes);
    }

    /**
     * Get an attribute
     *
     * @param key
     * @return
     * @throws PicketBoxSessionException
     */
    public Object getAttribute(final String key) throws PicketBoxSessionException {
        checkIfIsInvalid();

        this.eventManager.raiseEvent(new SessionGetAttributeEvent(this, key));

        return this.attributes.get(key);
    }

    /**
     * Add an attribute for applications.
     *
     * Application attributes are for the use by applications and not for users.
     *
     * @param key
     * @param val
     * @throws PicketBoxSessionException
     */
    public void setApplicationStateAttributes(final String key, final Object val) throws PicketBoxSessionException {
        checkIfIsInvalid();

        this.applicationAttributes.put(key, val);
    }

    /**
     * Get a read only copy of the attributes set aside for applications.
     *
     * Application attributes are for the use by applications and not for users.
     *
     * @return
     * @throws PicketBoxSessionException
     */
    public Map<String, Object> getApplicationStateAttributes() throws PicketBoxSessionException {
        checkIfIsInvalid();
        return Collections.unmodifiableMap(this.applicationAttributes);
    }

    /**
     * Get an attribute for applications.
     *
     * Application attributes are for the use by applications and not for users.
     *
     * @param key
     * @return
     * @throws PicketBoxSessionException
     */
    public Object getApplicationStateAttributes(final String key) throws PicketBoxSessionException {
        checkIfIsInvalid();

        return this.applicationAttributes.get(key);
    }

    /**
     * Is the session valid?
     *
     * @return
     */
    public boolean isValid() {
        return !this.invalid;
    }

    /**
     * Invalidate the session and notify the registered {@link PicketBoxSessionListener}.
     *
     * @throws PicketBoxSessionException
     */
    public void invalidate() throws PicketBoxSessionException {
        invalidate(true);
    }

    /**
     * <p>
     * Invalidate the session and notigy the registered {@link PicketBoxSessionListener} only if the specified argument is true.
     * </p>
     *
     * @param raiseEvent
     * @throws PicketBoxSessionException
     */
    public void invalidate(boolean raiseEvent) throws PicketBoxSessionException {
        checkIfIsInvalid();
        if (raiseEvent) {
            this.eventManager.raiseEvent(new SessionInvalidatedEvent(this));
        }
        this.attributes.clear();
        this.invalid = true;
        if (this.userContext != null && this.userContext.isAuthenticated()) {
            this.userContext.invalidate();
        }
    }

    /**
     * Method to indicate that there was a operation that touches the session and thereby extending the session expiry
     */
    public void touch() {
        if (this.eventManager != null) {
            this.eventManager.raiseEvent(new SessionTouchedEvent(this));
        }
    }

    /**
     * Expire the session
     *
     * @throws PicketBoxSessionException
     */
    public void expire() throws PicketBoxSessionException {
        invalidate();
        this.eventManager.raiseEvent(new SessionExpiredEvent(this));
    }

    /**
     * @return the subject
     */
    public UserContext getUserContext() {
        return this.userContext;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * <p>
     * Checks if the session is invalid.
     * </p>
     *
     * @throws PicketBoxSessionException in the case this instance is marked as invalid.
     */
    private void checkIfIsInvalid() throws PicketBoxSessionException {
        if (this.invalid)
            throw PicketBoxMessages.MESSAGES.invalidatedSession();
    }

    protected void setEventManager(PicketBoxEventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public String toString() {
        return "SessionId: " + this.id + " / Creation Date: " + getCreationDate();
    }
}
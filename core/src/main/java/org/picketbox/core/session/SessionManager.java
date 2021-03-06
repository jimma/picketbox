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

import org.picketbox.core.PicketBoxLifecycle;
import org.picketbox.core.UserContext;

/**
 * <p>
 * Session managers are responsible for managing the {@link PicketBoxSession} instances. Session managers usually delegate the
 * storage of sessions to {@link SessionStore} implementations.
 * </p>
 *
 * @author Anil Saldhana
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 *
 * @see SessionStore
 */
public interface SessionManager extends PicketBoxLifecycle {

    /**
     * Construct a session
     *
     * @param authenticatedUserContext
     * @return
     */
    PicketBoxSession create(UserContext authenticatedUserContext);

    /**
     * <p>
     * Retrieve a {@link PicketBoxSession} using its {@link SessionId}.
     * </p>
     *
     * @param id
     * @return
     */
    PicketBoxSession retrieve(SessionId<? extends Serializable> id);

    /**
     * <p>
     * Removes a {@link PicketBoxSession}.
     * </p>
     *
     * @param session
     */
    void remove(PicketBoxSession session);

    /**
     * <p>
     * Updates a {@link PicketBoxSession}.
     * </p>
     *
     * @param session
     */
    void update(PicketBoxSession session);

    /**
     * <p>
     * Restores a {@link PicketBoxSession} for the given {@link UserContext}.
     * </p>
     *
     * @param userContext
     * @return
     */
    PicketBoxSession restoreSession(UserContext userContext);
}
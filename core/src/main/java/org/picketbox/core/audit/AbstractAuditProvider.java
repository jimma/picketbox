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

package org.picketbox.core.audit;

import org.picketbox.core.PicketBoxManager;
import org.picketbox.core.audit.event.PostAuditEvent;
import org.picketbox.core.audit.event.PreAuditEvent;

/**
 * Base class for {@link AuditProvider} implementations.
 *
 * @author <a href="mailto:Anil.Saldhana@jboss.org">Anil Saldhana</a>
 * @since Aug 21, 2006
 */
public abstract class AbstractAuditProvider implements AuditProvider {

    private PicketBoxManager picketBoxManager;

    @Override
    public void audit(AuditEvent ae) {
        if (this.picketBoxManager != null) {
            this.picketBoxManager.getEventManager().raiseEvent(new PreAuditEvent(ae));
        }

        doAudit(ae);

        if (this.picketBoxManager != null) {
            this.picketBoxManager.getEventManager().raiseEvent(new PostAuditEvent(ae));
        }
    }

    protected abstract void doAudit(AuditEvent ae);

    public void setPicketBoxManager(PicketBoxManager picketBoxManager) {
        this.picketBoxManager = picketBoxManager;
    }
}
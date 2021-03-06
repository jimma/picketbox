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

package org.picketbox.core.config;

import java.util.List;

import org.picketbox.core.authentication.AuthenticationMechanism;

/**
 * Defines a configuration for authentication
 *
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 */
public class AuthenticationConfiguration {
    private EventManagerConfiguration eventManager;
    private List<AuthenticationMechanism> mechanisms;
    private ClientCertConfiguration certConfiguration;

    public AuthenticationConfiguration(List<AuthenticationMechanism> mechanisms, EventManagerConfiguration eventManager,
            ClientCertConfiguration certConfiguration) {
        this.eventManager = eventManager;
        this.mechanisms = mechanisms;
        this.certConfiguration = certConfiguration;
    }

    /**
     * Get a list of authentication mechanisms
     *
     * @return
     */
    public List<AuthenticationMechanism> getMechanisms() {
        return this.mechanisms;
    }

    /**
     * Get the Event Manager configuration
     *
     * @return
     */
    public EventManagerConfiguration getEventManager() {
        return this.eventManager;
    }

    public ClientCertConfiguration getCertConfiguration() {
        return this.certConfiguration;
    }
}
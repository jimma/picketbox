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
package org.picketbox.core.authorization.impl;

import java.util.ArrayList;
import java.util.List;

import org.picketbox.core.authorization.Resource;

/**
 * An implementation of {@link Resource} that encloses a collection of resources
 *
 * @author anil saldhana
 * @since Feb 6, 2013
 */
public class SimpleEnclosingResource extends SimpleResource implements Resource {

    private static final long serialVersionUID = -9038254109218663011L;

    protected List<Resource> resources = new ArrayList<Resource>();

    /**
     * Create a {@link SimpleEnclosingResource}
     *
     * @param name name of the resource
     */
    public SimpleEnclosingResource(String name) {
        super(name);
    }

    /**
     * Add a {@link Resource}
     *
     * @param resource
     * @return
     */
    public SimpleEnclosingResource add(Resource resource) {
        this.resources.add(resource);
        return this;
    }

    /**
     * Add a list of {@link Resource}
     *
     * @param resources
     * @return
     */
    public SimpleEnclosingResource addAll(List<Resource> resources) {
        this.resources.addAll(resources);
        return this;
    }
}
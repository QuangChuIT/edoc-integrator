/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.bkav.edoc.service.kernel.io;

import com.bkav.edoc.service.kernel.string.lang.ClassLoaderPool;
import com.bkav.edoc.service.kernel.string.lang.ClassResolverUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamClass;

/**
 * @author Shuyang Zhou
 */
public class ProtectedAnnotatedObjectInputStream
        extends ProtectedObjectInputStream {

    public ProtectedAnnotatedObjectInputStream(InputStream inputStream)
            throws IOException {

        super(inputStream);
    }

    @Override
    protected Class<?> doResolveClass(ObjectStreamClass objectStreamClass)
            throws ClassNotFoundException, IOException {

        String contextName = readUTF();

        ClassLoader classLoader = ClassLoaderPool.getClassLoader(contextName);

        String className = objectStreamClass.getName();

        return ClassResolverUtil.resolve(className, classLoader);
    }

}
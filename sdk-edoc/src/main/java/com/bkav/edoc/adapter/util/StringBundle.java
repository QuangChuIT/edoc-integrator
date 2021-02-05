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

package com.bkav.edoc.adapter.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

public class StringBundle implements Serializable {

    public static String concat(Object... objects) {
        String[] strings = new String[objects.length];

        for (int i = 0; i < objects.length; i++) {
            strings[i] = String.valueOf(objects[i]);
        }

        return _toString(strings, strings.length);
    }

    public static String concat(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null) {
                strings[i] = StringPool.NULL;
            }
        }

        return _toString(strings, strings.length);
    }

    public StringBundle() {
        _array = new String[_DEFAULT_ARRAY_CAPACITY];
    }

    public StringBundle(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = _DEFAULT_ARRAY_CAPACITY;
        }

        _array = new String[initialCapacity];
    }

    public StringBundle(String s) {
        _array = new String[_DEFAULT_ARRAY_CAPACITY];

        _array[0] = s;

        _arrayIndex = 1;
    }

    public StringBundle(String[] stringArray) {
        this(stringArray, 0);
    }

    public StringBundle(String[] stringArray, int extraSpace) {
        _array = new String[stringArray.length + extraSpace];

        for (String s : stringArray) {
            if ((s != null) && (s.length() > 0)) {
                _array[_arrayIndex++] = s;
            }
        }
    }

    public StringBundle append(boolean b) {
        if (b) {
            return append(StringPool.TRUE);
        }

        return append(StringPool.FALSE);
    }

    public StringBundle append(char c) {
        return append(String.valueOf(c));
    }

    public StringBundle append(char[] chars) {
        if (chars == null) {
            return append("null");
        }

        return append(new String(chars));
    }

    public StringBundle append(double d) {
        return append(String.valueOf(d));
    }

    public StringBundle append(float f) {
        return append(String.valueOf(f));
    }

    public StringBundle append(int i) {
        return append(String.valueOf(i));
    }

    public StringBundle append(long l) {
        return append(String.valueOf(l));
    }

    public StringBundle append(Object obj) {
        return append(String.valueOf(obj));
    }

    public StringBundle append(String s) {
        if (s == null) {
            s = StringPool.NULL;
        }

        if (s.length() == 0) {
            return this;
        }

        if (_arrayIndex >= _array.length) {
            expandCapacity(_array.length * 2);
        }

        _array[_arrayIndex++] = s;

        return this;
    }

    public StringBundle append(String[] stringArray) {
        if ((stringArray == null) || (stringArray.length == 0)) {
            return this;
        }

        if ((_array.length - _arrayIndex) < stringArray.length) {
            expandCapacity((_array.length + stringArray.length) * 2);
        }

        for (String s : stringArray) {
            if ((s != null) && (s.length() > 0)) {
                _array[_arrayIndex++] = s;
            }
        }

        return this;
    }

    public StringBundle append(StringBundle sb) {
        if ((sb == null) || (sb._arrayIndex == 0)) {
            return this;
        }

        if ((_array.length - _arrayIndex) < sb._arrayIndex) {
            expandCapacity((_array.length + sb._arrayIndex) * 2);
        }

        System.arraycopy(sb._array, 0, _array, _arrayIndex, sb._arrayIndex);

        _arrayIndex += sb._arrayIndex;

        return this;
    }

    public int capacity() {
        return _array.length;
    }

    public String[] getStrings() {
        return _array;
    }

    public int index() {
        return _arrayIndex;
    }

    public int length() {
        int length = 0;

        for (int i = 0; i < _arrayIndex; i++) {
            length += _array[i].length();
        }

        return length;
    }

    public void setIndex(int newIndex) {
        if (newIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(newIndex);
        }

        if (newIndex > _array.length) {
            String[] newArray = new String[newIndex];

            System.arraycopy(_array, 0, newArray, 0, _arrayIndex);

            _array = newArray;
        }

        if (_arrayIndex < newIndex) {
            for (int i = _arrayIndex; i < newIndex; i++) {
                _array[i] = StringPool.BLANK;
            }
        }

        if (_arrayIndex > newIndex) {
            for (int i = newIndex; i < _arrayIndex; i++) {
                _array[i] = null;
            }
        }

        _arrayIndex = newIndex;
    }

    public void setStringAt(String s, int index) {
        if ((index < 0) || (index >= _arrayIndex)) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        _array[index] = s;
    }

    public String stringAt(int index) {
        if ((index < 0) || (index >= _arrayIndex)) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        return _array[index];
    }

    @Override
    public String toString() {
        return _toString(_array, _arrayIndex);
    }

    public void writeTo(Writer writer) throws IOException {
        for (int i = 0; i < _arrayIndex; i++) {
            writer.write(_array[i]);
        }
    }

    protected void expandCapacity(int newCapacity) {
        String[] newArray = new String[newCapacity];

        System.arraycopy(_array, 0, newArray, 0, _arrayIndex);

        _array = newArray;
    }

    private static String _toString(String[] array, int arrayIndex) {
        if (arrayIndex == 0) {
            return StringPool.BLANK;
        }

        if (arrayIndex == 1) {
            return array[0];
        }

        if (arrayIndex == 2) {
            return array[0].concat(array[1]);
        }

        if (arrayIndex == 3) {
            if (array[0].length() < array[2].length()) {
                return array[0].concat(
                        array[1]
                ).concat(
                        array[2]
                );
            }

            return array[0].concat(array[1].concat(array[2]));
        }

        int length = 0;

        for (int i = 0; i < arrayIndex; i++) {
            length += array[i].length();
        }

        StringBuilder sb = null;

        if (length > _THREAD_LOCAL_BUFFER_LIMIT) {
            Reference<StringBuilder> reference =
                    _stringBuilderThreadLocal.get();

            if (reference != null) {
                sb = reference.get();
            }

            if (sb == null) {
                sb = new StringBuilder(length);

                _stringBuilderThreadLocal.set(new SoftReference<>(sb));
            } else if (sb.capacity() < length) {
                sb.setLength(length);
            }

            sb.setLength(0);
        } else {
            sb = new StringBuilder(length);
        }

        for (int i = 0; i < arrayIndex; i++) {
            sb.append(array[i]);
        }

        return sb.toString();
    }

    private static final int _DEFAULT_ARRAY_CAPACITY = 16;

    private static final int _THREAD_LOCAL_BUFFER_LIMIT;

    private static final ThreadLocal<Reference<StringBuilder>>
            _stringBuilderThreadLocal;
    private static final long serialVersionUID = 1L;

    static {
        int threadLocalBufferLimit = Integer.getInteger(
                StringBundle.class.getName() + ".threadlocal.buffer.limit",
                Integer.MAX_VALUE);

        if ((threadLocalBufferLimit > 0) &&
                (threadLocalBufferLimit < Integer.MAX_VALUE)) {

            _THREAD_LOCAL_BUFFER_LIMIT = threadLocalBufferLimit;

            _stringBuilderThreadLocal = null;
        } else {
            _THREAD_LOCAL_BUFFER_LIMIT = Integer.MAX_VALUE;

            _stringBuilderThreadLocal = null;
        }
    }

    private String[] _array;
    private int _arrayIndex;

}
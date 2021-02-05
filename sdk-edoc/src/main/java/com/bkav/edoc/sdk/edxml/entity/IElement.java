package com.bkav.edoc.sdk.edxml.entity;

import org.jdom2.Element;

public interface IElement<T> {
    T getData(Element element);
}

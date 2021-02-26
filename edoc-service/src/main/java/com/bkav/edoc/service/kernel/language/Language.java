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

package com.bkav.edoc.service.kernel.language;


import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 */
public interface Language {

    public String format(
            HttpServletRequest httpServletRequest, String pattern,
            LanguageWrapper argument);

    public String format(
            HttpServletRequest httpServletRequest, String pattern,
            LanguageWrapper argument, boolean translateArguments);

    public String format(
            HttpServletRequest httpServletRequest, String pattern,
            LanguageWrapper[] arguments);

    public String format(
            HttpServletRequest httpServletRequest, String pattern,
            LanguageWrapper[] arguments, boolean translateArguments);

    public String format(
            HttpServletRequest httpServletRequest, String pattern, Object argument);

    public String format(
            HttpServletRequest httpServletRequest, String pattern, Object argument,
            boolean translateArguments);

    public String format(
            HttpServletRequest httpServletRequest, String pattern,
            Object[] arguments);

    public String format(
            HttpServletRequest httpServletRequest, String pattern,
            Object[] arguments, boolean translateArguments);

    public String format(Locale locale, String pattern, List<Object> arguments);

    public String format(Locale locale, String pattern, Object argument);

    public String format(
            Locale locale, String pattern, Object argument,
            boolean translateArguments);

    public String format(Locale locale, String pattern, Object[] arguments);

    public String format(
            Locale locale, String pattern, Object[] arguments,
            boolean translateArguments);

    public String format(
            ResourceBundle resourceBundle, String pattern, Object argument);

    public String format(
            ResourceBundle resourceBundle, String pattern, Object argument,
            boolean translateArguments);

    public String format(
            ResourceBundle resourceBundle, String pattern, Object[] arguments);

    public String format(
            ResourceBundle resourceBundle, String pattern, Object[] arguments,
            boolean translateArguments);

    public String formatStorageSize(double size, Locale locale);

    public String get(
            HttpServletRequest httpServletRequest, ResourceBundle resourceBundle,
            String key);

    public String get(
            HttpServletRequest httpServletRequest, ResourceBundle resourceBundle,
            String key, String defaultValue);

    public String get(HttpServletRequest httpServletRequest, String key);

    public String get(
            HttpServletRequest httpServletRequest, String key, String defaultValue);

    public String get(Locale locale, String key);

    public String get(Locale locale, String key, String defaultValue);

    public String get(ResourceBundle resourceBundle, String key);

    public String get(
            ResourceBundle resourceBundle, String key, String defaultValue);

    public Set<Locale> getAvailableLocales();

    public Set<Locale> getAvailableLocales(long groupId);

    public String getBCP47LanguageId(HttpServletRequest httpServletRequest);

    public String getBCP47LanguageId(Locale locale);

    public Set<Locale> getCompanyAvailableLocales(long companyId);

    public String getLanguageId(HttpServletRequest httpServletRequest);

    public String getLanguageId(Locale locale);

    public default long getLastModified() {
        return System.currentTimeMillis();
    }

    public Locale getLocale(long groupId, String languageCode);

    public Locale getLocale(String languageCode);

    public Set<Locale> getSupportedLocales();

    public String getTimeDescription(
            HttpServletRequest httpServletRequest, long milliseconds);

    public String getTimeDescription(
            HttpServletRequest httpServletRequest, long milliseconds,
            boolean approximate);

    public String getTimeDescription(
            HttpServletRequest httpServletRequest, Long milliseconds);

    public String getTimeDescription(Locale locale, long milliseconds);

    public String getTimeDescription(
            Locale locale, long milliseconds, boolean approximate);

    public String getTimeDescription(Locale locale, Long milliseconds);

    public void init();

    public boolean isAvailableLanguageCode(String languageCode);

    public boolean isAvailableLocale(Locale locale);

    public boolean isAvailableLocale(long groupId, Locale locale);

    public boolean isAvailableLocale(long groupId, String languageId);

    public boolean isAvailableLocale(String languageId);

    public boolean isBetaLocale(Locale locale);

    public boolean isDuplicateLanguageCode(String languageCode);

    public boolean isInheritLocales(long groupId) throws Exception;

    public boolean isSameLanguage(Locale locale1, Locale locale2);

    public String process(
            Supplier<ResourceBundle> resourceBundleSupplier, Locale locale,
            String content);

    public void resetAvailableGroupLocales(long groupId);

    public void resetAvailableLocales(long companyId);

    public void updateCookie(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse, Locale locale);

}
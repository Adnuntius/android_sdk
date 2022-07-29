/*
 * Copyright (c) 2022 Adnuntius AS.  All rights reserved.
 */
package com.adnuntius.android.sdk.data;

import com.adnuntius.android.sdk.StringUtils;
import com.adnuntius.android.sdk.http.HttpUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Page extends DataRequest {
    private List<String> keywords;
    private List<String> categories;
    private String domainName;

    public Page() {
        super(DataTarget.page);
    }

    /**
     * Optional constructor to automatically extract the domain name and categories from the url
     * @param pageUrl
     */
    public Page(final String pageUrl) {
        super(DataTarget.page);

        final URL url = newUrl(pageUrl);
        domainName = url.getHost();
        final String path = url.getPath();
        if (path.length() > 0) {
            final String categories[] = path.split("/");
            for (String category : categories) {
                // skip the first category, as the path begins with /
                if (!category.isEmpty()) {
                    addCategories(category);
                }
            }
        }
    }

    private URL newUrl(final String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param keywords
     */
    public void addKeywords(final String ... keywords) {
        StringUtils.validateNotBlank(keywords);
        if (this.keywords == null) {
            this.keywords = new ArrayList<>();
        }
        for (String keyword : keywords) {
            StringUtils.validateNotBlank(keyword);
            this.keywords.add(keyword);
        }
    }

    /**
     * Categories, additional to those derived if you used the page url constructor
     *
     * @param categories
     */
    public void addCategories(final String ... categories) {
        if (this.categories == null) {
            this.categories = new ArrayList<>(categories.length);
        }
        for (String category : categories) {
            StringUtils.validateNotBlank(category);
            this.categories.add(category);
        }
    }

    /**
     * The domain name of the page, if not set by passing the page URL
     * as a constructor argument.
     *
     * @param domainName
     */
    public void setDomainName(final String domainName) {
        StringUtils.validateNotBlank(domainName);
        this.domainName = domainName;
    }

    public List<String> getCategories() {
        if (this.categories == null) {
            return Collections.emptyList();
        }
        return categories;
    }

    public String getDomainName() {
        return domainName;
    }

    public List<String> getKeywords() {
        if (this.keywords == null) {
            return Collections.emptyList();
        }
        return keywords;
    }
}

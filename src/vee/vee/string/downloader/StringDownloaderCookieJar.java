/**
 * Author		:	VenomVendor
 * Dated		:	7 Nov, 2013 - 2:45:48 AM
 * Project		:	String-Downloader
 * Contact		:	info@VenomVendor.com
 * URL			:	https://www.google.com/search?q=VenomVendor
 * Copyright(c)	:	2013-Present, VenomVendor.
 * License		:	DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE - Version 2.
 **/

package vee.vee.string.downloader;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class StringDownloaderCookieJar {

    BasicCookieStore cookieStore = new BasicCookieStore();
    HttpContext localContext = new BasicHttpContext();

    public BasicCookieStore getCookieStore() {
        // printCookies();
        return cookieStore;
    }

    public void setCookieStore(BasicCookieStore cookieStore) {
        this.cookieStore = cookieStore;
        // Set & Print
        // printCookies();
    }

    public HttpContext getLocalContext() {
        // printLocalContext();
        return localContext;
    }

    public void setLocalContext(HttpContext localContext) {
        this.localContext = localContext;
        // Set & Print
        // printLocalContext();
    }

    @SuppressWarnings("unused")
    private void printCookies() {

        System.out.println("COOKIE SIZE SET: " + cookieStore.getCookies().size());
        for (int i = 0; i < cookieStore.getCookies().size(); i++) {

            System.out.println(
                    "COOKIES " + i + " :: " + cookieStore.getCookies().get(i));

        }

    }

    @SuppressWarnings("unused")
    private void printLocalContext() {

        System.out.println(" ::::::::: " + localContext.hashCode());
        System.out.println(" ::::::::: " + localContext.toString());

    }

}

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

/**
 * @author VenomVendor <br>
 *         <b>Message: </b>Listens the status of data being downloaded
 */
public interface StringDownloaderListener {

    /**
     * <i>Triggered when data get's downloaded successfully.</i><br>
     * <b>Caution! </b> Remember, you are still in background thread, <b>DO NOT
     * update UI here.</b> But, you can parse your heavy/light JSON/XML.<br>
     * If you have multiple URLs, then <i>"StringDownloader"</i> waits until
     * your process gets done and proceeds to download form next URL. <br>
     * <br>
     * <b>Result : </b> <i>Required data from Server.</i><br>
     * <b>URL : </b> <i>The data fetched from URL.</i><br>
     * <b>URL's Position : </b> <i>Nth of URL, starts from 0.</i><br>
     * <b>Cookie : </b> <i>Cookie, if available, else null.</i> Use null check
     * while using cookies. <br>
     * <br>
     */
    String stringDownloadedSuccessfully(String result, String url,
            int position, BasicCookieStore cookie);

    /**
     * <i>Triggered if data fails to download, mostly due to bad/unavailable<b>
     * Internet Connection</b></i><br>
     * 
     * @return error message from <b>catch</b><br>
     *         <b>URL : </b> <i>The data failed from URL.</i><br>
     *         <b>URL's Position : </b> <i>Nth of URL, starts from 0.</i><br>
     *         <b>Cookie : </b> <i>Cookie, if available, else null.</i> Use null
     *         check while using cookies. <br>
     * <br>
     */
    void stringFetchingFailed(String reasonForFailure, String url,
            int position, BasicCookieStore cookieStore);

    /**
     * <i>Triggered if any error in server side.</i><br>
     * 
     * @return error<br>
     *         <b>Status Code : </b> <i>HTTP status code.</i><br>
     *         <b>Status Description : </b> <i>HTTP status code's equivalent
     *         description.</i><br>
     *         <b>URL : </b> <i>The data failed from URL.</i><br>
     *         <b>URL's Position : </b> <i>Nth of URL, starts from 0.</i><br>
     *         <b>Cookie : </b> <i>Cookie, if available, else null.</i> Use null
     *         check while using cookies.<br>
     * <br>
     */
    void errorInServerSide(int statusCode, String statusDescription,
            String url, int position, BasicCookieStore cookieStore);

    /**
     * <br>
     * <i>Triggered in <b>PostExecute</b>. Start Updating your UI here.</i><br>
     * <br>
     * <b>Returns</b><br>
     * <i>true</i> - if data from all URL(s) is downloaded successfully.<br>
     * <i>false</i> - if data from any URL fails to download.<br>
     * <b><i>Result</i></b> - has last fetched result, better when needed to
     * parse and update UI immediately.<br>
     * <br>
     * 
     * @see dataDownloadFailed
     */
    void isAllStringsDownloadedSuccessfully(boolean status,
            String lastFetchedResult);

}

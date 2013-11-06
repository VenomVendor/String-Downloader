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

public class StringDownloaderSetterGetter {

    /**
     * <b>true</b> : if all URL(s) downloaded successfully.<br>
     * <b>false</b> : if any URL(s) fails to download.
     */
    boolean downloadStatus = false;

    protected final boolean isDownloadedSuccessfully() {
        return downloadStatus;
    }

    protected final void setDownloadStatus(boolean downloadStatus) {
        this.downloadStatus = downloadStatus;
    }
}

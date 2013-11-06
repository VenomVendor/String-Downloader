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

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URL;

/**
 * <b>Author :</b> VenomVendor<br>
 * <b>Details :</b> Downloads data(String) from any given URL(s).<br>
 * All data are downloaded in AsyncTask&lt;String, Integer, String&gt;
 */
public class StringDownloader extends AsyncTask<String, Integer, String> {

    /** The StringDownloaderListener. */
    StringDownloaderListener strgDwnLsnr;

    /** The activity from which StringDownloader is Executed. */
    Activity activity;

    /** Print in log???. */
    boolean debug;

    /** The dialog. */
    ProgressDialog dialog;

    /** The Apache HttpGet. */
    HttpGet httpGet;

    /** The Apache response. */
    HttpResponse response;

    /**
     * Has the data from server.
     */
    String result;

    /**
     * Reason, if download fails.
     */
    String reasonForFailure;

    /** Tag, in log. */
    String tag = "VenomVendor";

    /** StringDownloaderSnippets. */
    StringDownloaderSnippets mSnippets = new StringDownloaderSnippets();

    /** The m setter getter. */
    StringDownloaderSetterGetter mSetterGetter = new StringDownloaderSetterGetter();

    /** Apache HTTP Client. */
    DefaultHttpClient httpClient = new DefaultHttpClient();

    /** Handles Cookies. */
    StringDownloaderCookieJar mCookieJar = new StringDownloaderCookieJar();

    /**
     * <b>Details: </b> Pass <b>Activity</b>, DO NOT pass <b><i>context</i></b>.<br>
     *
     * @param Activity activity
     * @param <b>boolean</b> debug
     */
    public StringDownloader(Activity activity, boolean debug) {
        this.activity = activity;
        this.debug = debug;
        mCookieJar.getLocalContext().setAttribute(ClientContext.COOKIE_STORE,
                mCookieJar.getCookieStore());
    }

    /**
     * Sets the string download listener.
     *
     * @param stringDownloaderListener : Starts download listener
     */
    public void setStringDownloadListener(
            StringDownloaderListener stringDownloaderListener) {
        this.strgDwnLsnr = stringDownloaderListener;

    }

    /**
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (debug) {
            Log.w(tag, "onPreExecute");
        }
        dialog = new ProgressDialog(activity);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        publishProgress(1);
        dialog.show();

    }

    /**
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    @Override
    protected String doInBackground(String... params) {
        if (activity.checkCallingOrSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED)
        {
            mSetterGetter.setDownloadStatus(false);
            publishProgress(1000);
            Log.wtf(tag, StringDownloaderSnippets.noInternetPermission);
            return null;
        }

        if (debug) {
            Log.w(tag, "doInBackground");
            Log.w(tag, "Number of URL(s) :<>: " + params.length);
        }
        String url;

        for (int i = 0; i < params.length; i++) {

            if (debug) {
                Log.v(tag, "Executing");
            }

            url = params[i];

            publishProgress(1);
            if (!dialog.isShowing()) {

                try {
                    dialog = new ProgressDialog(activity);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } catch (Exception e) {
                    if (debug) {
                        Log.wtf(tag, "Dialog Exception" + e.getMessage());
                    }
                    continue;
                }

            }

            result = null;
            try {

                url = new URL(url).toURI().toString();

                if (debug) {
                    Log.w(tag, "Executing URL :<>: " + (i + 1));
                    Log.v(tag, url);
                }

                httpGet = new HttpGet(url);

                response = httpClient.execute(httpGet,
                        mCookieJar.getLocalContext());

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
                        && response.getEntity() != null) {

                    publishProgress(2);

                    HttpEntity httpEntity = response.getEntity();

                    result = EntityUtils.toString(httpEntity);

                    // Do NOT UPDATE UI IN THREAD, Move this to PostExe
                    // strgDwnLsnr.stringDownloadedSuccessfully(result);

                    if (debug) {
                        Log.i(tag, "URL > " + (i + 1)
                                + " > Downloaded Successfully");
                    }
                    mSetterGetter.setDownloadStatus(true);

                    if (mCookieJar.getCookieStore() != null) {
                        mCookieJar.setCookieStore(mCookieJar.getCookieStore());
                    }
                    if (mCookieJar.getLocalContext() != null) {
                        mCookieJar
                                .setLocalContext(mCookieJar.getLocalContext());
                    }

                    if (result != null) {
                        strgDwnLsnr.stringDownloadedSuccessfully(result, url,
                                i, mCookieJar.getCookieStore());
                    }
                    else {

                        /* There is highly NO chance of coming here */

                        reasonForFailure = StringDownloaderSnippets.serverScrewed;
                        strgDwnLsnr.errorInServerSide(response.getStatusLine()
                                .getStatusCode(), getStatusEquivalent(response
                                .getStatusLine().getStatusCode()), url, i,
                                mCookieJar.getCookieStore());
                    }

                }
                else {

                    strgDwnLsnr.errorInServerSide(response.getStatusLine()
                            .getStatusCode(), getStatusEquivalent(response
                            .getStatusLine().getStatusCode()), url, i,
                            mCookieJar.getCookieStore());
                    result = null;
                    mSetterGetter.setDownloadStatus(false);
                    publishProgress(500);
                    // Do NOT UPDATE UI IN THREAD, Move this to PostExe
                    // strgDwnLsnr.dataDownloadFailed();
                }
            } catch (Exception e) {

                result = null;
                mSetterGetter.setDownloadStatus(false);
                publishProgress(1000);
                reasonForFailure = e.getMessage();
                strgDwnLsnr.stringFetchingFailed(reasonForFailure, url, i,
                        mCookieJar.getCookieStore());
                if (debug) {
                    Log.wtf(tag, reasonForFailure);
                }

            }

        }

        return result;
    }

    /**
     * @param statusCode
     * @return <b>Status Code's Equivalent</b>
     */
    private String getStatusEquivalent(int statusCode) {
        String statusCodeDescription = null;

        switch (statusCode) {
            case 100:
                statusCodeDescription = StringDownloaderSnippets._100;
                break;
            case 101:
                statusCodeDescription = StringDownloaderSnippets._101;
                break;
            case 102:
                statusCodeDescription = StringDownloaderSnippets._102;
                break;
            case 103:
                statusCodeDescription = StringDownloaderSnippets._103;
                break;
            case 122:
                statusCodeDescription = StringDownloaderSnippets._122;
                break;
            case 200:
                statusCodeDescription = StringDownloaderSnippets._200;
                break;
            case 201:
                statusCodeDescription = StringDownloaderSnippets._201;
                break;
            case 202:
                statusCodeDescription = StringDownloaderSnippets._202;
                break;
            case 203:
                statusCodeDescription = StringDownloaderSnippets._203;
                break;
            case 204:
                statusCodeDescription = StringDownloaderSnippets._204;
                break;
            case 205:
                statusCodeDescription = StringDownloaderSnippets._205;
                break;
            case 206:
                statusCodeDescription = StringDownloaderSnippets._206;
                break;
            case 207:
                statusCodeDescription = StringDownloaderSnippets._207;
                break;
            case 208:
                statusCodeDescription = StringDownloaderSnippets._208;
                break;
            case 226:
                statusCodeDescription = StringDownloaderSnippets._226;
                break;
            case 300:
                statusCodeDescription = StringDownloaderSnippets._300;
                break;
            case 301:
                statusCodeDescription = StringDownloaderSnippets._301;
                break;
            case 302:
                statusCodeDescription = StringDownloaderSnippets._302;
                break;
            case 303:
                statusCodeDescription = StringDownloaderSnippets._303;
                break;
            case 304:
                statusCodeDescription = StringDownloaderSnippets._304;
                break;
            case 305:
                statusCodeDescription = StringDownloaderSnippets._305;
                break;
            case 306:
                statusCodeDescription = StringDownloaderSnippets._306;
                break;
            case 307:
                statusCodeDescription = StringDownloaderSnippets._307;
                break;
            case 308:
                statusCodeDescription = StringDownloaderSnippets._308;
                break;
            case 400:
                statusCodeDescription = StringDownloaderSnippets._400;
                break;
            case 401:
                statusCodeDescription = StringDownloaderSnippets._401;
                break;
            case 402:
                statusCodeDescription = StringDownloaderSnippets._402;
                break;
            case 403:
                statusCodeDescription = StringDownloaderSnippets._403;
                break;
            case 404:
                statusCodeDescription = StringDownloaderSnippets._404;
                break;
            case 405:
                statusCodeDescription = StringDownloaderSnippets._405;
                break;
            case 406:
                statusCodeDescription = StringDownloaderSnippets._406;
                break;
            case 407:
                statusCodeDescription = StringDownloaderSnippets._407;
                break;
            case 408:
                statusCodeDescription = StringDownloaderSnippets._408;
                break;
            case 409:
                statusCodeDescription = StringDownloaderSnippets._409;
                break;
            case 410:
                statusCodeDescription = StringDownloaderSnippets._410;
                break;
            case 411:
                statusCodeDescription = StringDownloaderSnippets._411;
                break;
            case 412:
                statusCodeDescription = StringDownloaderSnippets._412;
                break;
            case 413:
                statusCodeDescription = StringDownloaderSnippets._413;
                break;
            case 414:
                statusCodeDescription = StringDownloaderSnippets._414;
                break;
            case 415:
                statusCodeDescription = StringDownloaderSnippets._415;
                break;
            case 416:
                statusCodeDescription = StringDownloaderSnippets._416;
                break;
            case 417:
                statusCodeDescription = StringDownloaderSnippets._417;
                break;
            case 422:
                statusCodeDescription = StringDownloaderSnippets._422;
                break;
            case 423:
                statusCodeDescription = StringDownloaderSnippets._423;
                break;
            case 424:
                statusCodeDescription = StringDownloaderSnippets._424;
                break;
            case 425:
                statusCodeDescription = StringDownloaderSnippets._425;
                break;
            case 426:
                statusCodeDescription = StringDownloaderSnippets._426;
                break;
            case 428:
                statusCodeDescription = StringDownloaderSnippets._428;
                break;
            case 429:
                statusCodeDescription = StringDownloaderSnippets._429;
                break;
            case 431:
                statusCodeDescription = StringDownloaderSnippets._431;
                break;
            case 444:
                statusCodeDescription = StringDownloaderSnippets._444;
                break;
            case 449:
                statusCodeDescription = StringDownloaderSnippets._449;
                break;
            case 450:
                statusCodeDescription = StringDownloaderSnippets._450;
                break;
            case 451:
                statusCodeDescription = StringDownloaderSnippets._451;
                break;
            case 499:
                statusCodeDescription = StringDownloaderSnippets._499;
                break;
            case 500:
                statusCodeDescription = StringDownloaderSnippets._500;
                break;
            case 501:
                statusCodeDescription = StringDownloaderSnippets._501;
                break;
            case 502:
                statusCodeDescription = StringDownloaderSnippets._502;
                break;
            case 503:
                statusCodeDescription = StringDownloaderSnippets._503;
                break;
            case 504:
                statusCodeDescription = StringDownloaderSnippets._504;
                break;
            case 505:
                statusCodeDescription = StringDownloaderSnippets._505;
                break;
            case 506:
                statusCodeDescription = StringDownloaderSnippets._506;
                break;
            case 507:
                statusCodeDescription = StringDownloaderSnippets._507;
                break;
            case 508:
                statusCodeDescription = StringDownloaderSnippets._508;
                break;
            case 509:
                statusCodeDescription = StringDownloaderSnippets._509;
                break;
            case 510:
                statusCodeDescription = StringDownloaderSnippets._510;
                break;
            case 511:
                statusCodeDescription = StringDownloaderSnippets._511;
                break;
            case 598:
                statusCodeDescription = StringDownloaderSnippets._598;
                break;
            case 599:
                statusCodeDescription = StringDownloaderSnippets._599;
                break;
            default:
                statusCodeDescription = "UNKNOWN STATUS CODE";
                break;
        }

        return statusCodeDescription;
    }

    /**
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (debug) {
            Log.w(tag, "onPostExecute");
        }

        if (dialog.isShowing()) {
            dialog.cancel();
        }

    }

    /**
     * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
     */
    @Override
    protected void onProgressUpdate(Integer... values) {

        if (debug) {
            Log.v(tag, "Updating Progess");
        }

        switch (values[0]) {
            case 1:
                setDialog(StringDownloaderSnippets.loading);
                break;

            case 2:
                setDialog(StringDownloaderSnippets.readingData);
                break;

            case 99:
                setDialog(StringDownloaderSnippets.noData);
                break;

            case 404:
                failureMessage(StringDownloaderSnippets._404);
                break;

            case 500:
                failureMessage(StringDownloaderSnippets._500);
                break;

            case 1000:
                failureMessage(StringDownloaderSnippets.noInternet);
                break;

            default:
                break;
        }

        super.onProgressUpdate(values);
    }

    /**
     * Sets the dialog.
     *
     * @param dialogMsg the new dialog
     */
    private void setDialog(String dialogMsg) {

        dialog.setMessage(dialogMsg);
        dialog.setIndeterminate(true);

    }

    /**
     * Failure message.
     *
     * @param toastMsg Toast Message
     */
    private void failureMessage(String toastMsg) {

        if (dialog.isShowing()) {
            dialog.cancel();
        }
        Toast.makeText(activity, toastMsg, Toast.LENGTH_SHORT).show();

    }

}

package com.shahedrahim.downloadintentservice;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class DownloadIntentService extends IntentService {
    private static final String TAG = "DownloadIntentService";

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String DOWNLOAD_MUSIC =
            "com.shahedrahim.retrofitgson.action.DOWNLOAD_MUSIC";
    private static final String DOWNLOAD_IMAGE =
            "com.shahedrahim.retrofitgson.action.DOWNLOAD_IMAGE";
    private static final String DOWNLOAD_VIDEO =
            "com.shahedrahim.retrofitgson.action.DOWNLOAD_VIDEO";
    private static final String DOWNLOAD_FILE =
            "com.shahedrahim.retrofitgson.action.DOWNLOAD_FILE";

    private static final String DOWNLOAD_PATH =
            "com.shahedrahim.retrofitgson.extra.DOWNLOAD_PATH";
    private static final String DESTINATION_PATH =
            "com.shahedrahim.retrofitgson.extra.DESTINATION_PATH";
    private static final String DESTINATION_REPLACE =
            "com.shahedrahim.retrofitgson.extra.DESTINATION_REPLACE";

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    /**
     * Starts this service to perform action DownloadMusic with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startDownloadMusic(
            Context context,
            String downloadPath,
            String destPath,
            boolean destReplace) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(DOWNLOAD_MUSIC);
        startService(context, intent, downloadPath, destPath, destReplace);
    }

    /**
     * Starts this service to perform action DownloadImage with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startDownloadImage(
            Context context,
            String downloadPath,
            String destPath,
            boolean destReplace) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(DOWNLOAD_IMAGE);
        startService(context, intent, downloadPath, destPath, destReplace);
    }

    /**
     * Starts this service to perform action DownloadVideo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startDownloadVideo(
            Context context,
            String downloadPath,
            String destPath,
            boolean destReplace) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(DOWNLOAD_VIDEO);
        startService(context, intent, downloadPath, destPath, destReplace);
    }

    /**
     * Starts this service to perform action DownloadFile with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startDownloadFile(
            Context context,
            String downloadPath,
            String destPath,
            boolean destReplace) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(DOWNLOAD_FILE);
        startService(context, intent, downloadPath, destPath, destReplace);
    }

    public static void startService(
            Context context,
            Intent intent,
            String downloadPath,
            String destPath,
            boolean destReplace) {
        intent.putExtra(DOWNLOAD_PATH, downloadPath);
        intent.putExtra(DESTINATION_PATH, destPath);
        intent.putExtra(DESTINATION_REPLACE, destReplace);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String downloadPath = intent.getStringExtra(DOWNLOAD_PATH);
            final String destPath = intent.getStringExtra(DESTINATION_PATH);
            final boolean destReplace = intent.getBooleanExtra(
                    DESTINATION_REPLACE,
                    true);
            if (DOWNLOAD_MUSIC.equals(action)) {
                handleDownloadMusic(downloadPath, destPath, destReplace);
            } else if (DOWNLOAD_IMAGE.equals(action)) {
                handleDownloadImage(downloadPath, destPath, destReplace);
            } else if (DOWNLOAD_VIDEO.equals(action)) {
                handleDownloadVideo(downloadPath, destPath, destReplace);
            } else if (DOWNLOAD_FILE.equals(action)) {
                handleDownloadFile(downloadPath, destPath, destReplace);
            }
        }
    }

    /**
     * Handle action Music in the provided background thread with the provided
     * parameters.
     */
    private void handleDownloadMusic(String url, String path, boolean destReplace) {
        String title = "Music Download";
        handleFileDownload(url, path, Environment.DIRECTORY_MUSIC, destReplace, title);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleDownloadImage(String url, String path, boolean destReplace) {
        String title = "Image Download";
        handleFileDownload(url, path, Environment.DIRECTORY_PICTURES, destReplace, title);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleDownloadVideo(String url, String path, boolean destReplace) {
        String title = "Video Download";
        handleFileDownload(url, path, Environment.DIRECTORY_MOVIES, destReplace, title);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleDownloadFile(String url, String path, boolean destReplace) {
        String title = "File Download";
        handleFileDownload(url, path, Environment.DIRECTORY_DOWNLOADS, destReplace, title);
    }

    private void handleFileDownload(
            String url,
            String path,
            String envPath,
            boolean destReplace,
            String title) {
        Uri uri = Uri.parse(url); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);

        // Tell on which network you want to download file.
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // This will hide notification
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        if (destReplace) {
            File file = new File(
                    Environment.getExternalStorageDirectory()+File.separator+envPath,
                    path);
            if (file.exists()) {
                boolean deleted = file.delete();
                Log.d(
                        TAG,
                        "handleFileDownload: deleted file "+file.getPath()+" : "+deleted);
            } else {
                Log.d(
                        TAG,
                        "handleFileDownload: file "+file.getPath()+" exists: "+file.exists());
            }
        } else {
            Log.d(TAG, "handleFileDownload: delete file " + destReplace);
        }


        // Title for notification.
        request.setTitle(title);
        request.setVisibleInDownloadsUi(true);
        // Storage directory path
        request.setDestinationInExternalPublicDir(envPath, path);
        // This will start downloading
        ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }
}

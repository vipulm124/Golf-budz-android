package com.adcoretechnologies.golfbudz.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.adcoretechnologies.golfbudz.BuildConfig;
import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.auth.PojoUser;
import com.adcoretechnologies.golfbudz.utils.api.APIHelper;
import com.adcoretechnologies.golfbudz.utils.api.IApiService;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Irfan on 27/05/16.
 */
public class Common {

    public static String getVersionName(Context context) {


        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "Version: " + versionName;
    }

    public static int getRandomInteger(int minimum, int maximum) {
        Random rn = new Random();
        int range = maximum - minimum + 1;
        int randomNum = rn.nextInt(range) + minimum;
        return randomNum;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static String getFormattedDateTime(String dateString) {
        String formattedDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateString);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy ' :'  HH:mm a");
            formattedDate = sdf.format(date);
        } catch (Exception ex) {
            formattedDate = "";
        }
        return formattedDate;
    }

    public static String getFormattedDateTime(Date date) {
        String formattedDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy ':'  HH:mm a");
            formattedDate = sdf.format(date);
        } catch (Exception ex) {
            formattedDate = "";
        }
        return formattedDate;
    }

    public static String getTimestampString() {
        return getFormattedDateTime(new Date());
    }

    public static String getTimestamp() {
        String time = getFormattedDateTime(new Date());
        return time;
    }

    public static long getTimestampLong() {
        return new Date().getTime();
    }

    public static String convertLongToTimestamp(long millis) {
        String dateString = new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(millis);
        return dateString;
    }

    public static void logException(Context context, String message, Throwable t, String toast) {
        if (toast == null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        try {
//            Crashlytics.setUserEmail(Pref.Read(context, Const.KEY_USER_ID) + Pref.Read(context, Const.KEY_DISPLAY_NAME));
//            Crashlytics.setUserName(Pref.Read(context, Const.KEY_USER_NAME));
//            Crashlytics.log(Log.ERROR, message, t.getMessage());
//            Crashlytics.log(Log.ERROR, message, t.getCause().getMessage());
//            Crashlytics.logException(t);
            log("Message: " + t.getMessage());
            log("stack:" + t.getStackTrace());
        } catch (Exception e) {

        }
    }
    public static void logException(Context context, String message, DatabaseError t, String toast) {
        if (toast == null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        try {
//            Crashlytics.setUserEmail(Pref.Read(context, Const.KEY_USER_ID) + Pref.Read(context, Const.KEY_DISPLAY_NAME));
//            Crashlytics.setUserName(Pref.Read(context, Const.KEY_USER_NAME));
//            Crashlytics.log(Log.ERROR, message, t.getMessage());
//            Crashlytics.log(Log.ERROR, message, t.getCause().getMessage());
//            Crashlytics.logException(t);
        } catch (Exception e) {

        }
    }

    public static boolean validateName(String text) {
        String pattern = "^[a-zA-Z\\s\\.]+$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static void showBigImage(Context context, ImageView imageView, String url) {
        try {
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_default_gallery)
                    .resize(380, 380)
                    .error(R.drawable.ic_default_gallery)
                    .into(imageView);
        } catch (Exception ex) {
            log("Unable to load round image. message: " + ex.getMessage());
        }
    }
    public static void showRoundImage(Context context, ImageView imageView, String url) {
        if (null == url || url.isEmpty()) {
            log("Image url is null");
        } else {
            try {
                Picasso.with(context)
                        .load(url)
                        .resize(200, 200)
                        .transform(new RoundedTransformation(100, 4))
                        .into(imageView);
            } catch (Exception ex) {
                log("Unable to load round image. message: " + ex.getMessage());
            }
        }
    }



    public static void showSmallRoundImage(Context context, ImageView imageView, String url) {
        if (null == url || url.isEmpty()) {
            log("Image url is null");
        } else {
            try {
                Picasso.with(context)
                        .load(url)
                        .resize(150, 150)
                        .transform(new RoundedTransformation(100, 4))
                        .into(imageView);
            } catch (Exception ex) {
                log("Unable to load round image. message: " + ex.getMessage());
            }
        }
    }

    public static void shareApp(Activity context) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux = "\nI would like to recommend you this application\n\n";
            sAux = sAux + getAPIUrl()+" \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) { //e.toString();
            Common.logException(context, "Error while sharing app", e, null);
        }
    }
    public static void openPlayStoreApp(Activity context){
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }



    private static class RoundedTransformation implements com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin;  // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);

            if (source != output) {
                source.recycle();
            }

            return output;
        }

        @Override
        public String key() {
            return "rounded";
        }

    }

    private static void log(String message) {
        Log.d(Const.DEBUG_TAG, "Common : " + message);
    }





    public static void sendSupportMail(Activity context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822"); //MIME type for email
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rny.roomnearyou@gmail.com"}); // receiver
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello!"); //subject of email
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi there I am sending you a test email"); //Text of mail
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(emailIntent, "Choose your mail application")); //start mail application chooser with message
    }

    public static File getStorageDirectoryOfImages() {
        String storageDirectory = Environment.getExternalStorageDirectory() + File.separator + Const.PROJECT_FOLDER + File.separator + Const.PROJECT_IMAGES;
        File directory = new File(storageDirectory);
        if (!directory.exists()) {
            boolean isCreaetd = directory.mkdir();
            log("Directory created ; " + isCreaetd);
        }
        return directory;
    }

    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getPlaystoreUrl() {
        return "https://play.google.com/store/apps/details?id=com.adcoretechnologies.rny";
    }
    public static String getAPIUrl() {
        if (BuildConfig.BUILD_TYPE.contentEquals("release"))
            return Const.API_BASE_URL_PROD;
        else {
            return Const.IS_PRODUCTION ? Const.API_BASE_URL_PROD : Const.API_BASE_URL_DEV;
        }

    } public static String getAPIUrlChange() {
        if (BuildConfig.BUILD_TYPE.contentEquals("release"))
            return Const.API_BASE_URL_PROD_CHANGE;
        else {
            return Const.IS_PRODUCTION ? Const.API_BASE_URL_PROD_CHANGE : Const.API_BASE_URL_DEV_CHANGE;
        }

    }
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public static void saveUpdatedPushOnServer(Context context, String token) {
        String userId = Pref.Read(context, Const.PREF_USER_ID);

        if(userId!=null && !userId.equals("")){
            IApiService service = APIHelper.getAppServiceMethod();
            Call<PojoUser> call = service.saveUpdatedPushId(userId, token);
            call.enqueue(new Callback<PojoUser>() {
                @Override
                public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                    if (response.isSuccessful()) {
                    }
                }

                @Override
                public void onFailure(Call<PojoUser> call, Throwable t) {
                }
            });}
    }
    public static void shareStatus(Context context, String status, String unhuLink, String userName) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "GolfBudz status");
        share.putExtra(Intent.EXTRA_TEXT, status + ".  \n Download now " + unhuLink + " \n to connect with " + userName);
        context.startActivity(Intent.createChooser(share, "Share link!"));
    }

    public static void shareUserVideo(Context context, String title, String thumb, String url, String unhuLink, String userName) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "GolfBudz video");
        share.putExtra(Intent.EXTRA_TEXT, "Watch " + title + " " + url + ".\n Download now " + unhuLink + " \n to connect with "+userName);
        context.startActivity(Intent.createChooser(share, "Share link!"));
    }
    public static void sharePhoto(Context context, List<String> filesToSend, String title, String unhuLink, String userName) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Unhu Pics");
            intent.setType("image/jpeg"); /* This example is sharing jpeg images. */
            ArrayList<Uri> files = new ArrayList<Uri>();
            for (String path : filesToSend /* List of the files you want to send */) {
                File file = new File(path);
                //Uri uri = Uri.fromFile(file);
                Uri uri = FileProvider.getUriForFile(
                        context,
                        context.getApplicationContext()
                                .getPackageName() + Const.FILE_PROVIDER_SUFFIX, file);
                files.add(uri);
            }
            intent.putExtra(Intent.EXTRA_TEXT, title + " on Unhu.\n Download now " + unhuLink + " \n to connect with " + userName);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Can not share images due to permission restriction", Toast.LENGTH_SHORT).show();
        }
    }
}

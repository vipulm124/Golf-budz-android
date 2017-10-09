package com.adcoretechnologies.golfbudz.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ShowAlert {
    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     *                - pass null if you don't want icon
     *                author   Gaurav Jain
     *                since 0.9
     */



    public static  void showAlertDialog(final Context context, String title, String message, final Boolean status) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Setting Dialog Title
        builder.setTitle(title);

        // Setting Dialog Message
        builder.setMessage(message);


        if (status != null)
            // Setting alert dialog icon
//         builder.setIcon((status) ? R.drawable.tick : R.drawable.delete);

            // Setting OK Button
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {



                }
            });

        // Showing Alert Message
        builder.show();
    }
}

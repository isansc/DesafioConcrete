package com.isansc.desafioconcrete.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.isansc.desafioconcrete.R;
import com.isansc.desafioconcrete.controller.communication.core.exceptions.CommunicationException;

/**
 * Created by Isan on 01-Nov-17.
 */

public class UiUtil {

    /**
     * Method to notify the user about a communication error that has occurred
     * @param activity
     * @param error communication error to retrieve the correct message to show
     */
    public static void notifyCommunicationError(Activity activity, CommunicationException error){

        String errorMessage = "";
        switch(error.getType()){
            case NETWORK_ERROR:
                errorMessage = activity.getString(R.string.message_error_communication_network);
                break;
            case SERVER_UNAUTHORIZED_ERROR:
                errorMessage = activity.getString(R.string.message_error_communication_unauthorized);
                break;
            case SERVER_RESPONSE_ERROR:
                errorMessage = error.getMessage();
                break;
            case SERVER_ERROR:
            case PARSE_ERROR:
                errorMessage = activity.getString(R.string.message_error_communication_server);
                break;
            case UNKNOWN_ERROR:
            default:
                errorMessage = activity.getString(R.string.message_error_communication_unknown);
                break;
        }

        notifyError(activity, errorMessage);
    }

    /**
     * Method to notify the user about an error that has occurred
     * @param activity
     * @param errorMessage
     */
    public static void notifyError(Activity activity, String errorMessage){
        showSnackbarForError(activity, errorMessage);
    }

    /**
     * Method to notify the user about an error that has occurred explicitlt in Toast format
     * @param context
     * @param errorMessage
     */
    public static void notifyErrorAsToast(Context context, String errorMessage){
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    }


//// SnackBar //////////////////////////////////////////////////////////////////////////////////////

    public enum SnackType {SUCCESS, ERROR, WARNING}
    public static void showSnackBar(Activity activity, @StringRes int stringResId, SnackType type){
        showSnackBar(activity, activity.getString(stringResId), type);
    }
    public static void showSnackBar(Activity activity, String message, SnackType type){
        int iconResId;

        switch (type) {
            case SUCCESS:
                iconResId = R.drawable.ic_check_circle_24dp;

                break;
            case ERROR:
                iconResId = R.drawable.ic_highlight_off_24dp;

                break;
            case WARNING:
                iconResId = R.drawable.ic_info_outline_24dp;

                break;
            default:
                iconResId = R.drawable.ic_check_circle_24dp;
        }

        showSnackBar(activity, message, type, iconResId);
    }

    public static void showSnackBar(Activity activity, String message, SnackType type, @DrawableRes int iconResId){
        View snackContainer = activity.findViewById(android.R.id.content);

        TSnackbar snackbar = TSnackbar.make(snackContainer, message, TSnackbar.LENGTH_LONG);
        snackbar.setMaxWidth(3000); //if you want fullsize on tablets
        snackbar.setIconPadding(30);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);

        snackbar.setIconLeft(iconResId, 40); //Size in dp - 24 is great!

        switch (type) {
            case SUCCESS:
                snackbarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.green));
                snackbar.setActionTextColor(Color.WHITE);
                textView.setTextColor(Color.WHITE);

                break;
            case ERROR:
                snackbarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.red));
                snackbar.setActionTextColor(Color.WHITE);
                textView.setTextColor(Color.WHITE);

                break;
            case WARNING:
                snackbarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.yellow));
                snackbar.setActionTextColor(Color.BLACK);
                textView.setTextColor(Color.BLACK);

                break;
        }

        snackbar.show();
    }

    public static void showSnackbarForError(Activity activity, @StringRes int message){
        showSnackbarForError(activity, activity.getString(message));
    }
    public static void showSnackbarForError(Activity activity, String message){
        showSnackBar(activity, message, SnackType.ERROR);
    }

    public static void showSnackbarForWarning(Activity activity, @StringRes int message){
        showSnackbarForWarning(activity, activity.getString(message));
    }
    public static void showSnackbarForWarning(Activity activity, String message){
        showSnackBar(activity, message, SnackType.WARNING);
    }

    public static void showSnackbarForSuccess(Activity activity, @StringRes int message){
        showSnackbarForSuccess(activity, activity.getString(message));
    }
    public static void showSnackbarForSuccess(Activity activity, String message){
        showSnackBar(activity, message, SnackType.SUCCESS);
    }
}

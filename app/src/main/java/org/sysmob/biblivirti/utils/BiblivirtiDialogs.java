package org.sysmob.biblivirti.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import org.sysmob.biblivirti.R;

/**
 * Created by djalmocruzjr on 11/01/2017.
 */

public abstract class BiblivirtiDialogs {

    public static void showMessageDialog(Context context, String title, String message, String buttonTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.BiblivirtiTheme_AlertDialog);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showMessageDialog(Context context, String title, String message, String buttonTitle, DialogInterface.OnClickListener positiveButtonOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.BiblivirtiTheme_AlertDialog);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonTitle, positiveButtonOnClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showConfirmationDialog(Context context, String title, String message, String positiveButtonTitle, String negativeButtonTitle, DialogInterface.OnClickListener positiveButtonOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.BiblivirtiTheme_AlertDialog);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonTitle, positiveButtonOnClickListener);
        builder.setNegativeButton(negativeButtonTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showConfirmationDialog(Context context, String title, String message, String positiveButtonTitle, String negativeButtonTitle, DialogInterface.OnClickListener positiveButtonOnClickListener, DialogInterface.OnClickListener negativeButtonOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.BiblivirtiTheme_AlertDialog);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonTitle, positiveButtonOnClickListener);
        builder.setNegativeButton(negativeButtonTitle, negativeButtonOnClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showInputDialog(Context context, int layoutResId, String buttonTitle, DialogInterface.OnClickListener positiveButtonOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.BiblivirtiTheme_AlertDialog);
        builder.setView(layoutResId);
        builder.setCancelable(false);
        builder.setPositiveButton(buttonTitle, positiveButtonOnClickListener);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

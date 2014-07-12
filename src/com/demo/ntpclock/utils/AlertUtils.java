package com.demo.ntpclock.utils;

import com.demo.ntpclock.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertUtils {

	public static void showMessageAlert(Context context, String message) {
		final AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
		alertbox.setTitle(context.getString(R.string.popup_title));
		alertbox.setMessage(message);

		alertbox.setPositiveButton(context.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});

		alertbox.show();
	}
}

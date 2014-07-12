package com.demo.ntpclock;

import com.demo.ntpclock.utils.NetworkUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class SplashScreenActivity extends Activity{

	private int mSplashTime = 3000; // time to display the splash screen in ms

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash_screen);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!NetworkUtils.isNetworkAvailable(this)) {

			showWifiMessageAlert();	

		} else {

			final Thread splashTread = new Thread() {
				@Override
				public void run() {
					try {
						int waited = 0;
						while (waited < mSplashTime) {
							sleep(100);
							waited += 100;
						}
					} catch (final InterruptedException e) {
						e.printStackTrace();
					} finally {

						gotoHomeActivity();
					}
				}
			};
			splashTread.start();						
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AppConfigs.REQUEST_WIFI_SETTING_CODE) {

			if (NetworkUtils.isNetworkAvailable(this)) {
				gotoHomeActivity();
			}
		}
	}

	private void gotoHomeActivity() {
		finish();

		Intent intent = new Intent(SplashScreenActivity.this,
				HomeActivity.class);

		startActivity(intent);
	}

	private void showWifiMessageAlert() {

		final AlertDialog.Builder alertbox = new AlertDialog.Builder(this.getApplicationContext());
		alertbox.setTitle(getString(R.string.popup_title));
		alertbox.setMessage(getString(R.string.message_network_error));

		alertbox.setPositiveButton(getString(R.string.setting),
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				Intent intent = new Intent(
						Settings.ACTION_WIFI_SETTINGS);
				startActivityForResult(intent,
						AppConfigs.REQUEST_WIFI_SETTING_CODE);
			}
		});

		alertbox.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				finish();
			}
		});

		alertbox.show();
	}

}

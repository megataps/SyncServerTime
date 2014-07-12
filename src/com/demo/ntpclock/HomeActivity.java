package com.demo.ntpclock;

import java.util.Date;

import org.apache.commons.net.ntp.TimeInfo;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import com.demo.ntpclock.services.NtpSyncService;
import com.demo.ntpclock.utils.AlertUtils;
import com.demo.ntpclock.utils.DateTimeUtils;
import com.demo.ntpclock.utils.NetworkUtils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.demo.ntpclock.widget.AnalogClock;
import com.demo.ntpclock.widget.DigitalClock;

import android.widget.ImageButton;
import android.widget.TextView;

public class HomeActivity extends ActionBarActivity{

	private HtmlTextView mTxtInfo;
	private TextView mTxtSyncCountDown;

	private CountDownTimer mCountDownTimer;		
	private DigitalClock mDigitalClock;
	private AnalogClock mAnalogClock;
	
	private ImageButton mBtnAnalogClock;
	private ImageButton mBtnDigitalClock;
	
	private ProgressDialog mProgDlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		
		mTxtSyncCountDown = (TextView)findViewById(R.id.txtSyncCountDown);
		mTxtInfo = (HtmlTextView)findViewById(R.id.txtInfo);		
		mDigitalClock = (DigitalClock)findViewById(R.id.digitalClock);
		mAnalogClock = (AnalogClock)findViewById(R.id.analogClock);		
		
		mBtnAnalogClock = (ImageButton)findViewById(R.id.btnAnalogClock);
		mBtnDigitalClock = (ImageButton)findViewById(R.id.btnDigitalClock);
		
		mBtnAnalogClock.setSelected(false);
		mBtnDigitalClock.setSelected(true);
		
		mDigitalClock.setVisibility(View.VISIBLE);
		mAnalogClock.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		syncTimeProcess();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if(mCountDownTimer != null) {
			mCountDownTimer.cancel();			
		} 
	}

	public void onBtnDigitalClockClick(View view) {		
		mBtnAnalogClock.setSelected(false);
		mBtnDigitalClock.setSelected(true);
		
		mDigitalClock.setVisibility(View.VISIBLE);
		mAnalogClock.setVisibility(View.GONE);
	}
	
	public void onBtnAnalogClockClick(View view) {		
		mBtnAnalogClock.setSelected(true);
		mBtnDigitalClock.setSelected(false);
		mDigitalClock.setVisibility(View.GONE);
		mAnalogClock.setVisibility(View.VISIBLE);
	}
	
	private void syncTimeProcess() {
		
		if(!NetworkUtils.isNetworkAvailable(this)) {
			AlertUtils.showMessageAlert(this, getString(R.string.message_network_error));
		}else {
			new GetServerTimeAsyncTask().execute();
		}
		
	}
	
	private class GetServerTimeAsyncTask extends AsyncTask<Void, Void, TimeInfo> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
						
			if(mCountDownTimer != null) {
				mCountDownTimer.cancel();
			} else {
				mCountDownTimer = new SyncCountDownTimer(AppConfigs.START_COUNDDOWN_TIME, AppConfigs.COUNTDOWN_ITNTERVAL);
			}		
			
			mProgDlg = ProgressDialog.show(HomeActivity.this, "", getText(R.string.loading).toString(), true);
		}

		@Override
		protected TimeInfo doInBackground(Void... params) {
			try {								
				return NtpSyncService.getCurrentNetworkTimeInfo(AppConfigs.NTP_SERVER_URL);				
			} catch (final Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(TimeInfo timeInfo) {
			super.onPostExecute(timeInfo);

			if (mProgDlg != null) {
				mProgDlg.dismiss();
			}
			
			if(timeInfo == null) {
				AlertUtils.showMessageAlert(HomeActivity.this, getString(R.string.message_sync_error));
				return;
			}
			
			String strTimeInfo = NtpSyncService.parseTimeInfo(timeInfo, HomeActivity.this);
			
			long dateTime = timeInfo.getMessage().getTransmitTimeStamp().getTime(); 
			Date currentDate = new Date(dateTime);

			StringBuilder htmlContent = new StringBuilder();
			
			htmlContent.append("<p>");
			htmlContent.append("<font color='#000000'>");			
			htmlContent.append("<b>");
			htmlContent.append(getString(R.string.ntp_server));
			htmlContent.append("</b>");
			htmlContent.append("&nbsp;&nbsp;");
			htmlContent.append(AppConfigs.NTP_SERVER_URL);
			htmlContent.append("</font>");
			htmlContent.append("</p>");

			htmlContent.append("<p>");
			htmlContent.append("<font color='#000000'>");			
			htmlContent.append("<b>");
			htmlContent.append(getString(R.string.current_date));
			htmlContent.append("</b>");
			htmlContent.append("&nbsp;&nbsp;");
			htmlContent.append(DateTimeUtils.getStringFromDate(currentDate, AppConfigs.CURRENT_DATE_FORMAT));
			htmlContent.append("</font>");
			htmlContent.append("</p>");

			htmlContent.append(strTimeInfo);

			mTxtInfo.setHtmlFromString(htmlContent.toString(),true);
															
			mDigitalClock.setCurrentTime(dateTime);
			mAnalogClock.setCurrentTime(dateTime);
			
			mCountDownTimer.start();
		}		
	}
	
	private class SyncCountDownTimer extends CountDownTimer {
		
        public SyncCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onTick(long millisUntilFinished) {
        	
        	final long secondTime = millisUntilFinished / 1000;        	        	
			final long lday = secondTime % (60 * 60 * 24);			
			final long lhour = lday % (60 * 60);
			final long minutes = (int) (lhour / 60);
			final long lmin = lhour % 60;
			final long seconds = (int) lmin;

			final String remainTime = String.format(AppConfigs.COUNTDONW_TIMER_FORMAT, minutes, seconds);			    	
	    	mTxtSyncCountDown.setText(remainTime);	    	
	    	
	    	if(secondTime <= 1) {
        		syncTimeProcess();
        		return;
        	}	    	
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()){	        
	        case R.id.action_sync_now:
	        	syncTimeProcess();
	            return true;
	    }
	 
	    return false;
	}
}

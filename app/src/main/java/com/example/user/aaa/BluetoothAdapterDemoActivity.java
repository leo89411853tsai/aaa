package com.example.user.aaa;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neurosky.connection.ConnectionStates;
import com.neurosky.connection.DataType.MindDataType;
import com.neurosky.connection.EEGPower;
import com.neurosky.connection.TgStreamHandler;
import com.neurosky.connection.TgStreamReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This activity demonstrates how to use the constructor:
 * public TgStreamReader(BluetoothAdapter ba, TgStreamHandler tgStreamHandler)
 * and related functions:
 * (1) Make sure that the device supports Bluetooth and Bluetooth is on
 * (2) setGetDataTimeOutTime
 * (3) startLog
 * (4) Using connect() and start() to replace connectAndStart()
 * (5) isBTConnected
 * (6) Use close() to release resource
 * (7) Demo of TgStreamHandler
 * (8) Demo of MindDataType
 * (9) Demo of recording raw data
 *
 */
public class BluetoothAdapterDemoActivity extends Activity {
//	FileWriter pw = new FileWriter("/sdcard/test.txt", false);
//	String starttime,endtime;
	private static final String TAG = BluetoothAdapterDemoActivity.class.getSimpleName();
	private TgStreamReader tgStreamReader;

	private BluetoothAdapter mBluetoothAdapter;
//	int test_count_25_26 = 0,test_count_51 = 0;
int count=0,a=0;
	int attention,miditation,delta,theta,alpha,beta,gamma,fatigue,sleep_quality;
	int[] T_attention =new int[86400];
	int[] T_miditation =new int[86400];
	int[] T_delta =new int[86400];
	int[] T_theta =new int[86400];
	int[] T_alpha =new int[86400];
	int[] T_beta =new int[86400];
	int[] T_gamma =new int[86400];
	private AlertDialog alertDialog;
	private CountDownTimer mCountDownTimer;

	public BluetoothAdapterDemoActivity() throws IOException {
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.first_view);

		initView();
		setUpDrawWaveView();

		try {
			// (1) Make sure that the device supports Bluetooth and Bluetooth is on 
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
				Toast.makeText(
						this,
						"Please enable your Bluetooth and re-run this program !",
						Toast.LENGTH_LONG).show();
				finish();
//				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "error:" + e.getMessage());
			return;
		}

		// Example of constructor public TgStreamReader(BluetoothAdapter ba, TgStreamHandler tgStreamHandler)
		tgStreamReader = new TgStreamReader(mBluetoothAdapter,callback);
		// (2) Demo of setGetDataTimeOutTime, the default time is 5s, please call it before connect() of connectAndStart()
		tgStreamReader.setGetDataTimeOutTime(6);
		// (3) Demo of startLog, you will get more sdk log by logcat if you call this function
		tgStreamReader.startLog();
	}

	private TextView tv_ps = null;
	private TextView tv_attention = null;
	private TextView tv_meditation = null;
	private TextView tv_delta = null;
	private TextView tv_theta = null;
	private TextView tv_lowalpha = null;


	private TextView  tv_highalpha = null;
	private TextView  tv_lowbeta = null;
	private TextView  tv_highbeta = null;

	private TextView  tv_lowgamma = null;
	private TextView  tv_middlegamma  = null;
	private TextView  tv_badpacket = null;

	private Button btn_start = null;
	private Button btn_stop = null;
	private LinearLayout wave_layout;

	private int badPacketCount = 0;

	private void initView() {

		final SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
		tv_ps = (TextView) findViewById(R.id.tv_ps);
		tv_attention = (TextView) findViewById(R.id.tv_attention);
		tv_meditation = (TextView) findViewById(R.id.tv_meditation);
		tv_delta = (TextView) findViewById(R.id.tv_delta);
		tv_theta = (TextView) findViewById(R.id.tv_theta);
		tv_lowalpha = (TextView) findViewById(R.id.tv_lowalpha);

		tv_highalpha = (TextView) findViewById(R.id.tv_highalpha);
		tv_lowbeta= (TextView) findViewById(R.id.tv_lowbeta);
		tv_highbeta= (TextView) findViewById(R.id.tv_highbeta);

		tv_lowgamma = (TextView) findViewById(R.id.tv_lowgamma);
		tv_middlegamma= (TextView) findViewById(R.id.tv_middlegamma);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		wave_layout = (LinearLayout) findViewById(R.id.wave_layout);
		Intent intent1 = this.getIntent();
		final String cc = intent1.getStringExtra("count");

		btn_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Date curdate = new Date(System.currentTimeMillis());
//				starttime = formatter.format(curdate);

				badPacketCount = 0;

				// (5) demo of isBTConnected 
				if(tgStreamReader != null && tgStreamReader.isBTConnected()){

					// Prepare for connecting
					tgStreamReader.stop();
					tgStreamReader.close();
				}

				// (4) Demo of  using connect() and start() to replace connectAndStart(),
				// please call start() when the state is changed to STATE_CONNECTED
				//tgStreamReader.connect();

				tgStreamReader.connectAndStart();

				new Thread(new Runnable() {
					@Override
					public void run() {

						while(a!=2) {
							Log.d("WWW", String.valueOf(a));
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								alertDialog = new AlertDialog.Builder(BluetoothAdapterDemoActivity.this).create();
								alertDialog.setTitle("讀取中");
								alertDialog.setMessage("請等待5秒...");
								alertDialog.show();
								if(a!=0){
									mCountDownTimer = new CountDownTimer(5000,1000){

										@Override
										public void onFinish() {
											alertDialog.dismiss();
											btn_stop.callOnClick();
											mCountDownTimer.cancel();

										}

										@Override
										public void onTick(long millisUntilFinished) {
											alertDialog.setMessage("請等待"+ (millisUntilFinished/1000) + "秒...");
										}

									}.start();
								}
							}
						});

					}
				}).start();
			}
		});

		btn_stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Date curdate = new Date(System.currentTimeMillis());
//				endtime = formatter.format(curdate);

				Intent intent1 = BluetoothAdapterDemoActivity.this.getIntent();
				final String cc = intent1.getStringExtra("count");
				final String insomnia = intent1.getStringExtra("失眠");
				final String anxiety = intent1.getStringExtra("焦慮");
				final String excitement = intent1.getStringExtra("激動");
				final String concern = intent1.getStringExtra("憂慮");
				for (int i = 0; i < count; i++) {
					attention += T_attention[i];
					miditation += T_miditation[i];
					delta += T_delta[i];
					theta += T_theta[i];
					alpha += T_alpha[i];
					beta += T_beta[i];
					gamma += T_gamma[i];
				}
				attention = attention / count;
				miditation = miditation / count;
				delta = delta / count;
				theta = theta / count;
				alpha = alpha / count;
				beta = beta / count;
				gamma = gamma / count;
				sleep_quality = (((beta / 2) + (theta / 20)) / ((alpha / 2) + (delta / 20))) * 100;
				count = 0;

				Intent intent = new Intent();
				intent.setClass(BluetoothAdapterDemoActivity.this, Upload.class);
				intent.putExtra("a",String.valueOf(attention));
				intent.putExtra("b",String.valueOf(miditation));
				intent.putExtra("c",String.valueOf(alpha));
				intent.putExtra("d",String.valueOf(beta));
				intent.putExtra("e",String.valueOf(gamma));
				intent.putExtra("f",String.valueOf(delta));
				intent.putExtra("g",String.valueOf(theta));
				intent.putExtra("h",String.valueOf(cc));
				intent.putExtra("i",String.valueOf(fatigue));
				intent.putExtra("失眠",insomnia);
				intent.putExtra("焦慮",anxiety);
				intent.putExtra("激動",excitement);
				intent.putExtra("憂慮",concern);
				startActivity(intent);
//				try {
//					pw.write("Start Time : " + starttime +"\n" + "End Time" + endtime + "\n" +"PoorSignal =  25 or 26 :" + "\n" +test_count_25_26 +"PoorSignal =  51 :" + test_count_51);
//					pw.flush();
//					pw.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				tgStreamReader.stop();
				tgStreamReader.close();
				BluetoothAdapterDemoActivity.this.finish();
			}

		});
	}

	public void stop() {
		if(tgStreamReader != null){
			tgStreamReader.stop();
			tgStreamReader.close();
		}
	}

	@Override
	protected void onDestroy() {
		//(6) use close() to release resource 
		if(tgStreamReader != null){
			tgStreamReader.close();
			tgStreamReader = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		stop();
	}

	DrawWaveView waveView = null;

	public void setUpDrawWaveView() {
		waveView = new DrawWaveView(getApplicationContext());
		wave_layout.addView(waveView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		waveView.setValue(2048, 2048, -2048);
	}

	public void updateWaveView(int data) {
		if (waveView != null) {
			waveView.updateData(data);
		}
	}

    // (7) demo of TgStreamHandler
	private TgStreamHandler callback = new TgStreamHandler() {

		@Override
		public void onStatesChanged(int connectionStates) {
			// TODO Auto-generated method stub
			Log.d(TAG, "connectionStates change to: " + connectionStates);
			switch (connectionStates) {
			case ConnectionStates.STATE_CONNECTING:
				// Do something when connecting
				break;
			case ConnectionStates.STATE_CONNECTED:
				// Do something when connected
				tgStreamReader.start();
				showToast("Connected", Toast.LENGTH_SHORT);
				break;
			case ConnectionStates.STATE_WORKING:
				// Do something when working

				//(9) demo of recording raw data , stop() will call stopRecordRawData,
				//or you can add a button to control it.
				//You can change the save path by calling setRecordStreamFilePath(String filePath) before startRecordRawData
				tgStreamReader.startRecordRawData();

				break;
			case ConnectionStates.STATE_GET_DATA_TIME_OUT:
				// Do something when getting data timeout

				//(9) demo of recording raw data, exception handling
				tgStreamReader.stopRecordRawData();

				showToast("Get data time out!", Toast.LENGTH_SHORT);
				break;
			case ConnectionStates.STATE_STOPPED:
				// Do something when stopped
				// We have to call tgStreamReader.stop() and tgStreamReader.close() much more than
				// tgStreamReader.connectAndstart(), because we have to prepare for that.

				break;
			case ConnectionStates.STATE_DISCONNECTED:
				// Do something when disconnected
				break;
			case ConnectionStates.STATE_ERROR:
				// Do something when you get error message
				break;
			case ConnectionStates.STATE_FAILED:
				// Do something when you get failed message
				// It always happens when open the BluetoothSocket error or timeout
				// Maybe the device is not working normal.
				// Maybe you have to try again
				break;
			}
			Message msg = LinkDetectedHandler.obtainMessage();
			msg.what = MSG_UPDATE_STATE;
			msg.arg1 = connectionStates;
			LinkDetectedHandler.sendMessage(msg);
		}

		@Override
		public void onRecordFail(int flag) {
			// You can handle the record error message here
			Log.e(TAG,"onRecordFail: " +flag);

		}

		@Override
		public void onChecksumFail(byte[] payload, int length, int checksum) {
			// You can handle the bad packets here.
			badPacketCount ++;
			Message msg = LinkDetectedHandler.obtainMessage();
			msg.what = MSG_UPDATE_BAD_PACKET;
			msg.arg1 = badPacketCount;
			LinkDetectedHandler.sendMessage(msg);

		}

		@Override
		public void onDataReceived(int datatype, int data, Object obj) {
			// You can handle the received data here
			// You can feed the raw data to algo sdk here if necessary.

			Message msg = LinkDetectedHandler.obtainMessage();
			msg.what = datatype;
			msg.arg1 = data;
			msg.obj = obj;
			LinkDetectedHandler.sendMessage(msg);

			//Log.i(TAG,"onDataReceived");
		}

	};

	private boolean isPressing = false;
	private static final int MSG_UPDATE_BAD_PACKET = 1001;
	private static final int MSG_UPDATE_STATE = 1002;

	int raw;
	private Handler LinkDetectedHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// (8) demo of MindDataType



			switch (msg.what) {
			case MindDataType.CODE_RAW:
					updateWaveView(msg.arg1);
				break;
			case MindDataType.CODE_MEDITATION:
				Log.d(TAG, "HeadDataType.CODE_MEDITATION " + msg.arg1);
				if(msg.arg1 != 0) {
					tv_meditation.setText(String.valueOf(msg.arg1));
					T_miditation[count] = msg.arg1;
					count++;
					a=1;
				}
				break;
			case MindDataType.CODE_ATTENTION:
				Log.d(TAG, "CODE_ATTENTION " + msg.arg1);
				if(msg.arg1 != 0) {
					tv_attention.setText(String.valueOf(msg.arg1));
					T_attention[count] = msg.arg1;
				}
				break;
			case MindDataType.CODE_EEGPOWER:
				EEGPower power = (EEGPower)msg.obj;
				if(a != 0){
					a=2;
					tv_delta.setText("" +power.delta);
					tv_theta.setText("" +power.theta);
					tv_lowalpha.setText("" +power.lowAlpha);
					tv_highalpha.setText("" +power.highAlpha);
					tv_lowbeta.setText("" +power.lowBeta);
					tv_highbeta.setText("" +power.highBeta);
					tv_lowgamma.setText("" +power.lowGamma);
					tv_middlegamma.setText("" +power.middleGamma);

					T_delta[count] = power.delta;
					T_theta[count] = power.theta;
					T_alpha[count] = (power.highAlpha + power.lowAlpha);
					T_beta[count] = (power.highBeta + power.lowBeta);
					T_gamma[count] = (power.middleGamma + power.lowGamma);

					attention = miditation = delta = theta = alpha = beta = gamma = 0;

				}

				break;
			case MindDataType.CODE_POOR_SIGNAL:
//				SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
//				Date curDate = new Date(System.currentTimeMillis());
//				String date = formatter.format(curDate);
//				int poorSignal = msg.arg1;
//				Log.d(TAG, "poorSignal:" + poorSignal);
//				tv_ps.setText(""+msg.arg1);
//				if(poorSignal ==25 || poorSignal == 26)
//					test_count_25_26 ++;
//				else if(poorSignal == 51)
//					test_count_51 ++ ;
//
//				try {
//					pw.write(date + "   " + "poorSignal: " + poorSignal+"\n");
//					pw.flush();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}

				break;
			case MSG_UPDATE_BAD_PACKET:
				tv_badpacket.setText("" + msg.arg1);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};


	public void showToast(final String msg,final int timeStyle){
		BluetoothAdapterDemoActivity.this.runOnUiThread(new Runnable()
        {
            public void run()
            {
            	Toast.makeText(getApplicationContext(), msg, timeStyle).show();
            }

        });
	}
}

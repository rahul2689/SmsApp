package com.hc1mg.SmsApp;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsBroadcastReceiver extends BroadcastReceiver {

	public static final String SMS_BUNDLE = "pdus";
	private String mAddress = "";
	private String mMsgBody = "";
	private long mTimeMillis;
	private Context mContext;
	private String mDateText;
	static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	public void onReceive(Context context, Intent intent) {
		mContext = context;
		Bundle intentExtras = intent.getExtras();
		if (intentExtras != null) {
			Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
			String smsMessageStr = "";
			for (int i = 0; i < sms.length; ++i) {
				SmsMessage smsMessage = SmsMessage
						.createFromPdu((byte[]) sms[i]);

				mMsgBody = smsMessage.getMessageBody().toString();
				mAddress = smsMessage.getOriginatingAddress();
				mTimeMillis = smsMessage.getTimestampMillis();

				Date date = new Date(mTimeMillis);
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
				mDateText = format.format(date);

				smsMessageStr += mAddress + " at " + "\t" + mDateText + "\n";
				smsMessageStr += mMsgBody + "\n";
			}
			/*Notification noti = new Notification.Builder(mContext)
					.setContentTitle("New SMS from " + mAddress.toString())
					.setContentText(mMsgBody).setSmallIcon(0)
					.setLargeIcon(null).build();

			noti.notify();*/

			ReceiveSmsActivity inst = ReceiveSmsActivity.instance();
			SmsInfo info = new SmsInfo();
			info.setPhoneNumber(mAddress);
			info.setMessageBody(mMsgBody);
			info.setDate(mDateText);
			info.setIsReadOrNot(false);
			inst.updateList(info);

		}
	}
}
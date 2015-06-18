package com.hc1mg.SmsApp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiveSmsActivity extends Activity implements OnItemClickListener {

	private static ReceiveSmsActivity inst;
	private List<SmsInfo> smsMessagesListRead = new ArrayList<SmsInfo>();
	private List<SmsInfo> smsMessagesListUnRead = new ArrayList<SmsInfo>();
	private List<SmsInfo> smsMessagesList = new ArrayList<SmsInfo>();
	private ListView smsListView;
	private SmsAdapter smsAdapter;
	private Button buttonCompose;
	private String mDateText;
	private String smsMessageStr;

	public static ReceiveSmsActivity instance() {
		if (inst == null) {
			inst = new ReceiveSmsActivity();
		}
		return inst;
	}

	@Override
	public void onStart() {
		super.onStart();
		inst = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_sms);
		smsListView = (ListView) findViewById(R.id.lv_receive_sms_list);
		List<SmsInfo> listRead = refreshSmsInboxRead();
		List<SmsInfo> listUnread = refreshSmsInboxUnread();
		smsMessagesList.addAll(listRead);
		smsMessagesList.addAll(listUnread);
		smsAdapter = new SmsAdapter(getApplicationContext(), smsMessagesList);
		smsListView.setAdapter(smsAdapter);
		smsListView.setOnItemClickListener(this);
		buttonCompose = (Button) findViewById(R.id.button_receive_sms_compose);
		buttonCompose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goToCompose();
			}
		});
	}

	public void goToCompose() {
		Intent intent = new Intent(ReceiveSmsActivity.this,
				ComposeSmsActivity.class);
		startActivity(intent);
	}

	public List<SmsInfo> refreshSmsInboxRead() {
		ContentResolver contentResolver = getContentResolver();
		Cursor smsInboxCursor = contentResolver.query(
				Uri.parse("content://sms/inbox"), null, "read = 1", null, null);
		smsInboxCursor.moveToFirst();
		int indexBody = smsInboxCursor.getColumnIndex("body");
		int indexAddress = smsInboxCursor.getColumnIndex("address");

		/*
		 * if (indexBody < 0 || !smsInboxCursor.moveToFirst()) { return true; }
		 */
		smsMessagesListRead.clear();
		do {
			SmsInfo info = extractSmsInfo(smsInboxCursor, indexBody,
					indexAddress, true);
			smsMessagesListRead.add(info);
		} while (smsInboxCursor.moveToNext());
		return smsMessagesListRead;
	}

	public List<SmsInfo> refreshSmsInboxUnread() {
		ContentResolver contentResolver = getContentResolver();
		Cursor smsInboxCursor = contentResolver.query(
				Uri.parse("content://sms/inbox"), null, "read = 0", null, null);
		smsInboxCursor.moveToFirst();
		int indexBody = smsInboxCursor.getColumnIndex("body");
		int indexAddress = smsInboxCursor.getColumnIndex("address");
		/*
		 * if (indexBody < 0 || !smsInboxCursor.moveToFirst()) { return; }
		 */
		smsMessagesListUnRead.clear();
		do {
			SmsInfo info = extractSmsInfo(smsInboxCursor, indexBody,
					indexAddress, false);
			smsMessagesListUnRead.add(info);
		} while (smsInboxCursor.moveToNext());
		return smsMessagesListUnRead;
	}

	private SmsInfo extractSmsInfo(Cursor smsInboxCursor, int indexBody,
			int indexAddress, boolean val) {
		String name = getContactName(getApplicationContext(),
				smsInboxCursor.getString(smsInboxCursor
						.getColumnIndex("address")));
		SmsInfo info = new SmsInfo();
		info.setName(name);
		info.setPhoneNumber(smsInboxCursor.getString(indexAddress));
		info.setMessageBody(smsInboxCursor.getString(indexBody));
		info.setIsReadOrNot(val);
		String date = smsInboxCursor.getString(smsInboxCursor
				.getColumnIndex("date"));
		Long timestamp = Long.parseLong(date);
		Date newDate = new Date(timestamp);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
		mDateText = format.format(newDate);
		info.setDate(mDateText);
		return info;
	}

	public void updateList(final SmsInfo info) {
		smsMessagesList.add(0, info);
		smsAdapter.notifyDataSetChanged();
	}

	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		try {
			if (smsMessagesList.get(pos).getName() != null) {
				String name = smsMessagesList.get(pos).getName();
				String message = smsMessagesList.get(pos).getMessageBody();
				smsMessageStr = name + "\n";
				smsMessageStr += message;
			} else {
				String phoneNumber = smsMessagesList.get(pos).getPhoneNumber();
				String message = smsMessagesList.get(pos).getMessageBody();
				smsMessageStr = phoneNumber + "\n";
				smsMessageStr += message;
			}
			Toast.makeText(this, smsMessageStr, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void goToCompose(View view) {
		Intent intent = new Intent(ReceiveSmsActivity.this,
				ComposeSmsActivity.class);
		startActivity(intent);
	}

	public String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return contactName;
	}
}
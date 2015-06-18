package com.hc1mg.SmsApp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SmsAdapter extends BaseAdapter {

	private List<SmsInfo> mMessageList;
	private Context mContext;
	private TextView phoneNumberTv;
	private TextView messageTv;
	private TextView dateTv;
	private TextView timeTv;
	private RelativeLayout relativeLayoutId;

	public SmsAdapter(Context context, List<SmsInfo> smsMessagesList) {
		mContext = context;
		mMessageList = smsMessagesList;
	}

	@Override
	public int getCount() {
		return mMessageList.size();
	}

	@Override
	public SmsInfo getItem(int position) {
		return mMessageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.inbox_view_adapter, parent, false);
		}
		relativeLayoutId = (RelativeLayout) convertView
				.findViewById(R.id.rl_sms_adapter);
		phoneNumberTv = (TextView) convertView
				.findViewById(R.id.tv_receive_sms_adapter_contact);
		messageTv = (TextView) convertView
				.findViewById(R.id.tv_receive_sms_adapter_msg_boby);
		dateTv = (TextView) convertView
				.findViewById(R.id.tv_receive_sms_adapter_date);
		if (mMessageList.get(position).getName() != null) {
			phoneNumberTv.setText(mMessageList.get(position).getName());
			relativeLayoutId.setBackgroundColor(mContext.getResources()
					.getColor(R.color.lighter_grey));
		} else {
			phoneNumberTv.setText(mMessageList.get(position).getPhoneNumber());
			relativeLayoutId.setBackgroundColor(mContext.getResources()
					.getColor(R.color.white));
		}
		dateTv.setText(mMessageList.get(position).getDate());
		setMessage(position);
		return convertView;
	}

	private void setMessage(int position) {
		if (!mMessageList.get(position).isIsReadOrNot()) {
			messageTv.setText(mMessageList.get(position).getMessageBody());
			messageTv.setTypeface(null, Typeface.BOLD);
		} else {
			messageTv.setText(mMessageList.get(position).getMessageBody());
			messageTv.setTypeface(null, Typeface.NORMAL);
		}
	}

}

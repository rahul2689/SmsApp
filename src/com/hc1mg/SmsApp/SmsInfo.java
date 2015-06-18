package com.hc1mg.SmsApp;

public class SmsInfo {
	private String mPhoneNumber;
	private String mDate;
	private String mMessageBody;
	private String mTime;
	private boolean mIsReadOrNot;
	
	public SmsInfo (){
		
	}
	public SmsInfo(String phoneNumber, String date, String message, String time){
		mPhoneNumber = phoneNumber;
		mDate = date;
		mMessageBody = message;
		mTime = time;
	}

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.mPhoneNumber = phoneNumber;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		this.mDate = date;
	}

	public String getMessageBody() {
		return mMessageBody;
	}

	public void setMessageBody(String messageBody) {
		this.mMessageBody = messageBody;
	}

	public String getName() {
		return mTime;
	}

	public void setName(String time) {
		this.mTime = time;
	}
	
	public boolean isIsReadOrNot() {
		return mIsReadOrNot;
	}
	public void setIsReadOrNot(boolean isReadOrNot) {
		this.mIsReadOrNot = isReadOrNot;
	}
}

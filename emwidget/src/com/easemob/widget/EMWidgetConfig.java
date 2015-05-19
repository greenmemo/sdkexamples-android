package com.easemob.widget;

import android.graphics.Bitmap;

public class EMWidgetConfig {
	
	public interface EMAvatarCallBack {
		public abstract Bitmap getAvatar(String username);
	}
	
	static EMWidgetConfig instance;
	EMAvatarCallBack avatarCallBack;
	
	public static EMWidgetConfig getInstance() {
		if (instance == null) {
			instance = new EMWidgetConfig();
		}
		return instance;
	}
	
	public void setAvatarCallBack(EMAvatarCallBack callback) {
		avatarCallBack = callback;
	}
	
	public Bitmap getAvatar(String username) {
		if (avatarCallBack != null) {
			return avatarCallBack.getAvatar(username);
		}
		return null;
	}
}

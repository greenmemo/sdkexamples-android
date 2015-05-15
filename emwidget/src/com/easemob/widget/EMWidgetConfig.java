package com.easemob.widget;

import android.graphics.Bitmap;

public class EMWidgetConfig {
	
	public interface EMAvatarCallBack {
		public abstract Bitmap getAvatar(String username);
	}
	
	EMWidgetConfig instance;
	EMAvatarCallBack avatarCallBack;
	
	public EMWidgetConfig getInstance() {
		if (instance == null) {
			instance = new EMWidgetConfig();
		}
		return instance;
	}
	
	public void setAvatarCallBack(EMAvatarCallBack callback) {
		avatarCallBack = callback;
	}
}

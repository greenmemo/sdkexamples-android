/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easemob.uidemo.R;

public class AlertDialog extends Dialog {
	
	public interface AlertDialogUser {
		public void onResult(boolean confirmed, Bundle bundle);
	}

	private String title;
	private String msg;
	private AlertDialogUser user;
	private Bundle bundle;
	private boolean showCancel = false;
	
	public AlertDialog(Context context, String title, String msg) {
		super(context);
		this.title = title;
		this.msg = msg;
		this.setCanceledOnTouchOutside(true);
	}
			
	public AlertDialog(Context context, String title, String msg, Bundle bundle, AlertDialogUser user) {
		super(context);
		this.title = title;
		this.msg = msg;
		this.user = user;
		this.bundle = bundle;
		this.setCanceledOnTouchOutside(true);
	}

	public AlertDialog(Context context, String title, String msg, Bundle bundle, AlertDialogUser user, boolean showCancel) {
		super(context);
		this.title = title;
		this.msg = msg;
		this.user = user;
		this.bundle = bundle;
		this.showCancel = showCancel;
		this.setCanceledOnTouchOutside(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_alert_dialog);

		if (title != null)
			setTitle(title);
		
		Button cancel = (Button)findViewById(R.id.btn_cancel);
		if (showCancel) {
			cancel.setVisibility(View.VISIBLE);
		}

		if (msg != null)
		    ((TextView)findViewById(R.id.alert_message)).setText(msg);
	}
	
	public void onOk(View view){
		this.dismiss();
		if (this.user != null) {
			this.user.onResult(true, this.bundle);
		}
	}
	
	public void onCancel(View view) {
		this.dismiss();
		if (this.user != null) {
			this.user.onResult(false, this.bundle);
		}
	}
}

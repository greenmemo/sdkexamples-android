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
package com.easemob.chatuidemo.domain;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;

import com.easemob.chat.EMContact;

public class User extends EMContact {
	private int unreadMsgCount;
	private String header;
	private String avatar;
	private byte[] avatarBlob;
	
	public User(){}
	
	public User(String username){
	    this.username = username;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}

	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}

	public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

	public byte[] getAvatarBlob() {
        return avatarBlob;
    }

    public void setAvatarBlob(byte[] data) {
        this.avatarBlob = data;
    }

    
    @Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof User)) {
			return false;
		}
		return getUsername().equals(((User) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
	
	public static User fromJson(JSONObject json) {
		User user = new User();
		try {
			user.username = json.getString("username");
			user.eid = json.getString("eid");
			user.nick = json.getString("nick");
			String data = json.getString("avatar");
			if (data != "") {
//				byte[] byteData = Base64.decode(data, 0);
//				InputStream stream = new ByteArrayInputStream(byteData);
//				user.icon = BitmapFactory.decodeStream(stream);
				user.avatarBlob = Base64.decode(data, 0);
			}
			return user;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("username", username).put("eid", eid).put("nick", nick);
			
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//	        icon.compress(Bitmap.CompressFormat.PNG, 0, baos);
//	        byte[] bytes = baos.toByteArray();
			json.put("avatar", Base64.encode(avatarBlob, 0));
			return json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
}

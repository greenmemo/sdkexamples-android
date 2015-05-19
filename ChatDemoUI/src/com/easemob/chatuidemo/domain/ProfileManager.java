package com.easemob.chatuidemo.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.cloud.HttpClientConfig;
import com.easemob.cloud.HttpClientManager;
import com.easemob.util.EMLog;
import com.easemob.util.NetUtils;

public class ProfileManager {
	
	/**
	 * 每个应用需要配置自己的服务器定义自己的联系人详情，并处理头像等信息
	 * 下面的回调函数分别定义获取联系人详情的函数, 和设置自己的详情
	 * @author 
	 *
	 */
	public interface ProfileUrlGen {
		/**
		 * 获取联系人详情对应的url 
		 * @param user
		 * @return
		 */
		public abstract String getProfileUrl(String username);

		/**
		 * 设置当前用户详情对应的url 
		 * @param user
		 * @return
		 */
		public abstract String postProfileUrl(String username);
	}

	private Context context;
	private ProfileUrlGen urlGen;
	private Bitmap defaultAvatar;
	private static ProfileManager instance;
	
	public static ProfileManager getInstance(Context context) {
		if (instance == null) {
			instance = new ProfileManager(context);
		}
		return instance;
	}
	
	ProfileManager(Context context) {
		this.context = context;
	}
	
	public void setUrlGen(ProfileUrlGen gen) {
		urlGen = gen;
	}
	

	protected String getProfileUrl(String user) {
		if (urlGen == null) {
			return null;
		}
		return urlGen.getProfileUrl(user);
	}
	
	protected String postProfileUrl(String user) {
		if (urlGen == null) {
			return null;
		}
		return urlGen.postProfileUrl(user);
	}

	private User onDownloadCompleted(String username, HttpResponse response) throws Exception {
		HttpEntity entity = response.getEntity();
		if(entity == null){
    		return null;
    	}    	
    	InputStream input = null;
		OutputStream output = null;

		try {
			input = entity.getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		int bufSize = NetUtils.getDownloadBufSize(context);
		byte[] buffer = new byte[bufSize];
		int count = 0;
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(bufSize);
		try {
			while ((count = input.read(buffer)) != -1) {
				byteBuffer.put(buffer, 0, count);
			}
			
			JSONObject json = new JSONObject(byteBuffer.toString());
			User user = User.fromJson(json);
			return user;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally{
			output.close();
			input.close();
		}
	}
	
	public User retriveProfile(String username) {
		String remoteUrl = getProfileUrl(username);
		if (remoteUrl == null) {
			return null;
		}
		
		DefaultHttpClient client = HttpClientConfig.getDefaultHttpClient();
		try {
			HttpGet request = new HttpGet(remoteUrl);
			HttpResponse response = client.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
			switch(responseCode){
			case HttpStatus.SC_OK:
				User user = onDownloadCompleted(username, response);
				(new UserDao(context)).saveContact(user);
				return user;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void postCurrentUserProfile() {
		
		// TODO: 需要鉴权，只能修改自己的Profile, 使用SSL，暂时不做

		String currentUser = EMChatManager.getInstance().getCurrentUser();
		String remoteUrl = postProfileUrl(currentUser);
		if (remoteUrl == null) {
			return;
		}
		
		User user = (new UserDao(context)).getContact(currentUser);
		String body = user.getJson();
		
		EMLog.d("ProfileManager", "send:" + body);
		
		try {
			Map<String, String> headers = new HashMap<String, String>();
			HttpClientManager.httpExecute(remoteUrl, headers, body, "POST");
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Bitmap getDefaultAvatar() {
		if (defaultAvatar == null) {
			defaultAvatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.em_default_avatar);
		}
		return defaultAvatar;
	}
	
	public Bitmap getAvatar(String username) {
		// ======================== begin
		// 验证通过，不过效率非常低，看来缓存是必须的
//		Bitmap avatar_0 = BitmapFactory.decodeResource(context.getResources(), R.drawable.avatar_0);
//		
//		// bitmap to bytearray
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		avatar_0.compress(Bitmap.CompressFormat.PNG, 100, out);
//		byte[] byteData= out.toByteArray();
//		
//		// byte To String
//		String strData = Base64.encodeToString(byteData, 0);
//		
//		// String to byte
//		byteData = Base64.decode(strData, 0);
//
//		avatar_0 = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
//
//		return avatar_0;
		// ======================== end
		User user = (new UserDao(context)).getContact(username);
		if (user == null) {
			return null;
		}
		byte[] blob = user.getAvatarBlob();
		if (blob == null) {
//			return getDefaultAvatar();
			return null;
		}
		InputStream is = new ByteArrayInputStream(blob);
		return BitmapFactory.decodeStream(is);
	}
}

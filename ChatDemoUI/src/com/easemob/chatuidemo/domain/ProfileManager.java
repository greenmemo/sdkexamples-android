package com.easemob.chatuidemo.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
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

import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.cloud.HttpClientConfig;
import com.easemob.cloud.HttpClientManager;
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
	
	public ProfileManager(Context context) {
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
		
		try {
			HttpClientManager.httpExecute(remoteUrl, null, body, "POST");
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
}

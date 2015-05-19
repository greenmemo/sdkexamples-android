package com.easemob.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.easemob.uidemo.R;
import com.easemob.widget.EMWidgetConfig;

public class UserUtils {
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
//        User user = getUserInfo(username);
//        User user = null;
//        if(user != null){
//            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.em_default_avatar).into(imageView);
//        }else{
//            Picasso.with(context).load(R.drawable.em_default_avatar).into(imageView);
//        }
    	
    	Bitmap bitmap = EMWidgetConfig.getInstance().getAvatar(username);
    	if (bitmap != null) {
    		imageView.setImageBitmap(bitmap);
    	} else {
    		imageView.setImageResource(R.drawable.em_default_avatar);
    	}
    }
    
}

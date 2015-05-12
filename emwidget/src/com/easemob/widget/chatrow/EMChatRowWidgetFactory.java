package com.easemob.widget.chatrow;

import android.content.Context;
import android.view.ViewGroup;

import com.easemob.chat.EMMessage;

public abstract class EMChatRowWidgetFactory {
	
	public abstract EMChatRowWidget getEMChatRowWidget(Context context, EMMessage message, int position, ViewGroup parent);

	private static EMChatRowWidgetFactory callFactory;
	private static EMChatRowWidgetFactory fileFactory;
	private static EMChatRowWidgetFactory imageFactory;
	private static EMChatRowWidgetFactory locationFactory;
	private static EMChatRowWidgetFactory textFactory;
	private static EMChatRowWidgetFactory videoFactory;
	private static EMChatRowWidgetFactory voiceFactory;
	
	public static void setEMChatRowCallFactory(EMChatRowWidgetFactory factory) {
		callFactory = factory;
	}

	public static void setEMChatRowFileFactory(EMChatRowWidgetFactory factory) {
		fileFactory = factory;
	}

	public static void setEMChatRowImageFactory(EMChatRowWidgetFactory factory) {
		imageFactory = factory;
	}

	public static void setEMChatRowLocationFactory(EMChatRowWidgetFactory factory) {
		locationFactory = factory;
	}
	
	public static void setEMChatRowTextFactory(EMChatRowWidgetFactory factory) {
		textFactory = factory;
	}
	
	public static void setEMChatRowVideoFactory(EMChatRowWidgetFactory factory) {
		videoFactory = factory;
	}
	
	public static void setEMChatRowVoiceFactory(EMChatRowWidgetFactory factory) {
		voiceFactory = factory;
	}

	public static EMChatRowWidget getCallWidget(Context context, EMMessage message, int position, ViewGroup parent) {
		if (callFactory == null) {
			callFactory = new EMChatRowWidgetCallFactoryImpl();
		}
		return callFactory.getEMChatRowWidget(context, message, position, parent);
	}
	
	public static EMChatRowWidget getFileWidget(Context context, EMMessage message, int position, ViewGroup parent) {
		if (fileFactory == null) {
			fileFactory = new EMChatRowWidgetFileFactoryImpl();
		}
		return fileFactory.getEMChatRowWidget(context, message, position, parent);
	}
	
	public static EMChatRowWidget getImageWidget(Context context, EMMessage message, int position, ViewGroup parent) {
		if (imageFactory == null) {
			imageFactory = new EMChatRowWidgetImageFactoryImpl();
		}
		return imageFactory.getEMChatRowWidget(context, message, position, parent);
	}
	
	public static EMChatRowWidget getLocationWidget(Context context, EMMessage message, int position, ViewGroup parent) {
		if (locationFactory == null) {
			locationFactory = new EMChatRowWidgetLocationFactoryImpl();
		}
		return locationFactory.getEMChatRowWidget(context, message, position, parent);
	}
	
	public static EMChatRowWidget getTextWidget(Context context, EMMessage message, int position, ViewGroup parent) {
		if (textFactory == null) {
			textFactory = new EMChatRowWidgetTextFactoryImpl();
		}
		return textFactory.getEMChatRowWidget(context, message, position, parent);
	}
	
	public static EMChatRowWidget getVideoWidget(Context context, EMMessage message, int position, ViewGroup parent) {
		if (videoFactory == null) {
			videoFactory = new EMChatRowWidgetVideoFactoryImpl();
		}
		return videoFactory.getEMChatRowWidget(context, message, position, parent);
	}
	
	public static EMChatRowWidget getVoiceWidget(Context context, EMMessage message, int position, ViewGroup parent) {
		if (voiceFactory == null) {
			voiceFactory = new EMChatRowWidgetVoiceFactoryImpl();
		}
		return voiceFactory.getEMChatRowWidget(context, message, position, parent);
	}
	
	private static class EMChatRowWidgetCallFactoryImpl extends EMChatRowWidgetFactory {

		@Override
		public EMChatRowWidget getEMChatRowWidget(Context context, EMMessage message, int position, ViewGroup parent) {
			return new EMChatRowCallWidget(context, message, position, parent);
		}
	}
	
	private static class EMChatRowWidgetFileFactoryImpl extends EMChatRowWidgetFactory {

		@Override
		public EMChatRowWidget getEMChatRowWidget(Context context, EMMessage message, int position, ViewGroup parent) {
			return new EMChatRowFileWidget(context, message, position, parent);
		}
	}
	
	private static class EMChatRowWidgetImageFactoryImpl extends EMChatRowWidgetFactory {

		@Override
		public EMChatRowWidget getEMChatRowWidget(Context context, EMMessage message, int position, ViewGroup parent) {
			return new EMChatRowImageWidget(context, message, position, parent);
		}
	}
	
	private static class EMChatRowWidgetLocationFactoryImpl extends EMChatRowWidgetFactory {

		@Override
		public EMChatRowWidget getEMChatRowWidget(Context context, EMMessage message, int position, ViewGroup parent) {
			return new EMChatRowLocationWidget(context, message, position, parent);
		}
	}
	
	private static class EMChatRowWidgetTextFactoryImpl extends EMChatRowWidgetFactory {

		@Override
		public EMChatRowWidget getEMChatRowWidget(Context context, EMMessage message, int position, ViewGroup parent) {
			return new EMChatRowTextWidget(context, message, position, parent);
		}
	}
	
	private static class EMChatRowWidgetVideoFactoryImpl extends EMChatRowWidgetFactory {

		@Override
		public EMChatRowWidget getEMChatRowWidget(Context context, EMMessage message, int position, ViewGroup parent) {
			return new EMChatRowVideoWidget(context, message, position, parent);
		}
	}
	
	private static class EMChatRowWidgetVoiceFactoryImpl extends EMChatRowWidgetFactory {

		@Override
		public EMChatRowWidget getEMChatRowWidget(Context context, EMMessage message, int position, ViewGroup parent) {
			return new EMChatRowVoiceWidget(context, message, position, parent);
		}
	}
}

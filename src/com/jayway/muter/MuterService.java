package com.jayway.muter;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

public class MuterService extends Service {

	private static final int MUTE = 1;
	private static final int STOP = 2;
	private MuteHandler muteHandler;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread thread = new HandlerThread("Continuous muting thread",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		muteHandler = new MuteHandler(thread.getLooper());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && "stop".equals(intent.getDataString())) {
			muteHandler.sendEmptyMessage(STOP);
			return Service.START_NOT_STICKY;
		} else {
			muteHandler.sendEmptyMessage(MUTE);
			return Service.START_STICKY;
		}
	}

	private class MuteHandler extends Handler {

		private final AudioManager audioManager;

		public MuteHandler(Looper looper) {
			super(looper);
			audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MUTE:
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
				Message newMsg = obtainMessage(MUTE);
				sendMessageDelayed(newMsg, 500);
				break;
			case STOP:
				audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
				this.removeMessages(MUTE);
				getLooper().quit();
				stopSelf();
				break;
			default:
				break;
			}
		}
	}
}

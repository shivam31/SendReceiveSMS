package com.shivamdev.sendreceivesms;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class L {
	public static void t(Context a, String msg) {
		Toast.makeText(a, msg, Toast.LENGTH_SHORT).show();
	}

	public static void l(String msg) {
		Log.d("LOG", msg);
	}
	
	public static void l(String tag, String msg) {
		Log.d(tag, msg);
	}
}

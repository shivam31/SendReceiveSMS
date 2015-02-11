package com.shivamdev.sendreceivesms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    EditText etTextMsg, etPhoneNum, etMessages;
    Button sendButton;

    static String messages = "";

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTextMsg = (EditText) findViewById(R.id.txtMsgEditText);
        etPhoneNum = (EditText) findViewById(R.id.pNumEditText);
        etMessages = (EditText) findViewById(R.id.messagesEditText);
        sendButton = (Button) findViewById(R.id.sendButton);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                etMessages.setText(messages);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void sendMessage(View view) {

        String phoneNumber = etPhoneNum.getText().toString();
        String message = etTextMsg.getText().toString();
        try {


            SmsManager smsManager = SmsManager.getDefault();

            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            L.t(this, "Message Sent");
        } catch (IllegalArgumentException e) {
            L.t(this, "Enter a Phone Number or Message");
            L.l(e.getMessage());
        }

        messages = messages + "You : " + message + "\n";
    }

    public static class SmsReceiver extends BroadcastReceiver {

        final SmsManager smsManager = SmsManager.getDefault();

        public SmsReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle bundle = intent.getExtras();

            try {
                if(bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for(int i = 0; i < pdusObj.length; i++) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                        String phoneNumber = smsMessage.getDisplayOriginatingAddress();

                        String message = smsMessage.getDisplayMessageBody();

                        messages = messages + phoneNumber + " : " + message + "\n";
                    }
                }
            } catch (Exception e) {
                L.l("SmsReceiver", "Exception smsReceiver");
            }


        }
    }

    public class MMSReceiver extends BroadcastReceiver {

        public MMSReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            throw new UnsupportedOperationException("Not Implemented Yet");
        }
    }

    public class HeadlessSmsSendService extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            throw new UnsupportedOperationException("Not Implemented Yet");
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}

package com.alexander.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {


    private TextView textView;

    private Messenger mService;

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();
    }

    private void initViews(){

        textView = findViewById(R.id.textView);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mService = new Messenger(service);
            Message message = Message.obtain(null,
                    MyIntentService.MSG_REGISTER_CLIENT);
            message.replyTo = mMessenger;
            try {
                mService.send(message);
            }
            catch (RemoteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mService = null;
        }
    };

    private class IncomingHandler extends Handler{

        @Override
        public void handleMessage(Message message){
            super.handleMessage(message);
            switch (message.what){

                case MyIntentService.MSG_SET_VALUE:
                    textView.setText(message.obj.toString());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindService();
    }

    public void bindService(){

     bindService(MyIntentService.newIntent(this),mServiceConnection,Context.BIND_AUTO_CREATE);

    }
    public void unBindService(){
        Message msg = Message.obtain(null,
                MyIntentService. MSG_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;

        try {
            mService.send(msg);
        } catch (RemoteException e){
            e.printStackTrace();
        }

        unbindService(mServiceConnection);
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, SecondActivity.class);
        return intent;
    }
}

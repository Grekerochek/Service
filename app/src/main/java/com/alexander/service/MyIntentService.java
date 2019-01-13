package com.alexander.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MyIntentService extends IntentService {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 0;
    public static final int MSG_SET_VALUE = 2;


    private List<Messenger> mClients = new ArrayList<>();
    private Messenger mMessenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return mMessenger.getBinder();
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int i=0;
        while(true) {
            for (Messenger messenger: mClients){

                try {
                    messenger.send(Message.obtain(null, MSG_SET_VALUE, i));
                    i++;
                    if (i>100) {
                        return;
                    }
                    Thread.sleep(2000);
                } catch (RemoteException e){

                    Log.v("RemoteException", e.getMessage());

                } catch (InterruptedException t){

                    Log.v("InterruptedException", t.getMessage());
                }
            }
        }
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MyIntentService.class);
        return intent;
    }

    private class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_VALUE:
                    break;
            }
        }
    }
}

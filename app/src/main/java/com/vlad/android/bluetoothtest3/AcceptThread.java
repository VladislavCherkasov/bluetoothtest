package com.vlad.android.bluetoothtest3;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

//to start as a server

public class AcceptThread extends Thread{
    private static final String TAG = "AcceptThread";

    Context mContext;

    // The local server socket
    private final BluetoothServerSocket mmServerSocket;
    private final BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private BluetoothDevice mmDevice;


    public ConnectedThread getmConnectedThread() {
        return mConnectedThread;
    }

    private ConnectedThread mConnectedThread;

    public AcceptThread(Context context){
        BluetoothServerSocket tmp = null;
        mContext = context;


        // Create a new listening server socket
        try{
            tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

            Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
        }catch (IOException e){
            Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
        }

        mmServerSocket = tmp;
    }

    public void run(){
        Log.d(TAG, "run: AcceptThread Running.");


        BluetoothSocket socket = null;

        try{
            // This is a blocking call and will only return on a
            // successful connection or an exception
            Log.d(TAG, "run: RFCOM server socket start.....");

            socket = mmServerSocket.accept();

            Log.d(TAG, "run: RFCOM server socket accepted connection.");


        }catch (IOException e){
            Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
        }

        //talk about this is in the 3rd
        if(socket != null){
            //connected(socket,mmDevice);
            mConnectedThread = new ConnectedThread(socket,mContext);
            mConnectedThread.start();

        }

        Log.i(TAG, "END mAcceptThread ");
    }

    public void cancel() {
        Log.d(TAG, "cancel: Canceling AcceptThread.");
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
        }
    }
}

package com.vlad.android.bluetoothtest3;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

//to start as a client

public class ConnectThread extends Thread{

    private static final String TAG = "ConnectThread";
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    Context mContext;

    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private BluetoothSocket mmSocket;

    public ConnectedThread getmConnectedThread() {
        return mConnectedThread;
    }

    private ConnectedThread mConnectedThread;

    public ConnectThread(BluetoothDevice device, UUID uuid,Context context) {
        Log.d(TAG, "ConnectThread: started.");
        mmDevice = device;
        deviceUUID = uuid;
        mContext = context;
    }

    public void run(){
        BluetoothSocket tmp = null;
        Log.i(TAG, "RUN mConnectThread ");

        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                    +MY_UUID_INSECURE );
            tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
        } catch (IOException e) {
            Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
        }

        mmSocket = tmp;

        // Always cancel discovery because it will slow down a connection
        mBluetoothAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket

        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mmSocket.connect();

            Log.d(TAG, "run: ConnectThread connected.");

        } catch (IOException e) {
            // Close the socket
            try {
                mmSocket.close();
                Log.d(TAG, "run: Closed Socket.");
            } catch (IOException e1) {
                Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
            }
            Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE );
        }

        //will talk about this in the 3rd video
      //  connected(mmSocket,mmDevice);
        mConnectedThread = new ConnectedThread(mmSocket,mContext);
        mConnectedThread.start();

    }
    public void cancel() {
        try {
            Log.d(TAG, "cancel: Closing Client Socket.");
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
        }
    }

}

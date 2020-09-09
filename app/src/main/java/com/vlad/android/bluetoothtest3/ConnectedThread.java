package com.vlad.android.bluetoothtest3;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ConnectedThread extends Thread{

    private static final String TAG = "ConnectedThread";

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    Context mContext;

    public ConnectedThread(BluetoothSocket socket, Context context) {
        Log.d(TAG, "ConnectedThread: Starting.");
        mContext=context;

        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;




        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run(){
        byte[] buffer = new byte[1024];  // buffer store for the stream

        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            // Read from the InputStream
            try {
                bytes = mmInStream.read(buffer);
                String incomingMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "InputStream: " + incomingMessage);

                // передача данных активности приложения
                Intent incomingMesageIntent = new Intent("incomingMessage");
                incomingMesageIntent.putExtra("theMessage",incomingMessage);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(incomingMesageIntent);

            } catch (IOException e) {
                Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                break;
            }
        }
    }

    //Call this from the main activity to send data to the remote device
    public void write(byte[] bytes) {
        String text = new String(bytes, Charset.defaultCharset());
        Log.d(TAG, "write: Writing to outputstream: " + text);
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}

package com.websarva.wings.android.recycle_button_layout;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectSocketAsyncTaskLoader extends AsyncTaskLoader<String> {
    private String ipAddress;
    private String sendMessage;

    private String mResult;
    private boolean mIsStarted = false;

    ConnectSocketAsyncTaskLoader(Context _context, String _ipAddress, String _sendMessage){
        super(_context);
        this.ipAddress = _ipAddress;
        this.sendMessage = _sendMessage;
    }
    @Override
    public String loadInBackground(){
        if(!isLoadInBackgroundCanceled()){
            try{
                InetSocketAddress endPoint_ = new InetSocketAddress(ipAddress, 8084);
                Socket sender_ = new Socket();

                sender_.connect(endPoint_, 1000);
                if(sender_.isConnected()){
                    PrintWriter pw_ = new PrintWriter(sender_.getOutputStream(), true);
                    pw_.println(sendMessage);
                    pw_.close();
                }
                sender_.close();

                return "String data sent";
            }
            catch (UnknownHostException uhe){
                return uhe.getMessage();
            }
            catch (IOException ioe){
                System.out.println(ioe.getMessage());
              return null;
            }
        }
        return null;
    }
    @Override
    protected void onStartLoading(){
        if(mResult != null){
            deliverResult(mResult);
            return;
        }
        if(!mIsStarted || takeContentChanged()) forceLoad();

        super.onStartLoading();
        forceLoad();
    }
    @Override
    protected void onForceLoad(){
        super.onForceLoad();
        mIsStarted = true;
    }
    @Override
    public void deliverResult(String _data){
        mResult = _data;
        super.deliverResult(_data);
    }
}

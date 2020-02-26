package com.websarva.wings.android.recycle_button_layout;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectSocketAsyncTaskLoader extends AsyncTaskLoader<LoggingResult> {
    private String ipAddress;
    private String sendMessage;
    private int productPos;

    private LoggingResult result;
    private boolean isStarted = false;

    ConnectSocketAsyncTaskLoader(Context _context, int _pos, String _ipAddress, String _sendMessage){
        super(_context);
        this.productPos = _pos;
        this.ipAddress = _ipAddress;
        this.sendMessage = _sendMessage;
    }
    @Override
    public LoggingResult loadInBackground(){
        if(!isLoadInBackgroundCanceled()){
            try{
                String output_ = null;
                InetSocketAddress endPoint_ = new InetSocketAddress(ipAddress, 8084);
                Socket sender_ = new Socket();

                sender_.connect(endPoint_, 1000);
                if(sender_.isConnected()){
                    PrintWriter pw_ = new PrintWriter(sender_.getOutputStream(), true);
                    pw_.println(sendMessage);

                    BufferedReader br_ = new BufferedReader(new InputStreamReader(sender_.getInputStream()));
                    output_ = String.valueOf(br_.readLine());
                    pw_.close();
                    br_.close();
                }
                sender_.close();

                return new LoggingResult(this.productPos, output_);
            }
            catch (UnknownHostException uhe){
                return new LoggingResult(this.productPos, null);
            }
            catch (IOException ioe){
              return new LoggingResult(this.productPos, null);
            }
        }
        return new LoggingResult(this.productPos, null);
    }
    @Override
    protected void onStartLoading(){
        if(result != null){
            deliverResult(result);
            return;
        }
        if(!isStarted || takeContentChanged()) forceLoad();

        super.onStartLoading();
        forceLoad();
    }
    @Override
    protected void onForceLoad(){
        super.onForceLoad();
        isStarted = true;
    }
    @Override
    public void deliverResult(LoggingResult _res){
        result = _res;
        super.deliverResult(_res);
    }
}

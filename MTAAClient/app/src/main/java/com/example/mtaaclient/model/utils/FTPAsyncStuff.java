package com.example.mtaaclient.model.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FTPAsyncStuff extends AsyncTask<File, String, String> {

    private Context context;
    private ResponseDelegate delegate = null;

    public FTPAsyncStuff(Context context, ResponseDelegate delegate) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected String doInBackground(File... files) {
        try {
            File toProcessFile = files[0];
            if (toProcessFile.exists()) {
                File file = new File(context.getFilesDir(), String.valueOf(System.currentTimeMillis()) + ".png");
                copyFile(toProcessFile, file);
                return FTPSender.sendFileToFtp(file);
            }
        } catch (Exception e) {

        }
        return null;
    }

    private void copyFile(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    @Override
    protected void onPostExecute(String fileUrl) {
        super.onPostExecute(fileUrl);
        if (delegate != null) {
            if (fileUrl == null) {
                delegate.processResponse(new ResponseResult(500));
            } else {
                delegate.processResponse(new ResponseResult(fileUrl, 200));
            }
        }
    }
}

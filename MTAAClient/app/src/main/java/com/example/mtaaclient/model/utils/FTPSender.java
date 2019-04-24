package com.example.mtaaclient.model.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class FTPSender {

    private static final String FTP_HOST = "HOST";

    private static final String FTP_USER = "USER";

    private static final String FTP_PASSWORD = "PASSWORD";

    public static final String FTP_URL = "URL";

    private static final String TAG = FTPSender.class.getSimpleName();

    public static String sendFileToFtp(File targetFile) {
        FTPClient client = null;
        try {
            client = obtainConnection();
            client.upload(targetFile, new FtpListenerAsObject());
            return FTP_URL + targetFile.getName();
        } catch (Exception e) {
            Log.e(TAG, "Upload exception " + e.getMessage());
        } finally {
            if (client != null) {
                try {
                    if (client.isConnected()) {
                        client.disconnect(true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "error during closing connection " + e.getMessage());
                }
            }
        }
        return null;
    }

    private static FTPClient obtainConnection() throws FTPException, IOException, FTPIllegalReplyException {
        FTPClient client = new FTPClient();
        client.connect(FTP_HOST, 21);
        client.login(FTP_USER, FTP_PASSWORD);
        client.setType(FTPClient.TYPE_AUTO);
        client.changeDirectory("mtaa_test");
        return client;
    }

    private static class FtpListenerAsObject implements FTPDataTransferListener {

        private final String TAG = FtpListenerAsObject.class.getSimpleName();


        @Override
        public void started() {
            Log.d(TAG, "transfer started");
        }

        @Override
        public void transferred(int i) {

        }

        @Override
        public void completed() {
            Log.d(TAG, "transfer completed");
        }

        @Override
        public void aborted() {

        }

        @Override
        public void failed() {
            Log.e(TAG, "transfer failed");
        }
    }


}


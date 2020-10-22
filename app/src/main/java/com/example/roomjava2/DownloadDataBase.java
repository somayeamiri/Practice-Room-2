package com.example.roomjava2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ir.mahdi.mzip.zip.ZipArchive;


// @tip : 8 Step
public class DownloadDataBase extends AsyncTask<String, Integer, Integer> {


    private ProgressDialog  progressDialog;
    private Context context;
    private String NAME;
    private String path;
    private UnzipListener unzipListener;

    public DownloadDataBase(Context context, String name,UnzipListener unzipListener) {
        this.context = context;
        this.NAME = name;
        this.unzipListener=unzipListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //create progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Download Database");
        progressDialog.setMessage("downloading ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }


    @Override
    protected Integer doInBackground(String... params) {

        String URL_ADDRESS=params[0];
        String PATHNAME=params[1]; // path for download and unzip
        path =PATHNAME;
        //check folder exist
        File file = new File(PATHNAME);
        if (!file.exists()) {
            file.mkdirs();
        }


        try {
            // 1- get url and create connection
            URL url = new URL(URL_ADDRESS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // 2 get file lenght
            int fileLength = connection.getContentLength();

            // 3- create input stream and output stream
            InputStream input = connection.getInputStream();
            OutputStream output = new FileOutputStream(PATHNAME + NAME);

            //4- create byte array for read data
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                //5-  publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }

            output.close();
            input.close();
            connection.disconnect();

        } catch (Exception e) {

            Log.e("log",e.getMessage());
        }


        //6- download finished and we want to update the progress dialog UI
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Unzipping");
                progressDialog.show();
            }
        });


        //7- Unzip
        ZipArchive zipArchive = new ZipArchive();
        zipArchive.unzip(PATHNAME + NAME,PATHNAME, "");


        return 0;
    }


    @Override
    protected void onPostExecute(Integer aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "download done", Toast.LENGTH_SHORT).show();

        //8 -delete zip file

        try {
            File file = new File(path +NAME);
            file.delete();
        }catch (Exception e){
            Log.e(MainActivity.TAG,e.getMessage());
        }

        //8- call interface from main activity
        unzipListener.unzipFinished();
//        ((MainActivity)context).connectDatabasAndQuery();
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        progressDialog.setProgress(values[0]);
    }


}

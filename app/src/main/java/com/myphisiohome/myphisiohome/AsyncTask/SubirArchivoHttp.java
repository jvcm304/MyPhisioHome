package com.myphisiohome.myphisiohome.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;


import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Vicente on 13/6/17.
 */

public class SubirArchivoHttp extends AsyncTask<Void, Void, Integer> {

    private static String URL_SERVIDOR = "http://myphisio.digitalpower.es/v1/subirImagen.php";
    private String imageString;
    private int result;
    public SubirArchivoHttp(String imageString){
        this.imageString=imageString;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        Log.e("REsult: :",Integer.toString(result));
    }

    @Override
    protected Integer doInBackground(Void... params) {

        RequestParams param = new RequestParams();
        File file= new File(imageString);
        try {

            param.put("imagen", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SyncHttpClient client = new SyncHttpClient();
        client.post(URL_SERVIDOR, param, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                System.out.println("statusCode "+statusCode);//statusCode 200
                result =statusCode;
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("ImagenString: :",imageString);
            }

        });
        return result;
    }

}
package com.myphisiohome.myphisiohome.AsyncTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vicente on 26/6/17.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{

    private CircleImageView imagen2;
    private ImageView imagen;
    private int tipo;
    public DownloadImageTask(ImageView imagen,CircleImageView imagen2,int tipo) {
        this.tipo=tipo;
        if(tipo==1){
            this.imagen=imagen;
        }else{
            this.imagen2=imagen2;
        }
    }

    protected void onPreExecute()
    {
        //imagen2.setImageDrawable(context.getResources().getDrawable(R.drawable.cargar2));
    }

    protected Bitmap doInBackground(String... urls)
    {
        Log.d("DEBUG", "drawable");

        return downloadImage(urls[0]);

    }

    protected void onPostExecute(Bitmap imagenB)
    {
        if(tipo==1){
            imagen.setImageBitmap(imagenB);
        }else{
            imagen2.setImageBitmap(imagenB);
        }

    }

    /**
     * Devuelve una imagen desde una URL
     *
     * @return Una imagen
     */
    private Bitmap downloadImage(String imageUrlS)
    {
        try
        {
            URL imageUrl = new URL(imageUrlS);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();

            return BitmapFactory.decodeStream(conn.getInputStream());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}

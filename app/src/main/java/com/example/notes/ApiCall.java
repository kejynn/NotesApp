package com.example.notes;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiCall
{
    public ApiCall (Activity context, String method, String endpoint)
    {
        Thread t = new Thread(() ->
        {
            try
            {
                URL url = new URL("http://kejynn.pythonanywhere.com/" + endpoint);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod(method);

                InputStream input = con.getInputStream();
                byte[] inbuf = new byte[512];
                String res = "";
                while (true)
                {
                    int num = input.read(inbuf);
                    if (num < 0) break;
                    res += new String(inbuf, 0, num);
                }

                con.disconnect();

                final String result = res;
                context.runOnUiThread(() -> {on_ready(result);});
            }
            catch (IOException ex)
            {
                Log.e("tag", ex.toString());
                context.runOnUiThread(() -> {on_fail();});
            }
        });
        t.start();
    }

    public void on_ready(String result) {}

    public void on_fail() {}
}

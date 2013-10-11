package com.example.adroitest.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static android.os.Environment.*;

public class DownloadService extends IntentService
{
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.example.adroitest.service";
    private int result = Activity.RESULT_CANCELED;

    public DownloadService()
    {
        super("DownloadService");
    }

    // Will be called asynchronously be Android...
    @Override
    protected void onHandleIntent(Intent intent)
    {
        // Check for external storage state...
        if (getExternalStorageState().equals(MEDIA_MOUNTED))
        {
            //1.  Get URL to download...
            String urlPath = intent.getStringExtra(URL);
            //1.1 Check urlPath...
            if(urlPath.isEmpty())
            {
                //1.2 Show warning message and abort service...
                //todo 1.2
                result = Activity.RESULT_CANCELED;
            }
            //2. Get the file name to save...
            String fileName = intent.getStringExtra(FILENAME);
            //2.1 Check fileName...
            if(fileName.isEmpty()){
                //2.2 Show warning message and abort service...
                //todo 2.2
            }
            //3. Create file output with path and filename...
            File output = new File(getExternalStorageDirectory(), fileName);
            //4. If file exists, delete...
            if (output.exists())
                output.delete();

            InputStream stream = null;
            FileOutputStream fos = null;

            try
            {
                //5. Open URL connection...
                URL url = new URL(urlPath);
                stream = url.openConnection().getInputStream();
                //6. Read stream data until finish...
                InputStreamReader reader = new InputStreamReader(stream);
                fos = new FileOutputStream(output.getPath());
                int next;
                while ((next = reader.read()) != -1)
                {
                    fos.write(next);
                }

                //6. Download successful finished...
                result = Activity.RESULT_OK;

                //7. Send data information to client intent...
                publishResults(output.getAbsolutePath(), result);
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                //8. Free memory objects...
                if (stream != null)
                {
                    try
                    {
                        stream.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (fos != null)
                {
                    try
                    {
                        fos.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        } else
        {
            // If external storage not mounted...

            //1. Show confirmation message to user to change directory...
            //todo 1

            //2. Collect user directory choice...
            //todo 2

            //3. Try save the file in that directory...
            //todo 3

            publishResults("External storage doesn't has mounted", Activity.RESULT_CANCELED);
        }
    }

    private void publishResults(String outputPath, int result)
    {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        // Send broadcast do system...
        sendBroadcast(intent);
    }
}

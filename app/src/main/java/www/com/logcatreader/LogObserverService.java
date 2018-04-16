package www.com.logcatreader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 18-4-8.
 */

public class LogObserverService extends Service implements Runnable {
    private String TAG = "logcat";
    public boolean isObserverLog = false;
    public File file=null;
    public BufferedWriter out = null;
    public final boolean status=false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action=intent.getAction();
        if(action==null)
            stopSelf();
        Log.i(TAG, "LogObserverService start");
        startLogObserver();
        return START_NOT_STICKY;
        //  return super.onStartCommand(intent, flags, startId);
    }

    public void startLogObserver() {
        Log.i(TAG, "startObserverLog");
        isObserverLog = true;
        Thread mTherad = new Thread(this);
        mTherad.start();
    }

    /**
     * 关闭检测日志
     */
    public void stopLogObserver() {
        isObserverLog = false;
    }

    @Override
    public void onDestroy() {
        stopLogObserver();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public void run() {
        try {
            File folder = new File("/mnt/sdcard/Android/syslog");
            if(!folder.exists())
            {
               boolean ret=folder.mkdirs();
               if(!ret) {
                   Log.e(TAG, "create folder failed");
                   return;
               }
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            String tg = simpleDateFormat.format(date);
            Log.e(TAG, tg);
            file = new File(folder, tg + ".txt");
            String line;
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    return;
                }
            }
            Process process = Runtime.getRuntime().exec("logcat");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            Log.e(TAG, "Process : " + process);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                out.write(line);
                out.write("\r\n");
                if(!isObserverLog){
                    out.close();
                    Log.e(TAG, "close" );
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        };
        Log.e(TAG, "thread exit" );
    }
}
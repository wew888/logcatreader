package www.com.logcatreader;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Button btn= null;
    private Intent logObserverIntent = null;
    private String TAG = "logcat";
    private static boolean flag=false;
    private Drawable start;
    private Drawable stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.start);
        start = getDrawable(R.drawable.start);
        stop = getDrawable(R.drawable.stop);
        logObserverIntent = new Intent(MainActivity.this, LogObserverService.class);
        logObserverIntent.setAction("start logObserverservice server");
        flag=isServiceWork(getApplicationContext(),"www.com.logcatreader.LogObserverService");
        if(flag)
            btn.setBackground(stop);
        else
            btn.setBackground(start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag) {
                    startLogObserverService();
                    btn.setBackground(stop);
                    Toast.makeText(getApplicationContext(), "开始录制" , Toast.LENGTH_SHORT).show();
                    flag=true;
                }
                else{
                    btn.setBackground(start);
                    stopService(logObserverIntent);
                    Toast.makeText(getApplicationContext(), "停止录制" , Toast.LENGTH_LONG).show();
                    flag=false;
                }
            }
        });
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_LOGS
        };
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void startLogObserverService() {
  //      stopService(logObserverIntent);
        startService(logObserverIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // if (logObserverIntent!=null){
       //     stopService(logObserverIntent);
       // }
    }

    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}

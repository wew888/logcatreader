package www.com.logcatreader;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by root on 18-4-8.
 */

public class BroastcastReceiverService extends Service {
    private BroadcastReceiver receiver;
    private String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
    private String TAG = "logcat";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"BroastcastReceiverService start");
        receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"onReceive");
            if (intent.getAction().equals(SECRET_CODE_ACTION)){
                Uri uri = intent.getData();
                String host=uri.getHost();
                String scheme=uri.getScheme();
                Log.e(TAG,host);
                Log.e(TAG,scheme);
                if(host.equals("888"))
                {
                    Log.e(TAG,"start logcatread app");
                    Intent intentobj = new Intent(BroastcastReceiverService.this,MainActivity.class);
                    startActivity(intentobj);
                }
            }
        }
    };
     IntentFilter filter = new IntentFilter(SECRET_CODE_ACTION);
     filter.addDataScheme("android_secret_code");
     registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
    }
}

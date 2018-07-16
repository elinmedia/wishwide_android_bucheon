package com.media.mobile.elin.wishwidemobile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkStateMonitoringReceiver
        extends BroadcastReceiver {
    private static final String TAG = "NetworkStateMonitoring";

    private Activity mActivity;

    public NetworkStateMonitoringReceiver(Activity activity) {
        this.mActivity = activity;
    }


    public NetworkStateMonitoringReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action= intent.getAction();

        Log.d(TAG, "액션 확인: " + action);


        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            try {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

                boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
                boolean isNetworkConnected = isNetworkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();

                if (isNetworkConnected) {
                    Log.i(TAG, "네트워크 연결됨");
                }
                else {
                    Log.i(TAG, "네트워크 끊김");
                }
            } catch (Exception e) {
                e.getStackTrace();
                Log.e(TAG, "네트워크 상태 모니터링 리시버 에러 발생");
            }
        }
    }

//    private boolean isNetworkAvailableAndConnected() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//
//        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
//        boolean isNetworkConnected = isNetworkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();
//
//        return isNetworkConnected;
//    }

//    private void checkAvailableConnection() {
//        ConnectivityManager connMgr = (ConnectivityManager) this
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        final android.net.NetworkInfo wifi = connMgr
//                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        final android.net.NetworkInfo mobile = connMgr
//                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//        if (wifi.isAvailable()) {
//
//            WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
//            WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
//            int ipAddress = myWifiInfo.getIpAddress();
//
//            System.out.println("WiFi address is "
//                    + android.text.format.Formatter.formatIpAddress(ipAddress));
//
//
//        } else if (mobile.isAvailable()) {
//
//            GetLocalIpAddress();
//        } else {
//
//        }
//    }

//    private String GetLocalIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf
//                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        return inetAddress.getHostAddress().toString();
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//            return "ERROR Obtaining IP";
//        }
//        return "No IP Available";
//    }
}

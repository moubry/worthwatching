package com.moubry.worthwatching.util;
import com.android.vending.licensing.AESObfuscator;
import com.android.vending.licensing.LicenseChecker;
import com.android.vending.licensing.LicenseCheckerCallback;
import com.android.vending.licensing.ServerManagedPolicy;
import com.moubry.worthwatching.R;
import com.moubry.worthwatching.ui.BaseActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;

public abstract class LicenseCheckActivity extends BaseActivity
{
    private static final String TAG = "LicenseCheckActivity";
    
    static boolean licensed = true;
    static boolean didCheck = false;
    static boolean checkingLicense = false;
    static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk2szvhl2bc2jPBowC+oCVEa/Bqw5KHJL8IH/XE1xcteFjf/GW6tJGtg+JlYDpP8W+YO4Syp3l7GWP/Re0lyr0Pn+ZA8W4HH8g3OVt6Y6j3XmLVlv/5ywFuqMDA0g+3SZ6teDjYeOEGsqbzIOVyKShsEDUzdg0/VkDEY1qUaTCGy/74ylmc8IZrgjurJar1z3kcYFdqDd0BY668H71zk16x2dTZQrSMV6y/n7NvJjKPzO2RCSijLvg5wTo8ZOHDn+Goe868rLyuAqIaSVxX0Wli+JF8lJIUCFlKQo4iU1NPrPNi8H6o3DviRmyvld0iRvxo1VggjLhY11lMFVoTdwxQIDAQAB";
 
    LicenseCheckerCallback mLicenseCheckerCallback;
    LicenseChecker mChecker;
    Handler mHandler;
 
    private static final byte[] SALT = new byte[]{
            -46, 65, 30, -128, -45, -57, 74, -64, 51, 21, -95, -49, 77, -116, -36, -113, -11, 32, -64, 89
    };
 
    protected void doCheck() {
 
        didCheck = false;
        checkingLicense = true; 
        mChecker.checkAccess(mLicenseCheckerCallback);
    }
 
    protected void checkLicense() {
 
        // Don't check the license for the free version or the Amazon Appstore version.
        if((Boolean.parseBoolean(getString(R.string.show_ads))) || (!getString(R.string.app_store).equals(getString(R.string.android_market))))
            return;
        
        Log.i(TAG, "checkLicense");
        mHandler = new Handler();
 
        // Try to use more data here. ANDROID_ID is a single point of attack.
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
 
        // Library calls this when it's done.
        mLicenseCheckerCallback = new MyLicenseCheckerCallback();
        // Construct the LicenseChecker with a policy.
        mChecker = new LicenseChecker(
                this, new ServerManagedPolicy(this,
                        new AESObfuscator(SALT, getPackageName(), deviceId)), BASE64_PUBLIC_KEY);
 
        doCheck();
    }
 
    protected class MyLicenseCheckerCallback implements LicenseCheckerCallback {
 
        public void allow() {
            Log.i(TAG, "allow");
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }

            licensed = true;
            checkingLicense = false;
            didCheck = true;
        }
 
        public void dontAllow() {
            Log.i(TAG, "dontAllow");
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            
            licensed = false;
            // Should not allow access. In most cases, the app should assume
            // the user has access unless it encounters this. If it does,
            // the app should inform the user of their unlicensed ways
            // and then either shut down the app or limit the user to a
            // restricted set of features.
            // In this example, we show a dialog that takes the user to Market.
            checkingLicense = false;
            didCheck = true;
 
            showDialog(0);
        }
 
        public void applicationError(ApplicationErrorCode errorCode) {
            Log.i(TAG, "error: " + errorCode);
            if (isFinishing()) {
                // Don't update UI if Activity is finishing.
                return;
            }
            licensed = true;
            // This is a polite way of saying the developer made a mistake
            // while setting up or calling the license checker library.
            // Please examine the error code and fix the error.
//            String result = String.format(getString(R.string.application_error), errorCode);
            checkingLicense = false;
            didCheck = true;

            //showDialog(0);
        }
    }
 
    protected Dialog onCreateDialog(int id) {
        // We have only one dialog.
        return new AlertDialog.Builder(this)
                .setTitle(R.string.unlicensed_dialog_title)
                .setMessage(R.string.unlicensed_dialog_body)
                .setPositiveButton(R.string.buy_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "http://market.android.com/details?id=" + getPackageName()));
                        startActivity(marketIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.quit_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
 
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener(){
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        Log.i(TAG, "Key Listener");
                        finish();
                        return true;
                    }
                })
                .create();
 
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChecker != null) {
            Log.i(TAG, "destroy checker");
            mChecker.onDestroy();
        }
    }
 
}

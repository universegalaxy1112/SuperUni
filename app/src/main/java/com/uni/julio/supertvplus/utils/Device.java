package com.uni.julio.supertvplus.utils;
import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.uni.julio.supertvplus.LiveTvApplication;
import java.io.File;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class Device {

    public static boolean treatAsBox = false;
    private static Device m_Device;
    public static boolean canTreatAsBox() {
        return treatAsBox;
    }
    public static Device getInstance() {
        if(m_Device == null) {
            m_Device = new Device();
        }
        return m_Device;
    }
    public static void setHDMIStatus() {

        /*if(true) { //change this to use a compilation flavor
            treatAsBox = true;
            return;
        }*/
        // The file '/sys/devices/virtual/switch/hdmi/state' holds an int -- if it's 1 then an HDMI device is connected.
        // An alternative file to check is '/sys/class/switch/hdmi/state' which exists instead on certain devices.
        File switchFile = new File("/sys/devices/virtual/switch/hdmi/state");
        if (!switchFile.exists()) {
            switchFile = new File("/sys/class/switch/hdmi/state");
        }
        try {
            Scanner switchFileScanner = new Scanner(switchFile);
            int switchValue = switchFileScanner.nextInt();
            switchFileScanner.close();
            treatAsBox = switchValue > 0;
        } catch (Exception e) {
            treatAsBox = false;
        }
        if (LiveTvApplication.getAppContext().getPackageManager().hasSystemFeature("android.software.live_tv")) {
            treatAsBox = true;
        }
        if (((UiModeManager) Objects.requireNonNull(LiveTvApplication.getAppContext().getSystemService(Context.UI_MODE_SERVICE))).getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            treatAsBox = true;
        }
        if (!LiveTvApplication.getAppContext().getPackageManager().hasSystemFeature("android.hardware.touchscreen")) {
            treatAsBox = true;
        }
        String model = Build.MODEL;
        if(model != null && (model.toLowerCase().contains("tx3") || model.toLowerCase().contains("t95") || model.toLowerCase().contains("mxq") || model.toLowerCase().contains("mbox"))) {
            treatAsBox = true;
        }

     }

    public static String getFW() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getModel() {

        return android.os.Build.MODEL;
    }

    public static String getCountry() {
        String country = "";
        try {
            final TelephonyManager tm = (TelephonyManager) LiveTvApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                country = simCountry.toLowerCase(Locale.US);
            }

            if (TextUtils.isEmpty(country)) {
                if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        country = networkCountry.toLowerCase(Locale.US);
                    }
                }

                if (TextUtils.isEmpty(country)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        country = LiveTvApplication.getAppContext().getResources().getConfiguration().getLocales().get(0).getCountry();
                    } else {
                        country = LiveTvApplication.getAppContext().getResources().getConfiguration().locale.getCountry();
                    }
                }
            }
        } catch (Exception e) {
            country = "mx";
        }
        return country;
    }

    public static String getIdentifier() {
        String id = DataManager.getInstance().getString("deviceIdentifier", "");
        /*if (!TextUtils.isEmpty(id) && id.length() >= 6) {
            return id;
        }

        TelephonyManager tm = (TelephonyManager) LiveTvApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        *//*if (LiveTvApplication.getAppContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            id = tm.getDeviceId();
        }*//*
         */
         if(TextUtils.isEmpty(id) || id.length() < 6) {
            id = UUID.randomUUID().toString();
        }
        DataManager.getInstance().saveData("deviceIdentifier", id);

        return id;
    }


    public static String getVersion() {
        String versionName = "1.0"; //default
        final PackageManager packageManager = LiveTvApplication.getAppContext().getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(LiveTvApplication.getAppContext().getPackageName(), 0);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = "1.0";
            }
        }
        return versionName;
    }
    public static String getVersionInstalled() {
        try {
            return LiveTvApplication.getAppContext().getPackageManager().getPackageInfo(LiveTvApplication.getAppContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "0.0.1";
        }
    }

}

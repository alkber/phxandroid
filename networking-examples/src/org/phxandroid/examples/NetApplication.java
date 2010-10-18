package org.phxandroid.examples;

import android.app.Application;
import android.content.SharedPreferences;

public class NetApplication extends Application {
    private String serverHostname;
    
    public String getServerHostname() {
        return serverHostname;
    }
    
    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        SharedPreferences prefs = getSharedPreferences(Prefs.NAME, 0);
        serverHostname = prefs.getString(Prefs.KEY_SERVER_HOSTNAME, "monolyth.local");
    }
}

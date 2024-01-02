package cather.lfree.workdscather;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class testB {

    Context contextM;

    public testB(Context constsdf ){
        contextM = constsdf;
    }

    public ArrayList<PInfo> getPackages() {
    ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
    final int max = apps.size();
    for (int i=0; i<max; i++) {
        apps.get(i).prettyPrint();
    }
    return apps;
}



  private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
    ArrayList<PInfo> res = new ArrayList<PInfo>();
    List<PackageInfo> packs = contextM.getPackageManager().getInstalledPackages(0);
    for(int i=0;i<packs.size();i++) {
        PackageInfo p = packs.get(i);
        if ((!getSysPackages) && (p.versionName == null)) {
            continue ;
        }
        PInfo newInfo = new PInfo();
        newInfo.appname = p.applicationInfo.loadLabel(contextM.getPackageManager()).toString();
        newInfo.pname = p.packageName;
        newInfo.versionName = p.versionName;
        newInfo.versionCode = p.versionCode;
        newInfo.icon = p.applicationInfo.loadIcon(contextM.getPackageManager());
        Log.d("dsf", " my app name: " + newInfo.appname + " - " + newInfo.pname);
        res.add(newInfo);
    }
    return res;
}

 Locale getCurrentLocale (Context context){
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               return context.getResources().getConfiguration().getLocales().get(0);
           } else {
               //noinspection deprecation
               return context.getResources().getConfiguration().locale;
           }
       }

    static class PInfo {
    private String appname = "";
    private String pname = "";
    private String versionName = "";
    private int versionCode = 0;
    private Drawable icon;
    private void prettyPrint() {
        Log.d("dsf",appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
     }
 }

}

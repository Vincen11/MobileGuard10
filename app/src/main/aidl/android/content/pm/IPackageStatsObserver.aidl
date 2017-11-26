// IPackageStatsObserver.aidl
package android.content.pm;

import android.content.pm.PackageStats;
oneway interface IPackageStatsObserver{

      void onGetStatsCompleted(in PackageStats pStats, boolean succeeded);
}

// Declare any non-default types here with import statements


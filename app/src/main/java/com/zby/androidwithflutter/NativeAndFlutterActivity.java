package com.zby.androidwithflutter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.idlefish.flutterboost.containers.FlutterFragment;

import io.flutter.embedding.android.DrawableSplashScreen;
import io.flutter.embedding.android.SplashScreen;
import io.flutter.embedding.android.SplashScreenProvider;


public class NativeAndFlutterActivity extends AppCompatActivity implements SplashScreenProvider {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_and_flutter);
        // 将flutter界面作为fragment添加到FrameLayout上
        FlutterFragment mFragment = new FlutterFragment.NewEngineFragmentBuilder().url("fragmentPage").build();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_stub, mFragment)
                .commit();
    }

    @Override
    public SplashScreen provideSplashScreen() {
        // 为显示加载loading
        Drawable manifestSplashDrawable = getSplashScreenFromManifest();
        if (manifestSplashDrawable != null) {
            return new DrawableSplashScreen(manifestSplashDrawable, ImageView.ScaleType.CENTER, 500L);
        } else {
            return null;
        }
    }

    protected static final String SPLASH_SCREEN_META_DATA_KEY = "io.flutter.embedding.android.SplashScreenDrawable";
    private Drawable getSplashScreenFromManifest() {
        try {
            @SuppressLint("WrongConstant") ActivityInfo activityInfo = getPackageManager().getActivityInfo(
                    getComponentName(),
                    PackageManager.GET_META_DATA | PackageManager.GET_ACTIVITIES
            );
            Bundle metadata = activityInfo.metaData;
            Integer splashScreenId = metadata != null ? metadata.getInt(SPLASH_SCREEN_META_DATA_KEY) : null;
            return splashScreenId != null
                    ? Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP
                    ? getResources().getDrawable(splashScreenId, getTheme())
                    : getResources().getDrawable(splashScreenId)
                    : null;
        } catch (PackageManager.NameNotFoundException e) {
            // This is never expected to happen.
            return null;
        }
    }
}

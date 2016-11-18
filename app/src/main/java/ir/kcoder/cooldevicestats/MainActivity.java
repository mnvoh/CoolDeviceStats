package ir.kcoder.cooldevicestats;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import ir.kcoder.cooldevicestats.Theme.ThemeArchive;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        new ThemeArchive(this).unpackDefaultThemes();
    }

    public void launchBatteryStats(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, BatteryStatsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        view.startAnimation(wiggle);
    }

    public void launchCpuStats(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, CpuStatsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        view.startAnimation(wiggle);
    }

    public void launchMemoryStats(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, MemoryStatusActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        view.startAnimation(wiggle);
    }

    public void launchProcessesStats(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, ProcessesActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        view.startAnimation(wiggle);
    }

    public void launchWifiStats(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, WifiStatsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        view.startAnimation(wiggle);
    }

    public void launchCellularStats(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, CellularStatsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        view.startAnimation(wiggle);
    }

    public void launchSettings(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, WidgetSettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        view.startAnimation(wiggle);
    }

    public void launchThemes(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(MainActivity.this, ThemesActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        view.startAnimation(wiggle);
    }

    public void launchHelp(View view) {
        Animation wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        wiggle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cds.kcoder.ir"));
                MainActivity.this.startActivity(browse);
            }
        });

        view.startAnimation(wiggle);
    }
}

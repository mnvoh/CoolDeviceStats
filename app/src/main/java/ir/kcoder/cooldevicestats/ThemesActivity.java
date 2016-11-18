package ir.kcoder.cooldevicestats;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import ir.kcoder.cooldevicestats.Theme.ThemeArchive;

import ir.kcoder.cooldevicestats.ThemeWidget.ThemeWidgetView;

public class ThemesActivity extends ActionBarActivity {
    public static final String THEMES_URL = "http://cds.kcoder.ir/get_themes.php";
    private LinearLayout themesListWrapper;
    private ProgressBar pbRefresh;
    private TextView txvNoInternet;
    private ArrayList<ThemeWidgetView> themeWidgetViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.themes);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        themesListWrapper = (LinearLayout)findViewById(R.id.themesListWrapper);

        pbRefresh = (ProgressBar)findViewById(R.id.pbThemesRefreshList);
        txvNoInternet = (TextView)findViewById(R.id.txvNoInternet);

        new ThemeListRefresher().execute();
    }

    public void refreshList(View v) {
        new ThemeListRefresher().execute();
    }

    public class ThemeListRefresher extends AsyncTask<Void, Void, Void> {
        private ArrayList<ThemeItem> list;
        private String themesPath;
        private boolean noInternet = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbRefresh.setVisibility(View.VISIBLE);

            if(themesListWrapper != null && themeWidgetViews != null && themeWidgetViews.size() > 0) {
                for(View v : themeWidgetViews) {
                    themesListWrapper.removeView(v);
                }
            }

            if(themeWidgetViews != null) {
                themeWidgetViews.clear();
            }

            noInternet = false;
            txvNoInternet.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getThemesList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            themeWidgetViews = new ArrayList<>();
            if(list.size() > 0) {
                for(int i = 0; i < list.size() && themesListWrapper != null; i++) {
                    ThemeWidgetView theme = new ThemeWidgetView(ThemesActivity.this);
                    theme.setTitle(list.get(i).themeName);
                    theme.setIsNew(list.get(i).isNew);
                    theme.setState(list.get(i).state);
                    themesListWrapper.addView(theme);
                    themeWidgetViews.add(theme);
                    theme.refreshThumbnail();
                }
            }
            pbRefresh.setVisibility(View.GONE);
            if(noInternet) {
                txvNoInternet.setVisibility(View.VISIBLE);
            }
        }

        private void getThemesList() {
            list = new ArrayList<>();
            ArrayList<String> tempList = new ArrayList<>();

            ThemeArchive themeArchive = new ThemeArchive(ThemesActivity.this);

            themeArchive.unpackDefaultThemes();

            themesPath = ThemesActivity.this.getExternalFilesDir(null).getParent() + "/themes/";
            File themesDir = new File(themesPath);

            File[] themes = themesDir.listFiles();

            if(themes != null && themes.length > 0) {
                for(File theme : themes) {
                    if(!theme.isDirectory() || !themeArchive.checkThemeIntegrity(theme.getName())) {
                        continue;
                    }
                    list.add(new ThemeItem(theme.getName(), false,
                            ThemeWidgetView.THEME_STATE_DOWNLOADED));
                    tempList.add(theme.getName());
                }
            }

            if(isNetworkAvailable()) {
                try {
                    URL listUrl = new URL(THEMES_URL);
                    BufferedReader br = new BufferedReader(new InputStreamReader(listUrl.openStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] __line = line.split(":");
                        if (!tempList.contains(__line[0])) {
                            boolean isNew = __line[1].equals("1");
                            list.add(new ThemeItem(__line[0], isNew, ThemeWidgetView.THEME_STATE_UNAVAILABLE));
                        }
                    }
                    br.close();
                } catch (IOException ioex) {
                    noInternet = true;
                } catch (Exception ex) {
                    noInternet = true;
                    ex.printStackTrace();
                }
            }
            else {
                noInternet = true;
            }

            sortByNew();
        }

        private void sortByNew() {
            if(list == null) {
                return;
            }
            ArrayList<ThemeItem> temp = new ArrayList<>();
            for(int i = 0; i < list.size(); i++) {
                if(list.get(i).isNew) {
                    temp.add(list.get(i));
                }
            }

            for(int i = 0; i < list.size(); i++) {
                if(!list.get(i).isNew) {
                    temp.add(list.get(i));
                }
            }
            this.list.clear();
            this.list = temp;
        }
    }

    public class ThemeItem {
        public String themeName;
        public boolean isNew;
        public int state;

        public ThemeItem(String themeName, boolean isNew, int state) {
            this.themeName = themeName;
            this.isNew = isNew;
            this.state = state;
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

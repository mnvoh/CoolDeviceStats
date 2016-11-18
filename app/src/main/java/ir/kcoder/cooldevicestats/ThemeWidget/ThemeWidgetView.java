package ir.kcoder.cooldevicestats.ThemeWidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ir.kcoder.cooldevicestats.CDSPrefsManager;
import ir.kcoder.cooldevicestats.R;
import ir.kcoder.cooldevicestats.Theme.ThemeArchive;
import ir.kcoder.cooldevicestats.ThemePreviewActivity;
import ir.kcoder.cooldevicestats.ThemesActivity;


/**
 * Created by mnvoh on 5/29/15.
 */
public class ThemeWidgetView extends LinearLayout implements View.OnClickListener {
    public static final int THEME_STATE_DOWNLOADED = 0x0;
    public static final int THEME_STATE_UNAVAILABLE = 0x1;

    private ImageView imvThumbnail;
    private ProgressBar pbDownload;
    private TextView txvTitle;
    private TextView txvIsNew;
    private Button btnThemeAction;
    private String themesPath;
    private String themeName;
    private int state;
    private CDSPrefsManager prefs;
    private OnBeginPurchaseRequest purchaseRequestListener = null;
    private ThemeArchive themeArchive;

    public ThemeWidgetView(Context context) {
        super(context);
        initializeViews(context);
    }

    public ThemeWidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public ThemeWidgetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    public String getTitle() {
        return themeName;
    }

    public void setTitle(String title) {
        themeName = title;
        txvTitle.setText(title);
        if(prefs == null) {
            prefs = new CDSPrefsManager(this.getContext());
        }
        if(themeName.equals(prefs.getActiveTheme())) {
            btnThemeAction.setVisibility(GONE);
        }
    }

    public void refreshThumbnail() {
        try {
            new ThemesPreviewRefreshser().execute();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setIsNew(boolean isNew) {
        if(!isNew) {
            txvIsNew.setVisibility(View.GONE);
        }
        else {
            this.setBackgroundResource(R.drawable.new_theme_bg);
        }
    }

    public void setState(int state) {
        this.state = state;
        if(state == THEME_STATE_DOWNLOADED) {
            btnThemeAction.setBackgroundResource(R.drawable.download_button_selector);
            btnThemeAction.setText(this.getContext().getText(R.string.set_theme));
        }
        else {
            btnThemeAction.setBackgroundResource(R.drawable.buy_button_selector);
            btnThemeAction.setText(this.getContext().getText(R.string.buy_and_download));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void initializeViews(Context context) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.layout_theme_compound_component, this);
        this.setOrientation(LinearLayout.HORIZONTAL);
        MarginLayoutParams lparams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(10, 10, 10, 20);
        this.setLayoutParams(lparams);
        this.setPadding(
                (int) this.getResources().getDimension(R.dimen.theme_widget_padding),
                (int) this.getResources().getDimension(R.dimen.theme_widget_padding),
                (int) this.getResources().getDimension(R.dimen.theme_widget_padding),
                (int) this.getResources().getDimension(R.dimen.theme_widget_padding)
        );

        imvThumbnail = (ImageView)this.findViewById(R.id.imvThemeThumbnail);
        pbDownload = (ProgressBar)this.findViewById(R.id.pbThemeDownloadProgress);
        txvTitle = (TextView)this.findViewById(R.id.txvThemeTitle);
        txvIsNew = (TextView)this.findViewById(R.id.txvIsNew);
        btnThemeAction = (Button)this.findViewById(R.id.btnThemeAction);
        btnThemeAction.setOnClickListener(this);
        imvThumbnail.setOnClickListener(this);

        themesPath = context.getExternalFilesDir(null).getParent() + "/themes/";

        prefs = new CDSPrefsManager(context);
        themeArchive = new ThemeArchive(context);
    }

    @Override
    public void onClick(View v) {
        if(v == btnThemeAction) {
            if(state == THEME_STATE_DOWNLOADED) {
                setDefaultTheme();
            }
            else {
                downloadTheme();
            }
        }
        else if(v == imvThumbnail) {
            Intent intent = new Intent(ThemeWidgetView.this.getContext(), ThemePreviewActivity.class);
            intent.putExtra(ThemePreviewActivity.EXTRA_THEME_NAME, themeName);
            this.getContext().startActivity(intent);
        }
    }

    public void setOnBeginPurchaseListener(OnBeginPurchaseRequest listener) {
        this.purchaseRequestListener = listener;
    }

    private void setDefaultTheme() {
        CDSPrefsManager prefs = new CDSPrefsManager(this.getContext());
        prefs.setActiveTheme(themeName);
        Toast.makeText(this.getContext(), this.getContext().getString(R.string.theme_changed) + ": "
                        + themeName, Toast.LENGTH_SHORT).show();
    }

    private void downloadTheme() {
        new ThemeDownloader().execute();
    }

    public class ThemesPreviewRefreshser extends AsyncTask<Void, Void, Void> {
        private String themesPath, outputPath;

        @Override
        protected Void doInBackground(Void... params) {
            getThemePreview();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(new File(outputPath).exists()) {
                imvThumbnail.setImageBitmap(BitmapFactory.decodeFile(outputPath));
            }
        }

        private void getThemePreview() {
            themesPath = ThemeWidgetView.this.getContext().getExternalFilesDir(null).getParent() +
                    "/themes/";

            File themeDir = new File(themesPath + themeName);
            if(!themeDir.exists()) {
                themeDir.mkdirs();
            }

            outputPath = themesPath + themeName + "/preview.jpg";

            if(!new File(outputPath).exists()) {
                InputStream input = null;
                FileOutputStream output = null;
                try {
                    String previewImageUrl = ThemesActivity.THEMES_URL + "?preview=" + themeName;
                    URL themePreviewUrl = new URL(previewImageUrl);
                    HttpURLConnection connection = (HttpURLConnection) themePreviewUrl.openConnection();
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        input = connection.getInputStream();
                        output = new FileOutputStream(outputPath);
                        int count;
                        byte buffer[] = new byte[4096];
                        while ((count = input.read(buffer)) != -1) {
                            output.write(buffer, 0, count);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (input != null) {
                            input.close();
                        }
                        if (output != null) {
                            output.flush();
                            output.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public class ThemeDownloader extends AsyncTask<Void, Integer, Void> {
        private String themesPath, outputPath;
        private boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(), "Starting Download...", Toast.LENGTH_SHORT).show();
            pbDownload.setVisibility(View.VISIBLE);
            pbDownload.setProgress(0);
            Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.slow_rotate);
            pbDownload.startAnimation(rotate);
        }

        @Override
        protected Void doInBackground(Void... params) {
            themesPath = ThemeWidgetView.this.getContext().getExternalFilesDir(null).getParent() +
                    "/themes/";

            outputPath = themesPath + "/" + themeName + ".cdstheme";

            InputStream input = null;
            FileOutputStream output = null;
            try {
                String strThemeUrl = ThemesActivity.THEMES_URL +
                        "?download=" + themeName;
                URL themeUrl = new URL(strThemeUrl);
                HttpURLConnection connection = (HttpURLConnection) themeUrl.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    input = connection.getInputStream();
                    output = new FileOutputStream(outputPath);
                    int count;
                    byte buffer[] = new byte[4096];
                    int filesize = connection.getContentLength();
                    int totalDone = 0;
                    while ((count = input.read(buffer)) != -1) {
                        output.write(buffer, 0, count);
                        totalDone += count;
                        publishProgress((int)((totalDone / (float)filesize) * 100));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                    if (output != null) {
                        output.flush();
                        output.close();
                        themeArchive.unpackTheme(themeName);
                        if(themeArchive.checkThemeIntegrity(themeName)) {
                            success = true;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);

            pbDownload.setVisibility(View.INVISIBLE);
            if(success) {
                Toast.makeText(
                        getContext(),
                        getContext().getString(R.string.theme_successfully_downloaded),
                        Toast.LENGTH_LONG
                ).show();
                setState(THEME_STATE_DOWNLOADED);
            }
            else {
                Toast.makeText(
                        getContext(),
                        getContext().getString(R.string.theme_download_failed),
                        Toast.LENGTH_LONG
                ).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pbDownload.setProgress(values[0]);
        }
    }
}

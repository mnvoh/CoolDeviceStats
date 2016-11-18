package ir.kcoder.cooldevicestats.Theme;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ir.kcoder.cooldevicestats.R;

/**
 * Created by mnvoh on 5/8/15.
 *
 */
public class ThemeArchive {
    Context context;
    public ThemeArchive(Context context) {
        this.context = context;
    }

    public boolean checkThemeIntegrity(String themeName) {
        String themePath = context.getExternalFilesDir(null).getParent() + "/themes/" +
                themeName + "/";

        if(!(new File(themePath + themeName + "-mdpi.conf").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-mdpi.png").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-hdpi.conf").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-hdpi.png").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-xhdpi.conf").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-xhdpi.png").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-xxhdpi.conf").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-xxhdpi.png").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-xxxhdpi.conf").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-xxxhdpi.png").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-tvdpi.conf").exists())) {
            return false;
        }

        if(!(new File(themePath + themeName + "-tvdpi.png").exists())) {
            return false;
        }

        return true;
    }

    public void unpackTheme(String themeName) {
        String themePath = context.getExternalFilesDir(null).getParent() + "/themes/";
        unpackZip(themePath, themeName + ".cdstheme");
    }

    public void unpackDefaultThemes() {
        final int BUFFER_LENGTH = 4096;
        if(checkThemeIntegrity("Flat")) {
            return;
        }
        String themePath = context.getExternalFilesDir(null).getParent() + "/themes/";
        File themesDir = new File(themePath);
        if(!themesDir.exists()) {
            themesDir.mkdirs();
        }
        InputStream is = context.getResources().openRawResource(R.raw.flat);
        int size;
        byte[] buffer = new byte[BUFFER_LENGTH];

        try {
            File defThemeFile = new File(themesDir, "Flat.cdstheme");
            defThemeFile.createNewFile();
            FileOutputStream fo = new FileOutputStream(defThemeFile);
            while ((size = is.read(buffer, 0, BUFFER_LENGTH)) > 0) {
                fo.write(buffer, 0, size);
            }
            is.close();
            fo.close();
        }
        catch(Exception ex) {
            Log.e("THEME_UNPACK_ERROR", ex.getMessage());
        }

        unpackZip(themePath, "Flat.cdstheme");
    }

    private boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            Log.e("THEME_DECOMPRESSION", e.getMessage());
            return false;
        }

        return true;
    }
}

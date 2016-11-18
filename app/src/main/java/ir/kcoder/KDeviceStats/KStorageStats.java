package ir.kcoder.KDeviceStats;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by mnvoh on 3/19/15.
 */
public class KStorageStats {
    private static KStorageStats storageStats = null;
    private long totalInternal, usedInternal;
    private long totalExternal, usedExternal;

    public static KStorageStats getInstance() {
        if(storageStats == null) {
            storageStats = new KStorageStats();
        }
        return storageStats;
    }

    protected KStorageStats() {
        refreshStats();
    }

    /** Refreshes the storage stats */
    public void refreshStats() {
        String internalPath = Environment.getDataDirectory().getPath();
        String externalPath = getExternalSdCardPath();
        StatFs internalStats = new StatFs(internalPath);

        StatFs externalStats = null;
        if(externalPath != null) {
            externalStats = new StatFs(externalPath);
        }

        if(Build.VERSION.SDK_INT >= 18) {
            totalInternal = internalStats.getTotalBytes();
            usedInternal = totalInternal - internalStats.getAvailableBytes();

            if(externalStats == null) {
                totalExternal = 0;
                usedExternal = 0;
            }
            else {
                totalExternal = externalStats.getTotalBytes();
                usedExternal = totalExternal - externalStats.getAvailableBytes();
            }
        }
        else {
            totalInternal = (long)internalStats.getBlockCount() * (long)internalStats.getBlockSize();
            usedInternal = totalInternal - (long)internalStats.getAvailableBlocks()
                    * (long)internalStats.getBlockSize();

            if(externalStats == null) {
                totalExternal = 0;
                usedExternal = 0;
            }
            else {
                totalExternal = (long) externalStats.getBlockCount() * (long) externalStats.getBlockSize();
                usedExternal = totalExternal - (long) externalStats.getAvailableBlocks()
                        * (long) externalStats.getBlockSize();

            }
        }

    }

    /** Gets the total amount of internal memory in MBs */
    public long getTotalInternal() { return totalInternal / (1024 * 1024); }

    /** Gets the total amount of used internal memory in MBs */
    public long getUsedInternal() { return usedInternal / (1024 * 1024); }

    /** Gets the percentage of used internal memory. */
    public int getInternalPercentage() {
        return (int) ((usedInternal / (double)totalInternal) * 100);
    }

    /** Gets the total amount of external memory in MBs. */
    public long getTotalExternal() { return totalExternal / (1024 * 1024); }

    /** Gets the total amount of used external memory in MBs. */
    public long getUsedExternal() { return usedExternal / (1024 * 1024); }

    /** Gets the percentage amount of used external memory. */
    public int getExternalPercentage() {
        if(totalExternal <= 0)
            return -1;
        return (int) ((usedExternal / (double)totalExternal) * 100);
    }

    /**
     * Finds the external SD card path (mount dir) from /proc/mounts.
     * @return the path to external SD card.
     */
    private String getExternalSdCardPath() {
        String path;
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/mounts", "r");
            while((path = reader.readLine()) != null) {
                if (path.contains("/dev/block/vold")) {
                    if (!path.contains("/mnt/secure")
                            && !path.contains("/mnt/asec")
                            && !path.contains("/mnt/obb")
                            && !path.contains("/dev/mapper")
                            && !path.contains("tmpfs")) {
                        break;
                    }
                }
                path = null;
            }
            reader.close();
        } catch(IOException ioex) {
            return null;
        }

        if(path != null) {
            String[] path_parts = path.split(" +");
            File file = new File(path_parts[1]);
            if(file.exists()) {
                return path_parts[1];
            }
            else {
                return null;
            }
        }
        return path;
    }
}

package ir.kcoder.KDeviceStats;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by mnvoh on 3/19/15.
 */
public class KRAMStats {
    private static KRAMStats ramStats = null;
    /** The total amount of memory available (kb) */
    private int total;

    /** The amount of memory actively being used. (kb) */
    private int active;

    /** The free amount of memory, excluding cached, inactive, etc values. (kb) */
    private int free;

    public static KRAMStats getInstance() {
        if(ramStats == null) {
            ramStats = new KRAMStats();
        }
        return ramStats;
    }

    protected KRAMStats() {
        refreshStats();
    }

    public void refreshStats() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
            String line;
            int readParams = 0;
            while ((line = reader.readLine()) != null) {
                if (line.substring(0, 9).toLowerCase().equals("memtotal:")) {
                    String[] tokenized = line.split(" +");
                    total = Integer.parseInt(tokenized[1]);
                    readParams++;
                }
                else if (line.substring(0, 7).toLowerCase().equals("active:")) {
                    String[] tokenized = line.split(" +");
                    active = Integer.parseInt(tokenized[1]);
                    readParams++;
                }
                else if (line.substring(0, 8).toLowerCase().equals("memfree:")) {
                    String[] tokenized = line.split(" +");
                    free = Integer.parseInt(tokenized[1]);
                    readParams++;
                }
                if(readParams >= 3)
                    break;
            }
            reader.close();
        } catch(IOException ioex) {

        }
    }

    /**
     * Returns the total amount of memory in KBs.
     * @return Total Memory in KBs.
     */
    public int getTotal() {
        return total;
    }

    /**
     * Returns the total amount of actively used memory in KBs.
     * @return Total amount of actively used memory in KBs.
     */
    public int getActive() {
        return active;
    }

    /**
     * Returns the free memory in KBs.
     * @return Free Memory in KBs.
     */
    public int getFree() {
        return free;
    }

    /**
     * The percentage amount of used memory which is active + cached + inactive + etc.
     * @return
     */
    public int getPercentUsed() { return (int)(((total - free) / (double)total) * 100); }

    /**
     * The percentage amount of actively used memory.
     * @return
     */
    public int getPercentActive() { return (int)((active / (double)total) * 100); }
}

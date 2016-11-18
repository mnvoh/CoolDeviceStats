package ir.kcoder.KDeviceStats;

import android.util.Log;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;


/**
 * Created by mnvoh on 3/19/15.<br><br>
 *
 *     This class gives statistical information about the CPU.<br>
 *     <b>USAGE:</b> <br>
 *         - Declare an instance of this class as a property of your desired class.<br>
 *         - Instantiate it ONLY ONCE. <br>
 *         - To get the cpu usage call {@link KCPUStats#calculateCpuUsage(int)}. The first time you <br>
 *             &#09; call it, the result will be 0. But the consequent times will be the <br>
 *             &#09; accurate usage percentage since last run. <br>
 *
 *     <font color="#ff5555">Attention:</font><br>
 *     This class should be initialized only once, and the methods called <br>
 *     several times.
 */

public class KCPUStats {
    public final short PROCSTAT_CPU_USER = 1;
    public final short PROCSTAT_CPU_NICE = 2;
    public final short PROCSTAT_CPU_SYSTEM = 3;
    public final short PROCSTAT_CPU_IDLE = 4;
    public final short PROCSTAT_CPU_IOWAIT = 5;
    public final short PROCSTAT_CPU_IRQ = 6;
    public final short PROCSTAT_CPU_SOFTIRQ = 7;
    public final short PROCSTAT_CPU_STEAL = 8;
    public final short PROCSTAT_CPU_GUEST = 9;
    public final short PROCSTAT_CPU_GUESTNICE = 10;

    private static KCPUStats cpuStats = null;

    /** Each time we read the /proc/stat file, we put the cpu <br>
     * lines in this array.
     */
    private String[] cpuLines;

    /**
     * The number of cpu cores, minus the total one.
     */
    private int coreCount;

    /**
    The previous read of the total amount of time the CPU spent on: <br>
     <b>1 - user:</b> normal processes executing in user mode <br>
     <b>2 - nice:</b> niced processes executing in user mode <br>
     <b>3 - system:</b> processes executing in kernel mode <br>
     <b>5 - iowait:</b> waiting for I/O to complete <br>
     <b>6 - irq:</b> servicing interrupts <br>
     <b>7 - softirq:</b> servicing softirqs <br>
     <b>8 - steal:</b> involuntary wait <br>
     <b>9 - guest:</b> running a normal guest <br>
     <b>10 - guest_nice:</b> running a niced guest <br>
     index 0 being the total, and index 1..N for each core.
    */
    private static long[] prevCpuWorkingTime;

    /**
    The previous read of the total amount of time the cpu spent on: <br>
    <b>4 - idle:</b> twiddling thumbs <br>
     index 0 being the total, and index 1..N for each core.
    */
    private static long[] prevCpuIdleTime;

    public static KCPUStats getInstance() {
        if(cpuStats == null) {
            cpuStats = new KCPUStats();
        }
        return cpuStats;
    }

    protected KCPUStats() {
        refreshStats();
    }

    /**
     * Refreshes the cpu lines from /proc/stat. After calling this method <br>
     * {@link #calculateCpuUsage(int)} must be called for each core.
     */
    public void refreshStats() {
        refreshCoreCount();
        int currentIndex = 0;
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String line;
            while((line = reader.readLine()) != null) {
                if(line.substring(0, 3).toLowerCase().equals("cpu")) {
                    //if(currentIndex >= cpuLines.length) {
                    //    String[] newArr = Arrays.copyOf(cpuLines, cpuLines.length + 1);
                    //    cpuLines = newArr;
                    //}
                    cpuLines[currentIndex] = line;
                    currentIndex++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the total number of cores.
     */
    public int getCoreCount() {
        return coreCount;
    }

    /**
     * Calculates the usage of each core, index 0 being the total of all cores <br>
     * since the last run of this method.
     * @param coreIndex The index of the core, 0 being the total. The <br>
     *                  number of cores minus the total of all cores usage <br>
     *                  is accessible through {@link #getCoreCount()}
     * @return The percent of the usage of the selected core.
     */
    public int calculateCpuUsage(int coreIndex) {
        //return 0 if the selected core doesn't exist
        if(coreIndex > coreCount + 1 || cpuLines[coreIndex] == null) { return 0; }

        //split the line at any one or more spaces
        String[] tokens = cpuLines[coreIndex].split(" +");

        long totalWorkingTime = longFromTokens(tokens, PROCSTAT_CPU_USER) +
                longFromTokens(tokens, PROCSTAT_CPU_NICE) +
                longFromTokens(tokens, PROCSTAT_CPU_SYSTEM) +
                longFromTokens(tokens, PROCSTAT_CPU_IOWAIT) +
                longFromTokens(tokens, PROCSTAT_CPU_IRQ) +
                longFromTokens(tokens, PROCSTAT_CPU_SOFTIRQ) +
                longFromTokens(tokens, PROCSTAT_CPU_STEAL) +
                longFromTokens(tokens, PROCSTAT_CPU_GUEST) +
                longFromTokens(tokens, PROCSTAT_CPU_GUESTNICE);
        long totalIdleTime = longFromTokens(tokens, PROCSTAT_CPU_IDLE);
        int cpuUsage = 0;
        if(prevCpuWorkingTime[coreIndex] > 0) {
            long workingDelta = totalWorkingTime - prevCpuWorkingTime[coreIndex];
            long idleDelta = totalIdleTime - prevCpuIdleTime[coreIndex];
            cpuUsage = (int) (((double)workingDelta / (double)(workingDelta + idleDelta)) * 100);
        }
        prevCpuWorkingTime[coreIndex] = totalWorkingTime;
        prevCpuIdleTime[coreIndex] = totalIdleTime;
        return cpuUsage;
    }

    /** Looks up the total number of cores and reallocates memory to arrays. */
    private void refreshCoreCount() {
        coreCount = 0;

        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String line;
            while((line = reader.readLine()) != null) {
                if(line.substring(0, 3).toLowerCase().equals("cpu")) {
                    coreCount++;
                }
            }
            reader.close();

            // We don't want the total amount to be counted as a core
            coreCount--;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(cpuLines == null || cpuLines.length < coreCount + 1) {
            cpuLines = new String[coreCount + 1];
            prevCpuWorkingTime = new long[coreCount + 1];
            prevCpuIdleTime = new long[coreCount + 1];
        }
    }

    /**
     * With the parameters in a cpu entry in /proc/stat tokenized and put inside <br>
     * an array, this method returns the corresponding parameter with an index as <br>
     * long.
     * @param tokens String[] An array of strings which is the tokenized version of <br>
     *               a line like: cpu 0 0 0 0 0 0 0 0
     * @param index short The index which is to be returned.
     * @return long The long value of the token specified by index.
     */
    private long longFromTokens(String[] tokens, short index) {
        if(tokens.length > index) {
            return Long.parseLong(tokens[index]);
        }
        return 0;
    }
}

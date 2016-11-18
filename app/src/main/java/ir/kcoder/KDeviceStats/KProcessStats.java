package ir.kcoder.KDeviceStats;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by mnvoh on 3/20/15.
 *
 */
public class KProcessStats {
    public class KProcessInfo {
        public static final char STATE_RUNNING = 'R';
        public static final char STATE_SLEEPING = 'S';
        public static final char STATE_UNINTERRUPTIBLE_WAIT = 'D';
        public static final char STATE_ZOMBIE = 'Z';
        public static final char STATE_TRACED = 'T';
        public static final int SIGNAL_CHLD = 18;

        /** The name of the process */
        private String name;

        /** The state of the process */
        private char state;

        /** The process ID */
        private int pid;

        /** The parent Process ID */
        private int ppid;

        /** The amount of memory currently being used by the process*/
        private int memory;

        /** The number of threads possessed by this process */
        private int threads;

        /** The uid of the user who owns the process */
        private int uid;

        public void setName(String name) {
            this.name = new String(name);
        }
        public String getName() {
            return name;
        }

        public void setState(char state) {
            this.state = state;
        }
        public char getState() {
            return state;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }
        public int getPid() {
            return pid;
        }

        public void setPPid(int ppid) {
            this.ppid = ppid;
        }
        public int getPPid() {
            return ppid;
        }

        public void setMemory(int memory) {
            this.memory = memory;
        }
        public int getMemory() {
            return memory;
        }

        public void setThreads(int threads) {
            this.threads = threads;
        }
        public int getThreads() {
            return threads;
        }

        public String toString() {
            return "Name(" + name + "), PID(" + pid + "), PPID(" + ppid + "), State(" + state +
                    "), Memory(" + memory + " KBs), Threads(" + threads + ")" + " UID(" + uid + ")";
        }

        public int getUid() {
            return uid;
        }
        public void setUid(int uid) {
            this.uid = uid;
        }
    }

    public int lastRefreshDuration;
    private ArrayList<KProcessInfo> processes;

    public KProcessStats() {
        refreshStats();
    }

    public void refreshStats() {
        long start = System.currentTimeMillis();

        File proc = new File("/proc");
        String[] ps = proc.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if(!filename.matches("[0-9]+"))
                    return false;
                return new File(dir, filename).isDirectory();
            }
        });

        processes = new ArrayList<KProcessInfo>();

        String line;

        for(String p : ps) {
            try {
                if(!(new File("/proc/" + p).exists())) { continue; }

                KProcessInfo process = new KProcessInfo();
                RandomAccessFile status = new RandomAccessFile("/proc/" + p + "/status", "r");
                int propsRead = 0;
                final int totalProps = 7;
                while((line = status.readLine()) != null) {

                    String[] linec = line.split("\\s+");

                    if(linec[0].toLowerCase().contains("name:")) {
                        process.setName(linec[1]);
                        propsRead++;
                    }
                    else if(linec[0].toLowerCase().contains("state:")) {
                        process.setState(linec[1].charAt(0));
                        propsRead++;
                    }
                    else if(linec[0].toLowerCase().startsWith("pid:")) {
                        int pid = Integer.parseInt(linec[1].trim());
                        process.setPid(pid);
                        propsRead++;
                    }
                    else if(linec[0].toLowerCase().startsWith("ppid:")) {
                        process.setPPid(Integer.parseInt(linec[1].trim()));
                        propsRead++;
                    }
                    else if(linec[0].toLowerCase().contains("vmrss:")) {
                        process.setMemory(Integer.parseInt(linec[1]));
                        propsRead++;
                    }
                    else if(linec[0].toLowerCase().contains("threads:")) {
                        process.setThreads(Integer.parseInt(linec[1]));
                        propsRead++;
                    }
                    else if(linec[0].toLowerCase().startsWith("uid:")) {
                        process.setUid(Integer.parseInt(linec[1]));
                    }

                    if(propsRead >= totalProps) { break; }
                }
                status.close();

                if(filterProcess(process)) { processes.add(process); }

            } catch(IOException ioex) { }
        }

        lastRefreshDuration = (int)(System.currentTimeMillis() - start);
    }

    public ArrayList<KProcessInfo> getProcesses() { return processes; }

    /** Returns true if the process has the conditions to be displayed, false otherwise. */
    private boolean filterProcess(KProcessInfo process) {
        //if(process.getUid() == 0) { return false; }
        //if(process.getPid() == 0 || process.getPid() == 1 || process.getPid() == 2) {
        //    return false;
        //}
        //if(process.getPPid() == 0 || process.getPPid() == 1 || process.getPPid() == 2) {
        //    return false;
        //}
        //if(process.getState() == KProcessInfo.STATE_ZOMBIE) { return false; }
        //if(process.getName().toLowerCase().contains("android")) { return false; }
        return true;
    }
}

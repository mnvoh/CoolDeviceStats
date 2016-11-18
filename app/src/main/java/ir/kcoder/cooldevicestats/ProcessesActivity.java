package ir.kcoder.cooldevicestats;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ir.kcoder.KDeviceStats.KProcessStats;


public class ProcessesActivity extends ActionBarActivity {
    private Activity activity;
    private KProcessStats processStats;
    private TableLayout tblProcessList;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processes);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.processes);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        activity = this;
        tblProcessList = (TableLayout)findViewById(R.id.tblProcessList);
        pb = (ProgressBar)findViewById(R.id.pbProcsListRefresh);
        refreshList(null);
    }

    public void refreshList(View v) {
        new ListRefresher().execute();
    }

    public class ListRefresher extends AsyncTask<Void, Void, Void> {
        ArrayList<KProcessStats.KProcessInfo> processes;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(processStats == null) {
                processStats = new KProcessStats();
            }
            processStats.refreshStats();
            processes = processStats.getProcesses();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tblProcessList.removeAllViews();
            tblProcessList.setStretchAllColumns(true);
            tblProcessList.bringToFront();

            TableRow header = new TableRow(activity);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            header.setLayoutParams(lp);

            TextView pid = new TextView(activity);
            pid.setText("PID");

            TextView processName = new TextView(activity);
            processName.setText("Process Name");

            TextView memory = new TextView(activity);
            memory.setText("Memory");

            TextView threads = new TextView(activity);
            threads.setText("Threads");

            pid.setTypeface(null, Typeface.BOLD);
            pid.setTextColor(0xffbf3700);
            processName.setTypeface(null, Typeface.BOLD);
            processName.setTextColor(0xffbf3700);
            memory.setTypeface(null, Typeface.BOLD);
            memory.setTextColor(0xffbf3700);
            threads.setTypeface(null, Typeface.BOLD);
            threads.setTextColor(0xffbf3700);

            header.addView(pid);
            header.addView(processName);
            header.addView(memory);
            header.addView(threads);

            tblProcessList.addView(header);

            for (int i = 0; i < processes.size(); i++) {
                TableRow row = new TableRow(activity);

                TextView _pid = new TextView(activity);
                _pid.setText(Integer.toString(processes.get(i).getPid()));
                row.addView(_pid);

                TextView _processName = new TextView(activity);
                _processName.setText(processes.get(i).getName());
                row.addView(_processName);

                TextView _memory = new TextView(activity);
                _memory.setText(processes.get(i).getMemory() + "kB");
                row.addView(_memory);

                TextView _threads = new TextView(activity);
                _threads.setText(Integer.toString(processes.get(i).getThreads()));
                row.addView(_threads);

                tblProcessList.addView(row);
            }
            pb.setVisibility(View.GONE);
        }
    }
}

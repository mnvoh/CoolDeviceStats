package ir.kcoder.cooldevicestats;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mnvoh on 5/15/15.
 *
 */
public class StatsListAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList data;
    private Resources resources;
    private static LayoutInflater inflater = null;
    private StatsListModel tempValues;
    int i;

    public StatsListAdapter(Activity activity, ArrayList d, Resources res) {
        this.activity = activity;
        this.data = d;
        this.resources = res;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(data == null || data.size() <= 0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView == null) {
            vi = inflater.inflate(R.layout.stats_list_view_item, null);
            holder = new ViewHolder();
            holder.title = (TextView)vi.findViewById(R.id.statTitle);
            holder.stats = (TextView)vi.findViewById(R.id.statStats);
            vi.setTag(holder);
        }
        else {
            holder = (ViewHolder)vi.getTag();
        }

        if(data.size() <= 0) {
            holder.title.setText("No Stats");
        }
        else {
            tempValues = null;
            tempValues = (StatsListModel)data.get(position);
            holder.title.setText(tempValues.getTitle());
            holder.stats.setText(tempValues.getStat());
            vi.setOnClickListener(new CopyStatToClipboard(tempValues.getStat()));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder {
        public TextView title;
        public TextView stats;
    }

    private class CopyStatToClipboard implements View.OnClickListener {
        private String stats;

        public CopyStatToClipboard(String stats) {
            this.stats = stats;
        }

        @Override
        public void onClick(View v) {
            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
            String title = (stats.length() > 16) ? stats.substring(0, 16) + "..." : stats;
            android.content.ClipData clip =
                    android.content.ClipData.newPlainText(title, stats);
            clipboard.setPrimaryClip(clip);

            String toastText = activity.getApplicationContext()
                    .getText(R.string.copied_to_clipboard).toString();
            Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show();

            Animation anim = new AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(100);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(3);
            v.startAnimation(anim);
        }
    }
}

package ir.kcoder.cooldevicestats;

/**
 * Created by mnvoh on 5/15/15.
 *
 */
public class StatsListModel {
    private String title;
    private String stat;

    public StatsListModel() { }
    public StatsListModel(String title, String stat) {
        this.title = title;
        this.stat = stat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}

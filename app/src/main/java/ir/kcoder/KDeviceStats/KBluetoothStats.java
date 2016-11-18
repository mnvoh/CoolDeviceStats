package ir.kcoder.KDeviceStats;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by mnvoh on 4/18/15.
 *
 */
public class KBluetoothStats {
    /**
     * Checks whether bluetooth is turned on or off
     * @return false for off, true for on
     */
    public static boolean getBluetoothState() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null) {
            return false;
        }
        int state = adapter.getState();
        if(state == BluetoothAdapter.STATE_OFF ||
                state == BluetoothAdapter.STATE_TURNING_OFF ||
                state == BluetoothAdapter.STATE_TURNING_ON) {
            return false;
        }
        return true;
    }
}

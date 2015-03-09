package android.test.com.br.myfirstapp.receiver;

import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Marcio on 07/03/2015.
 */
public class GeoPositionResultReceiver extends ResultReceiver {
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public GeoPositionResultReceiver(Handler handler) {
        super(handler);
    }
}

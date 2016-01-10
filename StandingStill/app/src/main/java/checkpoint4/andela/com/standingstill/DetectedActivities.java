package checkpoint4.andela.com.standingstill;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by andeladev on 10/01/2016.
 */
public class DetectedActivities extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DetectedActivities(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}

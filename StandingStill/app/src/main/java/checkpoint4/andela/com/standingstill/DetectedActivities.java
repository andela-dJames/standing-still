package checkpoint4.andela.com.standingstill;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andeladev on 10/01/2016.
 */
public class DetectedActivities extends IntentService {

    private final static String TAG = "Detected Activity";


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    public DetectedActivities() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        DetectedActivity mostprobableActivity =  result.getMostProbableActivity();
        ArrayList<DetectedActivity> mostProbable = new ArrayList<>();
        mostProbable.add(mostprobableActivity);

        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivities);
        localIntent.putExtra(Constants.MOST_PROBABLE_ACTIVITY, mostProbable);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }
}

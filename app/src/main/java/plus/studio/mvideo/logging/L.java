package plus.studio.mvideo.logging;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yohananjr13 on 5/5/15.
 */

public class L {

    private static final Boolean debug_mode = true;

    public static final String TAG = "MATERIAL_DESIGN";

    public static void d(String message) {
        Log.d(TAG, "" + message);
    }

    public static void t(Context context, String message) {
        if(debug_mode){
            Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
        }
    }
}

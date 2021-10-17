package indi.nonoas.xbh.view;

import android.content.Context;
import android.widget.Toast;

public class CoverableToast {

    private static Toast toast;
    private Context context;

    public static void showToast(Context context, String msg, int length) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, length);
        toast.show();
    }
}

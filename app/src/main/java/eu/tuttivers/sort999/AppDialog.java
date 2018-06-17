package eu.tuttivers.sort999;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class AppDialog extends DialogFragment {

    private static final String ICON_KEY = "icon_key";
    private static final String TITLE_KEY = "title_key";
    private static final String MESSAGE_KEY = "message_key";
    private static final String BUTTON_TEXT_KEY = "button_text_key";


    public static void showWarning(FragmentActivity activity, String message) {
        showDialog(activity, R.drawable.ic_warning, activity.getString(R.string.error), message, android.R.string.ok);
    }

    public static void showDialog(FragmentActivity activity, int resIcon, String title, String message, int resButtonText) {
        AppDialog appDialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(ICON_KEY, resIcon);
        args.putString(TITLE_KEY, title);
        args.putString(MESSAGE_KEY, message);
        args.putInt(BUTTON_TEXT_KEY, resButtonText);
        appDialog.setArguments(args);
        appDialog.show(activity.getSupportFragmentManager(), null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        return new AlertDialog.Builder(getActivity())
                .setIcon(bundle.getInt(ICON_KEY))
                .setTitle(bundle.getString(TITLE_KEY))
                .setMessage(bundle.getString(MESSAGE_KEY))
                .setPositiveButton(bundle.getInt(BUTTON_TEXT_KEY), (dialog, which) -> dismiss())
                .create();
    }
}

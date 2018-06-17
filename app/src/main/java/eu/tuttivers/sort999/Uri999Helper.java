package eu.tuttivers.sort999;

import android.net.Uri;

import java.util.Set;

public class Uri999Helper {

    public static final String BASE_999_URL = "http://www.999.md";

    private static final String VIEW_TYPE_KEY = "view_type";
    private static final String VIEW_TYPE_DETAIL = "detail";
    private static final String CURRENCY = "selected_currency";
    private static final String MDL = "mdl";
    private static final String PAGE_KEY = "page";

    public static Uri adapt(Uri uri) {
        final Set<String> keys = uri.getQueryParameterNames();
        final Uri.Builder newUri = uri.buildUpon().clearQuery();
        for (String key : keys) {
            if (key.equals(VIEW_TYPE_KEY) || key.equals(CURRENCY) || key.equals(PAGE_KEY)) {
                continue;
            }
            for (String value : uri.getQueryParameters(key)) {
                newUri.appendQueryParameter(key, value);
            }
        }
        newUri.appendQueryParameter(VIEW_TYPE_KEY, VIEW_TYPE_DETAIL);
        newUri.appendQueryParameter(CURRENCY, MDL);
        return newUri.build();
    }

    public static Uri setPage(Uri uri, int page) {
        return uri.buildUpon().appendQueryParameter(PAGE_KEY, String.valueOf(page)).build();
    }
}

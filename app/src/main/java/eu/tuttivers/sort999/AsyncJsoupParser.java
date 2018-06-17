package eu.tuttivers.sort999;

import android.net.Uri;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AsyncJsoupParser extends AsyncTask<Uri, String, List<Item>> {

    private static final int TIMEOUT = 5000;

    @Override
    protected List<Item> doInBackground(Uri... uris) {

        List<Item> itemsList = new ArrayList<>();
        Uri adaptedUri = Uri999Helper.adapt(uris[0]);
        try {
            Document doc = Jsoup.connect(Uri999Helper.setPage(adaptedUri, 1).toString()).timeout(TIMEOUT).get();
            Element paginator = doc.getElementsByClass("paginator").first();
            if (paginator == null) {
                return parsePage(doc);
            } else {
                Element lastPageLi = paginator.getElementsByTag("li").last();
                Element a = lastPageLi.getElementsByTag("a").first();
                String href = a.attr("href");
                int totalPages = Integer.parseInt(Uri.parse(Uri999Helper.BASE_999_URL + href).getQueryParameter("page"));
                for (int i = 1; i <= totalPages; i++) {
                    publishProgress(String.format(Locale.US, "Loading page %d from %d", i, totalPages));
                    if (i > 1) {
                        doc = Jsoup.connect(Uri999Helper.setPage(adaptedUri, i).toString()).timeout(TIMEOUT).get();
                    }
                    itemsList.addAll(parsePage(doc));
                }
            }
        } catch (Exception e) {
            publishProgress(String.format("Error: %s", e.getMessage()));
        }
        return itemsList;
    }

    private List<Item> parsePage(Element webPage) {
        List<Item> list = new ArrayList<>();
        Element adsList = webPage.getElementsByClass("ads-list-detail").first();
        Elements adsListLi = adsList.getElementsByTag("li");
        for (Element item : adsListLi) {
            Element thumb = item.getElementsByClass("ads-list-detail-item-thumb").first();
            Element a = thumb.getElementsByTag("a").first();
            Element img = a.getElementsByTag("img").first();
            Element priceDiv = item.getElementsByTag("div").last();
            String link = a.attr("href");
            String imageUrl = img.attr("data-src");
            String title = img.attr("alt");
            String price = priceDiv.text().trim();
            if (price.contains("MDL") && price.indexOf("MDL") == price.length() - 3) {
                list.add(new Item(imageUrl, title, price, link));
            }
        }
        return list;
    }
}

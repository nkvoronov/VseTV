package Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static Common.Strings.*;

public class TestParser {

    public static void main(String[] args) {
        int ttype = 4;
        if (ttype == 1) {
            test_Channels();
        } else if (ttype == 2) {
            test_Programme();
        } else if (ttype == 3) {
            test_FullDesc();
        } else if (ttype == 4) {
            test_Parser();
        } else if (ttype == 5) {
            test_Date();
        }
    }

    public static void test_Channels() {
        ChannelList clst = new ChannelList("ru", true);
        clst.loadFromDB();
        System.out.println("SAVE ==========================================");
        clst.saveToFile("channels.cfg");
        System.out.println("LOAD ==========================================");
        clst.loadFromFile("channels.cfg");
        System.out.println("PRINT ==========================================");
        clst.print();
    }

    public static void test_Programme() {
        org.jsoup.nodes.Document doc = new HttpContent("schedule_channel_5_day_2015-09-22.html").getDocument();
        Elements items = doc.select("div[class~=(?:pasttime|onair|time)]");
        for (Element item : items){
            String etime = item.html();
            String etitle = "";
            String efulldescurl = "";
            String edesc = "";
            try {
                Elements title_itm = item.nextElementSibling().select("div[class~=(?:pastprname2|prname2)]");
                if (title_itm != null) {
                    efulldescurl = title_itm.select("a").attr("href");
                    if (!efulldescurl.isEmpty()) {
                        efulldescurl = Host + efulldescurl;
                    }
                    etitle = title_itm.text();
                }
            } catch (Exception ignored) {

            }
            try {
                Elements desc_itm = item.nextElementSibling().nextElementSibling().select("div[class~=(?:pastdesc|prdesc)]");
                if (desc_itm != null) {
                    edesc = desc_itm.text();
                }
            } catch (Exception ignored) {

            }
            System.out.println(etime + ";" + etitle.trim() + ";" + efulldescurl + ";" + edesc);
        }
    }

    public static void test_FullDesc() {
        org.jsoup.nodes.Document doc = new HttpContent("film_71535.html").getDocument();
        String ctmp;
        String cgenre;
        String cdirectors = "";
        String cactors = "";
        String cleading = "";
        String cdesc;
        String stars;
        Elements items = doc.select("td[class=showname]");
        if (items.html().contains("<strong>")) {
            ctmp = items.html().substring(items.html().indexOf("<br>"), items.html().indexOf("<strong>"));
        } else {
            ctmp = items.html().substring(items.html().indexOf("<br>"), items.html().indexOf("<!--"));
        }
        ctmp = Jsoup.parse(ctmp).text();
        cgenre = items.select("strong").text().replace(" / ", ",");

        Elements items1 = doc.select("td[class=showmain]");
        int beg;
        int end;
        try {
            beg = new String(items1.html().getBytes(), "UTF-8").indexOf("Режиссер(ы):");
            end = items1.html().indexOf("<br>");
            cdirectors = items1.html().substring(beg+12, end);
            cdirectors = Jsoup.parse(cdirectors).text();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            beg = new String(items1.html().getBytes(), "UTF-8").indexOf("Актеры:");
            end = items1.html().indexOf("<div>");
            cactors = items1.html().substring(beg+7, end);
            cactors = Jsoup.parse(cactors).text();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            beg = new String(items1.html().getBytes(), "UTF-8").indexOf("Ведущие:");
            end = items1.html().indexOf("<div>");
            cleading = items1.html().substring(beg, end);
            cleading = Jsoup.parse(cleading).text();
            if (cactors != "" && cleading != ""){
                cactors = cactors + "," + cleading;
            } else {
                cactors = cleading;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Elements desc = items1.select("span[class=big]");
        cdesc = desc.text();

        Elements strs = doc.select("span[class=name]");
        stars = strs.text().split(":")[1];

        System.out.println(ctmp);
        System.out.println(cgenre);
        System.out.println(cdirectors);
        System.out.println(cactors);
        System.out.println(cdesc);
        System.out.println(stars);
    }

    public static void test_Parser() {
        ParserVseTV parser = new ParserVseTV("vsetv.xml", "ru", 3, false);
        System.out.println("Create");
        parser.runParser();
        parser.saveXML();
        System.out.println("File saved!");
    }

    public static void test_Date() {
        Date dt = new Date();
        Calendar cur = Calendar.getInstance();
        cur.setTime(dt);
        Calendar last = Calendar.getInstance();
        last.setTime(dt);
        last.add(Calendar.DATE, 7);
        SimpleDateFormat ft = new SimpleDateFormat(DateFormatDot);
        while (!cur.getTime().equals(last.getTime())) {
            System.out.println(ft.format(cur.getTime()));
            cur.add(Calendar.DATE, 1);
        }
    }

}

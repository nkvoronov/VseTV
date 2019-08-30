package parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class HttpContent {
    private String direction;
    private Document document;

    public HttpContent(String direction) {
        this.direction = direction;
        Connection conn = Jsoup.connect(common.UtilStrings.HOST + this.direction);
        conn.userAgent(common.UtilStrings.USER_AGENT);
        try {
            this.document = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public String getDirection() {
        return direction;
    }

    public Document getDocument() {
        return document;
    }
}

package parser;

import org.w3c.dom.*;
import java.util.*;

public class Channel {
    public static final String SEP_FOLDER = ";";

    private int index;
    private String uName;
    private String oName;
    private String icon;
    private int correction;
    
    public static Comparator<Channel> channelIndexComparator = (c1, c2) -> {
        int channelIndex1 = c1.getIndex();
        int channelIndex2 = c2.getIndex();
        return channelIndex1 - channelIndex2;
    };
    
    public static Comparator<Channel> channelNameComparator = (c1, c2) -> {
        String channelName1 = c1.getoName().toUpperCase();
        String channelName2 = c2.getoName().toUpperCase();
        return channelName1.compareTo(channelName2);
    };

    public Channel(int index, String oName, String uName, String icon, int correction) {
        this.index = index;
        this.oName = oName;
        if (Objects.equals(uName, "")) {
            this.uName = "None";
        } else {
            this.uName = uName;
        }
        this.icon = icon;
        this.correction = correction;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCorrection() {
        return correction;
    }

    public void setCorrection(int correction) {
        this.correction = correction;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getoName() {
        return oName;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public void print() {
        System.out.println(getIndex() + SEP_FOLDER + getoName() + SEP_FOLDER + getuName() + SEP_FOLDER + getIcon() + SEP_FOLDER + getCorrection());
    }

    public void getXML(Document document, Element element){
        Element echannel = document.createElement("channel");
        echannel.setAttribute("id", Integer.toString(getIndex()));
        Element edisplayname = document.createElement("display-name");
        edisplayname.setAttribute("lang", "ru");
        if (getuName().equals("None")) {
            edisplayname.appendChild(document.createTextNode(getoName()));
        } else {
            edisplayname.appendChild(document.createTextNode(getuName()));
        }
        echannel.appendChild(edisplayname);
        if (!Objects.equals(getIcon(), "")) {
            Element eiconlink = document.createElement("icon");
            eiconlink.setAttribute("src", getIcon());
            echannel.appendChild(eiconlink);
        }
        element.appendChild(echannel);
    }
}
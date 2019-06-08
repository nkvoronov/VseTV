package Parser;

import org.w3c.dom.*;
import java.util.*;

public class Channel {
    public static final String sepFolder = ";";

    private int Index;
    public static Comparator<Channel> ChannelIndexComparator = (c1, c2) -> {
        int ChannelIndex1 = c1.getIndex();
        int ChannelIndex2 = c2.getIndex();
        return ChannelIndex1 - ChannelIndex2;
    };
    private String OName;
    public static Comparator<Channel> ChannelNameComparator = (c1, c2) -> {
        String ChannelName1 = c1.getOName().toUpperCase();
        String ChannelName2 = c2.getOName().toUpperCase();
        return ChannelName1.compareTo(ChannelName2);
    };
    private String UName;
    private String Icon;
    private int Correction;

    public Channel(int Index, String OName, String UName, String Icon, int Correction) {
        this.Index = Index;
        this.OName = OName;
        if (Objects.equals(UName, "")) {
            this.UName = "None";
        } else {
            this.UName = UName;
        }
        this.Icon = Icon;
        this.Correction = Correction;
    }

    public String getUName() {
        return UName;
    }

    public void setUName(String UName) {
        this.UName = UName;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    public int getCorrection() {
        return Correction;
    }

    public void setCorrection(int Correction) {
        this.Correction = Correction;
    }

    public int getIndex() {

        return Index;
    }

    public void setIndex(int Index) {
        this.Index = Index;
    }

    public String getOName() {
        return OName;
    }

    public void setOName(String OName) {
        this.OName = OName;
    }

    public void print() {
        System.out.println(getIndex() + sepFolder + getOName() + sepFolder + getUName() + sepFolder + getIcon() + sepFolder + getCorrection());
    }

    public void getXML(Document document, Element element){
        Element echannel = document.createElement("channel");
        echannel.setAttribute("id", Integer.toString(getIndex()));
        Element edisplayname = document.createElement("display-name");
        edisplayname.setAttribute("lang", "ru");
        if (getUName().equals("None")) {
            edisplayname.appendChild(document.createTextNode(getOName()));
        } else {
            edisplayname.appendChild(document.createTextNode(getUName()));
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
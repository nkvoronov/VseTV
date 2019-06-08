package Parser;

import Common.DBParams;
import Common.DBUtils;
import Common.ProgressMonitor;
import Common.Common;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

import static Common.Common.getIconsPatch;
import static Common.DBUtils.*;
import static Common.Strings.*;

public class ChannelList implements Runnable{
    private String Lang;
    private Boolean IndexSort;
    private ArrayList<Channel> Data;
    private Boolean isUpdChannels = true;
    private ProgressMonitor pMonitor;

    public ChannelList(String Lang, Boolean IndexSort) {
        this.Lang = Lang;
        this.IndexSort = IndexSort;
        this.Data = new ArrayList<>();
        this.pMonitor = new ProgressMonitor(0, true);
    }

    public Boolean getIsUpdChannels() {
        return isUpdChannels;
    }

    public void setIsUpdChannels(Boolean isUpdChannels) {
        this.isUpdChannels = isUpdChannels;
    }

    public ProgressMonitor getMonitor() {
        return pMonitor;
    }

    public String getLang() {
        return Lang;
    }

    public void setLang(String Lang) {
        this.Lang = Lang;
    }

    public Boolean getIndexSort() {
        return IndexSort;
    }

    public void setIndexSort(Boolean IndexSort) {
        this.IndexSort = IndexSort;
    }

    public ArrayList<Channel> getData() {
        return Data;
    }

    public void loadFromNet() {
        String cindex, cname, clink, cicon;
        Boolean flag = false;
        this.Data.clear();
        org.jsoup.nodes.Document doc = new HttpContent("channels.html").getDocument();
        Elements items = doc.select("option[value^=channel_]");
        for (org.jsoup.nodes.Element item : items){
            cname = item.text();
            try {
                flag = new String(cname.getBytes(), "UTF-8").endsWith("(на укр.)");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((!flag && this.Lang.equals("ru"))||(flag && this.Lang.equals("ua"))) {
                clink = item.attr("value");
                cindex = clink.split("_")[1];
                cicon = IconsPre + cindex + ".gif";
                Channel chn = new Channel(Integer.parseInt(cindex), cname, "", cicon, 120);
                this.Data.add(chn);
            }
        }
        if (IndexSort) {
            Collections.sort(this.Data, Channel.ChannelIndexComparator);
        } else {
            Collections.sort(this.Data, Channel.ChannelNameComparator);
        }
    }

    public void loadFromDB() {
        this.Data.clear();
        Connection conn = getConnection(DBUtils.DBDest);
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlMainUserChannels);
                try {
                    while (rs.next()) {
                        Channel chn = new Channel(rs.getInt(2), rs.getString(4), rs.getString(8), rs.getString(3), rs.getInt(5));
                        this.Data.add(chn);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finally {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFromFile(String FileName) {
        this.Data.clear();
        File file = new File(FileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] aline = line.split(Channel.sepFolder);
                    Channel chn = new Channel(Integer.parseInt(aline[0]), aline[1], aline[2], aline[3], Integer.parseInt(aline[4]));
                    this.Data.add(chn);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String FileName) {
        File file = new File(FileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            try (PrintWriter pw = new PrintWriter(file.getAbsoluteFile())) {
                for (Channel chn : this.Data) {
                    pw.println(chn.getIndex() + Channel.sepFolder + chn.getOName() + Channel.sepFolder + chn.getUName() + Channel.sepFolder + chn.getIcon() + Channel.sepFolder + chn.getCorrection());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        for (Channel chn : this.Data) {
            chn.print();
        }
    }

    public void getXML(org.w3c.dom.Document document, org.w3c.dom.Element element) {
        for (Channel chn : this.Data) {
            chn.getXML(document, element);
        }
    }

    public void saveChannelToDB(Channel channel, int Index) {
        int typeUpdate;
        String OldName = "";
        String logMsg = "";
        ResultSet rs;
        PreparedStatement pstmt;
        try {
            Connection conn = getConnection(DBDest);
            pstmt = conn.prepareStatement(sqlFindInChannelsIndex);
            DBParams[] Params = new DBParams[1];
            Params[0] = new DBParams(1, channel.getIndex(), Common.DBType.INTEGER);
            setParams(pstmt, Params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                typeUpdate = 0;
                OldName = rs.getString(3);
                if (!channel.getOName().equals(OldName)) {
                    typeUpdate = 1;
                }
            } else {
                typeUpdate = 2;
            }
            conn.close();
            if (typeUpdate == 0) {
                Params = new DBParams[1];
                Params[0] = new DBParams(1, channel.getIndex(), Common.DBType.INTEGER);
                if (getExecutePreparedUpdate(sqlUpdChannelDate, Params) != -1) {
                    logMsg = "Канал " + OldName + " - присутствует";
                }
            } else if (typeUpdate == 1) {
                Params = new DBParams[3];
                Params[0] = new DBParams(1, channel.getOName(), Common.DBType.STRING);
                Params[1] = new DBParams(2, channel.getIcon(), Common.DBType.STRING);
                Params[2] = new DBParams(3, channel.getIndex(), Common.DBType.INTEGER);
                if (getExecutePreparedUpdate(sqlUpdChannelName, Params) != -1) {
                    logMsg = "Обновлен канал - " + OldName + " на " + channel.getOName();
                }
            } else {
                Params = new DBParams[3];
                Params[0] = new DBParams(1, channel.getIndex(), Common.DBType.INTEGER);
                Params[1] = new DBParams(2, channel.getOName(), Common.DBType.STRING);
                Params[2] = new DBParams(3, channel.getIcon(), Common.DBType.STRING);
                if (getExecutePreparedUpdate(sqlInsChannel, Params) != -1) {
                    logMsg = "Добавлен новый канал - " + channel.getOName();
                }
            }
            pMonitor.setCurrent(logMsg, Index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveChannelIcon(Channel channel, int Index) {
        String logMsg;
        String cicon = channel.getIcon();
        String fileName = getIconsPatch() + Integer.toString(channel.getIndex()) + ".gif";
        try {
            BufferedImage img = ImageIO.read(new URL(cicon));
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
                logMsg = "Добавление иконки - " + channel.getIndex() + ".gif";
            } else {
                logMsg = "Обновление иконки - " + channel.getIndex() + ".gif";
            }
            ImageIO.write(img, "gif", file);
        } catch (FileNotFoundException e) {
            logMsg = "Ошибка записи - " + channel.getIcon();
        } catch (IOException e) {
            logMsg = "Ошибка загрузки - " + channel.getIcon();
        }
        pMonitor.setCurrent(logMsg, Index);
    }

    @Override
    public void run() {
        int i = 0;
        int resupd;
        pMonitor.start("");
        pMonitor.setIndeterminate(true);
        pMonitor.setCurrent("", 0);
        loadFromNet();
        pMonitor.setIndeterminate(false);
        pMonitor.setCurrent("", 0);
        pMonitor.setTotal(this.getData().size());
        pMonitor.start("");
        if (isUpdChannels) {
            getExecuteUpdate(sqlUpdChannelDateNull);
            for (Channel chn : this.Data) {
                saveChannelToDB(chn,i);
                i++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            resupd = getExecuteUpdate(sqlDelChannelDateNull);
            if (resupd != 0) {
                pMonitor.setCurrent("Удалено - " + resupd, i);
            }
        } else {
            for (Channel chn : this.Data) {
                saveChannelIcon(chn,i);
                i++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        pMonitor.setCurrent("", -2);
    }
}

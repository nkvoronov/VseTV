package parser;

import org.jsoup.select.Elements;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import common.CommonTypes;
import common.DBParams;
import common.ProgressMonitor;
import common.DBUtils;
import common.UtilStrings;

public class ChannelList implements Runnable{
    private Boolean indexSort;
    private List<Channel> data;
    private Boolean isUpdChannels = true;
    private ProgressMonitor pMonitor;

    public ChannelList(Boolean indexSort) {
        this.indexSort = indexSort;
        this.data = new ArrayList<>();
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

    public Boolean getIndexSort() {
        return indexSort;
    }

    public void setIndexSort(Boolean indexSort) {
        this.indexSort = indexSort;
    }

    public List<Channel> getData() {
        return data;
    }

    public void loadFromNet() {   
        String lang = "all";
        this.data.clear();
        org.jsoup.nodes.Document doc = new HttpContent("channels.html").getDocument();
        Elements items = doc.select("option[value^=channel_]");
        for (org.jsoup.nodes.Element item : items){
        	String cname = item.text();
        	try {
	        	if (new String(cname.getBytes(), "UTF-8").endsWith("(на укр.)")) {
	                lang = "ukr";
	            } else {
	                lang = "rus";
	            }
        	} catch (UnsupportedEncodingException e) {
        		e.printStackTrace();
			}
        	String clink = item.attr("value");
        	String cindex = clink.split("_")[1];
        	String cicon = UtilStrings.ICONS_PRE + cindex + ".gif";
            Channel channel = new Channel(Integer.parseInt(cindex), cname, "", cicon, 120, lang);           
            this.data.add(channel);
        }
        if (indexSort) {
            Collections.sort(this.data, Channel.channelIndexComparator);
        } else {
            Collections.sort(this.data, Channel.channelNameComparator);
        }
    }

    public void loadFromDB() {
        this.data.clear();
        Connection conn = DBUtils.getConnection(DBUtils.DB_DEST);
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(DBUtils.SQL_DBCHANNELS);
                try {
                    while (rs.next()) {
                        Channel channel = new Channel(rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7));
                        this.data.add(channel);
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

    public void loadFromFile(String fileName) {
        this.data.clear();
        File file = new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] aline = line.split(Channel.SEP_FOLDER);
                    Channel chn = new Channel(Integer.parseInt(aline[0]), aline[1], aline[2], aline[3], Integer.parseInt(aline[4]), aline[5]);
                    this.data.add(chn);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String fileName) {
        File file = new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            try (PrintWriter pw = new PrintWriter(file.getAbsoluteFile())) {
                for (Channel channel : this.data) {
                    pw.println(channel.getIndex() + Channel.SEP_FOLDER + channel.getoName() + Channel.SEP_FOLDER + channel.getuName() + Channel.SEP_FOLDER + channel.getIcon() + Channel.SEP_FOLDER + channel.getLang() + Channel.SEP_FOLDER + channel.getCorrection());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        for (Channel chn : this.data) {
            chn.print();
        }
    }

    public void getXML(org.w3c.dom.Document document, org.w3c.dom.Element element) {
        for (Channel chn : this.data) {
            chn.getXML(document, element);
        }
    }

    public void saveChannelToDB(Channel channel, int index) {
        int typeUpdate;
        String oldName = "";
        String logMsg = "";
        ResultSet rs;
        PreparedStatement pstmt;
        try {
            Connection conn = DBUtils.getConnection(DBUtils.DB_DEST);
            pstmt = conn.prepareStatement(DBUtils.SQL_FINDINCHANNELS_INDEX);
            DBParams[] aParams = new DBParams[1];
            aParams[0] = new DBParams(1, channel.getIndex(), CommonTypes.DBType.INTEGER);
            DBUtils.setParams(pstmt, aParams);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                typeUpdate = 0;
                oldName = rs.getString(3);
                if (!channel.getoName().equals(oldName)) {
                    typeUpdate = 1;
                }
            } else {
                typeUpdate = 2;
            }
            conn.close();
            if (typeUpdate == 0) {
                aParams = new DBParams[1];
                aParams[0] = new DBParams(1, channel.getIndex(), CommonTypes.DBType.INTEGER);
                if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_UPD_CHANNELDATE, aParams) != -1) {
                    logMsg = "Канал " + oldName + " - присутствует";
                }
            } else if (typeUpdate == 1) {
                aParams = new DBParams[4];
                aParams[0] = new DBParams(1, channel.getoName(), CommonTypes.DBType.STRING);
                aParams[1] = new DBParams(2, channel.getIcon(), CommonTypes.DBType.STRING);
                aParams[2] = new DBParams(3, channel.getLang(), CommonTypes.DBType.STRING);
                aParams[3] = new DBParams(4, channel.getIndex(), CommonTypes.DBType.INTEGER);
                if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_UPD_CHANNELNAME, aParams) != -1) {
                    logMsg = "Обновлен канал - " + oldName + " на " + channel.getoName();
                }
            } else {
                aParams = new DBParams[4];
                aParams[0] = new DBParams(1, channel.getIndex(), CommonTypes.DBType.INTEGER);
                aParams[1] = new DBParams(2, channel.getoName(), CommonTypes.DBType.STRING);
                aParams[2] = new DBParams(3, channel.getIcon(), CommonTypes.DBType.STRING);
                aParams[3] = new DBParams(4, channel.getLang(), CommonTypes.DBType.STRING);
                if (DBUtils.getExecutePreparedUpdate(DBUtils.SQL_INS_CHANNEL, aParams) != -1) {
                    logMsg = "Добавлен новый канал - " + channel.getoName();
                }
            }
            pMonitor.setCurrent(logMsg, index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveChannelIcon(Channel channel, int index) {
        String logMsg;
        String cicon = channel.getIcon();
        String fileName = CommonTypes.getIconsPatch() + Integer.toString(channel.getIndex()) + ".gif";
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
        pMonitor.setCurrent(logMsg, index);
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
        	DBUtils.getExecuteUpdate(DBUtils.SQL_UPD_CHANNELDATE_NULL);
            for (Channel chn : this.data) {
                saveChannelToDB(chn,i);
                i++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            resupd = DBUtils.getExecuteUpdate(DBUtils.SQL_DEL_CHANNELDATE_NULL);
            if (resupd != 0) {
                pMonitor.setCurrent("Удалено - " + resupd, i);
            }
        } else {
            for (Channel chn : this.data) {
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

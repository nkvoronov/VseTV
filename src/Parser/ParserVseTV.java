package Parser;

import Common.*;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Objects;

import static Common.DBUtils.getExecutePreparedUpdate;
import static Common.Strings.*;

public class ParserVseTV implements Runnable {
    private ChannelList Channels;
    private ProgrammeList Programmes;
    private int CountDay;
    private Boolean FullDesc;
    private String XMLOut;
    private String Lang;
    private ProgressMonitor pMonitor;
	private Formatter format;

    public ParserVseTV(String XMLOut, String Lang, int CountDay, Boolean FullDesc) {
        this.XMLOut = XMLOut;
        this.CountDay = CountDay;
        this.FullDesc = FullDesc;
        this.Lang = Lang;
        Channels = new ChannelList(this.Lang, true);
        Programmes = new ProgrammeList();
        this.pMonitor = new ProgressMonitor(0,false);
    }

    public String getLang() {
        return Lang;
    }

    public ChannelList getChannels() {
        return Channels;
    }

    public ProgrammeList getProgrammes() {
        return Programmes;
    }

    public int getCountDay() {
        return CountDay;
    }

    public void setCountDay(int countDay) {
        this.CountDay = countDay;
    }

    public Boolean getFullDesc() {
        return FullDesc;
    }

    public void setFullDesc(Boolean fullDesc) {
        this.FullDesc = fullDesc;
    }

    public String getXMLOut() {
        return XMLOut;
    }

    public void setXMLOut(String XMLOut) {
        this.XMLOut = XMLOut;
    }

    public void saveXML() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            Element rootElement = document.createElement("tv");
            rootElement.setAttribute("generator-info-name", "vsetv");
            document.appendChild(rootElement);
            this.Channels.getXML(document, rootElement);
            this.Programmes.getXML(document, rootElement);
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                try {
                    transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream(this.getXMLOut())));
                } catch (TransformerException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void setUpdDateChannel(int ChannelIndex) {
        DBParams[] Params = new DBParams[1];
        Params[0] = new DBParams(1, ChannelIndex, Common.DBType.INTEGER);
        getExecutePreparedUpdate(DBUtils.sqlMainUserChannelsUpdDate, Params);
    }

    public void getContent(Boolean isGUI) {
        Date dt = new Date();
        Calendar cur = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        last.setTime(dt);
        last.add(Calendar.DATE, this.CountDay);
        SimpleDateFormat ft = new SimpleDateFormat(DateFormatDot);
        int i = 0;
        if (isGUI) {
            int count = this.CountDay * this.Channels.getData().size();
            pMonitor.setTotal(count);
        }
        this.Programmes.getData().clear();
        for (Channel chn : this.Channels.getData()) {
            cur.setTime(dt);
            while (!cur.getTime().equals(last.getTime())) {
                if (isGUI) {
                	format = new Formatter();
                	format.format(StrGetChannel, chn.getOName(), ft.format(cur.getTime()));
                    pMonitor.setCurrent(format.toString(), i);
                }
                getContentDay(chn, cur.getTime());
                setUpdDateChannel(chn.getIndex());
                cur.add(Calendar.DATE, 1);
                i++;
            }
        }
    }

    public void getContentDay(Channel chn, Date date) {
        SimpleDateFormat ftd = new SimpleDateFormat(DateFormat);
        SimpleDateFormat ftdt = new SimpleDateFormat(DateFormatTime);
        Calendar dt = Calendar.getInstance();
        dt.setTime(date);
        String otime = StrOTime;
        format = new Formatter();
        format.format(StrScheduleChannel, chn.getIndex(), ftd.format(date));
        String vdirection = format.toString();
        org.jsoup.nodes.Document doc = new HttpContent(vdirection).getDocument();
        Elements items = doc.select(StrElmDocSelect);
        for (org.jsoup.nodes.Element item : items){
            String etime = item.html();
            Date startDate = null;
            Date endDate = null;
            if (Integer.parseInt(etime.split(":")[0]) < Integer.parseInt(otime.split(":")[0])) {
                dt.add(Calendar.DATE, 1);
            }
            otime = etime;
            try {
                startDate = ftdt.parse(ftd.format(dt.getTime()) + " " + etime + ":00");
                endDate = ftdt.parse(ftd.format(dt.getTime()) + " " + "23:59:59");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String etitle = "";
            String efulldescurl = "";
            String edesc = "";
            try {
                Elements title_itm = item.nextElementSibling().select(StrElmDocTitle);
                if (title_itm != null) {
                    efulldescurl = title_itm.select("a").attr("href");
                    etitle = title_itm.text();
                }
            } catch (Exception e) {
                //
            }
            try {
                Elements desc_itm = item.nextElementSibling().nextElementSibling().select(StrElmDocDesc);
                if (desc_itm != null) {
                    edesc = desc_itm.text();
                }
            } catch (Exception e) {
                //
            }
            Programme prg = new Programme(chn.getIndex(), startDate, endDate, etitle);            
            prg.setCorrectionTime(chn.getCorrection());
            getCategoryFromTitle(prg);
            
            if (edesc.length() > 0 && !Objects.equals(edesc, "")) {
                prg.setDesc(edesc);
            }
            if (efulldescurl.length() > 0 && !Objects.equals(efulldescurl, "") && this.FullDesc) {
                prg.setUrlFullDesc(efulldescurl);
                getFullDesc(prg);
            }
            getProgrammes().getData().add(prg);
        }

    }

    private Boolean TitleContainsDictWorlds(String title, String Dicts) {
        Boolean res = false;
        String[] list = Dicts.split(",");
        for (String str:list) {
            res = res || title.contains(str);
        }
        return res;
    }

    private void getCategoryFromTitle(Programme prg) {
        try {
            String ctitle = new String(prg.getTitle().getBytes(), "UTF-8").toLowerCase();
            prg.setCategoryLangRU(StrCategoryLangRU);
            prg.setCategoryLangEN(StrCategoryLangEN);
            try {
            	for (CategoryProgramme cp : Common.CatList.getData()) {
            		if (cp.getId() != 0) {
            			if (TitleContainsDictWorlds(ctitle, cp.getDictionary())) {
            				prg.setCategoryLangRU(cp.getName_ru());
            				prg.setCategoryLangEN(cp.getName_en());
            				break;
            			}
            		}
            	}
            } catch (Exception e) {
                //
            }
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getFullDesc(Programme prg) {
        //
    }

    public void runParser() {
        getChannels().loadFromDB();
        getContent(false);
        getProgrammes().setProgrammeStop();
        saveXML();
    }

    public void runParserGUI() {
        pMonitor.start("");
        pMonitor.setIndeterminate(true);
        pMonitor.setCurrent(StrLoadChanels, 0);
        getChannels().loadFromDB();
        getProgrammes().clearDBSchedule();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pMonitor.setIndeterminate(false);
        getContent(true);
        pMonitor.setIndeterminate(true);
        pMonitor.setCurrent(StrProcessShelude, 0);
        getProgrammes().setProgrammeStop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pMonitor.setIndeterminate(false);
        getProgrammes().saveToDB(pMonitor);
        pMonitor.setCurrent("", -2);
    }

    @Override
    public void run() {
        runParserGUI();
    }

    public ProgressMonitor getMonitor() {
        return pMonitor;
    }
}

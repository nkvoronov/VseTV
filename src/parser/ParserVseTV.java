package parser;

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

import common.CategoryProgramme;
import common.CommonTypes;
import common.DBParams;
import common.DBUtils;
import common.ProgressMonitor;
import common.UtilStrings;
import gui.Messages;

public class ParserVseTV implements Runnable {
    private ChannelList channels;
    private ProgrammeList programmes;
    private int countDay;
    private Boolean fullDesc;
    private String outXML;
    private ProgressMonitor pMonitor;
	private Formatter format;

    public ParserVseTV(String outXML, int countDay, Boolean fullDesc) {
        this.outXML = outXML;
        this.countDay = countDay;
        this.fullDesc = fullDesc;
        channels = new ChannelList(true);
        programmes = new ProgrammeList();
        this.pMonitor = new ProgressMonitor(0,false);
    }

    public ChannelList getChannels() {
        return channels;
    }

    public ProgrammeList getProgrammes() {
        return programmes;
    }

    public int getCountDay() {
        return countDay;
    }

    public void setCountDay(int countDay) {
        this.countDay = countDay;
    }

    public Boolean getFullDesc() {
        return fullDesc;
    }

    public void setFullDesc(Boolean fullDesc) {
        this.fullDesc = fullDesc;
    }

    public String getOutXML() {
        return outXML;
    }

    public void setOutXML(String outXML) {
        this.outXML = outXML;
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
            this.channels.getXML(document, rootElement);
            this.programmes.getXML(document, rootElement);
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                try {
                    transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream("ru")));
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

    public void getContent(Boolean isGUI) {
        Date dt = new Date();
        Calendar cur = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        last.setTime(dt);
        last.add(Calendar.DATE, this.countDay);
        SimpleDateFormat ft = new SimpleDateFormat(UtilStrings.DATE_FORMAT);
        int i = 0;
        if (isGUI) {
            int count = this.countDay * this.channels.getData().size();
            pMonitor.setTotal(count);
        }
        this.programmes.getData().clear();
        for (Channel chn : this.channels.getData()) {
            cur.setTime(dt);
            while (!cur.getTime().equals(last.getTime())) {
                if (isGUI) {
                	format = new Formatter();
                	format.format(UtilStrings.STR_GETCHANEL, chn.getoName(), ft.format(cur.getTime()));
                    pMonitor.setCurrent(format.toString(), i);
                }
                getContentDay(chn, cur.getTime());
                cur.add(Calendar.DATE, 1);
                i++;
            }
        }
    }

    public void getContentDay(Channel chn, Date date) {
        SimpleDateFormat ftd = new SimpleDateFormat(UtilStrings.DATE_FORMAT);
        SimpleDateFormat ftdt = new SimpleDateFormat(UtilStrings.DATE_FORMATTIME);
        Calendar dt = Calendar.getInstance();
        dt.setTime(date);
        String otime = UtilStrings.STR_OTIME;
        format = new Formatter();
        format.format(UtilStrings.STR_SCHEDULECHANNEL, chn.getIndex(), ftd.format(date));
        String vdirection = format.toString();
        org.jsoup.nodes.Document doc = new HttpContent(vdirection).getDocument();
        Elements items = doc.select(UtilStrings.STR_ELMDOCSELECT);
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
                Elements titleItem = item.nextElementSibling().select(UtilStrings.STR_ELMDOCTITLE);
                if (titleItem != null) {
                    efulldescurl = titleItem.select("a").attr("href");
                    etitle = titleItem.text();
                }
            } catch (Exception e) {
                //
            }
            try {
                Elements descItem = item.nextElementSibling().nextElementSibling().select(UtilStrings.STR_ELMDOCDESC);
                if (descItem != null) {
                    edesc = descItem.text();
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
            if (efulldescurl.length() > 0 && !Objects.equals(efulldescurl, "") && this.fullDesc) {
                prg.setUrlFullDesc(efulldescurl);
                getFullDesc(prg);
            }
            getProgrammes().getData().add(prg);
        }

    }

    private Boolean titleContainsDictWorlds(String title, String dicts) {
        Boolean res = false;
        String[] list = dicts.split(",");
        for (String str:list) {
            res = res || title.contains(str);
        }
        return res;
    }

    private void getCategoryFromTitle(Programme prg) {
        try {
            String ctitle = new String(prg.getTitle().getBytes(), "UTF-8").toLowerCase();
            prg.setCategoryLangRU(Messages.getString("StrCategoryLangRU"));
            prg.setCategoryLangEN(Messages.getString("StrCategoryLangEN"));
            try {
            	for (CategoryProgramme cp : CommonTypes.catList.getData()) {
            		if (cp.getId() != 0) {
            			if (titleContainsDictWorlds(ctitle, cp.getDictionary())) {
            				prg.setCategoryLangRU(cp.getNameRU());
            				prg.setCategoryLangEN(cp.getNameEN());
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
        pMonitor.setCurrent(Messages.getString("StrLoadChanels"), 0);
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
        pMonitor.setCurrent(Messages.getString("StrProcessShelude"), 0);
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

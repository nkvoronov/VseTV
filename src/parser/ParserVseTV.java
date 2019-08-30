package parser;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import common.CategoryProgramme;
import common.CommonTypes;
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

    public ParserVseTV(String outXML, int countDay, Boolean fullDesc) {
        this.outXML = outXML;
        this.countDay = countDay;
        this.fullDesc = fullDesc;
        this.channels = new ChannelList(true);
        this.programmes = new ProgrammeList();
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

    public void saveXML() throws TransformerConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            Element rootElement = document.createElement("tv");
            rootElement.setAttribute("generator-info-name", "vsetv");
            document.appendChild(rootElement);
            getChannels().getXML(document, rootElement);
            getProgrammes().getXML(document, rootElement);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(getOutXML()));
            transformer.transform(domSource, streamResult);
        } catch (TransformerException|ParserConfigurationException e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
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
            int count = this.countDay * getChannels().getData().size();
            pMonitor.setTotal(count);
        }
        this.programmes.getData().clear();
        for (Channel channel : getChannels().getData()) {
            cur.setTime(dt);
            while (!cur.getTime().equals(last.getTime())) {
                if (isGUI) {
                    pMonitor.setCurrent(String.format(UtilStrings.STR_GETCHANEL, channel.getoName(), ft.format(cur.getTime())), i);
                }
                getContentDay(channel, cur.getTime());
                cur.add(Calendar.DATE, 1);
                i++;
            }
        }
    }

    public void getContentDay(Channel channel, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(UtilStrings.DATE_FORMAT);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(UtilStrings.DATE_FORMATTIME);
        Calendar dt = Calendar.getInstance();
        dt.setTime(date);
        String otime = UtilStrings.STR_OTIME;
        String direction = String.format(UtilStrings.STR_SCHEDULECHANNEL, channel.getIndex(), dateFormat.format(date));
        org.jsoup.nodes.Document doc = new HttpContent(direction).getDocument();
        Elements elements = doc.select(UtilStrings.STR_ELMDOCSELECT);
        for (org.jsoup.nodes.Element element : elements){
        	//Time
            String string_time = element.html().trim();
            Date startDate = null;
            Date endDate = null;
            if (Integer.parseInt(string_time.split(":")[0]) < Integer.parseInt(otime.split(":")[0])) {
                dt.add(Calendar.DATE, 1);
            }
            otime = string_time;
            try {
                startDate = dateTimeFormat.parse(dateFormat.format(dt.getTime()) + " " + string_time + ":00");
                endDate = dateTimeFormat.parse(dateFormat.format(dt.getTime()) + " " + "23:59:59");
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            //Title
            String string_title = "";
            String string_full_description_url = "";
            org.jsoup.nodes.Element element_title = element.nextElementSibling();
            try {
            	if (element_title != null) {
            		Elements elements_title = element_title.select(UtilStrings.STR_ELMDOCTITLE);
	                if (elements_title != null) {
	                	string_full_description_url = elements_title.select("a").attr("href");
	                    string_title = elements_title.text();
	                }
            	}
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            //Description
            String string_description = "";
            String string_description_head = "";
            org.jsoup.nodes.Element element_description = element_title.nextElementSibling();
            try {
            	if (element_description != null) {
            		Elements elements_description = element_description.select(UtilStrings.STR_ELMDOCDESC);
	                if (elements_description != null && !CommonTypes.FULL_DESC) {
	                	string_description = elements_description.html();
                        string_description_head = elements_description.select("b").text();
                        string_description = Jsoup.parse(string_description.replaceAll("<br>", ";")
                                .replace(string_description_head, ""))
                                .text()
                                .replaceAll(";", "<br>");
	                }
            	}
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            Programme programme = new Programme(channel.getIndex(), startDate, endDate, string_title);            
            programme.setCorrectionTime(channel.getCorrection());
            setCategory(programme);
            setDescription(programme, string_description, string_description_head, string_full_description_url);
            getProgrammes().add(programme);
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

    private void setCategory(Programme programme) {
        try {
            String ctitle = new String(programme.getTitle().getBytes(), "UTF-8").toLowerCase(); 
        	for (CategoryProgramme category : CommonTypes.catList.getData()) {
        		if (category.getId() != 0 && titleContainsDictWorlds(ctitle, category.getDictionary())) {
        			programme.setCategory(category.getId());
        			break;
        		}
        	}
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        }
    }

    private void setDescription(Programme programme, String description, String head_description, String url_description) {
    	
        if (description.length() > 0 && !description.equals("") && !CommonTypes.FULL_DESC) {
            if (head_description.length() > 0 && !head_description.equals("")) {
                String[] list = head_description.split(",");
                programme.setCountry(list[0].trim());
                programme.setYear(list[1].trim());
                programme.setGenres(list[2].trim().replace(" / ", ", "));
            }

            programme.setDescription(description.replaceFirst("<br>", ""));
        }        
        if (url_description.length() > 0 && !url_description.equals("")) {
            //Parse url
        	//String link = UtilStrings.HOST + url_description;
            String[] list_url = url_description.replace(".html", "").split("_");
            String type = list_url[0].trim();
            programme.setType(type);
            String catalog = list_url[1].trim();
            programme.setCatalog(Integer.parseInt(catalog));
            if (programme.getCategory() == 0) {
                if (type.equals("film")) {
                	programme.setCategory(1);
                }
                if (type.equals("series")) {
                	programme.setCategory(2);
                }
                if (type.equals("show")) {
                	programme.setCategory(7);
                }
            }
            if (CommonTypes.FULL_DESC) {
                //
            }
        }
    }

    public void runParser() {
        getChannels().loadFromDB();
        getContent(false);
        getProgrammes().setProgrammeStop();
        try {
        	saveXML();
        } catch (TransformerConfigurationException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
        		
    }

    public void runParserGUI() throws InterruptedException {
        pMonitor.start("");
        pMonitor.setIndeterminate(true);
        pMonitor.setCurrent(Messages.getString("StrLoadChanels"), 0);
        getChannels().loadFromDB();
        getProgrammes().clearDBSchedule();
        Thread.sleep(100);
        pMonitor.setIndeterminate(false);
        getContent(true);
        pMonitor.setIndeterminate(true);
        pMonitor.setCurrent(Messages.getString("StrProcessShelude"), 0);
        getProgrammes().setProgrammeStop();
        Thread.sleep(100);
        pMonitor.setIndeterminate(false);
        getProgrammes().saveToDB(pMonitor);
        pMonitor.setCurrent("", -2);
    }

    @Override
    public void run() {
    	try {
    		runParserGUI();
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    		Thread.currentThread().interrupt();
    		System.out.println(e.getMessage());
    	}

    }

    public ProgressMonitor getMonitor() {
        return pMonitor;
    }
}

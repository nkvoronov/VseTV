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
import common.ScheduleCategory;
import common.CommonTypes;
import common.ProgressMonitor;
import common.UtilStrings;
import gui.Messages;

public class ParserVseTV implements Runnable {
    private ChannelsList channels;
    private SchedulesList schedules;
    private int countDay;
    private boolean fullDesc;
    private String outXML;
    private ProgressMonitor pMonitor;

    public ParserVseTV(String outXML, int countDay, boolean fullDesc) {
        this.outXML = outXML;
        this.countDay = countDay;
        this.fullDesc = fullDesc;
        this.channels = new ChannelsList(true);
        this.schedules = new SchedulesList();
        this.pMonitor = new ProgressMonitor(0,false);
    }

    public ChannelsList getChannels() {
        return channels;
    }

    public SchedulesList getSchedules() {
        return schedules;
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
            getSchedules().getXML(document, rootElement);
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
        getSchedules().getData().clear();
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
	                if (elements_description != null && !CommonTypes.appConfig.isFullDesc()) {
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
            Schedule schedule = new Schedule(channel.getIndex(), startDate, endDate, string_title);            
            schedule.setCorrectionTime(channel.getCorrection());
            setCategory(schedule);
            setDescription(schedule, string_description, string_description_head, string_full_description_url);
            getSchedules().add(schedule);
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

    private void setCategory(Schedule schedule) {
        try {
            String ctitle = new String(schedule.getTitle().getBytes(), "UTF-8").toLowerCase(); 
        	for (ScheduleCategory category : CommonTypes.catList.getData()) {
        		if (category.getId() != 0 && titleContainsDictWorlds(ctitle, category.getDictionary())) {
        			schedule.setCategory(category.getId());
        			break;
        		}
        	}
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        }
    }

    private void setDescription(Schedule schedule, String description, String head_description, String url_description) {
    	
        if (description.length() > 0 && !description.equals("") && !CommonTypes.appConfig.isFullDesc()) {
            if (head_description.length() > 0 && !head_description.equals("")) {
                String[] list = head_description.split(",");
                schedule.setCountry(list[0].trim());
                schedule.setYear(list[1].trim());
                schedule.setGenres(list[2].trim().replace(" / ", ", "));
            }

            String[] list_desc = description.replaceFirst("<br>", "").split(" <br> <br>");
            schedule.setActors(list_desc[0]);
            schedule.setDescription(list_desc[1]);
        }        
        if (url_description.length() > 0 && !url_description.equals("")) {
            //Parse url
        	//String link = UtilStrings.HOST + url_description;
            String[] list_url = url_description.replace(".html", "").split("_");
            String type = list_url[0].trim();
            schedule.setType(type);
            String catalog = list_url[1].trim();
            schedule.setCatalog(Integer.parseInt(catalog));
            if (schedule.getCategory() == 0) {
                if (type.equals("film")) {
                	schedule.setCategory(1);
                }
                if (type.equals("series")) {
                	schedule.setCategory(2);
                }
                if (type.equals("show")) {
                	schedule.setCategory(7);
                }
            }
            if (CommonTypes.appConfig.isFullDesc()) {
                //
            }
        }
    }

    public void runParser() {
        getChannels().loadFromDB();
        if (getChannels().size() > 0) {
	        getContent(false);
	        getSchedules().setScheduleStop();
	        try {
	        	saveXML();
	        } catch (TransformerConfigurationException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
        }        		
    }

    public void runParserGUI() throws InterruptedException {
        pMonitor.start("");
        pMonitor.setIndeterminate(true);
        pMonitor.setCurrent(Messages.getString("StrLoadChanels"), 0);
        getChannels().loadFromDB();
        if (getChannels().size() > 0) {
	        getSchedules().clearDBSchedule();
	        Thread.sleep(100);
	        pMonitor.setIndeterminate(false);
	        getContent(true);
	        pMonitor.setIndeterminate(true);
	        pMonitor.setCurrent(Messages.getString("StrProcessShelude"), 0);
	        getSchedules().setScheduleStop();
	        Thread.sleep(100);
	        pMonitor.setIndeterminate(false);
	        getSchedules().saveToDB(pMonitor);
	        pMonitor.setCurrent("", -2);
        } else {
	        pMonitor.setIndeterminate(false);
        	pMonitor.setCurrent("", -2);
        }
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

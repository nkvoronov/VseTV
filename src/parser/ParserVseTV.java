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

    public Boolean isFullDesc() {
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
        last.add(Calendar.DATE, getCountDay());
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
	                if (elements_description != null) {
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
        String ctitle = schedule.getTitle().toLowerCase(); 
    	for (ScheduleCategory category : CommonTypes.catList.getData()) {
    		if (category.getId() != 0 && titleContainsDictWorlds(ctitle, category.getDictionary())) {
    			schedule.setCategory(category.getId());
    			break;
    		}
    	}
    }

    private void setDescription(Schedule schedule, String description, String head_description, String url_description) {   	
        if (description.length() > 0 && !description.equals("")) {
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
            //Parse url_description
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
            if (isFullDesc()) {
            	String type_desc = schedule.getType();
            	int catalog_desc = schedule.getCatalog();
            	Schedule copy_schedule = getSchedules().getScheduleForType(type_desc, catalog_desc);
            	if (copy_schedule != null) {            		
            		schedule.copyFullDesc(copy_schedule); 
            	} else {            		
                	String direction = String.format(UtilStrings.STR_SCHEDULEDESCRIPTION, type_desc, catalog_desc);
                    System.out.println("DIRECTIONS: " + direction);
                    org.jsoup.nodes.Document doc = new HttpContent(direction).getDocument();
                    String string_showname = "";
                    String string_showname_genre = "";                    
	            	try {
	            		Elements elements_showname = doc.select(UtilStrings.STR_ELMSHOWNAME);
	            		if (elements_showname != null) {
	            			string_showname = elements_showname.html();
	            			//Title	            			       			
//	            			string_showname = CommonTypes.parseString(string_showname, "</h2>", "<strong>");
//	            			string_showname = string_showname
//	            					.replace("</h2>", "")
//	            					.replace("<strong>", "")
//	            					.replace("\"", "")
//	            					.replace("<br><br>", ";")
//	            					.replaceAll("<br>", "");	            					
//	            			//TitleOrg
//	            			schedule.setTitle_org(string_showname.split(";")[0].trim());
//	            			//Country
//	            			schedule.setCountry(string_showname.split(";")[1].split(",")[0]);
//	            			//Year
//	            			schedule.setYear(string_showname.split(";")[1].split(",")[1]);
	            			//Genres
	            			string_showname_genre = elements_showname.select("strong").text().replace(" / ", ", ");
	            			schedule.setGenres(string_showname_genre);
	            			System.out.println("GENRES");
	            			System.out.println(string_showname_genre); 	            			
	            		}
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    System.out.println(e.getMessage());
	                }                                        
        			//Image
                    String string_img = "";
                    try {
	        			string_img = doc.select("img.mb_img").attr("src");
	        			if (string_img.length() > 0) {
	        				string_img = UtilStrings.HOST + string_img.replaceFirst("/", "");
	        				schedule.setImage(string_img);
	            			System.out.println("IMAGE");
	        				System.out.println(string_img);
	        				//Download image
	        				getSchedules().saveScheduleImage(schedule);
	        			}
			        } catch (Exception e) {
			        	e.printStackTrace();
			            System.out.println(e.getMessage());
			        }
        			//Full Description
                    String string_description = "";
                    try {
	        			Elements elements_big = doc.select("span.big");
	        			string_description = elements_big.html();	        			
	        			string_description = Jsoup.parse(string_description.replaceAll("<br>", ";"))
	        					.text()
	        					.replaceAll(";", "<br>")
	        					.trim();	            			
	        			schedule.setDescription(string_description);
	        			System.out.println("DESCRIPTION");
	        			System.out.println(string_description);	        			
			        } catch (Exception e) {
			            e.printStackTrace();
			            System.out.println(e.getMessage());
			        }
                    //Rating
                    String string_rating = "";
                    try {                    
	                    Elements elements_name = doc.select("span.name");
	                    string_rating = elements_name.get(0).text().split(":")[1].trim();
	                    schedule.setRating(string_rating);
	        			System.out.println("RATING");
	        			System.out.println(string_rating);
	        			System.out.println();
	        			System.out.println();	        			
			          } catch (Exception e) {
			              e.printStackTrace();
			              System.out.println(e.getMessage());
			          }                    		
            	}
            }
        }
    }
    
//    		String string_showmain = "";
//    		String string_showmain_directors = "";
//    		String string_showmain_actors = "";
//    		String string_showmain_vendors = "";
//    		String string_showmain_img = "";
//    		String string_showmain_description = "";
//    		String string_showmain_rating = "";
//    		try {            		
//        		Elements elements_showmain = doc.select(UtilStrings.STR_ELMSHOWMAIN);
//        		if (elements_showmain != null) {
//        			string_showmain = elements_showmain.html();
//        			System.out.println(string_showmain);
//        			System.out.println();
        			//Directors
//        			string_showmain_directors = CommonTypes.parseString(string_showmain, "Режиссер(ы):", "<br>");
//        			if (string_showmain_directors.length() > 0) {
//        				string_showmain_directors = Jsoup.parse(string_showmain_directors).text();
//        				schedule.setDirectors(string_showmain_directors);
//        			}
        			//Actors
//        			string_showmain_actors = CommonTypes.parseString(string_showmain, "Актеры:", "<br>");
//        			string_showmain_vendors = CommonTypes.parseString(string_showmain, "Ведущие:", "<br>");
//        			if (string_showmain_actors.length() > 0 || string_showmain_vendors.length() > 0) {
//        				if (string_showmain_actors.length() > 0) {
//        					string_showmain_actors = Jsoup.parse(string_showmain_actors).text();
//        					if (string_showmain_vendors.length() > 0) {
//        						string_showmain_actors = string_showmain_actors + ", ";
//        					}
//        				}
//        				if (string_showmain_vendors.length() > 0) {
//        					string_showmain_vendors = Jsoup.parse(string_showmain_vendors).text();
//        					string_showmain_actors = string_showmain_actors + string_showmain_vendors;
//        				}
//        				schedule.setActors(string_showmain_actors);
//        			}	            			

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

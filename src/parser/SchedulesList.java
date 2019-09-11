package parser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import common.CommonTypes;
import common.DBParams;
import common.DBUtils;
import common.ProgressMonitor;
import common.UtilStrings;
import gui.Messages;

public class SchedulesList {
    private List<Schedule> data;

    public SchedulesList() {
        this.data = new ArrayList<>();
    }

    public List<Schedule> getData() {
        return data;
    }
    
    public Schedule get(int position) {
        return getData().get(position);
    }
    
    public int size() {
        return getData().size();
    }

    public void clear() {
        getData().clear();
    }

    public void add(Schedule schedule) {
        getData().add(schedule);
    }

    public void saveToDB(ProgressMonitor monitor) {
        int i = 1;
        monitor.setTotal(this.data.size());
        for (Schedule schedule : getData()) {
            DBParams[] aParams = new DBParams[5];
            aParams[0] = new DBParams(1, schedule.getIndex(), CommonTypes.DBType.INTEGER);
            aParams[1] = new DBParams(2, schedule.getCategory(), CommonTypes.DBType.INTEGER);
            aParams[2] = new DBParams(3, CommonTypes.getDateFormat(schedule.getStart(), UtilStrings.DATE_FORMATTIME), CommonTypes.DBType.STRING);
            aParams[3] = new DBParams(4, CommonTypes.getDateFormat(schedule.getStop(), UtilStrings.DATE_FORMATTIME), CommonTypes.DBType.STRING);
            aParams[4] = new DBParams(5, schedule.getTitle(), CommonTypes.DBType.STRING);
            DBUtils.getExecutePreparedUpdate(DBUtils.SQL_SCHEDULE_INSERT, aParams);
            int scheduleID = getInsertScheduleID(schedule);
            saveDescriptionToDB(scheduleID, schedule);
            monitor.setCurrent(String.format(Messages.getString("StrSaveToBD"), i, this.size()), i);
            i++;
        }
    }

    private int getDescriptionID(Schedule schedule) {
        DBParams[] aParams = new DBParams[2];
        aParams[0] = new DBParams(1, schedule.getType(), CommonTypes.DBType.STRING);
        aParams[1] = new DBParams(2, schedule.getCatalog(), CommonTypes.DBType.INTEGER);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_DESCRIPTION_ID, aParams);
    }

    private void saveDescriptionToDB(int scheduleID, Schedule schedule) {
        int descriptionID;
        if (schedule.getDescription() != null) {
            descriptionID = getDescriptionID(schedule);
            if (descriptionID == -1) {
                DBParams[] aParams = new DBParams[11];
                if (schedule.getTitle_org() != null) {
                    aParams[0] = new DBParams(1, schedule.getTitle_org(), CommonTypes.DBType.STRING);
                } else {
                    aParams[0] = new DBParams(1, "", CommonTypes.DBType.STRING);
                }                
                aParams[1] = new DBParams(2, schedule.getDescription(), CommonTypes.DBType.STRING);
                if (schedule.getImage() != null) {
                    aParams[2] = new DBParams(3, schedule.getImage(), CommonTypes.DBType.STRING);
                } else {
                    aParams[2] = new DBParams(3, "", CommonTypes.DBType.STRING);
                }
                if (schedule.getGenres() != null) {
                    aParams[3] = new DBParams(4, schedule.getGenres(), CommonTypes.DBType.STRING);
                } else {
                    aParams[3] = new DBParams(4, "", CommonTypes.DBType.STRING);
                }
                if (schedule.getDirectors() != null) {
                    aParams[4] = new DBParams(5, schedule.getDirectors(), CommonTypes.DBType.STRING);
                } else {
                    aParams[4] = new DBParams(5, "", CommonTypes.DBType.STRING);
                }  
                if (schedule.getActors() != null) {
                    aParams[5] = new DBParams(6, schedule.getActors(), CommonTypes.DBType.STRING);
                } else {
                    aParams[5] = new DBParams(6, "", CommonTypes.DBType.STRING);
                }                 
                if (schedule.getCountry() != null) {
                    aParams[6] = new DBParams(7, schedule.getCountry(), CommonTypes.DBType.STRING);
                } else {
                    aParams[6] = new DBParams(7, "", CommonTypes.DBType.STRING);
                }
                if (schedule.getYear() != null) {
                    aParams[7] = new DBParams(8, schedule.getYear(), CommonTypes.DBType.STRING);
                } else {
                    aParams[7] = new DBParams(8, "", CommonTypes.DBType.STRING);
                }
                if (schedule.getRating() != null) {
                    aParams[8] = new DBParams(9, schedule.getRating(), CommonTypes.DBType.STRING);
                } else {
                    aParams[8] = new DBParams(9, "", CommonTypes.DBType.STRING);
                }
                if (schedule.getType() != null) {
                    aParams[9] = new DBParams(10, schedule.getType(), CommonTypes.DBType.STRING);
                } else {
                    aParams[9] = new DBParams(10, "", CommonTypes.DBType.STRING);
                }               
                aParams[10] = new DBParams(11, schedule.getCatalog(), CommonTypes.DBType.INTEGER);
                DBUtils.getExecutePreparedUpdate(DBUtils.SQL_DESCRIPTION_INSERT, aParams);
                descriptionID = getDescriptionID(schedule);
            }
            DBParams[] aParams = new DBParams[2];
            aParams[0] = new DBParams(1, scheduleID, CommonTypes.DBType.INTEGER);
            aParams[1] = new DBParams(2, descriptionID, CommonTypes.DBType.INTEGER);
            DBUtils.getExecutePreparedUpdate(DBUtils.SQL_SCHEDULE_DESCRIPTION_INSERT, aParams);
        }
    }

    private int getInsertScheduleID(Schedule schedule) {
        DBParams[] aParams = new DBParams[3];
        aParams[0] = new DBParams(1, CommonTypes.getDateFormat(schedule.getStart(), UtilStrings.DATE_FORMATTIME), CommonTypes.DBType.STRING);
        aParams[1] = new DBParams(2, CommonTypes.getDateFormat(schedule.getStop(), UtilStrings.DATE_FORMATTIME), CommonTypes.DBType.STRING);
        aParams[2] = new DBParams(3, schedule.getTitle(), CommonTypes.DBType.STRING);
        return DBUtils.getIdForPreparedStatement(DBUtils.SQL_MAINSCHEDULE_ID, aParams);
    }

    public void setScheduleStop() {
        int i = 0;
        while (i != size()) {
            Schedule schedule1 = this.data.get(i);
            Schedule schedule2;
            if (i + 1 != this.data.size()) {
            	schedule2 = this.data.get(i+1);
                if (schedule1.getIndex() == schedule2.getIndex()) {
                	schedule1.setStop(schedule2.getStart());
                }
            }
            i++;
        }
    }

    public int clearDBSchedule() {
        return DBUtils.getExecuteUpdate(DBUtils.SQL_MAINSCHEDULE_CLEAR);
    }

    public Schedule getScheduleForType(String type, int catalog) {
        for (Schedule schedule : getData()) {
            if (schedule.getType() != null && schedule.getType().equals(type) && schedule.getCatalog() != 0 && schedule.getCatalog() == catalog) {
            	return schedule;
            }
        }
        return null;
    }
    
    public void print() {
        for (Schedule schedule : getData()) {
        	schedule.print();
        }
    }

    public void getXML(org.w3c.dom.Document document, org.w3c.dom.Element element) {
        for (Schedule schedule : getData()) {
        	schedule.getXML(document, element);
        }
    }
    
    public void saveScheduleImage(Schedule schedule) {
        String cimage = schedule.getImage();
        String fileName = CommonTypes.getImagesPath() + String.format(UtilStrings.STR_IMAGE_NAME, schedule.getType(), schedule.getCatalog());
        try {
        	File file = new File(fileName);
        	if (!file.exists()) {
        		file.createNewFile();
        		BufferedImage img = ImageIO.read(new URL(cimage));
        		ImageIO.write(img, "jpg", file);                
            }
            
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        } catch (IOException e) {
        	e.printStackTrace();
        	System.out.println(e.getMessage());
        }
    }
}

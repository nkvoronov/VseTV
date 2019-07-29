package common;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@SuppressWarnings("serial")
public class DBTableModelOtherSchedule extends DBTableModel {
	
    public DBTableModelOtherSchedule(String aQuery){
        super(aQuery);
        this.fieldsSet = new DBFielsSetOtherSchedule();
    }

    @Override
    public Object getIconObject(Object obj) {
        String iconPatch = (String) obj;
        if (iconPatch != null) {
            File file = new File(iconPatch);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(iconPatch);
                Image img = icon.getImage();
                Image newimg = img.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                return new ImageIcon(newimg);
            } else return null;
        } else return null;
    }

}

package common;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@SuppressWarnings("serial")
public class DBTableModelChannels extends DBTableModel{

    public DBTableModelChannels(String aQuery){
        super(aQuery);
        this.fieldsSet = new DBFieldsSetChannels();
    }

    @Override
    public Object getIconObject(Object obj) {
        String iconPatch = (String) obj;
        if (iconPatch != null) {
            if (iconPatch.lastIndexOf('/') != -1) {
                iconPatch = CommonTypes.getIconsPatch() + iconPatch.substring(iconPatch.lastIndexOf('/') + 1);
                File file = new File(iconPatch);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(iconPatch);
                    Image img = icon.getImage();
                    Image newimg = img.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                    return new ImageIcon(newimg);
                } else return null;
            } else return null;
        } else return null;
    }

}

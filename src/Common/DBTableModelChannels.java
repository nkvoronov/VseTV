package Common;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static Common.Common.getIconsPatch;

@SuppressWarnings("serial")
public class DBTableModelChannels extends DBTableModel{

    public DBTableModelChannels(String Query){
        super(Query);
        this.fieldsSet = new DBFieldsSetChannels();
    }

    @Override
    public Object getIconObject(Object obj) {
        String IconPatch = (String) obj;
        if (IconPatch != null) {
            if (IconPatch.lastIndexOf("/") != -1) {
                IconPatch = getIconsPatch() + IconPatch.substring(IconPatch.lastIndexOf("/") + 1);
                File file = new File(IconPatch);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(IconPatch);
                    Image img = icon.getImage();
                    Image newimg = img.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                    return new ImageIcon(newimg);
                } else return null;
            } else return null;
        } else return null;
    }

}

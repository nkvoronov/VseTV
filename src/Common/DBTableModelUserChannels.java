package Common;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@SuppressWarnings("serial")
public class DBTableModelUserChannels extends DBTableModel {

    public DBTableModelUserChannels(String Query){
        super(Query);
        this.fieldsSet = new DBFieldsSetUserChannels();
    }

    @Override
    public Object getIconObject(Object obj) {
        String IconPatch = (String) obj;
        if (IconPatch != null) {
            File file = new File(IconPatch);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(IconPatch);
                Image img = icon.getImage();
                Image newimg = img.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
                return new ImageIcon(newimg);
            } else return null;
        } else return null;
    }

}

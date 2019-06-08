package Common;

import javax.swing.*;
import java.awt.*;

import static Common.Common.intToTime;

@SuppressWarnings("serial")
public class DBTableRenderUserChannels extends DBTableRender{

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            cellComponent.setBackground(new Color(51,153,255));
            cellComponent.setForeground(Color.WHITE);
        } else {
            cellComponent.setBackground(Color.WHITE);
            cellComponent.setForeground(Color.BLACK);
        }

        if (column == 4) {
            cellComponent.setText(intToTime(Integer.parseInt((String)value), ":", true, false));
        }
        return cellComponent;
    }
}

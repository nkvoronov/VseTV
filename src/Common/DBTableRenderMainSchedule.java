package Common;

import javax.swing.*;
import java.awt.*;

import static Common.Common.DTFormat;
import static Common.Common.intToTime;

@SuppressWarnings("serial")
public class DBTableRenderMainSchedule extends DBTableRender {

    private void SetColorCategory(JLabel cellComponent, JTable table, int row, int column) {
        for (CategoryProgramme cp : Common.CatList.getData()) {
            if (cp.getId() != 0) {
                if (cp.getName_ru().equals(table.getModel().getValueAt(row, 7))&&(column == 4)) {
                    cellComponent.setForeground(Color.decode("0x"+cp.getColor()));
                    break;
                }
            }
        }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        int tl = Common.isTimeLine((String)table.getModel().getValueAt(row,1),(String)table.getModel().getValueAt(row,2));
        cellComponent.setBackground(Color.WHITE);
        cellComponent.setForeground(Color.BLACK);

        if (tl == 0) {
            cellComponent.setFont(new Font("default", Font.ITALIC, 11));
            cellComponent.setForeground(Color.LIGHT_GRAY);
        } else {
            if (tl == 1) {
                cellComponent.setFont(new Font("default", Font.BOLD, 11));
            } else cellComponent.setFont(new Font("default", 0, 11));

            SetColorCategory(cellComponent, table, row, column);
        }

        if (isSelected) {
            cellComponent.setBackground(Color.decode("0x3399FF"));
            cellComponent.setForeground(Color.WHITE);
        }

        if (column == 1) {
            cellComponent.setText(DTFormat((String)value, false));
        }

        if (column == 3) {
            cellComponent.setText(intToTime(Integer.parseInt((String)value), ":", false, true));
        }
        return cellComponent;
    }
}

package common;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class DBTableRenderMainSchedule extends DBTableRender {

    private void setColorCategory(JLabel cellComponent, JTable table, int row, int column) {
    	String isOld;
        for (CategoryProgramme cp : CommonTypes.catList.getData()) {
            if (cp.getId() != 0) {
            	isOld = (String) table.getModel().getValueAt(row, 7);
                if (cp.getNameRU().equals(table.getModel().getValueAt(row, 9))&&(column == 4)&&(!isOld.equals("OLD"))) {
                    cellComponent.setForeground(Color.decode("0x"+cp.getColor()));
                    break;
                }
            }
        }
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String isOld = (String) table.getModel().getValueAt(row, 7);
        cellComponent.setBackground(Color.WHITE);
        cellComponent.setForeground(Color.BLACK);
        
        if (isSelected) {
            cellComponent.setBackground(Color.decode("0x3399FF"));
            cellComponent.setForeground(Color.WHITE);
        }

        if (isOld.equals("NOW")) {
        	cellComponent.setFont(new Font("default", Font.BOLD, 11));            
        } else {
            if (isOld.equals("OLD")) {
                cellComponent.setFont(new Font("default", Font.ITALIC, 11));
                cellComponent.setForeground(Color.LIGHT_GRAY);
            } else cellComponent.setFont(new Font("default", 0, 11));

            setColorCategory(cellComponent, table, row, column);
        }

        if (column == 1) {
            cellComponent.setText(CommonTypes.dtFormat((String)value, false));
        }

        if (column == 3) {
            cellComponent.setText(CommonTypes.intToTime(Integer.parseInt((String)value), ":", false, true));            
        }
        
        if (column == 1 || column == 3) {
        	cellComponent.setHorizontalAlignment(JLabel.CENTER);
        } else {
        	cellComponent.setHorizontalAlignment(JLabel.LEFT);
        }
        
        return cellComponent;
    }
}

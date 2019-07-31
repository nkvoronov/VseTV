package common;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class DBTableRenderMainSchedule extends DBTableRender {

    private void setColorCategory(JLabel cellComponent, JTable table, int row, int column) {
    	String isOld;
        for (CategoryProgramme cp : CommonTypes.catList.getData()) {
            if (cp.getId() != 0) {
            	isOld = (String) table.getModel().getValueAt(row, DBUtils.INDEX_ASCHELUDE_TIMETYPE);
                if (cp.getNameRU().equals(table.getModel().getValueAt(row, DBUtils.INDEX_ASCHELUDE_CAT_RU))&&(column == DBUtils.INDEX_ASCHELUDE_TITLE)&&(!isOld.equals("OLD"))) {
                    cellComponent.setForeground(Color.decode("0x"+cp.getColor()));
                    break;
                }
            }
        }
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String isOld = (String) table.getModel().getValueAt(row, DBUtils.INDEX_ASCHELUDE_TIMETYPE);
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

        if (column == DBUtils.INDEX_ASCHELUDE_SDATE) {
            cellComponent.setText(CommonTypes.dtFormat((String)value, false));
        }

        if (column == DBUtils.INDEX_ASCHELUDE_DURATION) {
            cellComponent.setText(CommonTypes.intToTime(Integer.parseInt((String)value), ":", false, true));            
        }
        
        if (column == DBUtils.INDEX_ASCHELUDE_SDATE || column == DBUtils.INDEX_ASCHELUDE_DURATION) {
        	cellComponent.setHorizontalAlignment(JLabel.CENTER);
        } else {
        	cellComponent.setHorizontalAlignment(JLabel.LEFT);
        }
        
        return cellComponent;
    }
}

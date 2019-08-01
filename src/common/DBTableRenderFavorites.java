package common;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class DBTableRenderFavorites extends DBTableRender {
	
    private void setColorCategory(JLabel cellComponent, JTable table, int row, int column) {
        for (CategoryProgramme cp : CommonTypes.catList.getData()) {
            if (cp.getId() != 0) {
                if (cp.getNameRU().equals(table.getModel().getValueAt(row, DBUtils.INDEX_FAVORITES_CAT_RU))&&(column == DBUtils.INDEX_FAVORITES_TITLE)) {
                    cellComponent.setForeground(Color.decode("0x"+cp.getColor()));
                    break;
                }
            }
        }
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        setColorCategory(cellComponent, table, row, column);

        if (column == DBUtils.INDEX_FAVORITES_SDATE) {
            cellComponent.setText(CommonTypes.dtFormat((String)value, false));
        }

        if (column == DBUtils.INDEX_FAVORITES_DURATION) {
            cellComponent.setText(CommonTypes.intToTime(Integer.parseInt((String)value), ":", false, true));
        }
        
        if (column == DBUtils.INDEX_FAVORITES_SDATE || column == DBUtils.INDEX_FAVORITES_DURATION) {
        	cellComponent.setHorizontalAlignment(JLabel.CENTER);
        } else {
        	cellComponent.setHorizontalAlignment(JLabel.LEFT);
        }
        
        return cellComponent;
    }

}

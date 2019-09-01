package common;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class DBTableRenderFavoritesChannels extends DBTableRender{
	
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cellComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (column == DBUtils.INDEX_FCHANNEL_CORRECTION) {
            cellComponent.setText(CommonTypes.intToTime(Integer.parseInt((String)value), ":", true, false));
        }
        
        if (column == DBUtils.INDEX_FCHANNEL_CORRECTION) {
        	cellComponent.setHorizontalAlignment(JLabel.CENTER);
        } else {
        	cellComponent.setHorizontalAlignment(JLabel.LEFT);
        }
        return cellComponent;
    }
}

package common;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

@SuppressWarnings("serial")
public class DBTableRender extends DefaultTableCellRenderer{

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        cellComponent.setBackground(SystemColor.window);
        cellComponent.setForeground(SystemColor.windowText);
            
        if (isSelected) {
            cellComponent.setBackground(SystemColor.textHighlight);
            cellComponent.setForeground(SystemColor.window);
        }
        
        return cellComponent;
    }

}

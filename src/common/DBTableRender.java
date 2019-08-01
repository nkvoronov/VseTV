package common;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

@SuppressWarnings("serial")
public class DBTableRender extends DefaultTableCellRenderer{

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        cellComponent.setBackground(Color.WHITE);
        cellComponent.setForeground(Color.BLACK);
            
        if (isSelected) {
            cellComponent.setBackground(Color.decode("0x3399FF"));
            cellComponent.setForeground(Color.WHITE);
        }
        
        return cellComponent;
    }

}

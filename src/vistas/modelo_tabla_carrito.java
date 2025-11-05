package vistas;

import modelo.Carrito;
import modelo.ItemCarrito;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class modelo_tabla_carrito extends AbstractTableModel {
    private final String[] COLUMNAS = {"ID Producto", "Nombre", "Cantidad", "Precio Unit.", "Subtotal"};
    private final Carrito carrito;

    public modelo_tabla_carrito(Carrito carrito) {
        this.carrito = carrito;
    }

    @Override
    public int getRowCount() {
        List<ItemCarrito> items = carrito.getItems();
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNAS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<ItemCarrito> items = carrito.getItems();
        ItemCarrito item = items.get(rowIndex);
        switch (columnIndex) {
            case 0: return item.getProducto().getId();
            case 1: return item.getProducto().getNombre();
            case 2: return item.getCantidad();
            case 3: return String.format("$%.2f", item.getProducto().getPrecio());
            case 4: return String.format("$%.2f", item.getSubtotal());
            default: return null;
        }
    }

    public String getProductoIdAt(int rowIndex) {
        List<ItemCarrito> items = carrito.getItems();
        return items.get(rowIndex).getProducto().getId();
    }

    public void refrescar() {
        fireTableDataChanged();
    }
}

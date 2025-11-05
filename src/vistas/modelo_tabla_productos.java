package vistas;

import modelo.Producto;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class modelo_tabla_productos extends AbstractTableModel {
    private final String[] COLUMNAS = {"ID", "Nombre", "Precio", "Stock"};
    private List<Producto> productos;
    public modelo_tabla_productos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public int getRowCount() {
        return productos.size();
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
        Producto producto = productos.get(rowIndex);
        switch (columnIndex) {
            case 0: return producto.getId();
            case 1: return producto.getNombre();
            case 2: return String.format("$%.2f", producto.getPrecio());
            case 3: return producto.getStock();
            default: return null;
        }
    }

    public Producto getProductoAt(int rowIndex) {
        return productos.get(rowIndex);
    }

    public void setProductos(List<Producto> nuevosProductos) {
        this.productos = nuevosProductos;
        fireTableDataChanged();
    }
}

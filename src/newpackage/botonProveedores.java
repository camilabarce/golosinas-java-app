package newpackage;

import java.awt.Component;
import java.awt.HeadlessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class botonProveedores extends javax.swing.JPanel {

    public botonProveedores() {
        initComponents();
        paramTablaProveedores = visor;
        mostrarProveedores(visor);
        jComboBoxProductos.setEnabled(true);
    }

    private JTable paramTablaProveedores;

    public void mostrarProveedores(JTable visor) {
        conexion conexion1 = new conexion("golosinas");
        Connection con = conexion1.conectar();

        // Consulta para obtener los proveedores y los productos que proveen con sus costos
        String sql = "SELECT p.proveedorID, p.nombre, p.telefono, p.correo, pp.costo, pr.descripcion "
                + "FROM Proveedores p "
                + "LEFT JOIN ProductosProveedores pp ON p.proveedorID = pp.proveedorID "
                + "LEFT JOIN Productos pr ON pp.productoID = pr.productoID";

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Nombre y apellido");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Correo");
        modelo.addColumn("Producto");
        modelo.addColumn("Costo");

        visor.setModel(modelo);

        // Alineación hacia la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        for (int i = 5; i <= 5; i++) {
            visor.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String codigo = rs.getString("proveedorID");
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                String producto = rs.getString("descripcion");
                String costo = rs.getString("costo");

                modelo.addRow(new Object[]{codigo, nombre, telefono, correo, producto, costo});

                // Ajustar el ancho de las columnas según el contenido
                for (int i = 0; i < 5; i++) {
                    TableColumn column = visor.getColumnModel().getColumn(i);
                    int maxWidth = 0;

                    for (int j = 0; j < visor.getRowCount(); j++) {
                        TableCellRenderer cellRenderer = visor.getCellRenderer(j, i);
                        Object value = visor.getValueAt(j, i);
                        Component c = cellRenderer.getTableCellRendererComponent(visor, value, false, false, j, i);
                        maxWidth = Math.max(maxWidth, c.getPreferredSize().width);
                    }

                    column.setPreferredWidth(maxWidth);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }

    public void seleccionarProveedor(JTable paramTablaProveedores, JTextField paramNombre, JTextField paramCorreo, JTextField paramTelefono, JComboBox<String> comboBoxProductos, JTextField paramCosto) {
        try {
            int fila = paramTablaProveedores.getSelectedRow();
            if (fila >= 0) { // Si se ha seleccionado una fila
                // Obtener los datos del proveedor
                String nombre = (paramTablaProveedores.getValueAt(fila, 1) != null) ? paramTablaProveedores.getValueAt(fila, 1).toString() : "";
                String correo = (paramTablaProveedores.getValueAt(fila, 3) != null) ? paramTablaProveedores.getValueAt(fila, 3).toString() : "";
                String telefono = (paramTablaProveedores.getValueAt(fila, 2) != null) ? paramTablaProveedores.getValueAt(fila, 2).toString() : "";

                // Mostrar los datos del proveedor en los campos de texto
                paramNombre.setText(nombre);
                paramCorreo.setText(correo);
                paramTelefono.setText(telefono);

                // Limpiar el ComboBox
                comboBoxProductos.removeAllItems();

                // Obtener el ID del proveedor seleccionado
                int proveedorID = Integer.parseInt(paramTablaProveedores.getValueAt(fila, 0).toString());

                // Agregar todos los productos al ComboBox
                String sqlTodosProductos = "SELECT descripcion FROM Productos ORDER BY descripcion";
                conexion conexion1 = new conexion("golosinas");
                Connection con = conexion1.conectar();
                PreparedStatement psTodosProductos = con.prepareStatement(sqlTodosProductos);
                ResultSet rsTodosProductos = psTodosProductos.executeQuery();
                while (rsTodosProductos.next()) {
                    comboBoxProductos.addItem(rsTodosProductos.getString(1)); // Añadir la descripción del producto al ComboBox
                }

                // Obtener los productos que provee el proveedor
                String sqlProductosProveedor = "SELECT p.descripcion FROM Productos p INNER JOIN ProductosProveedores pp ON p.productoID = pp.productoID WHERE pp.proveedorID = ? ORDER BY p.descripcion";
                PreparedStatement psProductosProveedor = con.prepareStatement(sqlProductosProveedor);
                psProductosProveedor.setInt(1, proveedorID);
                ResultSet rsProductosProveedor = psProductosProveedor.executeQuery();
                while (rsProductosProveedor.next()) {
                    String descripcionProducto = rsProductosProveedor.getString(1);
                    comboBoxProductos.removeItem(descripcionProducto); // Eliminar el producto del ComboBox
                    comboBoxProductos.insertItemAt(descripcionProducto, 0); // Insertar el producto al principio del ComboBox
                }

                // Mostrar el costo del primer producto en el ComboBox si hay al menos un producto
                if (comboBoxProductos.getItemCount() > 0) {
                    String descripcionProducto = comboBoxProductos.getItemAt(0);
                    String sqlCosto = "SELECT pp.costo FROM Productos p INNER JOIN ProductosProveedores pp ON p.productoID = pp.productoID WHERE p.descripcion = ? AND pp.proveedorID = ?";
                    PreparedStatement psCosto = con.prepareStatement(sqlCosto);
                    psCosto.setString(1, descripcionProducto);
                    psCosto.setInt(2, proveedorID);
                    ResultSet rsCosto = psCosto.executeQuery();
                    if (rsCosto.next()) {
                        paramCosto.setText(rsCosto.getString(1));
                    }
                } else {
                    paramCosto.setText(""); // Si no hay productos, dejar el campo de costo vacío
                }

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar el proveedor: " + e.toString());
        }
    }

    // Método para obtener el ID del producto seleccionado en el ComboBox
    private int obtenerIDProducto(String descripcionProducto) throws SQLException {
        String consulta = "SELECT productoID FROM Productos WHERE descripcion = ?";
        conexion conexion1 = new conexion("golosinas");
        PreparedStatement ps = conexion1.conectar().prepareStatement(consulta);
        ps.setString(1, descripcionProducto);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            throw new SQLException("No se encontró el ID del producto.");
        }
    }

    public void insertarProveedor(JTextField paramNombre, JTextField paramCorreo, JTextField paramTelefono, JComboBox<String> comboBoxProductos, JTextField paramCosto) {
        String nombre = paramNombre.getText();
        String correo = paramCorreo.getText();
        String telefono = paramTelefono.getText();

        conexion conexion1 = new conexion("golosinas");

        // Consulta para insertar el proveedor
        String consultaProveedor = "INSERT INTO Proveedores (nombre, correo, telefono) VALUES (?, ?, ?);";

        // Consulta para insertar el producto proveedor
        String consultaProductoProveedor = "INSERT INTO ProductosProveedores (productoID, proveedorID, costo) VALUES (?, ?, ?);";

        try {
            // Insertar el proveedor
            PreparedStatement psProveedor = conexion1.conectar().prepareStatement(consultaProveedor, Statement.RETURN_GENERATED_KEYS);
            psProveedor.setString(1, nombre);
            psProveedor.setString(2, correo);
            psProveedor.setString(3, telefono);
            psProveedor.executeUpdate();
            ResultSet rsProveedor = psProveedor.getGeneratedKeys();
            rsProveedor.next();
            int proveedorID = rsProveedor.getInt(1);

            // Obtener el ID del producto seleccionado en el ComboBox
            String productoSeleccionado = (String) comboBoxProductos.getSelectedItem();
            int productoID = obtenerIDProducto(productoSeleccionado);

            // Insertar el producto proveedor
            PreparedStatement psProductoProveedor = conexion1.conectar().prepareStatement(consultaProductoProveedor);
            psProductoProveedor.setInt(1, productoID);
            psProductoProveedor.setInt(2, proveedorID);
            if (!paramCosto.getText().isEmpty()) {
                double costo = Double.parseDouble(paramCosto.getText());
                psProductoProveedor.setDouble(3, costo);
            } else {
                psProductoProveedor.setNull(3, Types.DOUBLE);
            }
            psProductoProveedor.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se agregó correctamente el proveedor.");

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se agregó el proveedor :(, error " + e.toString());
        }
    }

    private void cargarProductosEnComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();

        // Consulta para obtener los productos
        String consultaProductos = "SELECT descripcion FROM Productos ORDER BY descripcion";

        try {
            conexion conexion1 = new conexion("golosinas");
            Connection con = conexion1.conectar();
            PreparedStatement ps = con.prepareStatement(consultaProductos);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getString("descripcion"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los productos: " + ex.toString());
        }
    }

    public void modificarProveedor(JTable paramTablaProveedores, JTextField paramNombre, JTextField paramCorreo, JTextField paramTelefono, JComboBox<String> comboBoxProductos, JTextField paramCosto) {
        String nombre = paramNombre.getText();
        String correo = paramCorreo.getText();
        String telefono = paramTelefono.getText();

        conexion conexion1 = new conexion("golosinas");

        // Consulta para actualizar los datos del proveedor
        String consultaProveedor = "UPDATE Proveedores SET nombre = ?, correo = ?, telefono = ? WHERE proveedorID = ?;";

        // Consulta para actualizar el costo del producto proveedor
        String consultaProductoProveedor = "UPDATE ProductosProveedores SET costo = ? WHERE productoID = ? AND proveedorID = ?;";

        try {
            // Obtener el proveedor seleccionado desde la tabla
            int fila = paramTablaProveedores.getSelectedRow();
            System.out.println("2- fila seleccionada : " + fila);
            if (fila >= 0) {
                int proveedorID = Integer.parseInt(paramTablaProveedores.getValueAt(fila, 0).toString());

                // Actualizar los datos del proveedor
                PreparedStatement psProveedor = conexion1.conectar().prepareStatement(consultaProveedor);
                psProveedor.setString(1, nombre);
                psProveedor.setString(2, correo);
                psProveedor.setString(3, telefono);
                psProveedor.setInt(4, proveedorID);
                psProveedor.executeUpdate();

                // Si hay un producto seleccionado en el ComboBox, actualizar su costo
                if (comboBoxProductos.getSelectedItem() != null) {
                    String productoSeleccionado = (String) comboBoxProductos.getSelectedItem();
                    int productoID = obtenerIDProducto(productoSeleccionado);

                    double costo = Double.parseDouble(paramCosto.getText());

                    PreparedStatement psProductoProveedor = conexion1.conectar().prepareStatement(consultaProductoProveedor);
                    psProductoProveedor.setDouble(1, costo);
                    psProductoProveedor.setInt(2, productoID);
                    psProductoProveedor.setInt(3, proveedorID);
                    psProductoProveedor.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "¡Modificación exitosa!");
            } else {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un proveedor para modificar.");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "No se pudo modificar el proveedor. Error: " + e.toString());
        }
    }

    public void aplicarFiltro(String criterio) {
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(paramTablaProveedores.getModel());
        paramTablaProveedores.setRowSorter(rowSorter);

        // Divide el criterio en palabras individuales
        String[] palabras = criterio.split("\\s+");

        // Crea una lista de filtros para cada palabra en nombre, telefono, correo, producto y costo
        var filtros = new ArrayList<RowFilter<Object, Object>>();
        for (String palabra : palabras) {
            RowFilter<Object, Object> filtroNombre = RowFilter.regexFilter("(?i)" + palabra, 1); // Filtrar por columna de Nombre 
            RowFilter<Object, Object> filtroTelefono = RowFilter.regexFilter("(?i)" + palabra, 2); // Filtrar por columna de Teléfono
            RowFilter<Object, Object> filtroCorreo = RowFilter.regexFilter("(?i)" + palabra, 3); // Filtrar por columna de Correo 
            RowFilter<Object, Object> filtroProducto = RowFilter.regexFilter("(?i)" + palabra, 4); // Filtrar por columna de Producto 
            RowFilter<Object, Object> filtroCosto = RowFilter.regexFilter("(?i)" + palabra, 5); // Filtrar por columna de Costo 

            // Combinar todos los filtros en uno solo para cada palabra
            var filtroPalabra = RowFilter.orFilter(Arrays.asList(filtroNombre, filtroTelefono, filtroCorreo, filtroProducto, filtroCosto));
            filtros.add(filtroPalabra);
        }

        // Combinar todos los filtros en uno solo
        var filtroFinal = RowFilter.andFilter(filtros);

        // Aplicar el filtro combinado
        rowSorter.setRowFilter(filtroFinal);
    }

    public void eliminarProductoProveedor(JTable paramTablaProveedores, JComboBox<String> comboBoxProductos) {
        conexion conexion1 = new conexion("golosinas");

        String consultaEliminarProductoProveedor = "DELETE FROM ProductosProveedores WHERE proveedorID = ? AND productoID = ?";

        try {
            int fila = paramTablaProveedores.getSelectedRow();
            if (fila >= 0) {
                String proveedorID = paramTablaProveedores.getValueAt(fila, 0).toString();
                System.out.println(proveedorID);
                String productoSeleccionado = comboBoxProductos.getSelectedItem().toString();
                System.out.println(productoSeleccionado);
                // Obtener el ID del producto seleccionado
                String consultaIDProducto = "SELECT productoID FROM Productos WHERE descripcion = ?";
                PreparedStatement psIDProducto = conexion1.conectar().prepareStatement(consultaIDProducto);
                psIDProducto.setString(1, productoSeleccionado);
                ResultSet rsIDProducto = psIDProducto.executeQuery();
                if (rsIDProducto.next()) {
                    int productoID = rsIDProducto.getInt("productoID");

                    // Eliminar el producto proveedor
                    PreparedStatement psEliminarProductoProveedor = conexion1.conectar().prepareStatement(consultaEliminarProductoProveedor);
                    psEliminarProductoProveedor.setString(1, proveedorID);
                    psEliminarProductoProveedor.setInt(2, productoID);
                    psEliminarProductoProveedor.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Se eliminó correctamente el producto del proveedor.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo obtener el ID del producto seleccionado.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un proveedor para eliminar el producto.");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto del proveedor: " + e.toString());
        }
    }

    public void borrarTextoBusqueda() {
        txfBuscar.setText("");
    }

    public void restaurarTextoBusqueda() {
        if (txfBuscar.getText().isEmpty()) {
            txfBuscar.setText("Buscar");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        visor = new javax.swing.JTable();
        jLabelNombre = new javax.swing.JLabel();
        jComboBoxProductos = new javax.swing.JComboBox<>();
        jLabelCorreo = new javax.swing.JLabel();
        jLabelTelefono = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txfBuscar = new javax.swing.JTextField();
        txfCorreo = new javax.swing.JTextField();
        txfTelefono = new javax.swing.JTextField();
        txfNombre = new javax.swing.JTextField();
        jLabelCosto = new javax.swing.JLabel();
        txfCosto = new javax.swing.JTextField();

        setBackground(new java.awt.Color(204, 204, 204));
        setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        setMaximumSize(new java.awt.Dimension(690, 450));
        setMinimumSize(new java.awt.Dimension(690, 450));
        setName("background"); // NOI18N
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Proveedores", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei Light", 0, 12), new java.awt.Color(102, 102, 102))); // NOI18N

        visor.setBackground(new java.awt.Color(255, 255, 255));
        visor.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 14)); // NOI18N
        visor.setForeground(new java.awt.Color(0, 0, 0));
        visor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        visor.setGridColor(new java.awt.Color(204, 204, 204));
        visor.setSelectionBackground(new java.awt.Color(102, 102, 102));
        visor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                visorMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(visor);

        jLabelNombre.setBackground(new java.awt.Color(255, 255, 255));
        jLabelNombre.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabelNombre.setForeground(new java.awt.Color(51, 51, 51));
        jLabelNombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelNombre.setText("Nombre:");

        jComboBoxProductos.setBackground(new java.awt.Color(255, 255, 255));
        jComboBoxProductos.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jComboBoxProductos.setForeground(new java.awt.Color(51, 51, 51));
        jComboBoxProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Productos" }));
        jComboBoxProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBoxProductosMouseClicked(evt);
            }
        });
        jComboBoxProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxProductosActionPerformed(evt);
            }
        });

        jLabelCorreo.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCorreo.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabelCorreo.setForeground(new java.awt.Color(51, 51, 51));
        jLabelCorreo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelCorreo.setText("Correo:");

        jLabelTelefono.setBackground(new java.awt.Color(255, 255, 255));
        jLabelTelefono.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabelTelefono.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTelefono.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTelefono.setText("Teléfono:");

        btnAgregar.setBackground(new java.awt.Color(204, 255, 204));
        btnAgregar.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnAgregar.setForeground(new java.awt.Color(0, 0, 0));
        btnAgregar.setText("Agregar");
        btnAgregar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(255, 204, 153));
        btnModificar.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(0, 0, 0));
        btnModificar.setText("Modificar");
        btnModificar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificar.setMaximumSize(new java.awt.Dimension(47, 20));
        btnModificar.setMinimumSize(new java.awt.Dimension(47, 20));
        btnModificar.setPreferredSize(new java.awt.Dimension(47, 20));
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setBackground(new java.awt.Color(255, 153, 153));
        btnEliminar.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(0, 0, 0));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        txfBuscar.setBackground(new java.awt.Color(204, 204, 204));
        txfBuscar.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        txfBuscar.setForeground(new java.awt.Color(102, 102, 255));
        txfBuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txfBuscar.setText("Buscar");
        txfBuscar.setName(""); // NOI18N
        txfBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txfBuscarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txfBuscarFocusLost(evt);
            }
        });
        txfBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfBuscarActionPerformed(evt);
            }
        });
        txfBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txfBuscarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txfBuscarKeyReleased(evt);
            }
        });

        txfCorreo.setBackground(new java.awt.Color(255, 255, 255));
        txfCorreo.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfCorreo.setForeground(new java.awt.Color(51, 51, 51));
        txfCorreo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfCorreo.setName(""); // NOI18N
        txfCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfCorreoActionPerformed(evt);
            }
        });

        txfTelefono.setBackground(new java.awt.Color(255, 255, 255));
        txfTelefono.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfTelefono.setForeground(new java.awt.Color(51, 51, 51));
        txfTelefono.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfTelefono.setName(""); // NOI18N
        txfTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfTelefonoActionPerformed(evt);
            }
        });

        txfNombre.setBackground(new java.awt.Color(255, 255, 255));
        txfNombre.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfNombre.setForeground(new java.awt.Color(51, 51, 51));
        txfNombre.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfNombre.setName(""); // NOI18N
        txfNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfNombreActionPerformed(evt);
            }
        });

        jLabelCosto.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCosto.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        jLabelCosto.setForeground(new java.awt.Color(51, 51, 51));
        jLabelCosto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelCosto.setText("Costo:");

        txfCosto.setBackground(new java.awt.Color(255, 255, 255));
        txfCosto.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfCosto.setForeground(new java.awt.Color(51, 51, 51));
        txfCosto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfCosto.setName(""); // NOI18N
        txfCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfCostoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addComponent(jLabelNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabelTelefono)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txfTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addComponent(jLabelCorreo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txfCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(txfBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                        .addGap(48, 48, 48))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBoxProductos, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(jLabelCosto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txfCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(54, 54, 54))
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelNombre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txfCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCorreo)
                            .addComponent(jLabelCosto)
                            .addComponent(txfCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txfBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txfTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelTelefono))))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 450));
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxProductosActionPerformed

    }//GEN-LAST:event_jComboBoxProductosActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        botonProveedores botonproveedores = new botonProveedores();
        botonproveedores.insertarProveedor(txfNombre, txfCorreo, txfTelefono, jComboBoxProductos, txfCosto);
        botonproveedores.mostrarProveedores(visor);
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        botonProveedores botonproveedores = new botonProveedores();
        botonproveedores.modificarProveedor(visor, txfNombre, txfCorreo, txfTelefono, jComboBoxProductos, txfCosto);
        botonproveedores.mostrarProveedores(visor);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        botonProveedores botonproveedores = new botonProveedores();
        botonproveedores.eliminarProductoProveedor(paramTablaProveedores, jComboBoxProductos);
        botonproveedores.mostrarProveedores(visor);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txfBuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txfBuscarFocusGained
        borrarTextoBusqueda();
    }//GEN-LAST:event_txfBuscarFocusGained

    private void txfBuscarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txfBuscarFocusLost
        restaurarTextoBusqueda();
    }//GEN-LAST:event_txfBuscarFocusLost

    private void txfBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfBuscarActionPerformed

    }//GEN-LAST:event_txfBuscarActionPerformed

    private void txfBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfBuscarKeyPressed
        String criterio = txfBuscar.getText();
        aplicarFiltro(criterio);
    }//GEN-LAST:event_txfBuscarKeyPressed

    private void txfBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfBuscarKeyReleased
        String criterio = txfBuscar.getText();
        aplicarFiltro(criterio);
    }//GEN-LAST:event_txfBuscarKeyReleased

    private void txfCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfCorreoActionPerformed

    private void txfTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfTelefonoActionPerformed

    private void txfNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfNombreActionPerformed

    private void txfCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfCostoActionPerformed

    private void visorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visorMouseClicked
        botonProveedores botonproveedores = new botonProveedores();
        botonproveedores.seleccionarProveedor(visor, txfNombre, txfCorreo, txfTelefono, jComboBoxProductos, txfCosto);
    }//GEN-LAST:event_visorMouseClicked

    private void jComboBoxProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxProductosMouseClicked
        String productoSeleccionado = (String) jComboBoxProductos.getSelectedItem();
        cargarProductosEnComboBox(jComboBoxProductos);
    }//GEN-LAST:event_jComboBoxProductosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> jComboBoxProductos;
    private javax.swing.JLabel jLabelCorreo;
    private javax.swing.JLabel jLabelCosto;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField txfBuscar;
    private javax.swing.JTextField txfCorreo;
    private javax.swing.JTextField txfCosto;
    private javax.swing.JTextField txfNombre;
    private javax.swing.JTextField txfTelefono;
    public javax.swing.JTable visor;
    // End of variables declaration//GEN-END:variables
}

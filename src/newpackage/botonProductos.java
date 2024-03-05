package newpackage;

import java.awt.Component;
import java.awt.HeadlessException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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

public class botonProductos extends javax.swing.JPanel {

    public botonProductos() {
        initComponents();
        paramTablaProductos = visor;
        mostrarProductos(visor);
        txfCodigo.setEnabled(false);
    }

    private JTable paramTablaProductos;

    String codigo;
    String marca;
    String descripcion;
    double precioUnidad;
    double precioCaja;
    int stock;
    String categoria;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public double getPrecioCaja() {
        return precioCaja;
    }

    public void setPrecioCaja(double precioCaja) {
        this.precioCaja = precioCaja;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void mostrarProductos(JTable visor) {
        conexion conexion1 = new conexion("golosinas");
        Connection con = conexion1.conectar();
        String sql = "SELECT "
                + "    p.productoID, "
                + "    c.categoria, "
                + "    m.marca AS marca, "
                + "    p.descripcion, "
                + "    p.precio_unidad, "
                + "    p.precio_caja, "
                + "    p.stock "
                + "FROM "
                + "    Productos p "
                + "JOIN "
                + "    Marca m ON p.marcaID = m.marcaID "
                + "JOIN "
                + "    Categorias c ON p.categoriaID = c.categoriaID "
                + "ORDER BY m.marca;";

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Categoría");
        modelo.addColumn("Marca");
        modelo.addColumn("Descripción");
        modelo.addColumn("Precio x unidad");
        modelo.addColumn("Precio x caja");
        modelo.addColumn("Stock");

        visor.setModel(modelo);

        String[] datos = new String[7];

        // Configurar la alineación hacia la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        for (int i = 4; i <= 6; i++) {
            visor.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                String[] filaDatos = new String[7];

                filaDatos[0] = rs.getString(1);
                filaDatos[1] = rs.getString(2);
                filaDatos[2] = rs.getString(3);
                filaDatos[3] = rs.getString(4);
                filaDatos[4] = rs.getString(5);
                filaDatos[5] = rs.getString(6);
                filaDatos[6] = rs.getString(7);

                modelo.addRow(filaDatos);
            }

            // Ajustar el ancho de las columnas según el contenido
            for (int i = 0; i < 7; i++) {
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error" + e.toString());
        }
    }

    public void seleccionarProducto(JTable paramTablaProductos, JTextField paramCodigo, JTextField paramMarca, JTextField paramDescripcion, JTextField paramPrecioUnidad, JTextField paramPrecioCaja, JTextField paramStock, JTextField paramCategoria) {
        try {
            int fila = paramTablaProductos.getSelectedRow();

            if (fila >= 0) { // Si es mayor a 0, se está seleccionando
                paramCodigo.setText((paramTablaProductos.getValueAt(fila, 0) != null) ? paramTablaProductos.getValueAt(fila, 0).toString() : "");
                paramCategoria.setText((paramTablaProductos.getValueAt(fila, 1) != null) ? paramTablaProductos.getValueAt(fila, 1).toString() : "");
                paramMarca.setText((paramTablaProductos.getValueAt(fila, 2) != null) ? paramTablaProductos.getValueAt(fila, 2).toString() : "");
                paramDescripcion.setText((paramTablaProductos.getValueAt(fila, 3) != null) ? paramTablaProductos.getValueAt(fila, 3).toString() : "");
                paramPrecioUnidad.setText((paramTablaProductos.getValueAt(fila, 4) != null) ? paramTablaProductos.getValueAt(fila, 4).toString() : "");
                paramPrecioCaja.setText((paramTablaProductos.getValueAt(fila, 5) != null) ? paramTablaProductos.getValueAt(fila, 5).toString() : "");
                paramStock.setText((paramTablaProductos.getValueAt(fila, 6) != null) ? paramTablaProductos.getValueAt(fila, 6).toString() : "");
                
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de selección: " + e.toString());
        }
    }

    public void insertarProducto(JTextField paramMarca, JTextField paramDescripcion, JTextField paramPrecioUnidad, JTextField paramPrecioCaja, JTextField paramStock, JTextField paramCategoria) {
        setMarca(paramMarca.getText());
        setDescripcion(paramDescripcion.getText());

        // Convertir el precio de unidad y precio de caja a double
        setPrecioUnidad(Double.parseDouble(paramPrecioUnidad.getText()));
        setPrecioCaja(Double.parseDouble(paramPrecioCaja.getText()));

        setStock(Integer.parseInt(paramStock.getText()));
        setCategoria(paramCategoria.getText());

        conexion conexion1 = new conexion("golosinas");

        // Consulta para obtener o insertar la marca y obtener su ID
        String consultaMarca = "INSERT INTO Marca (marca) VALUES (?) ON DUPLICATE KEY UPDATE marca = ?;";
        String consultaIDMarca = "SELECT marcaID FROM Marca WHERE marca = ?;";

        // Consulta para obtener o insertar la categoría y obtener su ID
        String consultaCategoria = "INSERT INTO Categorias (categoria) VALUES (?) ON DUPLICATE KEY UPDATE categoria = ?;";
        String consultaIDCategoria = "SELECT categoriaID FROM Categorias WHERE categoria = ?;";

        // Consulta para insertar el producto utilizando los IDs obtenidos
        String consultaProducto = "INSERT INTO Productos (marcaID, descripcion, precio_unidad, precio_caja, stock, categoriaID) "
                + "VALUES (?, ?, ?, ?, ?, ?);";

        try {
            // Verificar si la marca ya existe y obtener su ID
            PreparedStatement psMarca = conexion1.conectar().prepareStatement(consultaIDMarca);
            psMarca.setString(1, getMarca());
            ResultSet rsMarca = psMarca.executeQuery();
            int marcaID;
            if (rsMarca.next()) {
                marcaID = rsMarca.getInt("marcaID");
            } else {
                // Si la marca no existe, insertarla y obtener su ID
                PreparedStatement psInsertMarca = conexion1.conectar().prepareStatement(consultaMarca, Statement.RETURN_GENERATED_KEYS);
                psInsertMarca.setString(1, getMarca());
                psInsertMarca.setString(2, getMarca());
                psInsertMarca.executeUpdate();
                ResultSet rsInsertMarca = psInsertMarca.getGeneratedKeys();
                rsInsertMarca.next();
                marcaID = rsInsertMarca.getInt(1);
            }

            // Verificar si la categoría ya existe y obtener su ID
            PreparedStatement psCategoria = conexion1.conectar().prepareStatement(consultaIDCategoria);
            psCategoria.setString(1, getCategoria());
            ResultSet rsCategoria = psCategoria.executeQuery();
            int categoriaID;
            if (rsCategoria.next()) {
                categoriaID = rsCategoria.getInt("categoriaID");
            } else {
                // Si la categoría no existe, insertarla y obtener su ID
                PreparedStatement psInsertCategoria = conexion1.conectar().prepareStatement(consultaCategoria, Statement.RETURN_GENERATED_KEYS);
                psInsertCategoria.setString(1, getCategoria());
                psInsertCategoria.setString(2, getCategoria());
                psInsertCategoria.executeUpdate();
                ResultSet rsInsertCategoria = psInsertCategoria.getGeneratedKeys();
                rsInsertCategoria.next();
                categoriaID = rsInsertCategoria.getInt(1);
            }

            // Insertar el producto utilizando los IDs obtenidos
            PreparedStatement psProducto = conexion1.conectar().prepareStatement(consultaProducto);
            psProducto.setInt(1, marcaID);
            psProducto.setString(2, getDescripcion());
            psProducto.setDouble(3, getPrecioUnidad());
            psProducto.setDouble(4, getPrecioCaja());
            psProducto.setInt(5, getStock());
            psProducto.setInt(6, categoriaID);
            psProducto.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se agregó correctamente el producto.");

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se agregó el producto :(, error " + e.toString());
        }
    }

    public void modificarProducto(JTextField paramCodigo, JTextField paramMarca, JTextField paramDescripcion, JTextField paramPrecioUnidad, JTextField paramPrecioCaja, JTextField paramStock, JTextField paramCategoria) {
        setCodigo(paramCodigo.getText());
        setMarca(paramMarca.getText());
        setDescripcion(paramDescripcion.getText());
        setPrecioUnidad(Double.parseDouble(paramPrecioUnidad.getText()));
        setPrecioCaja(Double.parseDouble(paramPrecioCaja.getText()));
        setStock(Integer.parseInt(paramStock.getText()));
        setCategoria(paramCategoria.getText());

        conexion conexion1 = new conexion("golosinas");

        String consulta = "UPDATE Productos "
                + "SET marcaID = (SELECT marcaID FROM Marca WHERE marca = ?), "
                + "descripcion = ?, "
                + "precio_unidad = ?, "
                + "precio_caja = ?, "
                + "stock = ?, "
                + "categoriaID = (SELECT categoriaID FROM Categorias WHERE categoria = ?) "
                + "WHERE productoID = ?;";

        try {
            PreparedStatement ps = conexion1.conectar().prepareStatement(consulta);

            ps.setString(1, getMarca());
            ps.setString(2, getDescripcion());
            ps.setDouble(3, getPrecioUnidad()); // Usar setDouble para precio de unidad
            ps.setDouble(4, getPrecioCaja()); // Usar setDouble para precio de caja
            ps.setInt(5, getStock());
            ps.setString(6, getCategoria());
            ps.setString(7, getCodigo());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "¡Modificación exitosa!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se modificó. Error: " + e.toString());
        }
    }

    public void eliminarProducto(JTextField paramCodigo) {
        setCodigo(paramCodigo.getText());
        conexion conexion1 = new conexion("golosinas");

        String consulta = "DELETE FROM Productos WHERE productoID = ?;";

        try {
            CallableStatement cs = conexion1.conectar().prepareCall(consulta);
            cs.setString(1, getCodigo());

            cs.execute();

            JOptionPane.showMessageDialog(null, "Se eliminó correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }

    public void aplicarFiltro(String criterio) {
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(paramTablaProductos.getModel());
        paramTablaProductos.setRowSorter(rowSorter);

        // Divide el criterio en palabras individuales
        String[] palabras = criterio.split("\\s+");

        // Crea una lista de filtros para cada palabra en la marca, descripción, categoría y precios
        var filtros = new ArrayList<RowFilter<Object, Object>>();
        for (String palabra : palabras) {
            RowFilter<Object, Object> filtroMarca = RowFilter.regexFilter("(?i)" + palabra, 1); // Filtrar por columna de Categoria 
            RowFilter<Object, Object> filtroDescripcion = RowFilter.regexFilter("(?i)" + palabra, 2); // Filtrar por columna de Marca 
            RowFilter<Object, Object> filtroCategoria = RowFilter.regexFilter("(?i)" + palabra, 3); // Filtrar por columna de Descripción
            RowFilter<Object, Object> filtroPrecioUnidad = RowFilter.regexFilter("(?i)" + palabra, 4); // Filtrar por columna de Precio por Unidad 
            RowFilter<Object, Object> filtroPrecioCaja = RowFilter.regexFilter("(?i)" + palabra, 5); // Filtrar por columna de Precio por Caja 

            // Combinar todos los filtros en uno solo para cada palabra
            var filtroPalabra = RowFilter.orFilter(Arrays.asList(filtroMarca, filtroDescripcion, filtroCategoria, filtroPrecioUnidad, filtroPrecioCaja));
            filtros.add(filtroPalabra);
        }

        // Combinar todos los filtros en uno solo
        var filtroFinal = RowFilter.andFilter(filtros);

        // Aplicar el filtro combinado
        rowSorter.setRowFilter(filtroFinal);
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
        labelCodigo = new javax.swing.JLabel();
        labelMarca = new javax.swing.JLabel();
        labelDescripcion = new javax.swing.JLabel();
        labelPreciounidad = new javax.swing.JLabel();
        labelPreciocaja = new javax.swing.JLabel();
        labelStock = new javax.swing.JLabel();
        txfCodigo = new javax.swing.JTextField();
        txfMarca = new javax.swing.JTextField();
        txfDescripcion = new javax.swing.JTextField();
        txtPreciounidad = new javax.swing.JTextField();
        txfPreciocaja = new javax.swing.JTextField();
        txfStock = new javax.swing.JTextField();
        btnAgregarP = new javax.swing.JButton();
        btnModificarP = new javax.swing.JButton();
        btnEliminarP = new javax.swing.JButton();
        txfBuscar = new javax.swing.JTextField();
        labelCategoria = new javax.swing.JLabel();
        txfCategoria = new javax.swing.JTextField();

        setBackground(new java.awt.Color(204, 204, 204));
        setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        setMaximumSize(new java.awt.Dimension(690, 450));
        setMinimumSize(new java.awt.Dimension(690, 450));
        setName("background"); // NOI18N
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Productos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei Light", 0, 12), new java.awt.Color(102, 102, 102))); // NOI18N

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

        labelCodigo.setBackground(new java.awt.Color(255, 255, 255));
        labelCodigo.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelCodigo.setForeground(new java.awt.Color(51, 51, 51));
        labelCodigo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCodigo.setText("Código");

        labelMarca.setBackground(new java.awt.Color(255, 255, 255));
        labelMarca.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelMarca.setForeground(new java.awt.Color(51, 51, 51));
        labelMarca.setText("Marca");

        labelDescripcion.setBackground(new java.awt.Color(255, 255, 255));
        labelDescripcion.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelDescripcion.setForeground(new java.awt.Color(51, 51, 51));
        labelDescripcion.setText("Descripción");

        labelPreciounidad.setBackground(new java.awt.Color(255, 255, 255));
        labelPreciounidad.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelPreciounidad.setForeground(new java.awt.Color(51, 51, 51));
        labelPreciounidad.setText("Precio x unidad");

        labelPreciocaja.setBackground(new java.awt.Color(255, 255, 255));
        labelPreciocaja.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelPreciocaja.setForeground(new java.awt.Color(51, 51, 51));
        labelPreciocaja.setText("Precio x caja");

        labelStock.setBackground(new java.awt.Color(255, 255, 255));
        labelStock.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelStock.setForeground(new java.awt.Color(51, 51, 51));
        labelStock.setText("Stock");

        txfCodigo.setBackground(new java.awt.Color(255, 255, 255));
        txfCodigo.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfCodigo.setForeground(new java.awt.Color(51, 51, 51));
        txfCodigo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfCodigo.setName(""); // NOI18N
        txfCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfCodigoActionPerformed(evt);
            }
        });

        txfMarca.setBackground(new java.awt.Color(255, 255, 255));
        txfMarca.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfMarca.setForeground(new java.awt.Color(51, 51, 51));
        txfMarca.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfMarca.setName(""); // NOI18N
        txfMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfMarcaActionPerformed(evt);
            }
        });

        txfDescripcion.setBackground(new java.awt.Color(255, 255, 255));
        txfDescripcion.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfDescripcion.setForeground(new java.awt.Color(51, 51, 51));
        txfDescripcion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfDescripcion.setName(""); // NOI18N
        txfDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfDescripcionActionPerformed(evt);
            }
        });

        txtPreciounidad.setBackground(new java.awt.Color(255, 255, 255));
        txtPreciounidad.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txtPreciounidad.setForeground(new java.awt.Color(51, 51, 51));
        txtPreciounidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPreciounidad.setName(""); // NOI18N
        txtPreciounidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPreciounidadActionPerformed(evt);
            }
        });

        txfPreciocaja.setBackground(new java.awt.Color(255, 255, 255));
        txfPreciocaja.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfPreciocaja.setForeground(new java.awt.Color(51, 51, 51));
        txfPreciocaja.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfPreciocaja.setName(""); // NOI18N
        txfPreciocaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfPreciocajaActionPerformed(evt);
            }
        });

        txfStock.setBackground(new java.awt.Color(255, 255, 255));
        txfStock.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfStock.setForeground(new java.awt.Color(51, 51, 51));
        txfStock.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfStock.setName(""); // NOI18N
        txfStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfStockActionPerformed(evt);
            }
        });

        btnAgregarP.setBackground(new java.awt.Color(204, 255, 204));
        btnAgregarP.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnAgregarP.setForeground(new java.awt.Color(0, 0, 0));
        btnAgregarP.setText("Agregar");
        btnAgregarP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPActionPerformed(evt);
            }
        });

        btnModificarP.setBackground(new java.awt.Color(255, 204, 153));
        btnModificarP.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnModificarP.setForeground(new java.awt.Color(0, 0, 0));
        btnModificarP.setText("Modificar");
        btnModificarP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarP.setMaximumSize(new java.awt.Dimension(47, 20));
        btnModificarP.setMinimumSize(new java.awt.Dimension(47, 20));
        btnModificarP.setPreferredSize(new java.awt.Dimension(47, 20));
        btnModificarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarPActionPerformed(evt);
            }
        });

        btnEliminarP.setBackground(new java.awt.Color(255, 153, 153));
        btnEliminarP.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnEliminarP.setForeground(new java.awt.Color(0, 0, 0));
        btnEliminarP.setText("Eliminar");
        btnEliminarP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPActionPerformed(evt);
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

        labelCategoria.setBackground(new java.awt.Color(255, 255, 255));
        labelCategoria.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelCategoria.setForeground(new java.awt.Color(51, 51, 51));
        labelCategoria.setText("Categoría");
        labelCategoria.setMinimumSize(new java.awt.Dimension(53, 23));

        txfCategoria.setBackground(new java.awt.Color(255, 255, 255));
        txfCategoria.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfCategoria.setForeground(new java.awt.Color(51, 51, 51));
        txfCategoria.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfCategoria.setName(""); // NOI18N
        txfCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfCategoriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(labelMarca)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txfMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(labelCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(labelCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txfCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txfBuscar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addComponent(labelPreciounidad)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPreciounidad, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addComponent(labelPreciocaja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfPreciocaja, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addComponent(labelStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfStock, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addComponent(labelDescripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txfDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnModificarP, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnAgregarP, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEliminarP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCodigo)
                            .addComponent(txfDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelDescripcion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                    .addComponent(txfCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txfMarca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelMarca))
                                .addGap(48, 48, 48))
                            .addGroup(panelLayout.createSequentialGroup()
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPreciounidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelPreciounidad))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txfPreciocaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelPreciocaja))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txfStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelStock)
                                    .addComponent(txfBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(btnAgregarP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnModificarP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminarP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 307, Short.MAX_VALUE)))
                .addContainerGap())
        );

        add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 450));
    }// </editor-fold>//GEN-END:initComponents

    private void txfCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfCodigoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfCodigoActionPerformed

    private void txfMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfMarcaActionPerformed

    private void txfDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfDescripcionActionPerformed

    private void txtPreciounidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPreciounidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPreciounidadActionPerformed

    private void txfPreciocajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfPreciocajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfPreciocajaActionPerformed

    private void txfStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfStockActionPerformed

    private void btnAgregarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPActionPerformed
        botonProductos botonproductos = new botonProductos();
        botonproductos.insertarProducto(txfMarca, txfDescripcion, txtPreciounidad, txfPreciocaja, txfStock, txfCategoria);
        botonproductos.mostrarProductos(visor);
    }//GEN-LAST:event_btnAgregarPActionPerformed

    private void btnModificarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarPActionPerformed
        botonProductos botonproductos = new botonProductos();
        botonproductos.modificarProducto(txfCodigo, txfMarca, txfDescripcion, txtPreciounidad, txfPreciocaja, txfStock, txfCategoria);
        botonproductos.mostrarProductos(visor);
    }//GEN-LAST:event_btnModificarPActionPerformed

    private void btnEliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPActionPerformed
        botonProductos botonproductos = new botonProductos();
        botonproductos.eliminarProducto(txfCodigo);
        botonproductos.mostrarProductos(visor);
    }//GEN-LAST:event_btnEliminarPActionPerformed

    private void visorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visorMouseClicked
        botonProductos botonproductos = new botonProductos();
        botonproductos.seleccionarProducto(visor, txfCodigo, txfMarca, txfDescripcion, txtPreciounidad, txfPreciocaja, txfStock, txfCategoria);
    }//GEN-LAST:event_visorMouseClicked

    private void txfBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfBuscarActionPerformed
 
    }//GEN-LAST:event_txfBuscarActionPerformed

    private void txfCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfCategoriaActionPerformed

    private void txfBuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txfBuscarFocusGained
        borrarTextoBusqueda();
    }//GEN-LAST:event_txfBuscarFocusGained

    private void txfBuscarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txfBuscarFocusLost
        restaurarTextoBusqueda();
    }//GEN-LAST:event_txfBuscarFocusLost

    private void txfBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfBuscarKeyPressed
        String criterio = txfBuscar.getText();
        aplicarFiltro(criterio);
    }//GEN-LAST:event_txfBuscarKeyPressed

    private void txfBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfBuscarKeyReleased
        String criterio = txfBuscar.getText();
        aplicarFiltro(criterio);
    }//GEN-LAST:event_txfBuscarKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarP;
    private javax.swing.JButton btnEliminarP;
    private javax.swing.JButton btnModificarP;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCategoria;
    private javax.swing.JLabel labelCodigo;
    private javax.swing.JLabel labelDescripcion;
    private javax.swing.JLabel labelMarca;
    private javax.swing.JLabel labelPreciocaja;
    private javax.swing.JLabel labelPreciounidad;
    private javax.swing.JLabel labelStock;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField txfBuscar;
    private javax.swing.JTextField txfCategoria;
    private javax.swing.JTextField txfCodigo;
    private javax.swing.JTextField txfDescripcion;
    private javax.swing.JTextField txfMarca;
    private javax.swing.JTextField txfPreciocaja;
    private javax.swing.JTextField txfStock;
    private javax.swing.JTextField txtPreciounidad;
    public javax.swing.JTable visor;
    // End of variables declaration//GEN-END:variables
}

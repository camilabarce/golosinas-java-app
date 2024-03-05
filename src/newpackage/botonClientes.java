package newpackage;

import java.awt.Component;
import java.awt.HeadlessException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class botonClientes extends javax.swing.JPanel {

    public botonClientes() {
        initComponents();
        paramTablaClientes = visor;
        mostrarClientes(visor);
        txfCodCliente.setEnabled(false);
    }
    private JTable paramTablaClientes;

    String codigo;
    String nombre;
    String localidad;
    String direccion;
    String telefono;
    String correo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void mostrarClientes(JTable visor) {
        conexion conexion1 = new conexion("golosinas");
        Connection con = conexion1.conectar();
        String sql = "SELECT c.clienteID, c.nombre, l.localidad, c.direccion, c.telefono "
                + "FROM clientes c "
                + "INNER JOIN Localidad l ON c.localidadID = l.localidadID "
                + "ORDER BY l.localidad, c.nombre;";

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Nombre y apellido");
        modelo.addColumn("Localidad");
        modelo.addColumn("Dirección");
        modelo.addColumn("Teléfono");

        visor.setModel(modelo);

        String[] datos = new String[5];

        // Configurar la alineación hacia la derecha
//        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
//        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
//
//        for (int i = 0; i < modelo.getColumnCount(); i++) {
//            visor.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
//        }
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                modelo.addRow(datos);
            }

            // Ajustar el ancho de las columnas según el contenido
            for (int i = 0; i < visor.getColumnCount(); i++) {
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
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }

    public void seleccionarCliente(JTable paramTablaClientes, JTextField paramCodigo, JTextField paramNombre, JTextField paramTelefono, JTextField paramLocalidad, JTextField paramDireccion) {
        try {
            int fila = paramTablaClientes.getSelectedRow();
            //System.out.println("Fila seleccionada: " + fila);

            if (fila >= 0) { // Si se ha seleccionado una fila
                paramCodigo.setText((paramTablaClientes.getValueAt(fila, 0) != null) ? paramTablaClientes.getValueAt(fila, 0).toString() : "");
                paramNombre.setText((paramTablaClientes.getValueAt(fila, 1) != null) ? paramTablaClientes.getValueAt(fila, 1).toString() : "");
                paramLocalidad.setText((paramTablaClientes.getValueAt(fila, 2) != null) ? paramTablaClientes.getValueAt(fila, 2).toString() : "");
                paramDireccion.setText((paramTablaClientes.getValueAt(fila, 3) != null) ? paramTablaClientes.getValueAt(fila, 3).toString() : "");
                paramTelefono.setText((paramTablaClientes.getValueAt(fila, 4) != null) ? paramTablaClientes.getValueAt(fila, 4).toString() : "");
            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar el cliente: " + e.toString());
        }
    }

    public void insertarCliente(JTextField paramNombre, JTextField paramLocalidad, JTextField paramDireccion, JTextField paramTelefono) {
        setNombre(paramNombre.getText());
        setLocalidad(paramLocalidad.getText());
        setDireccion(paramDireccion.getText());
        setTelefono(paramTelefono.getText());

        conexion conexion1 = new conexion("golosinas");

        // Consulta para insertar o obtener el ID de la localidad
        String consultaLocalidad = "INSERT INTO Localidad (localidad) VALUES (?) ON DUPLICATE KEY UPDATE localidad = ?;";
        String consultaIDLocalidad = "SELECT localidadID FROM Localidad WHERE localidad = ?;";

        // Consulta para insertar el cliente utilizando el ID de la localidad
        String consultaCliente = "INSERT INTO clientes (nombre, localidadID, direccion, telefono) VALUES (?, ?, ?, ?);";

        try {
            // Verificar si la localidad ya existe y obtener su ID
            PreparedStatement psLocalidad = conexion1.conectar().prepareStatement(consultaIDLocalidad);
            psLocalidad.setString(1, getLocalidad());
            ResultSet rsLocalidad = psLocalidad.executeQuery();
            int localidadID;
            if (rsLocalidad.next()) {
                localidadID = rsLocalidad.getInt("localidadID");
            } else {
                // Si la localidad no existe, insertarla y obtener su ID
                PreparedStatement psInsertLocalidad = conexion1.conectar().prepareStatement(consultaLocalidad, Statement.RETURN_GENERATED_KEYS);
                psInsertLocalidad.setString(1, getLocalidad());
                psInsertLocalidad.setString(2, getLocalidad());
                psInsertLocalidad.executeUpdate();
                ResultSet rsInsertLocalidad = psInsertLocalidad.getGeneratedKeys();
                rsInsertLocalidad.next();
                localidadID = rsInsertLocalidad.getInt(1);
            }

            // Insertar el cliente utilizando el ID de la localidad
            PreparedStatement psCliente = conexion1.conectar().prepareStatement(consultaCliente);
            psCliente.setString(1, getNombre());
            psCliente.setInt(2, localidadID);
            psCliente.setString(3, getDireccion());
            psCliente.setString(4, getTelefono());
            psCliente.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se agregó correctamente el cliente.");

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se agregó el cliente :(, error " + e.toString());
        }
    }

    public void modificarCliente(JTextField paramCodigo, JTextField paramNombre, JTextField paramLocalidad, JTextField paramDireccion, JTextField paramTelefono) {
        setCodigo(paramCodigo.getText());
        setNombre(paramNombre.getText());
        setLocalidad(paramLocalidad.getText());
        setDireccion(paramDireccion.getText());
        setTelefono(paramTelefono.getText());

        conexion conexion1 = new conexion("golosinas");

        // Consulta para actualizar el cliente
        String consulta = "UPDATE clientes "
                + "SET nombre = ?, "
                + "localidadID = (SELECT localidadID FROM Localidad WHERE localidad = ?), "
                + "direccion = ?, "
                + "telefono = ? "
                + "WHERE clienteID = ?;";

        try {
            PreparedStatement ps = conexion1.conectar().prepareStatement(consulta);

            ps.setString(1, getNombre());
            ps.setString(2, getLocalidad());
            ps.setString(3, getDireccion());
            ps.setString(4, getTelefono());
            ps.setString(5, getCodigo());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "¡Modificación exitosa!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se modificó. Error: " + e.toString());
        }
    }

    public void eliminarCliente(JTextField paramCodigo) {
        setCodigo(paramCodigo.getText());
        conexion conexion1 = new conexion("golosinas");

        String consulta = "DELETE FROM clientes WHERE clienteID = ?;";

        try {
            PreparedStatement ps = conexion1.conectar().prepareStatement(consulta);
            ps.setString(1, getCodigo());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Se eliminó correctamente");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }

    public void buscar(String criterio) {
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(paramTablaClientes.getModel());
        paramTablaClientes.setRowSorter(rowSorter);

        // Divide el criterio en palabras individuales
        String[] palabras = criterio.split("\\s+");

        // Crea una lista de filtros para cada palabra en nombre, localidad, dirección y teléfono
        var filtros = new ArrayList<RowFilter<Object, Object>>();
        for (String palabra : palabras) {
            RowFilter<Object, Object> filtroNombre = RowFilter.regexFilter("(?i)" + palabra, 1); // Filtrar por columna de Nombre (1)
            RowFilter<Object, Object> filtroLocalidad = RowFilter.regexFilter("(?i)" + palabra, 2); // Filtrar por columna de Localidad (2)
            RowFilter<Object, Object> filtroDireccion = RowFilter.regexFilter("(?i)" + palabra, 3); // Filtrar por columna de Dirección (3)
            RowFilter<Object, Object> filtroTelefono = RowFilter.regexFilter("(?i)" + palabra, 4); // Filtrar por columna de Teléfono (4)

            // Combinar todos los filtros en uno solo para cada palabra
            var filtroPalabra = RowFilter.orFilter(Arrays.asList(filtroNombre, filtroLocalidad, filtroDireccion, filtroTelefono));
            filtros.add(filtroPalabra);
        }

        // Combinar todos los filtros en uno solo
        var filtroFinal = RowFilter.andFilter(filtros);

        // Aplicar el filtro combinado
        rowSorter.setRowFilter(filtroFinal);
    }

    public void borrarTextoBusqueda() {
        txfBuscar.setText(""); // Borra el texto del campo de búsqueda
    }

    public void restaurarTextoBusqueda() {
        if (txfBuscar.getText().isEmpty()) {
            txfBuscar.setText("Buscar"); // Restaura el texto predeterminado
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        visor = new javax.swing.JTable();
        labelCodigo = new javax.swing.JLabel();
        txfCodCliente = new javax.swing.JTextField();
        labelNombre = new javax.swing.JLabel();
        txfNombre = new javax.swing.JTextField();
        labelLocalidad = new javax.swing.JLabel();
        txfLocalidad = new javax.swing.JTextField();
        labelDireccion = new javax.swing.JLabel();
        txfDireccion = new javax.swing.JTextField();
        labelTelefono = new javax.swing.JLabel();
        txfTelefono = new javax.swing.JTextField();
        btnAgregarCliente = new javax.swing.JButton();
        btnModificarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        txfBuscar = new javax.swing.JTextField();

        setBackground(new java.awt.Color(204, 204, 204));
        setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        setMaximumSize(new java.awt.Dimension(690, 450));
        setMinimumSize(new java.awt.Dimension(690, 450));
        setName("background"); // NOI18N
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei Light", 0, 12), new java.awt.Color(102, 102, 102))); // NOI18N

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
        labelCodigo.setText("Código:");

        txfCodCliente.setBackground(new java.awt.Color(255, 255, 255));
        txfCodCliente.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfCodCliente.setForeground(new java.awt.Color(51, 51, 51));
        txfCodCliente.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfCodCliente.setName(""); // NOI18N
        txfCodCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfCodClienteActionPerformed(evt);
            }
        });

        labelNombre.setBackground(new java.awt.Color(255, 255, 255));
        labelNombre.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelNombre.setForeground(new java.awt.Color(51, 51, 51));
        labelNombre.setText("Nombre y apellido:");

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

        labelLocalidad.setBackground(new java.awt.Color(255, 255, 255));
        labelLocalidad.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelLocalidad.setForeground(new java.awt.Color(51, 51, 51));
        labelLocalidad.setText("Localidad:");

        txfLocalidad.setBackground(new java.awt.Color(255, 255, 255));
        txfLocalidad.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfLocalidad.setForeground(new java.awt.Color(51, 51, 51));
        txfLocalidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfLocalidad.setName(""); // NOI18N
        txfLocalidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfLocalidadActionPerformed(evt);
            }
        });

        labelDireccion.setBackground(new java.awt.Color(255, 255, 255));
        labelDireccion.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelDireccion.setForeground(new java.awt.Color(51, 51, 51));
        labelDireccion.setText("Dirección:");

        txfDireccion.setBackground(new java.awt.Color(255, 255, 255));
        txfDireccion.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        txfDireccion.setForeground(new java.awt.Color(51, 51, 51));
        txfDireccion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfDireccion.setName(""); // NOI18N
        txfDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfDireccionActionPerformed(evt);
            }
        });

        labelTelefono.setBackground(new java.awt.Color(255, 255, 255));
        labelTelefono.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        labelTelefono.setForeground(new java.awt.Color(51, 51, 51));
        labelTelefono.setText("Teléfono:");

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

        btnAgregarCliente.setBackground(new java.awt.Color(204, 255, 204));
        btnAgregarCliente.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnAgregarCliente.setForeground(new java.awt.Color(0, 0, 0));
        btnAgregarCliente.setText("Agregar");
        btnAgregarCliente.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarClienteActionPerformed(evt);
            }
        });

        btnModificarCliente.setBackground(new java.awt.Color(255, 204, 153));
        btnModificarCliente.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnModificarCliente.setForeground(new java.awt.Color(0, 0, 0));
        btnModificarCliente.setText("Modificar");
        btnModificarCliente.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnModificarCliente.setMaximumSize(new java.awt.Dimension(47, 20));
        btnModificarCliente.setMinimumSize(new java.awt.Dimension(47, 20));
        btnModificarCliente.setPreferredSize(new java.awt.Dimension(47, 20));
        btnModificarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarClienteActionPerformed(evt);
            }
        });

        btnEliminarCliente.setBackground(new java.awt.Color(255, 153, 153));
        btnEliminarCliente.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 12)); // NOI18N
        btnEliminarCliente.setForeground(new java.awt.Color(0, 0, 0));
        btnEliminarCliente.setText("Eliminar");
        btnEliminarCliente.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
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

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelNombre, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelCodigo, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(txfCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(labelDireccion)
                                .addGap(18, 18, 18)
                                .addComponent(txfDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txfTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelLayout.createSequentialGroup()
                                        .addComponent(labelLocalidad)
                                        .addGap(18, 18, 18)
                                        .addComponent(txfLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txfBuscar))))
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addComponent(labelTelefono)
                        .addGap(417, 417, 417)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addContainerGap(7, Short.MAX_VALUE)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelCodigo)
                            .addComponent(txfCodCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelDireccion)
                            .addComponent(txfDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelNombre)
                                .addComponent(txfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txfLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelLocalidad)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addComponent(btnEliminarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                            .addGroup(panelLayout.createSequentialGroup()
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelTelefono)
                                    .addComponent(txfTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txfBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(btnAgregarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnModificarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 450));
    }// </editor-fold>//GEN-END:initComponents

    private void txfCodClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfCodClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfCodClienteActionPerformed

    private void txfLocalidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfLocalidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfLocalidadActionPerformed

    private void txfDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfDireccionActionPerformed

    private void txfTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfTelefonoActionPerformed

    private void txfNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfNombreActionPerformed

    private void btnAgregarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarClienteActionPerformed
        botonClientes botonclientes = new botonClientes();
        botonclientes.insertarCliente(txfNombre, txfLocalidad, txfDireccion, txfTelefono);
        botonclientes.mostrarClientes(visor);
    }//GEN-LAST:event_btnAgregarClienteActionPerformed

    private void btnModificarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarClienteActionPerformed
        botonClientes botonclientes = new botonClientes();
        botonclientes.modificarCliente(txfCodCliente, txfNombre, txfLocalidad, txfDireccion, txfTelefono);
        botonclientes.mostrarClientes(visor);
    }//GEN-LAST:event_btnModificarClienteActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        botonClientes botonclientes = new botonClientes();
        botonclientes.eliminarCliente(txfCodCliente);
        botonclientes.mostrarClientes(visor);
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void visorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visorMouseClicked
        botonClientes botonclientes = new botonClientes();
        botonclientes.seleccionarCliente(visor, txfCodCliente, txfNombre, txfTelefono, txfLocalidad, txfDireccion);
    }//GEN-LAST:event_visorMouseClicked

    private void txfBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfBuscarActionPerformed

    }//GEN-LAST:event_txfBuscarActionPerformed

    private void txfBuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txfBuscarFocusGained
        borrarTextoBusqueda();
    }//GEN-LAST:event_txfBuscarFocusGained

    private void txfBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfBuscarKeyPressed
        String criterio = txfBuscar.getText();
        buscar(criterio);
    }//GEN-LAST:event_txfBuscarKeyPressed

    private void txfBuscarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txfBuscarFocusLost
        restaurarTextoBusqueda();
    }//GEN-LAST:event_txfBuscarFocusLost

    private void txfBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txfBuscarKeyReleased
        String criterio = txfBuscar.getText();
        buscar(criterio);
    }//GEN-LAST:event_txfBuscarKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarCliente;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnModificarCliente;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCodigo;
    private javax.swing.JLabel labelDireccion;
    private javax.swing.JLabel labelLocalidad;
    private javax.swing.JLabel labelNombre;
    private javax.swing.JLabel labelTelefono;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField txfBuscar;
    private javax.swing.JTextField txfCodCliente;
    private javax.swing.JTextField txfDireccion;
    private javax.swing.JTextField txfLocalidad;
    private javax.swing.JTextField txfNombre;
    private javax.swing.JTextField txfTelefono;
    public javax.swing.JTable visor;
    // End of variables declaration//GEN-END:variables
}

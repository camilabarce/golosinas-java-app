package newpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class conexion {
    String url = "jdbc:mysql://localhost:3306/";
    String bd = "golosinas";
    String usuario = "root";
    String clave = "";
    Connection con;
                
    public conexion(String bd) {       
        this.bd = bd;
    }
    
    public Connection conectar(){
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url+bd, usuario, clave); //cadena de conexión
            //System.out.println("Se conectó a la base");
        } catch (ClassNotFoundException |SQLException ex) {
            System.out.println("No se conectó a la base");
            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);           
        }
        return con;
    }     
    
    public void desconectar() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
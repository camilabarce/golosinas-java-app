package newpackage;

public class principal {
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new inicio().setVisible(true); 
            }
        });        
    }
}

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Lance le jeu de manière sécurisée
        SwingUtilities.invokeLater(() -> new Jeu());
    }
}
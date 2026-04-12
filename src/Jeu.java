import javax.swing.*;
import java.awt.*;

public class Jeu extends JFrame {
    private HistoriquePanel historique;
    private Plateau plateau;

    public Jeu() {
        setTitle("Jeu de Dames Officiel");
        setSize(1100, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        historique = new HistoriquePanel();
        add(historique, BorderLayout.EAST);

        plateau = new Plateau(historique);
        JPanel conteneurPlateau = creerStructurePlateau(plateau);
        add(conteneurPlateau, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel creerStructurePlateau(Plateau p) {
        JPanel principal = new JPanel(new BorderLayout());
        
        JPanel colLabels = new JPanel(new GridLayout(1, 10));
        for (char c = 'A'; c <= 'J'; c++) {
            JLabel l = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            l.setFont(new Font("Arial", Font.BOLD, 16));
            l.setPreferredSize(new Dimension(0, 30));
            colLabels.add(l);
        }

        JPanel rowLabels = new JPanel(new GridLayout(10, 1));
        for (int i = 10; i >= 1; i--) {
            JLabel l = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            l.setPreferredSize(new Dimension(40, 0));
            l.setFont(new Font("Arial", Font.BOLD, 16));
            rowLabels.add(l);
        }

        principal.add(rowLabels, BorderLayout.WEST);
        principal.add(p, BorderLayout.CENTER);
        
        JPanel pied = new JPanel(new BorderLayout());
        JPanel vide = new JPanel(); vide.setPreferredSize(new Dimension(40, 30));
        pied.add(vide, BorderLayout.WEST);
        pied.add(colLabels, BorderLayout.CENTER);
        principal.add(pied, BorderLayout.SOUTH);

        return principal;
    }
}

class HistoriquePanel extends JPanel {
    private DefaultListModel<String> model;
    private JList<String> liste;

    public HistoriquePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));
        setBorder(BorderFactory.createTitledBorder("Historique des coups"));
        model = new DefaultListModel<>();
        liste = new JList<>(model);
        liste.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(liste), BorderLayout.CENTER);
    }

    public void ajouterCoup(String coup) {
        model.addElement(coup);
        liste.ensureIndexIsVisible(model.getSize() - 1);
    }

    public void effacer() { model.clear(); }
}
import java.awt.*;
import javax.swing.*;

public class HistoriquePanel extends JPanel {
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
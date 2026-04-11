import java.awt.*;
import javax.swing.*;

public class Case extends JPanel {

    private final int ligne;
    private final int col;
    private Piece pion;
    private boolean selectionnee;

    public Case(int ligne, int col, Color fond) {
        this.ligne = ligne;
        this.col = col;
        this.pion = null;
        this.selectionnee = false;

        setBackground(fond);
        setPreferredSize(new Dimension(70, 70));
    }

    // --- Coordonnées ---
    public int getLigne() { return ligne; }
    public int getCol() { return col; }

    // --- Gestion du pion ---
    public boolean hasPion() { return pion != null; }
    public Piece getPion() { return pion; }

    public void setPion(Piece p) {
        this.pion = p;
        repaint();
    }

    public void vider() {
        this.pion = null;
        repaint();
    }

    // --- Sélection visuelle ---
    public void setSelectionnee(boolean s) {
        this.selectionnee = s;
        repaint();
    }

    // --- Dessin graphique ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Sélection (bord jaune)
        if (selectionnee) {
            g.setColor(Color.YELLOW);
            g.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
            g.drawRect(4, 4, getWidth() - 8, getHeight() - 8);
        }

        // Dessin du pion
        if (pion != null) {
            g.setColor(pion.getCouleur());
            g.fillOval(10, 10, getWidth() - 20, getHeight() - 20);

            // Dessin de la dame
            if (pion.isDame()) {
                g.setColor(Color.ORANGE);
                g.drawOval(15, 15, getWidth() - 30, getHeight() - 30);
                g.drawOval(20, 20, getWidth() - 40, getHeight() - 40);
            }
        }
    }
}
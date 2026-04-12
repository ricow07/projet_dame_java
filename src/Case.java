import java.awt.*;
import javax.swing.*;

public class Case extends JPanel {
    private int ligne, col;
    private Piece pion = null; 
    private boolean selectionnee = false;

    public Case(int l, int c, Color f) { 
        this.ligne = l; 
        this.col = c; 
        setBackground(f); 
    }

    public void setPion(Piece p) { this.pion = p; repaint(); }
    public void vider() { this.pion = null; repaint(); }
    public boolean hasPion() { return pion != null; }
    public Piece getPion() { return pion; }
    public int getLigne() { return ligne; }
    public int getCol() { return col; }
    public void setSelectionnee(boolean s) { this.selectionnee = s; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (pion != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(pion.getCouleur());
            g2.fillOval(12, 12, getWidth()-24, getHeight()-24);
            if (pion.isDame()) {
                g2.setColor(pion.getCouleur() == Color.WHITE ? Color.BLACK : Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(22, 22, getWidth()-44, getHeight()-44);
            }
            g2.setColor(selectionnee ? Color.YELLOW : Color.GRAY);
            g2.setStroke(new BasicStroke(selectionnee ? 5 : 2));
            g2.drawOval(12, 12, getWidth()-24, getHeight()-24);
        }
    }
}
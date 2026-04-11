import java.awt.Color;

public class Piece {
    private Color couleur;
    private boolean dame;

    public Piece(Color couleur) {
        this.couleur = couleur;
        this.dame = false;
    }

    public Color getCouleur() { return couleur; }
    public boolean isDame() { return dame; }
    public void setDame(boolean dame) { this.dame = dame; }
}
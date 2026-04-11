import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Plateau extends JPanel {
    private Case[][] cases = new Case[10][10];
    private Case caseSelectionnee = null;
    private Color tourActuel = Color.WHITE;
    private HistoriquePanel historique;
    private boolean enTrainDeRafle = false;

    public Plateau(HistoriquePanel hist) {
        this.historique = hist;
        setLayout(new GridLayout(10, 10));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        creerCases();
        initialiserPions();
    }

    private void creerCases() {
        for (int l = 0; l < 10; l++) {
            for (int c = 0; c < 10; c++) {
                Color fond = (l + c) % 2 == 0 ? Color.WHITE : Color.BLACK;
                cases[l][c] = new Case(l, c, fond);
                cases[l][c].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        gererClic((Case) e.getSource());
                    }
                });
                add(cases[l][c]);
            }
        }
    }

    public void initialiserPions() {
        for (int l = 0; l < 10; l++) {
            for (int c = 0; c < 10; c++) {
                cases[l][c].vider();
                if ((l + c) % 2 != 0) {
                    if (l < 4) cases[l][c].setPion(new Piece(Color.BLACK));
                    if (l > 5) cases[l][c].setPion(new Piece(Color.WHITE));
                }
            }
        }
        tourActuel = Color.WHITE;
        enTrainDeRafle = false;
        historique.effacer();
        historique.ajouterCoup("--- Nouvelle Partie ---");
    }

    private void gererClic(Case caseCliquee) {
        if (caseSelectionnee == null) {
            // Sélection initiale
            if (caseCliquee.hasPion() && caseCliquee.getPion().getCouleur().equals(tourActuel)) {
                caseSelectionnee = caseCliquee;
                caseSelectionnee.setSelectionnee(true);
            }
        } else {
            // Tentative de mouvement
            if (estDeplacementValide(caseSelectionnee, caseCliquee)) {
                effectuerMouvement(caseSelectionnee, caseCliquee);
                verifierFinDePartie();
            } else if (!enTrainDeRafle) {
                // Si on s'est trompé de pion et qu'on n'est pas bloqué dans une rafle, on change la sélection
                caseSelectionnee.setSelectionnee(false);
                if (caseCliquee.hasPion() && caseCliquee.getPion().getCouleur().equals(tourActuel)) {
                    caseSelectionnee = caseCliquee;
                    caseSelectionnee.setSelectionnee(true);
                } else {
                    caseSelectionnee = null;
                }
            }
        }
    }

    private void effectuerMouvement(Case dep, Case arr) {
        String coordDep = (char)('A' + dep.getCol()) + "" + (10 - dep.getLigne());
        String coordArr = (char)('A' + arr.getCol()) + "" + (10 - arr.getLigne());
        String joueur = (tourActuel == Color.WHITE) ? "BLANC" : "NOIR ";

        Case sautee = getCaseSautee(dep, arr);
        boolean etaitUnePrise = (sautee != null);

        if (etaitUnePrise) {
            sautee.vider();
            historique.ajouterCoup(joueur + " : " + coordDep + " x " + coordArr);
        } else {
            historique.ajouterCoup(joueur + " : " + coordDep + " - " + coordArr);
        }

        // On déplace l'objet Piece
        Piece pieceDeplacee = dep.getPion();
        arr.setPion(pieceDeplacee);
        dep.vider();

        // Promotion en Dame
        if (!pieceDeplacee.isDame()) {
            if (pieceDeplacee.getCouleur() == Color.WHITE && arr.getLigne() == 0) pieceDeplacee.setDame(true);
            if (pieceDeplacee.getCouleur() == Color.BLACK && arr.getLigne() == 9) pieceDeplacee.setDame(true);
        }

        // --- RÈGLE 2 : LA RAFLE ---
        // Si on vient de manger ET qu'on peut encore manger depuis la nouvelle case
        if (etaitUnePrise && peutSauter(arr)) {
            enTrainDeRafle = true;
            caseSelectionnee.setSelectionnee(false);
            caseSelectionnee = arr;
            caseSelectionnee.setSelectionnee(true);
            // Le tourActuel NE CHANGE PAS. C'est toujours au même joueur de cliquer.
        } else {
            enTrainDeRafle = false;
            caseSelectionnee.setSelectionnee(false);
            caseSelectionnee = null;
            tourActuel = (tourActuel == Color.WHITE) ? Color.BLACK : Color.WHITE;
        }
    }

    private Case getCaseSautee(Case dep, Case arr) {
        int dL = arr.getLigne() - dep.getLigne();
        int dC = arr.getCol() - dep.getCol();
        
        // Saut pour un Pion normal
        if (Math.abs(dL) == 2 && Math.abs(dC) == 2 && !dep.getPion().isDame()) {
            return cases[dep.getLigne() + dL/2][dep.getCol() + dC/2];
        }
        
        // Saut pour une Dame
        if (dep.getPion().isDame() && Math.abs(dL) == Math.abs(dC)) {
            int pasL = dL > 0 ? 1 : -1;
            int pasC = dC > 0 ? 1 : -1;
            Case cible = null;
            for (int i = 1; i < Math.abs(dL); i++) {
                Case inter = cases[dep.getLigne() + i*pasL][dep.getCol() + i*pasC];
                if (inter.hasPion()) {
                    if (inter.getPion().getCouleur().equals(dep.getPion().getCouleur())) return null; // Allié sur le chemin
                    if (cible != null) return null; // Deux ennemis collés sur le chemin
                    cible = inter;
                }
            }
            return cible;
        }
        return null;
    }

    // --- LE SCANNER POUR LA PRISE OBLIGATOIRE ---
    
    // 1. Vérifie si UNE case spécifique peut sauter (manger)
    private boolean peutSauter(Case dep) {
        int[][] directions = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
        for (int[] dir : directions) {
            int arrL = dep.getLigne() + dir[0];
            int arrC = dep.getCol() + dir[1];
            if (arrL >= 0 && arrL < 10 && arrC >= 0 && arrC < 10) {
                Case arr = cases[arrL][arrC];
                if (!arr.hasPion()) {
                    Case caseSautee = getCaseSautee(dep, arr);
                    if (caseSautee != null && caseSautee.hasPion() && !caseSautee.getPion().getCouleur().equals(dep.getPion().getCouleur())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 2. Vérifie si LE JOUEUR a au moins un pion qui peut manger sur tout le plateau
    private boolean unePriseEstPossible(Color couleurJoueur) {
        for (int l = 0; l < 10; l++) {
            for (int c = 0; c < 10; c++) {
                if (cases[l][c].hasPion() && cases[l][c].getPion().getCouleur().equals(couleurJoueur)) {
                    if (peutSauter(cases[l][c])) return true;
                }
            }
        }
        return false;
    }

    // --- VÉRIFICATION GLOBALE DU DÉPLACEMENT ---
    private boolean estDeplacementValide(Case dep, Case arr) {
        if (arr.hasPion() || arr.getBackground() == Color.WHITE) return false;
        
        int dL = arr.getLigne() - dep.getLigne();
        int dC = Math.abs(arr.getCol() - dep.getCol());
        
        Case sautee = getCaseSautee(dep, arr);
        boolean estUnSaut = (sautee != null && sautee.hasPion() && !sautee.getPion().getCouleur().equals(dep.getPion().getCouleur()));

        // RÈGLE 1 : LA PRISE OBLIGATOIRE
        if (unePriseEstPossible(dep.getPion().getCouleur())) {
            return estUnSaut; // Si une prise est possible, ON FORCE LE SAUT, tout mouvement normal est refusé !
        }

        // Si on est bloqué dans une rafle, seul le saut est autorisé
        if (enTrainDeRafle) return false; 

        // Si aucune prise n'est possible, on autorise les mouvements normaux
        if (!dep.getPion().isDame() && Math.abs(dL) == 1 && dC == 1) {
            if (dep.getPion().getCouleur() == Color.WHITE && dL == -1) return true;
            if (dep.getPion().getCouleur() == Color.BLACK && dL == 1) return true;
        }
        
        if (dep.getPion().isDame() && Math.abs(dL) == dC) {
            int pL = dL > 0 ? 1 : -1, pC = (arr.getCol() - dep.getCol()) > 0 ? 1 : -1;
            for (int i = 1; i < Math.abs(dL); i++) {
                if (cases[dep.getLigne() + i*pL][dep.getCol() + i*pC].hasPion()) return false; // Bloqué par un pion
            }
            return true;
        }
        return false;
    }

    private void verifierFinDePartie() {
        int blancs = 0, noirs = 0;
        for (int l = 0; l < 10; l++) {
            for (int c = 0; c < 10; c++) {
                if (cases[l][c].hasPion()) {
                    if (cases[l][c].getPion().getCouleur() == Color.WHITE) blancs++;
                    else noirs++;
                }
            }
        }

        if (blancs == 0 || noirs == 0) {
            String vainqueur = (blancs > 0) ? "LES BLANCS" : "LES NOIRS";
            int choix = JOptionPane.showConfirmDialog(this, 
                "Félicitations ! " + vainqueur + " ont gagné la partie !\nVoulez-vous rejouer ?", 
                "Fin de partie", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (choix == JOptionPane.YES_OPTION) initialiserPions();
            else System.exit(0);
        }
    }
}

// Classe visuelle pour une case individuelle
class Case extends JPanel {
    private int ligne, col;
    private Piece pion = null; 
    private boolean selectionnee = false;

    public Case(int l, int c, Color f) { this.ligne = l; this.col = c; setBackground(f); }
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
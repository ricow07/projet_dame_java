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
            if (caseCliquee.hasPion() && caseCliquee.getPion().getCouleur().equals(tourActuel)) {
                caseSelectionnee = caseCliquee;
                caseSelectionnee.setSelectionnee(true);
            }
        } else {
            if (estDeplacementValide(caseSelectionnee, caseCliquee)) {
                effectuerMouvement(caseSelectionnee, caseCliquee);
                verifierFinDePartie();
            } else if (!enTrainDeRafle) {
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

        Piece pieceDeplacee = dep.getPion();
        arr.setPion(pieceDeplacee);
        dep.vider();

        if (!pieceDeplacee.isDame()) {
            if (pieceDeplacee.getCouleur() == Color.WHITE && arr.getLigne() == 0) pieceDeplacee.setDame(true);
            if (pieceDeplacee.getCouleur() == Color.BLACK && arr.getLigne() == 9) pieceDeplacee.setDame(true);
        }

        if (etaitUnePrise && peutSauter(arr)) {
            enTrainDeRafle = true;
            caseSelectionnee.setSelectionnee(false);
            caseSelectionnee = arr;
            caseSelectionnee.setSelectionnee(true);
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
        
        if (Math.abs(dL) == 2 && Math.abs(dC) == 2 && !dep.getPion().isDame()) {
            return cases[dep.getLigne() + dL/2][dep.getCol() + dC/2];
        }
        
        if (dep.getPion().isDame() && Math.abs(dL) == Math.abs(dC)) {
            int pasL = dL > 0 ? 1 : -1;
            int pasC = dC > 0 ? 1 : -1;
            Case cible = null;
            for (int i = 1; i < Math.abs(dL); i++) {
                Case inter = cases[dep.getLigne() + i*pasL][dep.getCol() + i*pasC];
                if (inter.hasPion()) {
                    if (inter.getPion().getCouleur().equals(dep.getPion().getCouleur())) return null; 
                    if (cible != null) return null; 
                    cible = inter;
                }
            }
            return cible;
        }
        return null;
    }
    
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

    private boolean estDeplacementValide(Case dep, Case arr) {
        if (arr.hasPion() || arr.getBackground() == Color.WHITE) return false;
        
        int dL = arr.getLigne() - dep.getLigne();
        int dC = Math.abs(arr.getCol() - dep.getCol());
        
        Case sautee = getCaseSautee(dep, arr);
        boolean estUnSaut = (sautee != null && sautee.hasPion() && !sautee.getPion().getCouleur().equals(dep.getPion().getCouleur()));

        if (unePriseEstPossible(dep.getPion().getCouleur())) {
            return estUnSaut; 
        }

        if (enTrainDeRafle) return false; 

        if (!dep.getPion().isDame() && Math.abs(dL) == 1 && dC == 1) {
            if (dep.getPion().getCouleur() == Color.WHITE && dL == -1) return true;
            if (dep.getPion().getCouleur() == Color.BLACK && dL == 1) return true;
        }
        
        if (dep.getPion().isDame() && Math.abs(dL) == dC) {
            int pL = dL > 0 ? 1 : -1, pC = (arr.getCol() - dep.getCol()) > 0 ? 1 : -1;
            for (int i = 1; i < Math.abs(dL); i++) {
                if (cases[dep.getLigne() + i*pL][dep.getCol() + i*pC].hasPion()) return false; 
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
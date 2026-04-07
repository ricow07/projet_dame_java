import java.util.Random;

// Gère la grille, les cases, le robot et l'entrepôt
public class Plateau {

    private int largeur, hauteur;
    private Case[][] cases;      // Grille 2D
    private Robot robot;
    private Entrepot entrepot;

    public Plateau(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        cases = new Case[hauteur][largeur];
        initialiser(); // Génération aléatoire
    }

    // Remplit la grille avec mines, barrières, cases vides, entrepôt
    public void initialiser() {
        Random r = new Random();

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {

                int t = r.nextInt(10);

                // 60% vide
                if (t < 6) cases[y][x] = new CaseVide();

                    // 20% mines
                else if (t < 8) cases[y][x] = new Mine(3);

                    // 20% barrières
                else cases[y][x] = new Barriere();
            }
        }

        // Entrepôt placé en (0,0)
        entrepot = new Entrepot();
        cases[0][0] = entrepot;

        // Robot placé sur l'entrepôt
        robot = new Robot("RX-01", 0, 0);
    }

    // Vérifie que la position est dans la grille
    public boolean estValide(int x, int y) {
        return x >= 0 && x < largeur && y >= 0 && y < hauteur;
    }

    // Vérifie que la case est valide ET franchissable
    public boolean estFranchissable(int x, int y) {
        return estValide(x, y) && cases[y][x].estFranchissable();
    }

    public Case obtenirCase(int x, int y) {
        return cases[y][x];
    }

    public Robot getRobot() { return robot; }
    public Entrepot getEntrepot() { return entrepot; }

    // Affiche la grille complète
    public void afficherPlateau() {
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {

                // Si le robot est ici → afficher 'R'
                if (robot.getX() == x && robot.getY() == y)
                    System.out.print('R');
                else
                    System.out.print(cases[y][x].afficher());
            }
            System.out.println();
        }
    }
}



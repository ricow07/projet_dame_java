import java.util.Scanner;

// Gère la boucle de jeu, les commandes et la victoire
public class Jeu {

    private Plateau plateau;
    private boolean enCours = true;

    public Jeu() {
        plateau = new Plateau(8, 8); // Plateau 8x8
    }

    public void demarrer() {
        Scanner sc = new Scanner(System.in);

        while (enCours) {

            // Affichage du plateau + état du robot
            plateau.afficherPlateau();
            plateau.getRobot().afficherEtat();

            System.out.print("Commande (zqsd, repos, etat, quitter) : ");
            String cmd = sc.nextLine();

            traiterCommande(cmd);
        }
    }

    public void traiterCommande(String cmd) {

        Robot r = plateau.getRobot();
        int nx = r.getX();
        int ny = r.getY();

        // Déplacements
        switch (cmd) {
            case "z": ny--; break; // haut
            case "s": ny++; break; // bas
            case "q": nx--; break; // gauche
            case "d": nx++; break; // droite

            case "repos":
                r.reposer();
                return;

            case "etat":
                r.afficherEtat();
                return;

            case "quitter":
                enCours = false;
                return;

            default:
                System.out.println("Commande inconnue.");
                return;
        }

        // Vérifie si la case est accessible
        if (plateau.estFranchissable(nx, ny)) {
            r.deplacer(nx, ny); // Mise à jour position
            plateau.obtenirCase(nx, ny).interagir(r); // Interaction case
        } else {
            System.out.println("Déplacement impossible.");
        }

        // Condition de victoire
        if (plateau.getEntrepot().getTotal() >= 10) {
            System.out.println("Victoire ! 10 minerais déposés.");
            enCours = false;
        }
    }
}
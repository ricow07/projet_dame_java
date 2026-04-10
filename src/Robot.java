// Entité active : se déplace, collecte, dépose, consomme de l'énergie
public class Robot {

    private String nom;
    private int x, y;        // Position actuelle
    private int energie = 100;
    private int capacite = 5; // Capacité max de minerai
    private int charge = 0;   // Minerai transporté

    public Robot(String nom, int x, int y) {
        this.nom = nom;
        this.x = x;
        this.y = y;
    }

    // Vérifie si le robot peut encore collecter
    public boolean peutCollecter() {
        return charge < capacite;
    }

    // Ajoute 1 minerai
    public void collecter() {
        if (charge < capacite) charge++;
    }

    // Dépose tout le minerai et renvoie la quantité déposée
    public int deposer() {
        int tmp = charge;
        charge = 0;
        return tmp;
    }

    // Récupère de l'énergie
    public void reposer() {
        energie = Math.min(100, energie + 20);
        System.out.println("Le robot se repose.");
    }

    // Déplace le robot et consomme de l'énergie
    public void deplacer(int nx, int ny) {
        x = nx;
        y = ny;
        energie -= 5;
    }

    // Affiche l'état complet du robot
    public void afficherEtat() {
        System.out.println("Robot " + nom + " | Pos(" + x + "," + y + ") | Energie=" + energie + " | Charge=" + charge);
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
}
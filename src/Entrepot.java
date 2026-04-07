// Case spéciale : permet de déposer le minerai
public class Entrepot extends Case {

    private int total = 0; // Stock total accumulé

    @Override
    public char afficher() {
        return 'E'; // Symbole de l'entrepôt
    }

    @Override
    public void interagir(Robot robot) {
        int depose = robot.deposer(); // Récupère tout le minerai du robot
        total += depose;              // Ajoute au stock global
        System.out.println("Entrepôt : +" + depose + " minerai (total = " + total + ")");
    }

    @Override
    public boolean estFranchissable() {
        return true; // Le robot peut se tenir dessus
    }

    public int getTotal() {
        return total; // Utilisé pour vérifier la victoire
    }
}
// Case simple : le robot peut marcher dessus sans effet
public class CaseVide extends Case {

    @Override
    public char afficher() {
        return '.'; // Représentation visuelle
    }

    @Override
    public void interagir(Robot robot) {
        System.out.println("Zone vide."); // Aucun effet
    }

    @Override
    public boolean estFranchissable() {
        return true; // Le robot peut entrer
    }
}

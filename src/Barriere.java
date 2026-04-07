    // Case infranchissable : bloque le robot
    public class Barriere extends Case {

        @Override
        public char afficher() {
            return 'B'; // Symbole d'obstacle
        }

        @Override
        public void interagir(Robot robot) {
            System.out.println("Impossible : barrière !");
            // Aucun effet supplémentaire : le plateau empêche déjà l'entrée
        }

        @Override
        public boolean estFranchissable() {
            return false; // Le robot ne peut pas entrer
        }
    }
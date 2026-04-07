// Case contenant du minerai, avec une réserve limitée
public class Mine extends Case {

    private int reserve; // Quantité restante

    public Mine(int reserve) {
        this.reserve = reserve;
    }

    @Override
    public char afficher() {
        return reserve > 0 ? 'M' : 'm'; // 'M' = active, 'm' = épuisée
    }

    @Override
    public void interagir(Robot robot) {

        // Cas 1 : mine active + robot pas plein
        if (reserve > 0 && robot.peutCollecter()) {
            robot.collecter(); // Ajoute 1 minerai au robot
            reserve--;         // Décrémente la mine
            System.out.println("Le robot extrait du minerai !");
        }

        // Cas 2 : mine vide
        else if (reserve == 0) {
            System.out.println("Mine épuisée.");
        }

        // Cas 3 : robot plein
        else {
            System.out.println("Robot plein !");
        }
    }

    @Override
    public boolean estFranchissable() {
        return true; // Le robot peut entrer sur une mine
    }
}

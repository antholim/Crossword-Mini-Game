package ca.qc.bdeb.inf203.tp1;

import java.util.Scanner;

public class MainCmd {

    static private String nom = "mots-croises3.txt";
    static private MotCroise partie1 = new MotCroise(nom);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Scanner scChiffre = new Scanner(System.in);
        MotCroise partie1 = new MotCroise(nom);
        System.out.println(partie1.getGrille().length);
        if (partie1.getFichierMarche()) {
            System.out.println("Erreur dans le fichier");
        } else {
            System.out.println(partie1.getGrille().length);
            while (!partie1.getPartieFinie() && partie1.getPartiePeutCommencer()) {
                afficherGrilleDiviner(partie1);
                for (int i = 0; i < partie1.getDefinitionsDesMots().size(); i++) {
                    System.out.println(i + 1 + ". " + partie1.getDefinitionsDesMots().get(i));
                }
                System.out.println("Quel mot voulez-vous devinez?");
                System.out.println("(q pour quitter, s pour avoir la solution)");
                String numeroDuMot = sc.nextLine();
                char []numeroDuMotEnChar = numeroDuMot.toCharArray();

                System.out.println(numeroDuMot);

                if (quitterOuAbandonner(numeroDuMot)) {
                    break;
                }
                //Validation
                while ((numeroDuMotEnChar.length != 1) || (!(Character.getNumericValue(numeroDuMotEnChar[0]) <= partie1.getListeDesMots().size() && (Character.getNumericValue(numeroDuMotEnChar[0]) > 0)))) {
                    System.out.println(numeroDuMot);
                    if (quitterOuAbandonner(numeroDuMot)) {
                        break;
                    }
                    System.out.println(numeroDuMotEnChar.length);
                    System.out.println("Entrez quelque chose de valide");
                    numeroDuMot = sc.nextLine();
                    numeroDuMotEnChar = numeroDuMot.toCharArray();
                }

                int numeroDuMotEnInt = Integer.parseInt(numeroDuMot);

                System.out.println("Entrez le mot");
                String mot = sc.nextLine();
                char []motEnChar = mot.toCharArray();
                for (int i = 0; i < motEnChar.length; i++) {
                    if (Character.isDigit(motEnChar[i])) {
                        System.out.println("Entrez un mot sans chiffre");
                        mot = sc.nextLine();
                    }
                }
                System.out.println("Tentative pour le mot " + numeroDuMot + ": " + mot);

                if (partie1.devinerUnMot(mot, numeroDuMotEnInt - 1)) {
                    System.out.println("Bonne réponse");
                } else {
                    System.out.println("Mauvaise réponse");
                }

                if (partie1.getPartieGagnante()) {
                    System.out.println("Félicitations, voici un biscuit :D");
                    afficherGrilleDiviner(partie1);
                    break;
                }
            }
        }
    }
    /**
     * Cette méthode permet d'afficher la grille à deviner du Mot Croise
     * @param partie1 Elle prend en paramètre le mot croisé
     */
    public static void afficherGrilleDiviner(MotCroise partie1) {
        for (int i = 0; i < partie1.getGrilleDeviner().length; i++) {
            for (int j = 0; j < partie1.getGrilleDeviner()[i].length; j++) {
                if (partie1.getGrilleDeviner()[i][j] == null) {

                }
                System.out.print(partie1.getGrilleDeviner()[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Cette méthode permet d'afficher la réponse du Mot Croise
     * @param partie1 Elle prend en paramètre le mot croisé
     */
    public static void afficherReponse(MotCroise partie1) {
        for (int i = 0; i < partie1.getGrille().length; i++) {
            for (int j = 0; j < partie1.getGrille()[i].length; j++) {

                System.out.print(partie1.getGrille()[i][j] + " ");
            }
            System.out.println();
        }
    }
    /**
     * Cette méthode permet à l'utilisateur d'abandonner ou de quitter le programme
     * @param numeroDuMot Le String est la réponse de l'utilisateur
     * @return Retourne un boolean
     */
    public static boolean quitterOuAbandonner(String numeroDuMot) {
        if (numeroDuMot.equals("q")) {
            //Quitter
            return true;
        } else if (numeroDuMot.equals("s")) {
            //Donner sol
            afficherReponse(partie1);
            return true;
        }
        return false;
    }
}

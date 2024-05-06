package ca.qc.bdeb.inf203.tp1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MotCroise {
    /**
     * Cette classe est contient toute la logique d'un mot croisé. Elle permet de chercher un fichier
     * .txt et d'utiliser les valeurs pour faire la partie.
     @partieFinie Si la partie est finie
     @partieGagnante Si l'utilisateur a gagné
     @partiePeutCommencer Si la partie peut commencer
     @fichierMarche Si le fichier marche
     @maxHorizontal La taille de la grille horizontalement
     @maxVertical La taille de la grille verticalement
     @nomDuFichier Le nom du fichier à lire
     @grille[][] La grille réponse du mot croisé
     @grilleDeviner[][] La grille à deviner du mot croisé
     @grillePremiereLettreChiffre[][] La grille où la première lettre des mots sont leurs numéros de chiffres
     @listeDesMots Un ArrayList qui contient la liste des mots
     @coordonneesX Un ArrayList qui contient la liste des coordonnees en X des mots
     @coordonneesY Un ArrayList qui contient la liste des coordonnees en Y des mots
     @directionDuMot Un ArrayList qui contient la liste des directions des mots (V ou H)
     @definitionsDesMots Un ArrayList qui contient la liste des définitions des mots
     @lireFichier Un BufferedReader qui va lire le fichier
     */
    private boolean partieFinie = false;
    private boolean partieGagnante = false;
    private boolean partiePeutCommencer = false;
    private boolean fichierMarche = false;
    private int maxHorizontal = 0;
    private int maxVertical = 0;
    private String nomDuFichier;
    private String grille[][];
    private String grilleDeviner[][];
    private String grillePremiereLettreChiffre[][];
    private ArrayList<String> listeDesMots = new ArrayList<>();
    private ArrayList<Integer> coordonneesX = new ArrayList<>();

    private ArrayList<Integer> coordonneesY = new ArrayList<>();
    private ArrayList<String> directionDuMot = new ArrayList<>();

    private ArrayList<String> definitionsDesMots = new ArrayList<>();
    private BufferedReader lireFichier;

    /**
     * Le constructeur de la classe du MotCroisse
     * @param nomDuFichier
     */
    public MotCroise(String nomDuFichier) {
        this.nomDuFichier = nomDuFichier;
        this.partieFinie = partieFinie;
        this.partieGagnante = partieGagnante;
        this.partiePeutCommencer = partiePeutCommencer;
        fichierVersArrays();
        if (partiePeutCommencer) {
            tailleDeLaGrille();
            remplirLaGrille();
        }
    }

    /**
     * Une méthode qui prend un fichier et le valide, si et seulement si valide, sépare les données
     * dans des ArrayList différentes
     */
    public void fichierVersArrays() {
        try {
            lireFichier = new BufferedReader(new FileReader(nomDuFichier));
            String ligne;
            while ((ligne = lireFichier.readLine()) != null) {
                if ((ligne.charAt(0) != '#')) {
                    String tab[] = ligne.split(":");
                    if (tab.length != 5) {
                        //Lancer Erreur Fichier
                        fichierMarchePas();
                        partiePeutCommencer = false;
                        break;
                    } else if (!(tab[3].equals("V") || tab[3].equals("H"))) {
                        System.out.println(tab[3]);
                        fichierMarchePas();
                        partiePeutCommencer = false;
                        break;
                    } else if (!(estNumerique(tab[1]) && estNumerique(tab[2]))) {
                        fichierMarchePas();
                        partiePeutCommencer = false;
                        break;
                    } else {
                        partiePeutCommencer = true;
                    }

                    for (int i = 0; i < tab.length; i++) {
                        switch (i) {
                            case 0 -> {
                                listeDesMots.add(tab[i]);
                            }
                            case 1 -> {
                                coordonneesX.add(Integer.parseInt(tab[i]));
                            }
                            case 2 -> {
                                coordonneesY.add(Integer.parseInt(tab[i]));
                            }
                            case 3 -> {
                                directionDuMot.add(tab[i]);
                            }
                            case 4 -> {
                                definitionsDesMots.add(tab[i]);
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            fichierMarchePas();
        } catch (IOException e) {

        }
    }

    /**
     * Une méthode qui calcule les dimensions de la grille de jeu et
     * qui les initialise
     */
    public void tailleDeLaGrille() {
        for (int i = 0; i < directionDuMot.size(); i++) {
            if (directionDuMot.get(i).equals("H") && maxHorizontal < coordonneesX.get(i) + listeDesMots.get(i).length()) {
                maxHorizontal = coordonneesX.get(i) + listeDesMots.get(i).length();
            } else if (directionDuMot.get(i).equals("V") && maxVertical < coordonneesY.get(i) + listeDesMots.get(i).length()) {
                maxVertical = coordonneesY.get(i) + listeDesMots.get(i).length();
            }
        }
        grille = new String[maxVertical][maxHorizontal];
        grilleDeviner = new String[maxVertical][maxHorizontal];
        grillePremiereLettreChiffre = new String[maxVertical][maxHorizontal];

    }

    /**
     * Une méthode qui remplit la grille qui contient la réponse, la grille utilisée par l'utilisateur
     * et une grille qui contient la première case de chaque mot
     */
    public void remplirLaGrille() {
        for (int i = 0; i < listeDesMots.size(); i++) {
            char charDesMots[] = listeDesMots.get(i).toCharArray();
            if (directionDuMot.get(i).equals("H")) {
                for (int j = 0; j < charDesMots.length; j++) {
                    if (grille[coordonneesY.get(i)][coordonneesX.get(i) + j] == null) {
                        grille[coordonneesY.get(i)][coordonneesX.get(i) + j] = String.valueOf(charDesMots[j]);
                        grilleDeviner[coordonneesY.get(i)][coordonneesX.get(i) + j] = "?";
                    } else if (!(grille[coordonneesY.get(i)][coordonneesX.get(i) + j].equals(String.valueOf(charDesMots[j])))) {
                        fichierMarchePas();
                        partiePeutCommencer = false;
                    }
                    grille[coordonneesY.get(i)][coordonneesX.get(i) + j] = String.valueOf(charDesMots[j]);
                    grilleDeviner[coordonneesY.get(i)][coordonneesX.get(i) + j] = "?";
                    //Mettre le chiffre
                    if (j == 0) {
                        grilleDeviner[coordonneesY.get(i)][coordonneesX.get(i) + j] = String.valueOf(i + 1);
                        grillePremiereLettreChiffre[coordonneesY.get(i)][coordonneesX.get(i) + j] = String.valueOf(i + 1);

                    }
                }
            } else if (directionDuMot.get(i).equals("V")) {
                for (int j = 0; j < charDesMots.length; j++) {
                    if (grille[coordonneesY.get(i) + j][coordonneesX.get(i)] == null) {
                        grille[coordonneesY.get(i) + j][coordonneesX.get(i)] = String.valueOf(charDesMots[j]);
                        grilleDeviner[coordonneesY.get(i) + j][coordonneesX.get(i)] = "?";
                    } else if (!(grille[coordonneesY.get(i) + j][coordonneesX.get(i)].equals(String.valueOf(charDesMots[j])))) {
                        fichierMarchePas();
                        partiePeutCommencer = false;
                    }
                    if (j == 0) {
                        grilleDeviner[coordonneesY.get(i) + j][coordonneesX.get(i)] = String.valueOf(i + 1);
                        grillePremiereLettreChiffre[coordonneesY.get(i) + j][coordonneesX.get(i)] = String.valueOf(i + 1);

                    }
                }
            }
        }
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == null) {
                    grille[i][j] = ".";
                    grilleDeviner[i][j] = ".";
                }
            }
        }
    }

    /**
     * Cette méthode permet de vérifier si la partie du mot croisé est terminé
     * @return Elle retourne un boolean
     */
    public boolean verifierVictoire() {
        if (Arrays.deepEquals(grilleDeviner, grille)) {
            return partieGagnante = true;
        } else {
            return partieFinie = false;
        }
    }

    /**
     * Cette méthode permet à l'utilisateur de deviner un mot.
     * @param mot Le mot que
     * @param numeroDuMot L'index du mot dans l'ArrayList
     * @return
     */
    public boolean devinerUnMot(String mot, int numeroDuMot) {
        String motEntre = mot.toLowerCase();
        char motEntreEnChar[] = motEntre.toCharArray();

        if (directionDuMot.get(numeroDuMot).equals("H") && listeDesMots.get(numeroDuMot).equals(mot)) {
            for (int j = 0; j < listeDesMots.get(numeroDuMot).length(); j++) {
                grilleDeviner[coordonneesY.get(numeroDuMot)][j + coordonneesX.get(numeroDuMot)] = String.valueOf(motEntreEnChar[j]);
//                tab[coordonneesY.get(numeroDuMot)][j + coordonneesX.get(numeroDuMot)] = String.valueOf(motEntreEnChar[j]);
            }
            return true;
        } else if (directionDuMot.get(numeroDuMot).equals("V") && listeDesMots.get(numeroDuMot).equals(mot)) {
            for (int j = 0; j < listeDesMots.get(numeroDuMot).length(); j++) {
                grilleDeviner[j + coordonneesY.get(numeroDuMot)][coordonneesX.get(numeroDuMot)] = String.valueOf(motEntreEnChar[j]);
//                tab[j + coordonneesY.get(numeroDuMot)][coordonneesX.get(numeroDuMot)] = String.valueOf(motEntreEnChar[j]);
            }
            return true;
        }
        verifierVictoire();
        return false;
    }

    /**
     * Cette méthode permet de vérifier si un String est un Integer
     * Source : https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
     * @param str Le String a vérifier
     * @return Retourne un boolean
     */
    public static boolean estNumerique(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Cette méthode dit si le fichier marche ou pas
     * @return Retourne un boolean
     */
    public boolean fichierMarchePas() {
        return fichierMarche = true;
    }
    /**
     * Ces méthodes sont des getters, des méthodes qui permette à d'autre classe d'utiliser les valeurs en
     * respectant l'encapsulation
     */
    public String[][] getGrille() {
        return grille;
    }

    public String[][] getGrilleDeviner() {
        return grilleDeviner;
    }

    public ArrayList<String> getDefinitionsDesMots() {
        return definitionsDesMots;
    }

    public boolean getPartieFinie() {
        return partieFinie;
    }

    public boolean getPartieGagnante() {
        return partieGagnante;
    }
    public boolean getFichierMarche() {
        return fichierMarche;
    }

    public boolean getPartiePeutCommencer() {
        return partiePeutCommencer;
    }
    public ArrayList<String> getListeDesMots() {
        return listeDesMots;
    }
    public String[][] getGrillePremiereLettreChiffre() {
        return grillePremiereLettreChiffre;
    }
}
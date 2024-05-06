package ca.qc.bdeb.inf203.tp1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.ArrayList;


public class MainJavaFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @partie Une partie de MotCroise
     * @listeDeTextField Un ArrayList qui contient la liste des TextField
     * @listeDeDefinitions Un ArrayList qui contient la liste des définitions des mots
     * @listeCellules[][] Un tableau 2D qui mimitent la grille du MotCroise mais qui contient des HBox
     * @messageUtilisateur Un HBox où le message qui sera afficher pour l'utilisateur
     * @grilleDeJeu Un GridPane qui contient tous les cellules
     * @messagePourUtilisateur Le message qui sera afficher pour l'utilisateur
     * @FileChooser FileChooser qui permet de d'ouvrir un explorateur de fichier
     * @entreeLesMots Un VBox qui contient les TextField, les définitions et les index
     */
    private MotCroise partie = new MotCroise("mots-croises1.txt");
    private ArrayList<TextField> listeDeTextField = new ArrayList<>();
    private ArrayList<Text> listeDeDefinitions = new ArrayList<>();
    private HBox listeCellules[][] = new HBox [partie.getGrille().length][partie.getGrille()[0].length];
    private HBox messageUtilisateur = new HBox();

    private GridPane grilleDeJeu = new GridPane();
    private Text messagePourUtilisateur = new Text("");
    private FileChooser selecteurDeFichier = new FileChooser();
    private VBox entreeLesMots = new VBox();


    @Override
    public void start(Stage primaryStage) throws Exception {

        var root = new VBox();
        var scene = new Scene(root, 480, 480);
        //Première Boîte
        var textTitreDuJeuBox = new HBox();

        Image imageLogo = new Image("mots.png");
        ImageView imageLogoView = new ImageView(imageLogo);

        imageLogoView.setFitHeight(100);
        imageLogoView.setFitWidth(100);



        Text textTitre = new Text("Super Mots-Croisés Master 3000");
        textTitre.setFont(Font.font(28));
        textTitreDuJeuBox.getChildren().addAll(imageLogoView, textTitre);
        textTitreDuJeuBox.setAlignment(Pos.CENTER);

        //Deuxième Boîte
        var changerDeMotCroises = new HBox();
        var changerDeMotCroisesCol1 = new VBox();
        var changerDeMotCroisesCol2 = new VBox();

        Text textChanger = new Text("Changer de grille");
        textChanger.setFont(Font.font(20));
        changerDeMotCroisesCol1.getChildren().addAll(textChanger);

        ChoiceBox<String> choix = new ChoiceBox();
        choix.getItems().add("mots-croises1.txt");
        choix.getItems().add("mots-croises2.txt");
        choix.getItems().add("mots-croises3.txt");
        choix.getItems().add("invalide1.txt");
        choix.getItems().add("invalide2.txt");
        choix.getItems().add("invalide3.txt");

        Button ouvrirSelecteurDeFichier = new Button("Ouvrir un autre mots-croisés");
        //https://jenkov.com/tutorials/javafx/filechooser.html
        ouvrirSelecteurDeFichier.setOnAction(e -> {
            File fichierChoisie = selecteurDeFichier.showOpenDialog(primaryStage);
            String parcoursDuFichier = fichierChoisie.getName();
            if (!fichierChoisie.getName().endsWith(".txt")) {
                messagePourUtilisateur.setFill(Color.RED);
                messagePourUtilisateur.setText("C'EST PAS UN FICHIER TEXTE !!!");
            } else {
                choix.getItems().add(parcoursDuFichier);
                messagePourUtilisateur.setText("");
            }
        });
        changerDeMotCroisesCol2.getChildren().addAll(choix, ouvrirSelecteurDeFichier);
        changerDeMotCroises.setAlignment(Pos.CENTER);
        changerDeMotCroises.getChildren().addAll(changerDeMotCroisesCol1, changerDeMotCroisesCol2);




        choix.setOnAction((e -> {
            partie = new MotCroise(String.valueOf(choix.getValue()));
            if (!partie.getPartiePeutCommencer()) {
                messagePourUtilisateur.setText("FICHIER INVALIDE");
                messagePourUtilisateur.setFill(Color.RED);
            } else {
                listeCellules = new HBox[partie.getGrille().length][partie.getGrille()[0].length];
                System.out.println(String.valueOf(choix.getValue()));
                System.out.println(partie.getPartiePeutCommencer());

                messagePourUtilisateur.setText("");
                creeGrille();
                genererRentrerMot();
                for (int i = 0; i < partie.getListeDesMots().size(); i++) {
                    verifierBonneReponse(i);
                }
            }
                messagePourUtilisateur.setFont(Font.font(20));
                messageUtilisateur.setAlignment(Pos.CENTER);
                messageUtilisateur.getChildren().addAll(messagePourUtilisateur);
        }));


        //Add Final
        root.getChildren().addAll(textTitreDuJeuBox, changerDeMotCroises, messageUtilisateur, grilleDeJeu, entreeLesMots);

        /**
         * Cette petit code boût de code permet de fermer le stage en appuyant sur échappe
         * Source : https://stackoverflow.com/questions/14357515/javafx-close-window-on-pressing-esc
         */
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode key = event.getCode();
                if (key == KeyCode.ESCAPE) {
                    primaryStage.close();
                }
            }
        });

        /**
         * Lancer la scène
         */
        primaryStage.setTitle("Mots Croisés");
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Cette méthode vérifie si le String est un Integer
     * @param str Le String a vérifié
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
     * Cette méthode permet de mettre à jour la grille si l'utilisateur trouve la bonne réponse
     * @param i L'index du mot de la liste
     */
    public void verifierBonneReponse(int i) {
        listeDeTextField.get(i).setOnAction(event -> {
            if (partie.devinerUnMot(listeDeTextField.get(i).getText(), i)) {
                listeDeTextField.get(i).setDisable(true);
                listeDeDefinitions.get(i).setFill(Color.LIGHTGREY);
                messagePourUtilisateur.setText("Bonne réponse");
                messagePourUtilisateur.setFill(Color.GREEN);
                //Update la grilleDeviner
                creeGrille();
                verifierGagnant();
            } else {
                messagePourUtilisateur.setText("Mauvaise réponse");
                messagePourUtilisateur.setFill(Color.RED);
            }
        });
    }

    /**
     * Cette méthode crée la grille visuelle du Mot Croisé, qui est remplit de cellules qui sont des HBox
     */
    public void creeGrille() {
        grilleDeJeu.getChildren().clear();
        grilleDeJeu.setAlignment(Pos.CENTER);
        for (int i = 0; i < partie.getGrille().length; i++) {
            for (int j = 0; j < partie.getGrille()[i].length; j++) {
                HBox cellule = creerCellule();
                Text charDuMot = new Text();

                cellule.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                cellule.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                if (estNumerique(partie.getGrillePremiereLettreChiffre()[i][j])) {
                    var numero = new Text(String.valueOf(partie.getGrillePremiereLettreChiffre()[i][j]));
                    numero.setFont(Font.font(10));
                    cellule.getChildren().add(numero);
                }
                if (partie.getGrilleDeviner()[i][j].equals(".")) {
                    cellule.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
                } else if (!partie.getGrilleDeviner()[i][j].equals("?") && Character.isLetter(partie.getGrilleDeviner()[i][j].charAt(0))) {
                    charDuMot = new Text(partie.getGrilleDeviner()[i][j]);
                    cellule.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
                }
                charDuMot.setFont(Font.font("monospace", 20));
                cellule.getChildren().add(charDuMot);
                listeCellules[i][j] = cellule;
                grilleDeJeu.add(cellule, j, i, 1, 1);
            }
        }
    }
    /**
     * Cette méthode crée une cellule (HBox) avec les dimensions
     * @return Retourne un HBox (La cellule)
     */
    public HBox creerCellule() {
        var cellule = new HBox();

        cellule.setPadding(new Insets(3, 8, 3, 8));
        cellule.setMaxSize(30, 30);
        cellule.setMinSize(30, 30);

        return cellule;
    }

    /**
     * Cette méthode vérifie le gagnant et affiche le message de victoire
     */
    public void verifierGagnant() {
        if (partie.verifierVictoire()) {
            messagePourUtilisateur.setText("Victoire !");
            messagePourUtilisateur.setFill(Color.GREEN);
        }
    }

    /**
     * Cette méthode génère les TexteField pour l'utilisateur, leurs indices et les définitions des mots
     */
    public void genererRentrerMot() {
        listeDeTextField.clear();
        listeDeDefinitions.clear();
        entreeLesMots.getChildren().clear();

        for (int i = 0; i < partie.getListeDesMots().size(); i++) {
            var entreeLesMotsHorizontal = new HBox();
            Text chiffre = new Text(" " + String.valueOf(i + 1) + ".");
            chiffre.setFont(Font.font(16));
            TextField textParUtilisateur = new TextField();
            listeDeTextField.add(textParUtilisateur);
            if (textParUtilisateur.getText().equals(partie.getListeDesMots().get(i))) {
                entreeLesMotsHorizontal.setDisable(true);
            }

            Text definition = new Text(" " + partie.getDefinitionsDesMots().get(i));
            listeDeDefinitions.add(definition);
            definition.setFont(Font.font(16));
            entreeLesMotsHorizontal.getChildren().addAll(chiffre, textParUtilisateur, definition);
            entreeLesMots.getChildren().add(entreeLesMotsHorizontal);
            entreeLesMotsHorizontal.setAlignment(Pos.BASELINE_LEFT);
        }
    }
}

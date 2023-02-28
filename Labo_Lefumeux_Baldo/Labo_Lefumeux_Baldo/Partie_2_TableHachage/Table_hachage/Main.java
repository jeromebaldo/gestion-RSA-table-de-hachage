import java.io.File;
import java.io.IOException;
import java.util.Arrays;



public class Main
{
    public static void main(String[] args)
    {

        //constantes par défaut de la taille de la matrice servant pour le hash
        final int DEFAULT_LINES = 16;
        final int DEFAULT_COLS = 100;
        //constante pour la taille du hash
        final int DEFAULT_OCTETS = 16;

        /*/////pool de fichiers servant pour le laboratoire
        //bien prendre le chemin du fichier pour les tests sur votre ordinateur
        String nomChemin = "CHEMIN\\page_accueil.html";
        String nomChemin = "CHEMIN\\test.txt";
        String nomChemin = "CHEMIN\\test2.txt";
        String nomChemin = "CHEMIN\\pythonw.exe";
        String nomChemin = "CHEMIN\\table_Hashage.iml";
        */
        String nomChemin = "";

        //Paramètrage de la taille de la matrice
        int[] taille = CalibrerLignesColonnes(nomChemin, DEFAULT_LINES, DEFAULT_COLS);
        int lignes = taille[0];
        int colonnes = taille[1];
        System.out.println("taille de la matrice : " + lignes * colonnes + " lignes : " + lignes + " colonnes : " + colonnes);

        //Génération du hash avec le paramètrage
        GenerHash generHash = new GenerHash(nomChemin, lignes, colonnes, DEFAULT_OCTETS);
        byte[] hash = generHash.genererHash(); //récupération du hash
        //affichage du hash et de sa taille
        System.out.println("taille du hash : " + hash.length);
        System.out.println("Hash du fichier " + nomChemin + " : " + Arrays.toString(hash));
    }

    //Méthode pour calibrer le nombre de lignes et de colonnes pour une matrice en fonction de la taille d'un fichier donné.
    public static int[] CalibrerLignesColonnes(String nomChemin, int defaultLignes, int defaultColonnes)
    {
        File fichier = new File(nomChemin); //création d'un file avec le chemin donné en entrée de la méthode
        int lignes = defaultLignes;
        int colonnes = defaultColonnes;
        int tailleMatrice = lignes * colonnes;
        if (fichier.length() > tailleMatrice)//si la taille du fichier dépasse la taille de la matrice par défaut
        {
            //boucle multipliant par 10 jusqu'à ce que la matrice soit plus grande que le fichier à hasher
            while (fichier.length() > lignes * colonnes)
            {
                lignes *= 10;
                colonnes *= 10;
            }
        }
        int taille[] = {lignes, colonnes};

        return taille;//un tableau de int pour lignes et colonnes
    }
}
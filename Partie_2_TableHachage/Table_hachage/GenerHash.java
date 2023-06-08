import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class GenerHash
{

    private String nomChemin;
    private int lignes;
    private int colonnes;
    private int tailleOctet;//au lieu de 1
    private byte[] hash;
    private int tailleMatrice;

    public GenerHash(String nomChemin, int lignes, int colonnes, int tailleOctet)
    {
        this.nomChemin = nomChemin;
        this.lignes = lignes;
        this.colonnes = colonnes;
        this.tailleOctet = tailleOctet;
        this.tailleMatrice = this.lignes*this.colonnes;
        this.hash = new byte[this.lignes];
    }

    /*
    méthode pour générer un hash à partir d'un fichier en utilisant une matrice de bytes:
    //lecture du fichier en blocs et stockage des données dans une matrice.
    //opération de XOR sur chaque colonne de la matrice pour génération hash final.
    //La méthode calcule ensuite XOR de chaque colonne de la matrice en initialisant le buffer à 0 et
    en calculant le XOR de chaque ligne de la colonne j. Le résultat est stocké dans le buffer.
    // hash courant est XOR avec le contenu du buffer.
    //Si le nombre d'octets lus est inférieur à la taille de la matrice, remplissage des cases restantes avec des 0(parcours matrice)
     */
    public byte[] genererHash()
    {
        // Initialiser la matrice
        byte[][][] matrice = new byte[lignes][colonnes][tailleOctet];
        boolean eof = false; // fin de fichier
        byte[] buffer = new byte[16];
        while (!eof)
        {
            // Lire le fichier et stocker les données dans la matrice
            int byteslus = lireDansFichier(nomChemin, matrice);

            // Vérifier si la fin du fichier a été atteinte
            if (byteslus < this.tailleMatrice)
            {
                eof = true;//fin du fichier
            }

            // Calculer le XOR de chaque colonne de la matrice
            for (int j = 0; j < colonnes; j++)
            {
                // Initialiser le buffer à 0
                for (int i = 0; i < tailleOctet; i++)
                {
                    buffer[i] = 0;
                }
                // Calculer le XOR de chaque ligne de la colonne j et stocker le résultat dans le buffer
                for (int i = 0; i < lignes; i++)
                {
                    buffer = xor(buffer, matrice[i][j]);
                }
                // Calculer le XOR du hash courant avec le contenu du buffer
                hash = xor(hash, buffer);
            }

            // Si le nombre d'octets lus est inférieur à la taille de la matrice, remplir les cases restantes avec 0
            if (byteslus < this.tailleMatrice) {
                //int i = byteslus / 100;
                int i = byteslus / this.colonnes;
                //int j = byteslus % 100;
                int j = byteslus % this.colonnes;
                for (int k = 0; k < tailleOctet; k++)
                {
                    // Remplir les cases vides de la colonne j avec 0
                    for (int ii = i; ii < lignes; ii++)
                    {
                        matrice[ii][j][k] = 0;
                    }
                    // Remplir les cases vides de toutes les colonnes après la colonne j avec 0
                    for (int jj = j + 1; jj < colonnes; jj++)
                    {
                        for (int iii = 0; iii < lignes; iii++)
                        {
                            matrice[iii][jj][k] = 0;
                        }
                    }
                }
            }
        }
        return hash;
    }
    /*
    méthode lit les données d'un fichier en blocs de 16 bytes
    //à l'aide d'un flux d'entrée BufferedInputStream.
    //stocke les données lues dans une matrice tridimensionnelle.
    //utilise des boucles for et des indices de matrice pour stocker les données dans la matrice dans l'ordre correct.
    //retourne le nombre total de bytes lus à partir du fichier.
     */
    private int lireDansFichier(String nomFichier, byte[][][] matrice)
    {
        // Cette méthode prend en entrée le nom d'un fichier et une matrice à remplir avec les données lues dans le fichier.
        int bytesLus = 0;
        // Initialise le nombre de bytes lus à 0.
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(nomFichier)))
        {
            // Initialise un flux d'entrée BufferedInputStream pour lire les données du fichier.
            int i = 0;
            int j = 0;
            int k = 0;
            // Initialise des compteurs pour les indices de la matrice.
            byte[] buffer = new byte[16];
            // Initialise un tampon pour stocker les données lues à partir du fichier.
            while (bytesLus < this.tailleMatrice)
            {
                // Boucle jusqu'à ce que le nombre total de bytes lus atteigne la taille de la matrice.
                int n = bis.read(buffer);
                // Lit un bloc de données du fichier dans le tampon.
                if (n == -1)
                {
                    // Vérifie si la fin du fichier a été atteinte.
                    break;
                    // Sort de la boucle si la fin du fichier est atteinte.
                }
                for (int l = 0; l < n; l++)
                {
                    // Parcourt les données lues à partir du fichier dans le tampon.
                    matrice[i][j][k] = buffer[l];
                    // Stocke la valeur de chaque byte dans la matrice.
                    bytesLus++;
                    // Incrémente le nombre de bytes lus.
                    k++;
                    // Incrémente l'indice de la colonne de la matrice.
                    if (k == tailleOctet)
                    {
                        // Vérifie si la fin de la ligne est atteinte.
                        k = 0;
                        // Réinitialise l'indice de la colonne.
                        i++;
                        // Incrémente l'indice de la ligne.
                        if (i == lignes)
                        {
                            // Vérifie si la fin de la matrice est atteinte.
                            i = 0;
                            // Réinitialise l'indice de la ligne.
                            j++;
                            // Incrémente l'indice de la colonne.
                            if (j == colonnes)
                            {
                                // Sort de la boucle si la fin de la matrice est atteinte.
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e)
        {
            // Gère les exceptions éventuelles liées à la lecture du fichier.
            e.printStackTrace();
        }
        return bytesLus;
        // Retourne le nombre total de bytes lus.
    }

    /*
    Méthode effectue une opération XOR ("OU exclusif"):
    //entre chaque élément de deux tableaux de bytes (buffer et ligne) de même taille.
    // La méthode utilise une boucle for pour parcourir les deux tableaux et effectuer l'opération XOR
    // sur les éléments correspondants.
    */
    private byte[] xor(byte[] buffer, byte[] ligne)
    {
        // Cette méthode prend deux tableaux de bytes en entrée et retourne un troisième tableau de bytes.
        byte[] result = new byte[tailleOctet];
        // Crée un nouveau tableau de bytes de la même taille que les deux tableaux d'entrée.
        for (int i = 0; i < tailleOctet; i++)
        {
            // Parcourt les tableaux d'entrée en utilisant une boucle for.
            result[i] = (byte) (buffer[i] ^ ligne[i]);
            // Effectue une opération XOR sur les éléments correspondants des deux tableaux et stocke le résultat dans le nouveau tableau.
        }
        return result;// Retourne le nouveau tableau de bytes contenant les résultats de l'opération XOR.
    }

}


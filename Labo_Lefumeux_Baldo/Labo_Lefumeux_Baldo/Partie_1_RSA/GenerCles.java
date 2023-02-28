import java.math.BigInteger;
import java.security.SecureRandom;

public class GenerCles {

    private final static BigInteger ONE = new BigInteger("1");
    private final static SecureRandom RANDOM = new SecureRandom();
    private BigInteger modulChiffr;
    private BigInteger clesPublic;
    private BigInteger clesPrivee;

    /*
    Le constructeur génère les clés de chiffrement et de déchiffrement RSA:
        - constructuer prend une longueur en bits comme argument et
        - génère les attributs modulChiffr / clesPublic / clesPrivee
        - utilise la méthode exposantChiffre pour générer la clé public
        - utilise la méthode exposantDechiffre pour générer la clé privé
    */
    public GenerCles(int bitLength) {

        //generation de 2 nombres premiers p et q en appelant l'objet RANDOM de type SecureRandom
        // et en divisant par bitlenght(longueur en bit du nombre premier à générer)
        //L'objectif est d'avoir des nombres premiers p et q de longueur similaire, qui sont importants pour la sécurité de la clé.
        BigInteger p = BigInteger.probablePrime(bitLength / 2, RANDOM);
        BigInteger q = BigInteger.probablePrime(bitLength / 2, RANDOM);
        //generation de l'indice d'Euler selon la formule suivante : φ(n) = (p-1)*(q-1)
        BigInteger indicEuler = (p.subtract(ONE)).multiply(q.subtract(ONE));

        this.modulChiffr = p.multiply(q); //multiplication de p et q pour obtenir le module de chiffrement
        this.clesPublic = exposantChiffre(indicEuler, bitLength);
        this.clesPrivee = exposantDechiffre(indicEuler);
    }
    /*
    Cette méthode a pour but de calculer l'exposant de déchiffrement pour l'algorithme RSA:
        - prend en entree => indicEuler(indicateur d'Euler) + bitLenght(longueur en bit du nombre premier à générer)
        - utilise l'algorithme de génération de nombres premiers probablement premiers probablePrime de la classe BigInteger.
        - retourne le nombre premier généré comme résultat.
    */
    public BigInteger exposantChiffre(BigInteger indicEuler, int bitLength) {
        //initialise un nombre premier avec la longueur souhaitée
        BigInteger chiffDepart = BigInteger.probablePrime(bitLength, RANDOM);
        //vérifie que le nombre premier généré est compris entre 1 et phi, et que leur plus grand commun diviseur est égal à ZERO.
        while((chiffDepart.compareTo(ONE) < 0 && chiffDepart.compareTo(indicEuler) < 0 && chiffDepart.gcd(indicEuler).compareTo(ONE) > 0) || (chiffDepart.compareTo(ONE) > 0 && chiffDepart.compareTo(indicEuler) > 0 && chiffDepart.gcd(indicEuler).compareTo(ONE) > 0)) {
            //Si conditions ne sont pas remplies, génération du prochain nombre premier probable
            chiffDepart = chiffDepart.nextProbablePrime();
        }

        return chiffDepart;
    }
    /*
    Cette méthode a pour but de calculer l'exposant de déchiffrement pour l'algorithme RSA:
        - prend en entree => clé publique(expoChiffr) + indicEuler(indicateur d'Euler)
        - utilisation de la méthode "modInverse" de BigInteger pour calculer l'inverse modulaire
        - retourne un BigInteger qui correspond à l'exposant de dechiffrement.
        - cette methode correspond à la formule : d ≡ e^(-1) mod φ(n)
    */
    public BigInteger exposantDechiffre(BigInteger indicEuler){
        BigInteger chiffre;
        chiffre = this.clesPublic.modInverse(indicEuler);
        return chiffre;
    }
    //////accesseurs pour obtenir clé public / clé privé et module de chiffrement
    public BigInteger getModulChiffr() {return modulChiffr;}
    public BigInteger getClesPublic() {return clesPublic;}
    public BigInteger getClesPrivee() {return clesPrivee;}
}
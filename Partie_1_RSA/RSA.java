import java.math.BigInteger;

public class RSA {

    private BigInteger modulChiffr;
    private BigInteger clesPublic;
    private BigInteger clesPrivee;

    /*
    Contructeur du RSA prenant en paramètre l'objet GenerCles
        - récupération du module du chiffrement, de l'exposant de chiffrement et de déchiffrement
        - méthodes : Crypt + Décrypt
    */
    public RSA(GenerCles generCles) {
        this.modulChiffr = generCles.getModulChiffr();
        this.clesPublic = generCles.getClesPublic();
        this.clesPrivee = generCles.getClesPrivee();
    }
    /*
    Cette méthode implémente la méthode de chiffrement d'un message en clair en utilisant la méthode RSA:
    - Utilisation de la méthode modPow pour crypter un message donné, qui est passé en tant que paramètre.
    - La méthode modPow renvoie un objet BigInteger qui représente le résultat de (messageNum^clesPublic) % modulChiffr.
    - Utilisation de la clé public et du module de chiffrement
    - formule : x = y^e mod (n)
     */
    public BigInteger Crypt(BigInteger messageNum){return messageNum.modPow(this.clesPublic, this.modulChiffr);}
    /*
    Cette méthode implémente la méthode de déchiffrement d'un message crypté en utilisant la méthode RSA:
        - Le décryptage se réalise avec la clé privé et le module de chiffrement
        - prend en entrée le message crypté sous format BigInteger et retourne le message en clair sous format BigInteger
        - La méthode modPow calcule la valeur du message crypté exposant la clé privée modulo le module de chiffrement.
        - formule : y = x^d mod (n)
    */
    public BigInteger Decrypt(BigInteger messageCrypt){return messageCrypt.modPow(this.clesPrivee, this.modulChiffr);}
}

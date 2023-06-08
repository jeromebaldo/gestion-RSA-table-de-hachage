import java.math.BigInteger;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        //Par sûreté, il est couramment recommandé que la taille des clés RSA soit au moins de 2048 bits.
        int bitLength = 128;//longueur de clé à faire varier

        ////////CREATION DES CLES POUR ALICE////////
        System.out.println("1 - CREATION DES CLES D'ALICE");
        GenerCles clesAlice = new GenerCles(bitLength);
        System.out.println("Module de chiffrement : " + clesAlice.getModulChiffr() );
        System.out.println("Exposant de chiffrement : " + clesAlice.getClesPublic());
        System.out.println("Exposant de Déchiffrement : " + clesAlice.getClesPrivee());
        System.out.println("");

        ///////CREATION MODULE RSA /////////////
        System.out.println("2 - CREATION DU RSA en prenant les clés d'Alice");
        RSA rsa = new RSA(clesAlice);
        System.out.println("");
        /////////RECUP + TRAITEMENT DU TEXTE/////////
        System.out.println("3 - RECUPERATION ET TRAITEMENT DU TEXTE");
        String messageClair = "HALLEJUHA_J'ai fini;";
        System.out.println("Le message clair de Bob : " + messageClair);
        BlocNum num = new BlocNum(messageClair,clesAlice.getClesPublic());
        System.out.println("numerisation");
        System.out.println(num.getStrNum());
        System.out.println("decoupage du texte de bob");
        System.out.println(Arrays.toString(num.getStrTrans()));
        /////OPERATION DE CRYPTAGE ////////////
        System.out.println("4 - CRYPTAGE DU TEXTE AVEC CLE PUBLIC D'ALICE");
        String msgCrypt = "";
        BigInteger[] msgCryptBigInteger = new BigInteger[num.getStrTrans().length];
        int i = 0;
        for (String a : num.getStrTrans()) {
            BigInteger temp = new BigInteger(a);
            BigInteger bout = rsa.Crypt(temp);
            msgCryptBigInteger[i] = bout;
            msgCrypt = msgCrypt + bout;
            i++;
        }
        System.out.println("Le message crypté : " + msgCrypt);
        /////////////OPERATION DE DECRYPTAGE////////////
        System.out.println("5 - DECRYPTAGE DU TEXTE  CHIFFRE AVEC CLE PRIVEEE D'ALICE");
        int j = 0;
        String[] msgDecrypt = new String[msgCryptBigInteger.length];
        for (BigInteger part:msgCryptBigInteger) {
            BigInteger temp = rsa.Decrypt(part);
            System.out.print(String.valueOf(temp));
            msgDecrypt[j] = String.valueOf(temp);
            j++;
        }
        System.out.println("");
        System.out.println("ASSEMBLAGE DE LA DECRYPTION");
        String assembledMsgDecrypt = num.Assemblage(msgDecrypt);
        System.out.println(assembledMsgDecrypt);
        System.out.println("DENUMERISATION");
        System.out.println("message décrypté  : " + num.Denumerisation(assembledMsgDecrypt));
    }
}
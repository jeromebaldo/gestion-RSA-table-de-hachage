import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlocNum {
    private final String delimiterChar = "1234"; //Ce délimiteur permet de retrouver les nombre ASCII correspondant a chaque character
    private final String strOrigin;
    private BigInteger nbDecoup;
    //Definition d'un dictionnaire de données pour gérer '(39), "(36) et \(92)
    private final HashMap<Character, Integer> nbLetterDic = new HashMap<>() {{put("'".charAt(0),39);put('"',36);put('\\',92);}};
    private String strNum; //Variable qui va contenir la numérisation de mon message
    private String[] strTrans; //strNum sous forme de tableau après l'utilisation de la méthode Denumerisation()

    public BlocNum(String str, BigInteger n_cle) { //Constructeur de la méthode
        strOrigin = str;
        nbDecoup = BigInteger.valueOf(n_cle.toString().length()).subtract(BigInteger.valueOf(1)); //Vu que message numérique en tranche < nb de chiffre de n alors on peut juste faire n_cle -1
        Numerisation(); // On lance directement la numérisation dès qu'on crée l'objet
        Decoup(); //Et on découpe aussi ce qu'on a numérisé plus tôt
    }

    //L'ASCII comprend 128 charactères donc 222 sera ce qui permettra de délimiter chaque charactère
    private void Numerisation(){
        char[] chars = this.strOrigin.toCharArray(); // On met chaque char dans une case de tableau de char
        int[] res = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            if(nbLetterDic.get(chars[i]) != null){ // Si le char fait partie de nos exceptions alors on le transforme directement en ASCII grace au HashMap
                res[i] = nbLetterDic.get(chars[i]);
            } else {
                res[i] = chars[i]; //int -> ASCII
            }
        }
        //Puis on construit le string qui est le résultat de ma numérisation
        StringBuilder sb = new StringBuilder();
        for (int i : res) {
            sb.append(i);
            sb.append(delimiterChar);
        }
        this.strNum = sb.toString();
    }

    public String Denumerisation(String str){
        String[] tabDenumStr = str.split(delimiterChar); //On supprime toutes les occurence de mon délimiteur et on assigne chaque char (qui était délimité par mon délimiteur) dans une case d'un tableau
        int [] tabDenum = new int[tabDenumStr.length];
        for (int i = 0; i < tabDenumStr.length; i++) {
            tabDenum[i] = Integer.parseInt(tabDenumStr[i]); // String -> int
        }
        char[] resChar = new char[tabDenum.length];
        for (int i = 0; i < tabDenum.length; i++) {
            if(nbLetterDic.containsValue(resChar[i])){ //Si le character est dans mon tableau d'exception alors il va utiliser le HashMap défini plus haut
                resChar[i] = (char) getKeyByValue(nbLetterDic,tabDenum[i]);
            } else {
                resChar[i] = (char) tabDenum[i]; // int -> ASCII
            }
        }
        //Puis on assemble tout mes charactères fraichement traduit en ASCII pour créer un string qui contient la version dénumérisé
        String res="";
        for (char a: resChar) {
            res+=a;
        }
        return res;
    }

    //Méthode trouvé sur : https://stackoverflow.com/questions/1383797/java-hashmap-how-to-get-key-from-value qui va nous permettre de retrouver une clé depuis une valeur.
    //Si il ne trouve rien -> renvoie une valeur null
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    //Cette méthode permet d'assembler un tableau de string et renvoie tout sous forme de string
    public String Assemblage(String[] str){
        String test = "";
        for(String a:str){
            test += a;
        }
        return test;
    }


    private void Decoup(){
        // Calcul de la coupe
        BigInteger tailleCoup = BigInteger.valueOf(strNum.length()).divide(nbDecoup); //4
        if(tailleCoup.equals(BigInteger.ZERO)){ //Si la taille de la coupe n'est pas intéressante comparé à la taille du chiffrement alors on aura pas de découpage
            tailleCoup = BigInteger.valueOf(strNum.length());
            nbDecoup = BigInteger.ONE;
        } else if (strNum.length()/nbDecoup.intValueExact() != 0) { //Si la coupe ne peut être égale
            tailleCoup = tailleCoup.add(BigInteger.valueOf(strNum.length()).subtract(BigInteger.valueOf(tailleCoup.intValueExact()*nbDecoup.intValueExact())));
        }
        // Définition d'une variable temporaire
        String[] strTransTemp = new String[nbDecoup.intValueExact()];
        // Début de la découpe du string
        int j = 0;
        for (int i = 0; i < nbDecoup.intValueExact(); i++) {
            if(j < strNum.length()){ //Permet de vérifier si on a terminé ou pas de découper mon string
            try {
                strTransTemp[i] = strNum.substring(j, j+tailleCoup.intValueExact());
                j += tailleCoup.intValueExact();
            } catch(Exception e) { //Permet de gérer si on ne peut avoir une découpe égale dans notre tableau, Ici on s'occupera de mettre ce qui reste a ajouter dans noter tableau
                int reste = strNum.length() - j;
                strTransTemp[i] = strNum.substring(j, j+reste);
                j += reste;
            }}
        }

        this.strTrans = Arrays.stream(strTransTemp).filter(s -> s != null).toArray(String[]::new); // On retourne notre tableau en supprimant au passage toutes les valeurs null de mon tableau
    }

    public String[] getStrTrans() {
        return strTrans;
    }

    public String getStrNum() {
        return strNum;
    }
}

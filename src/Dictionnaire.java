import javax.swing.*;
import java.util.*;
import java.lang.System;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Dictionnaire{

  public HashMap<String, HashSet<String>> set ;
  private int limite_Nb_Mot = 375;

  public Dictionnaire(String s) throws FileNotFoundException{

    set = new HashMap<String, HashSet<String>>();

    System.setIn(new FileInputStream(s));

    Scanner scan = new Scanner(System.in);

    while(scan.hasNext()){

      String buff = scan.nextLine();
      String trigramme;

      if(buff.length() == 1){
        trigramme ="<"+buff+">";
        add(trigramme,buff);
      }
      else{
        trigramme = "<"+buff.substring(0,2);
        add(trigramme,buff);
        for(int i=0;i<buff.length()-2;i++){
          trigramme = buff.substring(i,i+3)+"";
          add(trigramme,buff);
        }
        trigramme = buff.substring(buff.length()-2,buff.length()-1)+">";
        add(trigramme,buff);
      }


    }

  }

  public void add(String trigramme, String n){
    if(set.get(trigramme) == null){
      set.put(trigramme,new HashSet<String>());
    }
    set.get(trigramme).add(n);
  }

  public void affiche(){
    for(String s : set.keySet()){
      for(String mot : set.get(s)){
        System.out.println(s +" "+mot);
      }
    }
  }

  public Boolean contains(String mot){
    if (mot.length() == 1 && set.get("<" + mot + ">") != null) {
      return set.get("<" + mot + ">").contains(mot);
    }
    if (mot.length() == 2 && set.get("<" + mot) != null) {
      return set.get("<" + mot).contains(mot);
    }

    if (mot.length() == 3 && set.get(mot) != null) {
      return set.get(mot).contains(mot);
    }
    if (mot.length() >= 4 && set.get(mot.substring(0,3)) != null){
      return set.get(mot.substring(0,3)).contains(mot);
    }
    return false;
  }

  public void correction(String s){
    //Le mot est dans le dictionnaire

    if(contains(s)){
      System.out.println("Le mot "+s+" est dans le dictionnaire");
      return;
    }
    //Le mot est dans le dictionnaire mais en majuscule
    if(contains(s.toUpperCase())){
        long t1 = System.nanoTime();
      System.out.println("Le mot "+s+" est dans le dictionnaire en majuscule");
      return;
    }
    //Le mot est dans le dictionnaire mais en minuscule
    if(contains(s.toLowerCase())){
      System.out.println("Le mot "+s+" est dans le dictionnaire en minuscule");
      return;
    }
    //Le mot d'écrit avec une majuscule au debut
    if(contains(s.substring(0,1).toUpperCase()+s.substring(1) )){
      System.out.println("Le mot "+s+" est dans le dictionnaire avec une majusucle en debut");
      return;
    }


    //Le mot n'est pas dans le dictionnaire


      System.out.println("Suggestion pour "+s+" : ");
      System.out.println(selectedWord(closestWord(s),s).toString());

    return;
  }


    //retourne un hashSet contenant tout les trigramme d'un mot
    public HashSet<String> trigramOfString(String str){
      HashSet<String> set = new HashSet<String>();
      String tri;
      if (str.length() == 1) {
        tri = "<" + str + ">";
        set.add(tri);
      }
      else{
        tri = "<" + str.substring(0,2);
        set.add(tri);
        for (int i=0;i < str.length() -2 ; i++) {
          tri = str.substring(i,i+3);
          set.add(tri);
        }
        tri = str.substring(str.length() - 2,str.length()) + ">";
        set.add(tri);
      }
      return set;
    }

    //retourne un hashSet des limite_Nb_Mot mot qui on le plus de trigramme en commun avec str
    public HashSet<String> closestWord(String str){
      HashMap<String , Integer> map = new HashMap<String , Integer>();

      for (String tri : trigramOfString(str)) {
        if (set.containsKey(tri)) {
          for (String mot : set.get(tri)) {
            if (!map.containsKey(mot)) {
              map.put(mot,1);
            }
            else{
              map.replace(mot,map.get(mot),map.get(mot)+1);
            }
          }
        }
      }
      if (map.size() <= limite_Nb_Mot) {
        HashSet<String> set = new HashSet<String>(map.keySet());
        return set;
      }
      return selectInMax(map, limite_Nb_Mot);
    }

    //retourne un hashSet de taille max contenant les string avec le plus petit integer
    public HashSet<String> selectInMin(HashMap<String, Integer> map, int max) {
      HashSet<String> set = new HashSet<String>(0);
        if(map.size()<=max){
          set = new HashSet<String>(map.keySet());
          return set;
        }
        List<Integer> listOfValue = new ArrayList<Integer>(map.values());
        Collections.sort(listOfValue);
        List<Integer> low5 = listOfValue.subList(0,max);
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                  if (set.size() < max) {
                    if (low5.contains(entry.getValue())){
                      low5.remove(low5.indexOf(entry.getValue()));
                      set.add(entry.getKey());
                      map.entrySet().remove(entry.getKey());
                    }
                  }
                }
        return set;
    }


    //retourne un hashSet de taille max contenant les string avec le plus grand integer
    public HashSet<String> selectInMax(HashMap<String, Integer> map, int max){
        HashSet<String> set = new HashSet<String>(0);
        List<Integer> listOfValue = new ArrayList<>(map.values());
        Collections.sort(listOfValue,Collections.reverseOrder());
        List<Integer> maxOcc = listOfValue.subList(0,limite_Nb_Mot);
        //int maxValue = Collections.max(map.values());
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (maxOcc.contains(entry.getValue()) && set.size()<max) {
              maxOcc.remove(maxOcc.indexOf(entry.getValue()));
              set.add(entry.getKey());
              map.entrySet().remove(entry.getKey());

            }
      }
        return set;


    }

    //retourne un hashSet des 5 mot qui ont une distance de Levenshtein la plus petite dans set avec str
    public HashSet<String> selectedWord(HashSet<String> set, String str){
      HashMap<String , Integer> map = new HashMap<String , Integer>();
      for (String mot : set) {
        map.put(mot, Levenshtein.levenshteinDistance(str,mot) );
      }
      return selectInMin(map, 5);
    }



}

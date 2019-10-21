import javax.swing.*;
import java.util.*;
import java.lang.System;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Dictionnaire{

  public HashMap<String, HashSet<String>> set ;
  private int limite_Nb_Mot = Integer.MAX_VALUE;

  public Dictionnaire(String s) throws FileNotFoundException{

    set = new HashMap<String, HashSet<String>>();

    System.setIn(new FileInputStream(s));

    Scanner scan = new Scanner(System.in);

    while(scan.hasNext()){

      String buff = scan.nextLine();
      String trigramme;

      if(buff.length() == 1){
        trigramme ="<"+buff+">";
        add(trigramme,buff,set);
      }
      else{
        trigramme = "<"+buff.substring(0,2);
        add(trigramme,buff,set);
        for(int i=0;i<buff.length()-2;i++){
          trigramme = buff.substring(i,i+3)+"";
          add(trigramme,buff,set);
        }
        trigramme = buff.substring(buff.length()-2,buff.length()-1)+">";
        add(trigramme,buff,set);
      }


    }

  }

  public void add(String trigramme, String n, HashMap<String, HashSet<String>> s){
    if(s.get(trigramme) == null){
      s.put(trigramme,new HashSet<String>());
    }
    s.get(trigramme).add(n);
  }

  public void affiche(){
    for(String s : set.keySet()){
      for(String mot : set.get(s)){
        System.out.println(s +" "+mot);
      }
    }
  }

  public Boolean contiens(String mot){
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

  public void corrige(String s, Boolean t){
    //Le mot est dans le dictionnaire

    if(contiens(s)){
      System.out.println("Le mot "+s+" est dans le dictionnaire");
      return;
    }
    //Le mot est dans le dictionnaire mais en majuscule
    if(contiens(s.toUpperCase())){
        long t1 = System.nanoTime();
      System.out.println("Le mot "+s+" est dans le dictionnaire en majuscule");
      return;
    }
    //Le mot est dans le dictionnaire mais en minuscule
    if(contiens(s.toLowerCase())){
      System.out.println("Le mot "+s+" est dans le dictionnaire en minuscule");
      return;
    }
    //Le mot d'écrit avec une majuscule au debut
    if(contiens(s.substring(0,1).toUpperCase()+s.substring(1) )){
      System.out.println("Le mot "+s+" est dans le dictionnaire avec une majusucle en debut");
      return;
    }
    //Le mot n'est pas dans le dictionnaire
    if(t){
      System.out.println("Suggestion pour "+s+" : ");
    }
      long t1 = System.nanoTime();
    for(String mot : selectedWord(closestWord(s),s)){

      if(!t){
        System.out.println("calcul en cours pour : "+mot+"");
      }
      if(t){
        System.out.println(mot + " ");
      }
    }
      long t2 = System.nanoTime();
      System.out.println("Temps de correction : "+ (t2 - t1) / 1_000_000_000.0 +" secondes");
    if(t){
      System.out.println();
    }


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
        if (set.get(tri) != null) {

          for (String mot : set.get(tri)) {

            if (map.get(mot) == null) {
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
      System.out.println(low5);
      if(map.containsValue(1)) System.out.println("WTF");
            int i = 0;
            while(set.size()<max && !map.isEmpty()){
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                  if (set.size() < max) {
                    if (entry.getValue() == low5.get(i)) {
                      set.add(entry.getKey());
                      map.entrySet().remove(entry.getKey());
                      i++;
                    }
                  }
                }
            }
      System.out.println(set.toString());
        return set;
    }


    //retourne un hashSet de taille max contenant les string avec le plus grand integer
    public HashSet<String> selectInMax(HashMap<String, Integer> map, int max){
        HashSet<String> set = new HashSet<String>(0);
        int maxValue = Collections.max(map.values());
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == maxValue && set.size()<max) {
              set.add(entry.getKey());
              map.entrySet().remove(entry.getKey());
              maxValue = Collections.max(map.values());
            }
            else maxValue--;
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

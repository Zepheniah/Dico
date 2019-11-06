import java.util.Scanner;
import java.lang.System;
import java.io.*;/*
import java.io.FileInputStream;
import java.io.FileNotFoundException;*/

import java.time.Instant;
import java.time.Duration;

public class Main{

  public static void main(String[] argv) throws FileNotFoundException{

    InputStreamReader stdin = new InputStreamReader(System.in);

    String Dicopathfile = "./src/dico.txt";
    String Fautepathfile ="./src/fautes.txt";

    Boolean aff=false;

    long temps = System.nanoTime();
    Dictionnaire dico = new Dictionnaire(Dicopathfile);
    long temps1 = System.nanoTime();

    System.out.println("Temps creation dictionnaire : "+(temps1 - temps)/ 1_000_000_000.0 + " secondes");

    Scanner scan2 = new Scanner(stdin);
    System.out.println("Voulez vous afficher les suggestion de correction ? [Y/N]");
    String s = scan2.nextLine();

    if(s.compareTo("Y")==0||s.compareTo("y")==0){
      aff=true;
    }
    long temps_debut = System.nanoTime();
    System.setIn(new FileInputStream(Fautepathfile));
    Scanner scan = new Scanner(System.in);

    while(scan.hasNext()){
      dico.corrige(scan.nextLine(),aff);
    }
    long temps_fin = System.nanoTime();

    System.out.println("Temps de correction : "+ (temps_fin - temps_debut) / 1_000_000_000.0 +" secondes");

    Boolean b = true;

    scan2 = new Scanner(stdin);
    String str;

    while(b){
      System.out.println("Entrez un mot ou 0 pour quitter");

      str = scan2.nextLine();
      if(str.compareTo("0") == 0){
        return;
      }
      if(str.compareTo("")!=0){
        dico.corrige(str,true);
      }
    }

  }

}

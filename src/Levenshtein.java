public class Levenshtein{
  final private String mdepart;
  final private String marrive;
  private int tab[][];
  private int m;
  private int n;


  public String getMdepart(){return this.mdepart;}
  public String getMarrive(){return this.marrive;}
  public int getLongueurmot1(){return this.m;}
  public int getLongueurmot2(){return this.n;}

  public Levenshtein(String mot1, String mot2){
    this.mdepart = mot1;
    this.marrive = mot2;
    this.m = mot1.length();
    this.n = mot2.length();
    this.tab = new int[mot1.length()+1][mot2.length()+1];
    for(int i=0; i<mot1.length()+1; i++){
      for(int j=0 ; j<mot2.length()+1;j++){
        this.tab[i][j] = -1;
      }
    }
  }

  public int min3(int a, int b, int c){
    return Math.min(Math.min(a,b),c);
  }

  public int distance(){
    return dist_progdyn(m, n);
  }
  public static int levenshteinDistance (String lhs, String rhs) {


    int len0 = lhs.length() + 1;
    int len1 = rhs.length() + 1;

    // the array of distances
    int[] cost = new int[len0];
    int[] newcost = new int[len0];

    // initial cost of skipping prefix in String s0
    for (int i = 0; i < len0; i++) cost[i] = i;

    // dynamically computing the array of distances

    // transformation cost for each letter in s1
    for (int j = 1; j < len1; j++) {
      // initial cost of skipping prefix in String s1
      newcost[0] = j;

      // transformation cost for each letter in s0
      for(int i = 1; i < len0; i++) {
        // matching current letters in both strings


        int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

        // computing cost for each transformation
        int cost_replace = cost[i - 1] + match;
        int cost_insert  = cost[i] + 1;
        int cost_delete  = newcost[i - 1] + 1;

        // keep minimum cost
        newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
      }

      // swap cost/newcost arrays
      int[] swap = cost;
      cost = newcost;
      newcost = swap;
    }

    // the distance is the cost for transforming all letters in both strings
    return cost[len0 - 1];
  }
  public int dist_progdyn(int i, int j){

    if(tab[i][j] != -1){
      return tab[i][j];
    }

    if(i==0){
      tab[i][j]=j;
      return tab[i][j];
    }
    if(j==0){
      tab[i][j]=i;
      return tab[i][j];
    }

    if(mdepart.charAt(i-1) == marrive.charAt(j-1)){
      tab[i][j] = min3(1+dist_progdyn(i-1,j) ,1+dist_progdyn(i,j-1), dist_progdyn(i-1,j-1));
      return tab[i][j];
    }
    else{
      tab[i][j] = min3(1+dist_progdyn(i-1,j) ,1+dist_progdyn(i,j-1), 1+dist_progdyn(i-1,j-1));
      return tab[i][j];
    }

  }

  public void affiche(){
    for(int i=0;i<m+1;i++){
      for(int j=0;j<n+1;j++){
        System.out.print(tab[i][j]+" ");
      }
      System.out.println("");

    }
  }

}

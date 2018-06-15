// Starter code for CS 6363.004 Project (Spring 2018).  Modify as needed.
//@author: simonwang

// Change name of package from "NetId" to your net id
package sxw173330;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.*;

// Change name of file and class from "NetId" to your net id
public class sxw173330Project6363 {
    static int VERBOSE = 0;
    
    static class Jewel {
    		public int weight, profit, min, max, fine, cap;
    		
    		Jewel(int w, int p, int n, int x, int f, int c) {
    			weight = w;  profit = p;  min = n;  max = x;  fine = f;  cap = c;
    		}

    		public String toString() { 
    			return weight + " " + profit + " " + min + " " + max + " " + fine + " " + cap; 
    		}
    }

    static class Pair {
    		public int p, n;
    		Pair(int p, int n) {
    			this.p = p;  
    			this.n = n;
    		}

    		public String toString() { 
    			return p + " " + n; 
    		}
    }
    
    static int OptNum=0;
    static int g=55645, N=50;
    public static int MP[][] = new int[g+1][N+1];
    public static Pair process(int G, Jewel[] items, int n) {

    		//Find the optimal profit
    		for(int i=0; i<=n; i++) {
    			for(int w=0; w<=G; w++) {
    				int max=0;
    				int top=0;
    				//base
				if(i==0)
    					MP[w][i]=0;
				
				//step
				else {
	    				for(int qi=0; qi<=items[i].max; qi++){
	    					if(w-(items[i].weight*qi) >= 0 ) {
	    						if(qi==0) {
	    							MP[w][i] = items[i].profit*qi + MP[w-(items[i].weight*qi)][i-1] - fine(i, qi, items); //qi=0
	    							if(w==0)
	    								MP[w][i] = items[i].profit*qi - fine(i, qi, items); //qi=0, w=0
	    						}
	    						
	    						else {	//qi>0
	    							top = Math.max(top, items[i].profit*qi + MP[w-(items[i].weight*qi)][i-1] - fine(i, qi, items));
	    							if(max<top) {
		    							max=top;
		    							MP[w][i]=max;
	    							}
	    						}
		    				}
	    				}
    				}
                    
    			}
    		}
    		
    		//Find the number of optimal profit
    		OptNum(n, G, items);
    		
    		
    		return new Pair(MP[G][n], OptNum);
    }

    //Fine for penalty
    public static int fine(int i, int qi, Jewel[] items) {
		if(qi >= 0 && qi < items[i].min)
			return Math.min(items[i].fine * (items[i].min - qi), items[i].cap); //fi*(ni-qi)
		else
			return 0;
    }
    
    //Enumerate for the optimal solution
    public static void enumerate(int s[], int i, int w, Jewel[] items) {
    		enumerate(s, i, w, MP, items);
    }   
    
    public static void enumerate(int s[], int i, int w, int MP[][], Jewel[] items) {
    		if(i==0)
    			visit(s); //print s
    		else {
    			for(int qi=0; qi <= Math.min(items[i].max, w/items[i].weight) ; qi++) {
    				if(MP[w][i] == items[i].profit*qi + MP[w-(items[i].weight*qi)][i-1] - fine(i, qi, items)) {
    					s[i]=qi;
      				enumerate(s, i-1, w-(items[i].weight*qi), MP,items);
    				}
    			}
    		}
    }
    
    //Find the number of optimal profit
    public static void OptNum(int i, int w, Jewel[] items) {
		if(i==0) 
			OptNum++;
		else {
			for(int qi=0; qi <= Math.min(items[i].max, w/items[i].weight) ; qi++) {
				if(MP[w][i] == items[i].profit*qi + MP[w-(items[i].weight*qi)][i-1] - fine(i, qi, items)) {
					OptNum(i-1, w-(items[i].weight*qi), items);
				}
			}
		}
    }
    
    //Visit for print out the optimal solution
    public static void visit(int s[]) {
    		int n=s.length-1;
    		for(int i=1; i<=n; i++) {
    			System.out.print(s[i]+ " ");
    		}
    		System.out.println();
    }
    
    public static void main(String[] args) throws FileNotFoundException {
	    	Scanner in;
	    	
	    	if(args.length == 0 || args[0].equals("-")) {
	    	    in = new Scanner(System.in);
	    	} 
	    	else {		
	    	    File inputFile = new File(args[0]);
	    	    in = new Scanner(inputFile);
	    	}
	    	if (args.length > 1) {
	    	    VERBOSE = Integer.parseInt(args[1]);
	    	}
	    	
	    	int G = in.nextInt();
	    	int n = in.nextInt();
	    	Jewel[] items = new Jewel[n+1];
	    	for(int i=0; i<n; i++) {
	    	    int index = in.nextInt();
	    	    int weight = in.nextInt();
	    	    int profit = in.nextInt();
	    	    int min = in.nextInt();
	    	    int max = in.nextInt();
	    	    int fine = in.nextInt();
	    	    int cap = in.nextInt();
	    	    items[index] = new Jewel(weight, profit, min, max, fine, cap);
	    	    if(VERBOSE > 0) { System.out.println(index + " " + items[index]); }
	    	}
	    	Pair answer= process(G, items, n);
	    	
	    	System.out.println("# Output:");	
	    	System.out.println(answer);	
	    	
	    	System.out.println("# List of optimal solutions:");
    		//Optimal solution
    		int s[] = new int[n+1];
    		enumerate(s, n, G, items);
    }
}

import java.io.*;
import java.util.*;


public class main {     

     
    public static void main(String[] args) {
    //TODO:build the hash table and insert keys using the insertKeyArray function.
    	//copied test cases
    	    
    	        int w = 10;
    	    	int XA = 683;
    	    	int YA = 590;
    	    	int[] Xlist = {12, 14, 77, 74, 63, 21, 69, 13, 84, 93, 35, 89, 45, 60, 15, 57, 51, 18, 42, 62};
    	    	int[] Ylist = {79, 13, 45, 64, 32, 95, 67, 27, 78, 18, 41, 69, 15, 29, 72, 57, 81, 50, 60, 14};
    	    	
    	        Chaining chainForX = new Chaining(w, 0, XA);
    	        Chaining chainForY = new Chaining(w, 0, YA);
    	        
    	        Open_Addressing openAddressForX = new Open_Addressing(w, 0, XA);
    	        Open_Addressing openAddressForY = new Open_Addressing(w, 0, YA); 
    	        
    	        int numberCollisions1 = chainForX.insertKeyArray(Xlist);
    	        int numberCollisions2 = chainForY.insertKeyArray(Ylist);

    	        int numberCollision3 = openAddressForX.insertKeyArray(Xlist);
    	        int numberCollision4 = openAddressForY.insertKeyArray(Xlist);

    	        
    	        System.out.println("The number of collisions in the chainForX: " + numberCollisions1);
    	        System.out.println("The number of collisions in the chainForY: " + numberCollisions2);
    	        
    	        System.out.println("The number of collisions in the openAddressForX: " + numberCollision3);
    	        System.out.println("The number of collisions in the openAddressForY: " + numberCollision4);

    }
}
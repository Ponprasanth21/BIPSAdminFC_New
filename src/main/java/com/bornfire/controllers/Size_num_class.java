package com.bornfire.controllers;




import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Size_num_class {


	
	static void RecursivePrint(File[] arr, int index, int level) throws FileNotFoundException
    {Scanner sc ;
    int count = 0;
        // terminate condition
        if (index == arr.length)
            return;
 
        // tabs for internal levels
        for (int i = 0; i < level; i++)
            System.out.print("\t");
 
        // for files
        if (arr[index].isFile()) {
        	 sc = new Scanner(arr[index]);
        while(sc.hasNextLine()) {
            sc.nextLine();
            count++;
          }
            System.out.println(arr[index].getName()+"||"+(double)arr[index].length()/1024+"||"+arr[index].getAbsolutePath()+"||"+count);
        }
        // for sub-directories
        else if (arr[index].isDirectory()) {
            System.out.println("[" + arr[index].getName()
                               + "]");
 
            // recursion for sub-directories
            RecursivePrint(arr[index].listFiles(), 0,
                           level + 1);
        }
 
        // recursion for main directory
        RecursivePrint(arr, ++index, level);
    }
 
    // Driver Method
    public static void main(String[] args) throws FileNotFoundException
    {
        // Provide full path for directory(change
        // accordingly)
        String maindirpath
            = " C:/Users/Administrator/git/ipspay/src/main/java/com/bornfire/clientService";
       
        // File object
        File maindir = new File(maindirpath);
 
        if (maindir.exists() && maindir.isDirectory()) {
             
              // array for files and sub-directories
            // of directory pointed by maindir
            File arr[] = maindir.listFiles();
 
            System.out.println(
                "**********************************************");
            System.out.println(
                "Files from main directory : " + maindir);
            System.out.println(
                "**********************************************");
 
            // Calling recursive method
            RecursivePrint(arr, 0, 0);
        }
    }
    
	private static long getFolderSize(File folder)
  {
      long length = 0;
     
      // listFiles() is used to list the
      // contents of the given folder
      File[] files = folder.listFiles();

      int count = files.length;

      // loop for traversing the directory
      for (int i = 0; i < count; i++) {
          if (files[i].isFile()) {
              length += files[i].length();
          }
          else {
              length += getFolderSize(files[i]);
          }
      }
      return length;
  }
}


import java.io.*;
import java.util.*;

public class Acl2 {
    

    public static void main(String[] args) throws IOException {
   

         /*  Scanner filename=new Scanner(System.in);
        System.out.println("enter ACl filename");	
  	String file1=filename.next();
        System.out.println("enter packet filename");	
  	String file2=filename.next();*/
        String file1="Aacl.txt";
        String file2="packet.txt";
          
        File f1 = new File(file1);
        Scanner s1=new Scanner(f1);
        File f2 = new File(file2);

          
        
         if (f1.exists()){
                 if (s1.hasNext()){
                    String acl = s1.nextLine();
                       String[] words = acl.split(" "); //slits words into arrays
                       int type = Integer.parseInt(words[1]); 
                        if(type > 0 && type < 100 ){
                           System.out.println("ACL");
                           acl(f1,f2);
                        }
                        if(type > 99 && type < 200 ){
                           System.out.println("ADVANCED ACL");
                           Advanedacl(f1,f2);
                        }     
                }
            }
         
        
    
    }   

    private static String acl(File f1, File f2) throws FileNotFoundException {
   Scanner s1=new Scanner(f1);   
        int acllines=0; String xx = "access-list";
           while (s1.hasNextLine()){
              String ok1 = s1.nextLine(); 
                if(ok1.contains(xx)){ 
                acllines++;
                }
             }
 // System.out.println("aa"+acllines);
  
        Scanner s2=new Scanner(f2);       
        int lines=0,o=0;
         String[] okie = new String[20]; 
        while (s2.hasNext()){
            String ok = s2.next(); 
             if(lines%2 != 0){
             okie[o]  = ok;   
            o++;
             }
            lines += 1;
        }   
      //    System.out.println("aa"+lines);

        String ip="",comp="";
        int i=0;
        String inputIP[] =  packetIP(f2,lines);      //read packet.txt to retrieve Source ip address           
        int inputCount = inputIP.length;
        System.out.println("inputs "+inputCount);
        for(i=0;i<inputCount;i++){
              Scanner s3=new Scanner(f1);  
              int counter= 0;
              while(s3.hasNextLine()){
                    String x = "access-list";
                    String line = s3.nextLine();
                    if(line.contains(x)){ 
                    ip = getStatusIpMask(line);     // got ip changed to any 
                    comp = compare(inputIP[i],ip);
                        /*  System.out.println (comp);
                          System.out.println(inputIP[i]);
                          System.out.println(line);
                          System.out.println(ip);*/
                         if( (comp.equals("same") ) ){
                             String[] done = line.split(" ");
                             System.out.println( inputIP[i]+" "+okie[i]+" : "+done[2]);
                          }
                         /* else if( (comp.equals("nope") && line.contains(inputIP[i]) ) ){
                                System.out.println( inputIP[i]+"deny");
                          }*/
                          else if( (comp.equals("nope") ) ){
                              
                              counter++;
                             // System.out.println(counter);
                              if(counter == acllines){
                                    System.out.println( inputIP[i]+" "+okie[i]+" : deny");
                                  
                              }
                         
                          
                          }
                        
                    }
              }     
          }

          
          return "";
    }
    
    
    
         private static String[] packetIP(File f2,int lines) throws FileNotFoundException {
         String ip="";
         String ipAddr ="";
          String[] SRC  = new String[lines/2];
         Scanner s2=new Scanner(f2);
           int i = 0;
          while (s2.hasNextLine()){
              ipAddr = s2.nextLine();
             String[] split = ipAddr.split(" ");
                 //if(i%2==0){   
              System.out.println(split[0]);
                SRC[i] = split[0];
            //}        
              i++;
          }
        return SRC;
    }
     
    
     private static String getStatusIpMask(String line){
             
           
             String[] parts =  line.split(" ");
             String s = parts[2];
             String i = parts[3];
             String m = parts[4];
             //System.out.println(m);
             String[] mask = m.split("\\.");
             String[] ip = i.split("\\.");
             int len = mask.length;
             for(int j=0;j<4;j++){
               int q =Integer.parseInt(mask[j]);
               int w =Integer.parseInt(ip[j]); 
               if(q == 255){
                   if(w== 0){
                       ip[j] = "any";
                   }
               }
           }  
             String iip = "";
            for (int j=0;j<4;j++) {
               if(j<3){
                iip += ip[j]+".";}
               else{
               iip += ip[j];}    
               }
             return iip;
     }

    private static String compare(String i, String ipA) {
       
        
         
        //  System.out.print(i+":"+ipA+"\n");
           String[] input = i.split("\\.");
           String[] ip = ipA.split("\\.");
        
             for(int j=0;j<4;j++)
             { 
                 if(input[j].equals(0) && ip[j].equals("any") ){
                         ip[j] = input[j];
                 }
                 else if(input[j].equals(ip[j]) )
                 {       
                    // System.out.print(input[j]+".");
                 }
                else if(!input[j].equals(ip[j]) )
                 {
                     if(ip[j].equals("any")){
                         ip[j] = input[j];
                     }
                     
                   // System.out.print("here"+input[j]+".");
                 }                 
             }
             
             String g="",h="";
             for(int j=0;j<4;j++)
             { 
                 g += input[j]+".";
             //System.out.print(input[j]+".");
             
             }
           //    System.out.print("\n");
              for(int j=0;j<4;j++)
             { 
                  h += ip[j]+".";
//             System.out.print(ip[j]+".");
             }
          /*   System.out.print("\n");
       
             System.out.print(g);
             System.out.print(h);
             System.out.print("\n");*/
       
             if(g.equals(h))
             {
                 return "same";
             }
             else{
                 return "nope";
                
             }
       
    }
    
    /*-------------------------------------------------------------------------------------------------------------------------------*/
    
    private static String Advanedacl(File f1, File f2) throws FileNotFoundException {
   Scanner s1=new Scanner(f1);   
        int acllines=0; String xx = "access-list";
           while (s1.hasNextLine()){
              String ok1 = s1.nextLine(); 
                if(ok1.contains(xx)){ 
                acllines++;
                }
             }
  System.out.println("aa"+acllines);
  
        Scanner s2=new Scanner(f2);       
        int lines=0,o=0;
         String[] okie = new String[20]; 
        while (s2.hasNext()){
            String ok = s2.next(); 
             if(lines%2 != 0){
             okie[o]  = ok;   
            o++;
             }
            lines += 1;
        }   
   System.out.println("aa"+lines);
        String ip="",comp="",ipdest="";
        int i=0;
        String inputIP[] =  packetIP1(f2,lines);      //read packet.txt to retrieve Source ip address           
        int inputCount = inputIP.length;
        System.out.println("inputs "+inputCount);
        for(i=0;i<inputCount;i++){
              Scanner s3=new Scanner(f1);  
              int counter= 0;
              while(s3.hasNextLine()){
                    String x = "access-list";
                    String line = s3.nextLine();
                    if(line.contains(x)){ 
                   
                   ipdest = getStatusIpMask1(line);

                        
                    ip = getStatusIpMask1(line);     // got ip changed to any 
                    comp = compare1(inputIP[i],ip);
                        /*  System.out.println (comp);
                          System.out.println(inputIP[i]);
                          System.out.println(line);
                          System.out.println(ip);*/
                         if( (comp.equals("same") ) ){
                             String[] done = line.split(" ");
                             System.out.println( inputIP[i]+" "+okie[i]+" : "+done[3]);
                          }
                        
                          else if( (comp.equals("nope") ) ){
                              
                              counter++;
                             // System.out.println(counter);
                              if(counter == acllines){
                                    System.out.println( inputIP[i]+" "+okie[i]+" : deny");
                                  
                              }
                         
                          
                          }
                        
                    }
              }     
          }

          
          return "";
    }
    
    
    
          private static String[] packetIP1(File f2,int lines) throws FileNotFoundException {
         String ip="";
         String ipAddr ="";
          String[] SRC  = new String[lines/2];
         Scanner s2=new Scanner(f2);
           int i = 0;
          while (s2.hasNextLine()){
              ipAddr = s2.nextLine();
             String[] split = ipAddr.split(" ");
                 //if(i%2==0){   
              System.out.println(split[0]);
                SRC[i] = split[0];
            //}        
              i++;
          }
        return SRC;
    }
     
    
     private static String getStatusIpMask1(String line){
             
           
             String[] parts =  line.split(" ");
             String s = parts[3];
             String i = parts[4];
             String m = parts[5];
        //     System.out.println(s+"    "+i+"    "+m);
             String[] mask = m.split("\\.");
             String[] ip = i.split("\\.");
             int len = mask.length;
             for(int j=0;j<4;j++){
               int q =Integer.parseInt(mask[j]);
               int w =Integer.parseInt(ip[j]); 
               if(q == 255){
                   if(w== 0){
                       ip[j] = "any";
                   }
               }
           }  
             String iip = "";
            for (int j=0;j<4;j++) {
               if(j<3){
                iip += ip[j]+".";}
               else{
               iip += ip[j];}    
               }
             return iip;
     }
     
         private static String getStatusIpMask2(String line){
             
           
             String[] parts =  line.split(" ");
             String s = parts[3];
             String i = parts[6];
             String m = parts[7];
             System.out.println(s+"    "+i+"    "+m);
             String[] mask = m.split("\\.");
             String[] ip = i.split("\\.");
             int len = mask.length;
             for(int j=0;j<4;j++){
               int q =Integer.parseInt(mask[j]);
               int w =Integer.parseInt(ip[j]); 
               if(q == 255){
                   if(w== 0){
                       ip[j] = "any";
                   }
               }
           }  
             String iip = "";
            for (int j=0;j<4;j++) {
               if(j<3){
                iip += ip[j]+".";}
               else{
               iip += ip[j];}    
               }
             return iip;
     }

    private static String compare1(String i, String ipA) {
       
        
         
         System.out.print(i+":"+ipA+"\n");
           String[] input = i.split("\\.");
           String[] ip = ipA.split("\\.");
        
             for(int j=0;j<4;j++)
             { 
                 if(input[j].equals(0) && ip[j].equals("any") ){
                         ip[j] = input[j];
                 }
                 else if(input[j].equals(ip[j]) )
                 {       
                    // System.out.print(input[j]+".");
                 }
                else if(!input[j].equals(ip[j]) )
                 {
                     if(ip[j].equals("any")){
                         ip[j] = input[j];
                     }
                     
                   // System.out.print("here"+input[j]+".");
                 }                 
             }
             
             String g="",h="";
             for(int j=0;j<4;j++)
             { 
                 g += input[j]+".";
             //System.out.print(input[j]+".");
             
             }
           //    System.out.print("\n");
              for(int j=0;j<4;j++)
             { 
                  h += ip[j]+".";
//             System.out.print(ip[j]+".");
             }
          /*   System.out.print("\n");
       
             System.out.print(g);
             System.out.print(h);
             System.out.print("\n");*/
       
             if(g.equals(h))
             {
                 return "same";
             }
             else{
                 return "nope";
                
             }
       
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
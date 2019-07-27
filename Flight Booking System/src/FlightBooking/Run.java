/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlightBooking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class Run{

   static Booking[] bookings = new Booking[40];
   static Booking[] queues = new Booking[40];
   static Flight[] flights = new Flight[8];
    
    public static void main(String[] args) {
          
      Flight t = new Flight();
        //1st choice
        t.date ="23/12/2018";
        t.flightName = "AirAsia";
        t.seats = 5;
        flights[0]=t;
        t= new Flight();
        
        //2nd choice
        t.date ="24/12/2018";
        t.flightName = "MAS";
        t.seats = 4;
        flights[1]=t;
        t= new Flight();
        
        //3rd choice
        t.date ="25/12/2018";
        t.flightName = "Lion Airline";
        t.seats = 3;
        flights[2]=t;
        t = new Flight();
        
        //4th choice
        t.date ="26/12/2018";
        t.flightName = "Malindo";
        t.seats = 5;
        flights[3]=t;
        t= new Flight();
        
        //5th choice
        t.date ="27/12/2018";
        t.flightName = "AirAsia";
        t.seats = 2;
        flights[4]=t;
        t= new Flight();
        
        //6th choice
        t.date ="28/12/2018";
        t.flightName = "Malindo";
        t.seats = 5;
        flights[5]=t;
        t= new Flight();
        
        //7th choice
        t.date ="29/12/2018";
        t.flightName = "MAS";
        t.seats = 2;
        flights[6]=t;
        t= new Flight();
        
        System.out.println("Confirm Booked Customer");
        System.out.println("------------------------------");
        displayBookingList();
        waitingList();
        System.out.println("_______________________________");
     /////////////MAIN MENU///////////   
        System.out.println("Main Menu");
        System.out.println("------------------------------");
        System.out.println("");
        System.out.println("Pick your choice : " );
        System.out.println("1. Book a Flight Ticket");
        System.out.println("2. Edit Ticket Information");
        System.out.println("3. View Ticket Status");
        System.out.println("4. Cancel a Ticket");
        System.out.println("------------------------------");
        Scanner sc = new Scanner(System.in);
        int ch = sc.nextInt();

        switch (ch) {
            case 1:
                reservation();
                break;
            case 2:
                editInfo();
                break;
            case 3:
                ticketStatus();
                break;
            case 4:
                cancelTicket();
        }

    
    
  
        
    }
    ////////BOOKING//////////////
      public static void reservation() {  
        for (int i = 0; i < 7; i++) {
            Flight f = flights[i];
            System.out.println(i + 1 + " " + f.date + " " + f.flightName);
        }

        Scanner sc = new Scanner(System.in);
        int ch = sc.nextInt();
        customerDetails(ch);
    }

      
      public static void customerDetails(int flightNo) {
            
        System.out.println("Enter your name: ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        System.out.println("Enter your ID number: ");
        String id = sc.nextLine();

        if (flights[flightNo - 1].booked < flights[flightNo - 1].seats) {
            
            try {
                PrintWriter pw= new PrintWriter(new FileWriter("FlightBooking.txt", true));
                pw.append(name + "," + id + "," + flightNo + "\n");
                pw.close();

            } catch (IOException ex) {
                Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            putIntoWaitingList(name, id, flightNo);
        }

        
        System.out.println("Your Ticket is booked");

    }
      
    ///////////////////EDIT INFORMATION//////////////////
    public static void editInfo() {
        System.out.println("Please enter your ID number (last 4 digit) : " );
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();
        int ind = -1;
        for (int i = 0; i < 40; i++) {
            if (bookings[i].id.equals(id)) {
                ind = i;
                break;
            }
        }
        if (ind != -1) {
            System.out.println("Do enter your name you wish to edit: ");
            bookings[ind].name = sc.nextLine();
            System.out.println("Do enter your ID number(last 4 digit) you wish to edit");
            bookings[ind].id = sc.nextLine();
            reloadFiles();
        } else {
            System.out.println("Not found");
        }

        System.out.println("Your Profile is updated");
    }

    
       
    ////////////READ FILE INFORMATION////////////////////////
    public static void displayBookingList() {
        BufferedReader br = null;
        try {
            File file = new File("FlightBooking.txt");
            br = new BufferedReader(new FileReader(file));
            String st;
            int j = 0;
            while ((st = br.readLine()) != null) {
                System.out.println(st);
                int flighNo = Integer.parseInt(st.split(",")[2]);
                String[] str = st.split(",");
                Booking booking = new Booking();
                booking.name = str[0];
                booking.flightNo = flighNo;
                booking.id = str[1];
                bookings[j++] = booking;
                flights[flighNo - 1].booked++;
            }

        } catch (Exception ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

     

         /////////////////////////KEY IN WAITINGLIST////////////////
    public static void putIntoWaitingList(String name, String id, int flightNo) {
        
        try {
            PrintWriter pw= new PrintWriter(new FileWriter("waitingList.txt", true));
            pw.append(name + "," + id + "," + flightNo + "\n");
            pw.close();

        } catch (IOException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Your ticket is pending now.");

    }
    
    
  /////////////////check whether the data is in WAITING LIST  or not ////////////////////
    
    public static boolean isQueued(String id) {
        BufferedReader br = null;
        int j = 0;
        try {
            File file = new File("waitingList.txt");
            br = new BufferedReader(new FileReader(file));
            String st;
            j = 0;
            while ((st = br.readLine()) != null) {
                String[] str = st.split(",");
                if (str[1].equals(id)) {
                    j = 1;
                    break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (j == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    
    //////////// WAITINGLIST //////////////////////////
    public static void waitingList() {
         
        try {
            File file = new File("waitingList.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            int j = 0;
            while ((st = br.readLine()) != null) {
//                System.out.println(st);
                if (st.equals("") || st == null) {
                    break;
                }
                int flighNo = Integer.parseInt(st.split(",")[2]);
                String[] str = st.split(",");
                Booking booking = new Booking();
                booking.name = str[0];
                booking.flightNo = flighNo;
                booking.id = str[1];
                queues[j++] = booking;
            }
            br.close();
        } catch (Exception ex) {
//            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        } 
        }

//    //////////////////////DISPLAY WAITINGLIST///////////////
//   public static void displayWaitingList() {
//     
//        try {
//            File file = new File("waitingListTmp.txt");
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String st;
//            int j = 0;
//            while ((st = br.readLine()) != null) {
////                System.out.println(st);
//                if (st.equals("") || st == null) {
//                    break;
//                }
//                int flighNo = Integer.parseInt(st.split(",")[2]);
//                String[] str = st.split(",");
//                Booking booking = new Booking();
//                booking.name = str[0];
//                booking.flightNo = flighNo;
//                booking.id = str[1];
//                queues[j++] = booking;
//            }
//
//        } catch (Exception ex) {
////            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                BufferedReader br = null;
//                br.close();
//            } catch (IOException ex) {
////                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//    }

    
 
    //////////////////CANCELLATION///////////////////
    public static void cancelTicket() {
        System.out.println("Please enter your ID number : " );
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();
        int ind = -1;
        for (int i = 0; i < 20; i++) {
            if (bookings[i].id.equals(id)) {
                ind = i;
                System.out.println("Your booking has been cancelled !");
                break;
            }
        }
        if (ind != -1) {
            bookings[ind] = queues[0];
            deQueue();
            reloadFiles();
        }
    }
     
    ////////////////////////Dequeue///////////////
    public static void deQueue() {
        for (int i = 1; i < 40; i++) {
            queues[i - 1] = queues[i];
        }
    }
    
    ////////////////////UPDATE INFORMATION IN FILE/////////////
      public static void reloadFiles() {
         BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter("waitingListTmp.txt", true));
            for (Booking queue : queues) {
                if (queue != null) {
                    writer.append(queue.name + "," + queue.id + "," + queue.flightNo + "\n");
                }
            }
            writer.close();

        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            writer = new BufferedWriter(new FileWriter("FlightBookingTmp.txt", true));
            for (Booking queue : bookings) {
                if (queue != null) {
                    writer.append(queue.name + "," + queue.id + "," + queue.flightNo + "\n");
                }
            }
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }

        File bookingFile = new File("FlightBooking.txt");
        File queueFile = new File("waitingList.txt");
        bookingFile.delete();
        queueFile.delete();
        File bookingFileTmp = new File("FlightBookingTmp.txt");
        File queueFileTmp = new File("waitingListTmp.txt");
        bookingFileTmp.renameTo(bookingFile);
        queueFileTmp.renameTo(queueFile);

    
      }
     
      ///////////////////CONFIRMATION/////////////////
 public static void ticketStatus() {

        System.out.println("Please enter your ID (last 4 digit) number : " );
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();
        int ind = -1;
        for (int i = 0; i < 40; i++) {
            if (bookings[i] != null) {
                if (bookings[i].id.equals(id)) {
                    ind = i;
                    break;
                }
            }
        }
        if (ind != -1) {
            System.out.println("You ticket is confirmed");
        } else if (isQueued(id)) {
            System.out.println("Your ticket is Pending.");
        } else {
            System.out.println("No information is found");
        }
    }

}
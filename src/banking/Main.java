package banking;

import java.sql.*;
import java.util.*;
//import java.io.*;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception{
		//Initialize SQL Connection
		Connection conn;

		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost/JavaCustomer";
		String user = "root";
		String password = "";
		
		conn = DriverManager.getConnection(url, user, password);

        
        boolean input = true;
        while(input){

            System.out.println("Would you like to insert new customer? (yes/no/exit): ");
    		// Using Scanner for Getting Input from User
    		Scanner in = new Scanner(System.in);
            String ans = in.next();
            
            //If user write yes asking for customer infos and save into db
              if(ans.equals("yes")){
            	  try{
      				System.out.println("Firstname: ");
      				Scanner fnameinput = new Scanner(System.in);
      				String fname = fnameinput.nextLine();
      				System.out.println("Lastname: ");
      				Scanner lnameinput = new Scanner(System.in);
      				String lname = fnameinput.nextLine();
      				System.out.println("Balance: ");
      				Scanner balanceinput = new Scanner(System.in);
      				String balance = balanceinput.nextLine();

      				Statement s = conn.createStatement();
      				
      				String query = "INSERT INTO Customers (fname, lname, balance) VALUES ('" + fname + "', '" + lname + "', '" + balance + "');";
      			
      				s.executeUpdate(query);
      			}catch(Exception e) {
      				e.printStackTrace();
      			}
              }
              //If user write no then take all customers from db and start withdraw from their money
              if(ans.equals("no")){
            	  try {
      				Statement s = conn.createStatement();
      				
      				String query = "SELECT * FROM Customers";
      				
      				ResultSet result = s.executeQuery(query);
      						
      				while(result.next()) {
      					String fname = result.getString("fname");
      					String lname = result.getString("lname");
      					String balance = result.getString("balance");
      					int balanc=Integer.parseInt(balance);

      					ThreadTest person = new ThreadTest(new Customer(fname + " " + lname, balanc));
      					person.start();
      				}
      			}catch(Exception e) {
      				e.printStackTrace();
      			}
              }
              //Terminate the program
              if(ans.equals("exit")){
            	  try {
      				input = false;
            	  }catch(Exception e) {
      				e.printStackTrace();
      			}
              }
        }
	}
}

class Customer {
	public String name;
	public int balance;
	public Customer(String name, int balance) {
		this.name = name;
		this.balance = balance;
	}
}

class BankAccount{
	static BankAccount account;
	static Customer customer;
	
	public static BankAccount getAccount(Customer customer) {
		if(account == null) {
			account = new BankAccount();
		}
		BankAccount.customer = customer;
		return account;
	}

	public static int getBalance() {
		return customer.balance;
	}
	
	public synchronized void withdraw(int balan) {
		try {
			if(customer.balance >= balan) {
				System.out.println(customer.name + " requested "
									+ balan + " Euro");
				Thread.sleep(1000);
				customer.balance -= balan;
				System.out.println(customer.name + " took "
									+ balan + " Euro and now his balance is " + customer.balance);
			} else {
				System.out.println(customer.name + " asked for "
									+ balan + " Euro and his current balance is "
									+ customer.balance + " Cannot give this amount of money to him...");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

class ThreadTest extends Thread implements Runnable{
	Customer customer;
	public ThreadTest(Customer customer) {
		this.customer = customer;
	}
	
	public void run() {
		for(int i = 0; i <= 100; i++) {
			try {
				BankAccount account = BankAccount.getAccount(customer);
				Random rnd = new Random();
				int num = (rnd.nextInt(20)+1)*10; 
				account.withdraw(num);
				Thread.sleep(1000);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
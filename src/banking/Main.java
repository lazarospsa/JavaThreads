package banking;

//import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
//		Thread t1 = new Thread(new MyThread(), "Thread 1");
//		Thread t2 = new Thread(new MyThread(), "Thread 2");
//		Thread t3 = new Thread(new MyThread(), "Thread 3");
//		t1.start();
//		t2.start();
//		try {
//			t1.join();
//		}catch(InterruptedException e) {
//			e.printStackTrace();
//		}
//		t3.start();
		ThreadTest person1 = new ThreadTest(new Customer("Lazaros"));
		ThreadTest person2 = new ThreadTest(new Customer("Psarokostas"));
		person1.start();
		person2.start();
	}
}

class Customer {
	public String name;
	public Customer(String name) {
		this.name = name;
	}
}

class BankAccount{
	static BankAccount account;
	static int balance = 10000;
	static Customer customer;
	
	public static BankAccount getAccount(Customer customer) {
		if(account == null) {
			account = new BankAccount();
		}
		BankAccount.customer = customer;
		return account;
	}

	public static int getBalance() {
		return balance;
	}
	
	public synchronized void withdraw(int balan) {
		try {
			if(balance >= balan) {
				System.out.println(customer.name + " requested "
									+ balan + " Euro");
				Thread.sleep(1000);
				balance -= balan;
				System.out.println(customer.name + " took "
									+ balan + " Euro and now his balance is " + balance);
			} else {
				System.out.println(customer.name + " asked for "
									+ balan + " Euro and his current balance is "
									+ balance + " Cannot give this amount of money to him...");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
//		System.out.println(customer.name + " Current balance is " + balance + " Euro");
	}
}

class ThreadTest extends Thread implements Runnable{
	Customer customer;
	public ThreadTest(Customer customer) {
		this.customer = customer;
	}
	
	public void run() {
		for(int i = 0; i <= 10; i++) {
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

//class MyThread implements Runnable{
//
//	@Override
//	public void run() {
//		System.out.println("Active Threads: " + Thread.activeCount());
//		System.out.println("Start Thread: " + Thread.currentThread().getName());
//		try {
//			Thread.sleep(3000);
//		}catch(InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("End Thread: " + Thread.currentThread().getName());
//	}
//	
//}
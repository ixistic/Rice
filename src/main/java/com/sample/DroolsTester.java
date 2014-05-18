package com.sample;

import java.util.Scanner;

public class DroolsTester {
	public static void main(String[] args) {
		Drools drools = new Drools();
		Scanner s = new Scanner(System.in);
		while (true) {
			System.out.print("ถาม: ");
			String question = s.nextLine();
			System.out.println("ตอบ: " + drools.getAnswer( question ));
			System.out.println("-----------");
		}
	}
}

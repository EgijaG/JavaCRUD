package code;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

	File file;
	String path;
	Order order;
	static ArrayList<Order> orders = new ArrayList<Order>();

	FileReader(String path) {
		this.path = path;
		this.file = new File(path);
	}

	public void createOrderList() {
		int count = 0;
		if (checkFile(this.file)) {
			try {
				Scanner read = new Scanner(this.file, "UTF-8");
				while (read.hasNextLine()) {
					String line = read.nextLine();
					String[] lines = line.trim().split(",");
//				choosing just buy deals with euros, not playing around with crypto to crypto data yet
					if (lines[lines.length - 1].contains("EUR")) {
						order = new Order(lines[1], lines[3], lines[5], lines[6], lines[7], lines[8]);
						orders.add(order);
						count++;
					}

				}
				read.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("This file contains " + count + " lines.");
			System.out.println("Orders list has: " + orders.size() + " elements");
		}
	}

	public boolean checkFile(File f) {
		if (!f.exists()) {
			System.out.println("This file does not exist!");
			return false;
		}
		return true;
	}

}

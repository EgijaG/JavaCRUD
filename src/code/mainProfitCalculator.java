package code;

import java.sql.SQLException;

public class mainProfitCalculator {

	public static void main(String[] args) {
		/*
		 * file that is going to be used: filled order(trade) report from my personal
		 * account in Coinbase Pro. 
		 * The file is in project folder - can replace that with a yours.
		 * Have fun! :)
		 */
		FileReader fr1 = new FileReader("src/fills.csv");
//		making sure that connection is good
//		boolean isConnectionOk = DataBase.connect();
//		if (isConnectionOk) {
//			if the connection is working and if the database doesn't exist program creates a database
//			DataBase.createDataBase("Crypto");
			System.out.println("------------------------------------------");
//			Reading the file, retrieving data
			fr1.createOrderList();
			System.out.println("------------------------------------------");
//			after data is retrieved from csv file creating tables and inserting data.(CRUD)
			DataBase.createTables();
			System.out.println("------------------------------------------");
			DataBase.insertData(FileReader.orders);
			System.out.println("------------------------------------------");
			DataBase.insertProfitData();
			
//			closing the connections and statement, to free up resources
			try {
				DataBase.con.close();
				DataBase.stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

//	}

}

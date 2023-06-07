package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBase {
	// temporary credentials, need change when project started locally
	static final String DB_URL = "jdbc:mysql://localhost/";
	static final String USER = "user";
	static final String PASS = "pass";

	static Connection con;
	static Statement stmnt;
	static String extraParams = "?useSSL=false&autoReconnect=true&allowMultiQueries=true";
	static String dBname;
	static Connection con2;
	static double allProfitSum = 0;
	static double allInvestmentSum = 0;

	static boolean connect() {
		try {
			System.out.println("Creating connection...");
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Creating the requested statement...");
			stmnt = con.createStatement();
			String query = "SET GLOBAL max_connections = 300";
			stmnt.executeQuery(query);
			System.out.println("max_connections update done!");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	static void createDataBase(String name) {
		if (!name.matches("^[a-zA-Z]+$")) {
			System.out.println("Bad name for database. The query will not be processed.");
			return;
		}
		dBname = name;
		try {
			System.out.println("Creating the requested database...");
			String query = "CREATE DATABASE IF NOT EXISTS " + name + ";";
			stmnt.executeUpdate(query);
			System.out.println("Database " + name + " created successfully! You did it bro!");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void createTables() {
		try {
			System.out.println("Trying to create a tables...");
			for (String c : Order.currencies) {
				con = DriverManager.getConnection(DB_URL + dBname, USER, PASS);
				stmnt = con.createStatement();
				String q = "CREATE TABLE IF NOT EXISTS " + c
						+ "(trade_id INT PRIMARY KEY DEFAULT(1), side VARCHAR(4), size double DEFAULT(1.0), price double DEFAULT(1.0), fee double DEFAULT(1.0), total double DEFAULT (1.0));";
				stmnt.executeUpdate(q);
			}
			String q = "CREATE TABLE IF NOT EXISTS profits(id INT PRIMARY KEY AUTO_INCREMENT, currency VARCHAR(4) UNIQUE,profit_sum double DEFAULT(1.0));";
			stmnt.executeUpdate(q);
			System.out.println("Tables created succesfully!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void insertProfitData() {
		try {
			for (String c : Order.currencies) {
				String q2 = "SELECT  SUM(total) FROM " + c + ";";
				stmnt.executeQuery(q2);
				ResultSet rs = stmnt.getResultSet();
				double sum = 0;
				while (rs.next()) {
					sum = rs.getDouble("SUM(total)");
				}
				String q3 = "INSERT IGNORE INTO profits(currency ,profit_sum) values(?,?);";
				PreparedStatement ps = con.prepareStatement(q3);
				ps.setString(1, c.toUpperCase());
				ps.setDouble(2, sum);
				ps.executeUpdate();
				Profit prft = new Profit(c, sum);
				/*
				 * The sum is positive if the table contains equal amount of buys and sells. If
				 * only buys have happened, then the sell is in the waiting list and will not be
				 * shown in this fills report. The awaiting orders are not factual events, just
				 * hope or guess that the sell will happen at that point(product price will
				 * rise)
				 */
				if (sum < 0) {
					allInvestmentSum += prft.profitSum;
				} else
					allProfitSum += prft.profitSum;
				System.out.println(prft.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("------------------------------------------");
//		Printing the total sum of gain/loss
		System.out.println("Money that's invested in total and waiting for SELL orders: "
				+ Profit.df.format(allInvestmentSum) + "EUR");
		System.out.println(
				"All profits of that are calculated from this report: " + Profit.df.format(allProfitSum) + "EUR");
	}

	static void insertData(ArrayList<Order> orders) {
		int countRows = 0;
		try {
			for (Order o : FileReader.orders) {
				String q = "INSERT IGNORE INTO " + o.currency.toLowerCase()
						+ "(trade_id,size,side, price, fee, total) values (?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(q);
				ps.setInt(1, o.tradeId);
				ps.setDouble(2, o.size);
				ps.setString(3, o.side);
				ps.setDouble(4, o.price);
				ps.setDouble(5, o.fee);
				ps.setDouble(6, o.total);
				ps.executeUpdate();
				countRows++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Print affected rows in console, so it feels like a database
		if (countRows == 1) {
			System.out.println(countRows + " row affected.");
		} else
			System.out.println(countRows + " rows affected.");
	}

}

package code;

import java.util.HashSet;

public class Order {
	int tradeId;
	double size;
	double price;
	double fee;
	static HashSet<String> currencies = new HashSet<String>();
	double total;
	String currency;
	String side;

	Order(String tradeId, String side, String size, String currency, String price, String fee) {
		this.tradeId = Integer.parseInt(tradeId);
		this.side = side;
		this.size = Double.parseDouble(size);
		this.currency = currency;
		this.price = Double.parseDouble(price);
		this.fee = Double.parseDouble(fee);
		currencies.add(this.currency.toLowerCase());
		getTotal(this);
		System.out.println(currencies);

	}

	private void getTotal(Order order) {
		if (order.side.matches("^BUY$")) {
			order.total = -((order.size * order.price) + order.fee);
			return;
		}
		if (order.side.matches("^SELL$")) {
			order.total = (order.size * order.price) - order.fee;
		}
	}

	public String toString() {
		return "Filled order: " + this.currency + " Size: " + this.size + " Price: " + this.price;
	}
}

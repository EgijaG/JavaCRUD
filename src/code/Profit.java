package code;

import java.text.DecimalFormat;

public class Profit {
	double profitSum;
	static DecimalFormat df = new DecimalFormat("##.00");
	String currency;

	public Profit(String currency, double profitSum) {
		this.currency = currency;
		this.profitSum = profitSum;
	}

	public String toString() {
		if (profitSum < 0) {
			return "Investment: " + this.currency.toUpperCase() + " and waiting for SELL orders: " + df.format(profitSum)
					+ "EUR";
		}
		return "Profits of " + this.currency.toUpperCase() + " that are calculated from provided report: "
				+ df.format(profitSum) + "EUR";
	}
}

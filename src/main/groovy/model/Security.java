package model;

import java.util.List;

public class Security {
	private static final int historyLength = 2500;
	private String symbol;
	private List<SecurityQuote> history;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public List<SecurityQuote> getHistory() {
		return history;
	}
	public void setHistory(List<SecurityQuote> history) {
		this.history = history;
	}
	public static int getHistorylength() {
		return historyLength;
	}
	

}

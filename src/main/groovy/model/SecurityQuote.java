package model;

public class SecurityQuote {
	private long date;
	private double open;
	private double high;
	private double low;
	private double adjClose;
	private String dateAsString;
	
	public String getDateAsString() {
		return dateAsString;
	}
	public void setDateAsString(String dateAsString) {
		this.dateAsString = dateAsString;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getAdjClose() {
		return adjClose;
	}
	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}
	

}

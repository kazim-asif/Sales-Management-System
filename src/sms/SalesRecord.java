package sms;

public class SalesRecord {
	private String date;
	private String region;
	private double amount;

	// Constructor
	public SalesRecord(double amount, String date, String region) {
        this.amount = amount;
        this.date = date;
        this.region = region;
    }
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "SalesRecord [date=" + date + ", region=" + region + ", amount=" + amount + "]";
	}

}

package testcode;

public interface Stock {

	public String getTicker();

	public String getName();

	public long getShares();

	public void setShares(long shares);
	
	public void buyShares(long shares);

	public void sellShares(long shares);

	public long getCost();
	
	public void setCost(long cost);

	public long getPrice();

	public void setPrice(long price);

	public long getValue();

	public long getProfitLoss();
	
	public void update();
	
	public boolean isDataAvailable();
}

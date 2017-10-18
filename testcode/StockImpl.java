package testcode;

public class StockImpl implements Stock {

	private String ticker;
	private String name;
	private long shares;
	private long cost;
	private long price;
	private boolean dataAvailable;

	public StockImpl(String ticker, String name, long shares, long cost) {
		this.ticker = ticker.toUpperCase();
		this.name = name;
		this.shares = shares;
		this.cost = cost;
		this.price = -1;
		this.dataAvailable = false;
	}

	public StockImpl(String ticker, String name) {
		this(ticker, name, 0, 0);
	}

	public StockImpl(String ticker) {
		this(ticker, ticker);
	}
	
	public String getTicker() {
		return ticker;
	}

	public String getName() {
		return name;
	}

	public long getShares() {
		return shares;
	}
	
	public void setShares(long shares) {
		this.shares = shares;
	}

	public void buyShares(long shares) {
		this.shares += shares;
		this.cost += shares * price;
	}

	public void sellShares(long shares) {
		if (shares > this.shares)
			shares = this.shares;
		this.shares -= shares;
		this.cost -= shares * price;
	}

	public long getCost() {
		return cost;
	}
	
	public void setCost(long cost) {
		this.cost = cost;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getValue() {
		return shares * price;
	}

	public long getProfitLoss() {
		return getValue() - cost;
	}
	
	public void update() {
		IQuote quote = new Quote(false);
		try {
			quote.setValues(ticker);
			setPrice((long) (quote.getLatest() * 100));
			this.name = quote.getName();
			this.dataAvailable = true;
		} catch (Exception e) {
			setPrice(-1);
			this.dataAvailable = false;
		}
	}
	
	@Override
	public int hashCode() {
		return ticker.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != this.getClass())
			return false;
		Stock stock = (Stock) o;
		return this.ticker.equals(stock.getTicker());
	}
	
	@Override
	public String toString() {
		return ticker + " (" + name + ") x " + shares + " at cost " + cost;
	}

	@Override
	public boolean isDataAvailable() {
		return dataAvailable;
	}

}
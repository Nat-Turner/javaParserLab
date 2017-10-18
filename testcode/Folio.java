package testcode;

import java.util.List;
import java.util.Observer;

/**
 * Created by Chris on 11/11/2016.
 */
public interface Folio {
    
    public List<Stock> getStocks();
    
    public Stock getStock(String ticker);
    
    public boolean addStock(Stock s);
    
    public void buyStock(String ticker, long shares);

    public String getName();
    
    public long getValue();
    
    public long getProfitLoss();

    public void addObserver(Observer o);
    
	public void sellStock(Stock stock, long shares);
	
	public void updateStocks();
	
	public void updateObservers(String msg);
}

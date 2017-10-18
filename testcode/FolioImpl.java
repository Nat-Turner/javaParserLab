package testcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

/**
 * Created by Chris on 11/11/2016.
 */
public class FolioImpl extends Observable implements Folio {

	private String name;
	private FolioTracker ft;
	private List<Stock> stocks;

	public FolioImpl(String name, FolioTracker ft) {
		this.name = name;
		this.ft = ft;
		stocks = new ArrayList<>();
	}

	public List<Stock> getStocks() {
		return stocks;
	}
	
	public boolean addStock(Stock s) {
		return stocks.add(s);
	}

	public void buyStock(String ticker, long  shares) {
		Stock stock;
		try {
			stock = ft.getQuote(ticker);
			stock.setShares(shares);
			stock.setCost(shares * stock.getPrice());
			this.setChanged();
			for (Stock s : stocks) {
				if (s.equals(stock)) {
					s.setPrice(stock.getPrice());
					s.buyShares(stock.getShares());
					notifyObservers();
					return;
				}
			}
			stocks.add(stock);
			notifyObservers();
		} catch (WebsiteDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTickerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	@Override
	public long getValue() {
		long total = 0;
		Iterator<Stock> it = stocks.iterator();
		while (it.hasNext()) {
			total += it.next().getValue();
		}
		return total;
	}

	@Override
	public long getProfitLoss() {
		long total = 0;
		Iterator<Stock> it = stocks.iterator();
		while (it.hasNext()) {
			total += it.next().getProfitLoss();
		}
		return total;
	}

	public void sellStock(Stock stock, long shares) {
		for (Stock s : stocks) {
			if (s.equals(stock)) {
				s.sellShares(shares);

				if (s.getShares() == 0) {
					stocks.remove(s);
				}
				setChanged();
				notifyObservers();
				return;
			}
		}

	}

	public Stock getStock(String ticker) {
		for (Stock s : stocks) {
			if (s.getTicker().equals(ticker)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public void updateStocks() {
		updateObservers("updating");
		new Thread(new UpdateStocksRunnable(this)).start();
	}

	@Override
	public void updateObservers(String msg) {
		this.setChanged();
		this.notifyObservers(msg);
	}

}

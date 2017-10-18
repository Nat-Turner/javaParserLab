package testcode;

public class UpdateStocksRunnable implements Runnable {
	
	private Folio folio;
	
	public UpdateStocksRunnable(Folio f) {
		this.folio = f;
	}

	@Override
	public void run() {
		for (Stock s : folio.getStocks()) {
			s.update();
			folio.updateObservers("changed");
		}
		folio.updateObservers("updated");
	}

}

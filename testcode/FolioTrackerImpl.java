package testcode;

import java.io.IOException;
import java.util.List;
import java.util.Observable;

public class FolioTrackerImpl extends Observable implements FolioTracker {

	public List<Folio> folios;
	private FolioLoader loader;

	public FolioTrackerImpl() {
		loader = new FolioLoader(this);
		folios = loader.loadFolios();
	}

	public FolioLoader getFolioLoader(){
        return this.loader;
    }

	public List<Folio> getFolios() {
		return this.folios;
	}

	public boolean addFolio(Folio folio) {
		if (folios.add(folio)) {
			return true;
		}
		return false;
	}
	
	public Folio addNewFolio(String name) {
		Folio f = new FolioImpl(name, this);
		addFolio(f);
		setChanged();
		notifyObservers();
		return f;
	}

	public boolean removeFolio(Folio folio) {
		if (folios.remove(folio)) {
			return true;
		}
		return false;
	}

	public Stock getQuote(String ticker) throws WebsiteDataException, NoSuchTickerException, MethodException, IOException {
		Stock s = new StockImpl(ticker);
		s.update();
		return s;
	}

}

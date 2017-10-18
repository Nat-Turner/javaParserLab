package testcode;

import testcode.Folio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 11/11/2016.
 */
public class FolioLoader {

	private FolioTracker ft;
	private List<Folio> folios;

	public FolioLoader(FolioTracker ft) {
		this.ft = ft;
		folios = new ArrayList<>();

		try {
			getFolioFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readFile(String fileName) throws IOException {

		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);

		String newLine;

		String folioName = br.readLine().trim();
		Folio folio = new FolioImpl(folioName, ft);

		while ((newLine = br.readLine()) != null) {
			String[] splits = newLine.split(";");
			String ticker = splits[0];
			//String name = splits[1];
			int shares = Integer.parseInt(splits[1]);
			int cost = Integer.parseInt(splits[2]);
			//int price = Integer.parseInt(splits[4]);

            try {
                Stock stock = ft.getQuote(ticker);
                stock.setShares(shares);
                stock.setCost(cost);
                folio.addStock(stock);
            } catch (WebsiteDataException e) {
                e.printStackTrace();
                System.out.println("Stock " + ticker + " could not be loaded.");
            } catch (NoSuchTickerException e) {
                e.printStackTrace();
            } catch (MethodException e) {
                e.printStackTrace();
            }


		}

		folios.add(folio);
		br.close();
	}

	/*
	*   Currently requires that the name for the folio given in the first
	*   line of the folio text file is the same as the text files name
	*   Not optimal but I don't know a way around this
	 */

	public void saveFolioFiles(){

        for(Folio f: folios) {
            try{
                FileWriter fw = new FileWriter(f.getName() + ".txt");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(f.getName());
                bw.newLine();
                for(Stock s: f.getStocks()){
                    bw.write(s.getTicker() + ";" + s.getShares() + ";"  + s.getCost());
                    bw.newLine();
                }
                bw.close();
                fw.close();

            }
            catch(java.io.IOException e){

            }
        }

    }

	private void getFolioFiles() throws IOException {
		FileReader fread = new FileReader("folioList.txt");
		BufferedReader bread = new BufferedReader(fread);

		String newLine;

		while ((newLine = bread.readLine()) != null) {
			readFile(newLine);
		}

		bread.close();
	}

	public List<Folio> loadFolios() {
		return folios;
	}

}

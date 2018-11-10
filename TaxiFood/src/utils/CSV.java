package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import model.Localization;

public class CSV {

	public CSV(){}
	
    public ArrayList<Localization> readCSV() {

        String csvFile = "C:\\Users\\natha\\eclipse-workspace\\TaxiFood\\src\\origin.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<Localization>  localizations= new ArrayList<Localization>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] origin = line.split(cvsSplitBy);
                localizations.add(new Localization(new BigDecimal(origin[0]),new BigDecimal(origin[1])));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localizations;
    }

}
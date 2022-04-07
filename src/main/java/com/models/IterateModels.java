/**
 * 
 */
package com.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import com.models.dao.OptionList;
import com.models.dao.OptionQuote;
import com.models.dao.StockQuote;
import com.models.utils.DateUtils;
import com.models.utils.OptionUtils;
import com.opencsv.CSVReader;
// import com.opencsv.exceptions.CsvValidationException;

import jdk.nashorn.internal.runtime.SpillProperty;

/**
 * @author Muthu
 *
 */
public class IterateModels {

	static HashMap<String, OptionList> optionsHM = new HashMap<String, OptionList>();
	static SortedMap<String, StockQuote> stockQuotesHM = new TreeMap<String, StockQuote>();
	static HashMap<String, OptionQuote> optionQuotesHM = new HashMap<String, OptionQuote>();

	static String DATA_DIR = "/Users/Muthu/Development/OptionList4/IBdata";
	static String[] zipFilter = { "zip" };
	static String[] csvFilter = { "csv" };

	static String stock = "AAPL";
	static int pStartDate = 20220301;
	static int noDaysToLoad = 1;
	static int noDaysToModel = 4;
	static int noWeeks = 3;
	
    static DecimalFormat df2 = new DecimalFormat("#,###.##");


	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		long startTime = System.currentTimeMillis();

		Collection<File> allZipFiles = FileUtils.listFiles(new File(DATA_DIR), zipFilter, true);
		Collections.sort((List<File>) allZipFiles);

		ArrayList<File> zipFiles = new ArrayList<File>();

		for (File f : allZipFiles) {
			if (f.getName().startsWith(stock)) {

				int dateInt = Integer.parseInt(f.getName().replaceAll(stock, "").replaceAll(".zip", ""));
				// System.out.println("File: " + f.getName() + " " + dateInt );
				if ((dateInt >= pStartDate) & (zipFiles.size() < noDaysToLoad)) {
					zipFiles.add(f);
				}
			}
		}

		for (File f : zipFiles) {
			System.out.println("Files being processed: " + f.getName());
		}

		// 1. Unzip & load files
		LoadData.loadFiles(zipFiles, stockQuotesHM, optionQuotesHM, optionsHM);
		System.out.println("Duration: " + Math.round((System.currentTimeMillis() - startTime) / 1000));
		System.out.println("Stock Quote Size " + stockQuotesHM.size());
		System.out.println("Option QuoteSize " + optionQuotesHM.size());
		System.out.println("Option List Size " + optionsHM.size());

		// For each model
		int ctr = 0;
		for (Integer modelNo : Models.modelNumbers) {
			System.out.println("Processing model: " + modelNo);

			for (StockQuote openStock : stockQuotesHM.values()) {
				// get all contract_ids
				for (OptionList option : OptionUtils.getContractIds(openStock.time, noWeeks, optionsHM)) {

					OptionQuote openOption = optionQuotesHM.get(openStock.time + option.con_id);
					if (openOption == null) { continue; }
					
					ArrayList<Double> dAL = OptionUtils.getComputedComponents(openStock.time, openStock.ask, openOption.bid, option.strike, option.expiry);
					
					System.out.println(ctr + ": " + openStock);
					System.out.println(ctr + ": " + openOption);
					System.out.println(ctr + ": " + option);
					System.out.println(ctr + ": open_tv: " + df2.format(dAL.get(0)) + " open_iv: "  
							+ df2.format(dAL.get(1)) + " open_theta: "  + df2.format(dAL.get(2)) + "\n");
					if (ctr>100) break;
					ctr++;
				}
				break;
			}
			break; // model
		} // for loop in models
	} // main method


} // class

package com.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import com.models.dao.OptionList;
import com.models.dao.OptionQuote;
import com.models.dao.StockQuote;
import com.opencsv.CSVReader;

public class LoadData {

	static String[] zipFilter = { "zip" };
	static String[] csvFilter = { "csv" };


	private static void unzipFolder(Path source, Path target) throws IOException {

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

			// list files in zip
			ZipEntry zipEntry = zis.getNextEntry();

			while (zipEntry != null) {

				boolean isDirectory = false;
				// example 1.1
				// some zip stored files & folders separately
				// e.g data/
				// data/folder/
				// data/folder/file.txt
				if (zipEntry.getName().endsWith(File.separator)) {
					isDirectory = true;
				}

				Path newPath = zipSlipProtect(zipEntry, target);

				if (isDirectory) {
					Files.createDirectories(newPath);
				} else {

					// example 1.2
					// some zip stored file path only, need create parent
					// directories
					// e.g data/folder/file.txt
					if (newPath.getParent() != null) {
						if (Files.notExists(newPath.getParent())) {
							Files.createDirectories(newPath.getParent());
						}
					}

					// copy files, nio
					Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

					// copy files, classic
					/*
					 * try (FileOutputStream fos = new
					 * FileOutputStream(newPath.toFile())) { byte[] buffer = new
					 * byte[1024]; int len; while ((len = zis.read(buffer)) > 0)
					 * { fos.write(buffer, 0, len); } }
					 */
				}

				zipEntry = zis.getNextEntry();

			}
			zis.closeEntry();

		}

	}

	private static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {

		// test zip slip vulnerability
		// Path targetDirResolved = targetDir.resolve("../../" +
		// zipEntry.getName());

		Path targetDirResolved = targetDir.resolve(zipEntry.getName());

		// make sure normalized file still has targetDir as its prefix
		// else throws exception
		Path normalizePath = targetDirResolved.normalize();
		if (!normalizePath.startsWith(targetDir)) {
			throw new IOException("Bad zip entry: " + zipEntry.getName());
		}

		return normalizePath;
	}

	private static boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	public static void loadFiles(Collection<File> zipFiles, SortedMap<String, StockQuote> stockQuotesHM,
			HashMap<String, OptionQuote> optionQuotesHM, HashMap<String, OptionList> optionsHM) throws IOException {

		for (File zipFile : zipFiles) {
			Path tmpdir = Files.createTempDirectory("tmpDirPrefix");
			System.out.println("TEMP dir: " + tmpdir + " ZIP file: " + zipFile.getName());
			try {

				unzipFolder(zipFile.toPath(), tmpdir);
				Collection<File> csvFiles = FileUtils.listFiles(tmpdir.toFile(), csvFilter, true);
				for (File csvFile : csvFiles) {

					// System.out.println("\tCSV FILE: " + csvFile.getName());
					List<List<String>> csvRecords = loadCsvFile(csvFile);

					if (csvFile.getName().startsWith("oq_")) {
						for (List<String> csvRecord : csvRecords) {
							OptionQuote oq = new OptionQuote(csvRecord);
							// 20220111091512
							int time = Integer.parseInt(oq.time.substring(8, 12));
							if (time >= 930 && time <= 1600) {
								optionQuotesHM.put(oq.time + oq.con_id, oq);
							}
						}

					} else {
						if (csvFile.getName().startsWith("sq_")) {
							for (List<String> csvRecord : csvRecords) {
								StockQuote sq = new StockQuote(csvRecord);
								int time = Integer.parseInt(sq.time.substring(8, 12));
								if (time >= 930 && time <= 1600) {
									stockQuotesHM.put(sq.time, sq);
								}
							}

						} else if (csvFile.getName().startsWith("ol_")) {
							for (List<String> csvRecord : csvRecords) {
								OptionList ol = new OptionList(csvRecord);
								optionsHM.put(ol.con_id, ol);
							}

						} else {
							System.out.println("skipping UNKNOWN file: " + csvFile.getAbsoluteFile());
						}
						System.out.println(" Completed file: " + zipFile);
						csvFile.delete();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			deleteDirectory(tmpdir.toFile());
			// break;
		}
	}

	private static List<List<String>> loadCsvFile(File csvFile) {
		List<List<String>> records = new ArrayList<List<String>>();
		try (CSVReader csvReader = new CSVReader(new FileReader(csvFile));) {
			String[] values = null;
			// skip 1st row
			csvReader.readNext();
			while ((values = csvReader.readNext()) != null) {
				records.add(Arrays.asList(values));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// } catch (CsvValidationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return records;

	}

}

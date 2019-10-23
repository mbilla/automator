package org.automator.data.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {

	private Workbook wrkBook;
	private Sheet workSheet;
	private Map<String, String> mapFilter;

	public ExcelParser(String excelFile, String workSheet) throws InvalidFormatException, IOException {
		String fileExtension = FilenameUtils.getExtension(excelFile);
		File excelFilebj = new File(excelFile);
		if (fileExtension.equals("xlsx")) {
			this.wrkBook = new XSSFWorkbook(excelFilebj);
		} else {
			this.wrkBook = new HSSFWorkbook(new FileInputStream(excelFilebj));
		}
		this.workSheet = wrkBook.getSheet(workSheet);
		mapFilter = new HashMap<>();
	}

	public void addFilter(String columnName, String value) {
		mapFilter.put(columnName, value);
	}

	public List<Map<String, String>> getFullDataSet(int headerRow) {
		return getFilteredDataSet(headerRow, x -> true);
	}

	public List<Map<String, String>> getFilteredDataSet(int headerRow, Predicate<Map<String, String>> predicate) {
		List<Map<String, String>> lstMap = new ArrayList<>();
		for (int rowNum = headerRow + 1; rowNum < workSheet.getPhysicalNumberOfRows(); rowNum++) {
			Row row = workSheet.getRow(rowNum);
			Map<String, String> mapRow = new HashMap<>();
			for (int colNum = 0; colNum < row.getPhysicalNumberOfCells(); colNum++) {
				mapRow.put(workSheet.getRow(headerRow).getCell(colNum).getStringCellValue(),
						row.getCell(colNum).getStringCellValue());
			}
			if (predicate.test(mapRow)) {
				lstMap.add(mapRow);
			}
		}
		return lstMap;
	}

	public List<Map<String, String>> getFilteredDataSetAsList(int headerRow) {
		return getFilteredDataSet(headerRow, x -> {
			Set<String> keys = mapFilter.keySet();
			boolean toBeIncluded = true;
			for (String key : keys) {
				if (x.containsKey(key) && x.get(key).equals(mapFilter.get(key))) {
					toBeIncluded = true;
				} else {
					toBeIncluded = false;
					break;
				}
			}
			return toBeIncluded;
		});
	}

	public Object[][] getFilteredDataSet() {
		List<Map<String, String>> lstData = getFilteredDataSetAsList(0);
		Object[][] objData = new Object[lstData.size()][];
		for (int i = 0; i < lstData.size(); i++) {
			objData[i][0] = lstData.get(i);
		}
		return objData;
	}

	public Row getRow(int rowNum) {
		Row row = workSheet.getRow(rowNum);
		if (row != null) {
			return row;
		} else {
			throw new NullPointerException("Row is not present at index: " + rowNum);
		}
	}

	public int getColumnNumber(String columnName, int headerRow) {
		Row row = workSheet.getRow(headerRow);
		if (row != null) {
			for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
				if (row.getCell(i).getStringCellValue().equals(columnName)) {
					return i;
				}

			}
		} else {
			throw new NullPointerException("Header row is not present at index: " + headerRow);
		}
		return -1;
	}

}

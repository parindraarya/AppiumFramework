package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	static FileInputStream fin;
	static XSSFWorkbook ws;
	static XSSFSheet mySheet;
	static File file;

	/**
	 * 
	 * @param xFilePath
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public static Object[][] takeData(String xFilePath, String sheet) throws IOException {
		file = new File(xFilePath);

		fin = new FileInputStream(file);

		ws = new XSSFWorkbook(fin);

		mySheet = ws.getSheet(sheet);

		int rows = mySheet.getLastRowNum();
		XSSFRow row = mySheet.getRow(0);
		int columns = row.getLastCellNum();
		Object[][] excelData = new Object[rows][columns];
//		System.out.println("Row: " + rows + "Columns: " + columns);

		for (int i = 1; i <= rows; i++) {
			for (int j = 0; j < columns; j++) {
//				System.out.println(mySheet.getRow(i).getCell(j).getStringCellValue());
				excelData[i - 1][j] = mySheet.getRow(i).getCell(j).getStringCellValue();
			}
		}
		return excelData;
	}


}

package Amazon.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AmazonUtil {
	
	public static Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);
	
	public static Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
	
	public static Duration ELEMENT_WAIT = Duration.ofSeconds(15);
	
	public static Duration ELEMENT_WAIT_MID = Duration.ofSeconds(30);
	
	public static Duration ELEMENT_WAIT_LONG = Duration.ofSeconds(200);
	
	public static Duration FLUENT_WAIT = Duration.ofSeconds(30);
	
	public static Duration FLUENT_PULL = Duration.ofSeconds(10);
	
	
	public static String TESTDATA_SHEET_PATH = System.getProperty("user.dir")+"/src/main/java/AASTestData/ProductTestData.xlsx";
	
	static Workbook book;
	static Sheet sheet;
	
	//Get Test Data From Excel File
	public static Object[][] getTestData(String sheetName) {
		FileInputStream file = null;
		try {
			file = new FileInputStream(TESTDATA_SHEET_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			book = null;
			book = WorkbookFactory.create(file);
		}catch (IOException e) {
			e.printStackTrace();
		}
		sheet = null;
		sheet = book.getSheet(sheetName);
		Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];
		// System.out.println(sheet.getLastRowNum() + "--------" +
		// sheet.getRow(0).getLastCellNum());
		System.out.println("Check00 " + sheet.getLastRowNum());
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
				//data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
				//sheet.getRow(i + 1).getCell(k).setCellType(Cell.class);
				data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
				System.out.println(data[i][k]);
			}
			System.out.println("Check01");
		}
		System.out.println("Check02");
		return data;
	}
	
	//Get Test Data From JSON File
	/*public List<HashMap<String,String>> getJsonDataToMap(String jsonFile) throws IOException{
		String jsonContent = FileUtils.readFileToString(new File(System.getProperty("user.dir")+"/src/main/java/AASTestData/"+jsonFile+".json"),
				StandardCharsets.UTF_8);
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String,String>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String,String>>>(){
			
		});
		return data;
	}*/


}

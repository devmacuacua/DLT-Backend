package dlt.dltbackendmaster.reports;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Classe responsável pela formatacao de documentos excel gerados nos relatorios
 * diversos
 * 
 * @author Francisco da Conceicao Alberto Macuacua
 *
 */
public class ExcelDocumentFormatting {
	private String documentPath;

	public ExcelDocumentFormatting(String documentPath) {
		this.documentPath = documentPath;
	}

	public void execute() {
		try (FileInputStream fileIn = new FileInputStream(documentPath); Workbook workbook = new XSSFWorkbook(fileIn)) {

			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 0; i < 5; i++) {
				formatRow(sheet, i, true);
			}

			try (FileOutputStream fileOut = new FileOutputStream(documentPath)) {
				workbook.write(fileOut);
				System.out.println("Excel file has been formatted successfully!");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void formatRow(Sheet sheet, int rowIndex, boolean bold) {
		Row row = sheet.getRow(rowIndex);
		if (row != null) {
			Font font = sheet.getWorkbook().createFont();
			font.setBold(bold);
		}
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
}

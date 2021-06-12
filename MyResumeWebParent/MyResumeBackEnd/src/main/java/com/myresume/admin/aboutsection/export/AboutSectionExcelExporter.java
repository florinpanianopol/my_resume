package com.myresume.admin.aboutsection.export;

import com.myresume.common.entity.AboutSection;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AboutSectionExcelExporter extends AbstractExporter {
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public AboutSectionExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
		sheet = workbook.createSheet("About_Section");
		XSSFRow row = sheet.createRow(0);
			
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		createCell(row, 0, "Id", cellStyle);
		createCell(row, 1, "Name", cellStyle);
		createCell(row, 2, "Header", cellStyle);
		createCell(row, 4, "Current Job", cellStyle);
		createCell(row, 5, "Short Description", cellStyle);
		createCell(row, 6, "Website", cellStyle);
		createCell(row, 7, "City", cellStyle);
		createCell(row, 8, "Degree", cellStyle);
		createCell(row, 9, "Email", cellStyle);
		createCell(row, 10, "Footer", cellStyle);
		createCell(row, 11, "Enabled", cellStyle);
		
	}
	
private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		
		cell.setCellStyle(style);
		
	}

	public void export(List<AboutSection> listAboutsSection, HttpServletResponse response) throws IOException {
	
		super.setResponseHeader(response, "application/octet-stream", ".xlsx");
		writeHeaderLine();
		writeDataLines(listAboutsSection);
		
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
}
	
	private void writeDataLines(List<AboutSection> listAboutsSection) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		for(AboutSection aboutSection : listAboutsSection) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;

			createCell(row, columnIndex++, aboutSection.getId(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getName(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getHeader(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getCurrentJob(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getShortDesc().replaceAll("\\<.*?\\>", "").replace("&nbsp;", ""),cellStyle);
			createCell(row, columnIndex++, aboutSection.getWebSite(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getCity(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getDegree(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getEmail(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getFooter(),cellStyle);
			createCell(row, columnIndex++, aboutSection.getCurrInd(),cellStyle);
		}
	}
}

package com.myresume.admin.section;

import java.util.List;
import java.awt.Color;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.myresume.common.entity.AboutSection;

public class AboutSectionPDFExporter extends AbstractExporter{
	
	public void export(List<AboutSection> listAboutSections, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(14);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("About Section Records", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(12);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {2.5f,3.5f,3.5f,4.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f,3.5f}); //trebuie umblat la latimi ca sa fie documentul lizibil
		//alta data
		
		writeTableHeader(table);
		writeTableData(table,listAboutSections);
		document.add(table);
		
		
		document.close();
	}

	private void writeTableData(PdfPTable table, List<AboutSection> listAboutSections) {
		
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setSize(9);
		
		
		for(AboutSection aboutSection: listAboutSections) {
			table.addCell(new PdfPCell(new Phrase(aboutSection.getId().toString(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getName(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getHeader(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getSubHeader(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getCurrentJob(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getShortDesc(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getWebSite(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getCity(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getDegree(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getEmail(),font)));
			table.addCell(new PdfPCell(new Phrase(aboutSection.getFooter(),font)));
			table.addCell(new PdfPCell(new Phrase(String.valueOf(aboutSection.getCurrInd()),font)));

			
		}
		
		}
		
	

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setSize(10);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("ID",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Header",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("SubHeader",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("CurrentJob",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Short Description",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Website",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("City",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Degree",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Footer",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled",font));
		table.addCell(cell);
		
	}
}

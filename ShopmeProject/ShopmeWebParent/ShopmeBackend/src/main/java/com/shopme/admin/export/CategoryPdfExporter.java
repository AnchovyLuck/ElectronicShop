package com.shopme.admin.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryPdfExporter extends AbstractExporter {
	
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setReponseHeader(response, "application/pdf", ".pdf", "categories_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(new Color (13,110,253));
		
		Paragraph paragraph = new Paragraph("List of Users", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.0f, 3.5f});
		
		writeTableHeader(table);
		writeTableData(table, listCategories);
		
		document.add(table);
		
		document.close();
	}

	private void writeTableData(PdfPTable table, List<Category> listCategories) {
		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.BLACK);
		
		for (Category cat : listCategories) {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPhrase(new Phrase(String.valueOf(cat.getId()), font));
			table.addCell(cell);
			cell.setPhrase(new Phrase(String.valueOf(cat.getName()), font));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(new Color (13,110,253));
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("Category ID", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Category Name", font));
		table.addCell(cell);
	}
}

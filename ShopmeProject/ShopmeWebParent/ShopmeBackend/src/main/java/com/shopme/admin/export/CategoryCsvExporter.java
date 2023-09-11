package com.shopme.admin.export;

import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.Category;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCsvExporter extends AbstractExporter {

	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setReponseHeader(response, "text/csv", ".csv", "categories_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Category ID", "Category Name"};
		String[] fieldMapping = { "id", "name"};

		csvWriter.writeHeader(csvHeader);

		listCategories.forEach(cat -> {
			cat.setName(cat.getName().replace("--", "  "));
			try {
				csvWriter.write(cat, fieldMapping);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		csvWriter.close();
	}
}

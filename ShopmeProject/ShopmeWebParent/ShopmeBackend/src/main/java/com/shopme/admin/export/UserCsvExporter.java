package com.shopme.admin.export;

import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstractExporter {

	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setReponseHeader(response, "text/csv", ".csv", "users_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled" };
		String[] fieldMapping = { "id", "email", "firstName", "lastName", "roles", "enabled" };

		csvWriter.writeHeader(csvHeader);

		listUsers.forEach(user -> {
			try {
				csvWriter.write(user, fieldMapping);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		csvWriter.close();
	}
}

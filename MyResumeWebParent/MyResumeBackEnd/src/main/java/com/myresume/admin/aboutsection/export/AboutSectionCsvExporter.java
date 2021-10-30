package com.myresume.admin.aboutsection.export;

import com.myresume.common.entity.AboutSection;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AboutSectionCsvExporter extends AbstractExporter {
	public void export(List<AboutSection> listAboutsSection, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response,  "text/csv",".csv");
				
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"Id","Name","Current Job","Short Description","WebSite","City","Degree","Email","Enabled"};
		String[] fieldMapping = {"id","name","currentJob","shortDesc","webSite","city","degree","email","currInd"};
		
		csvWriter.writeHeader(csvHeader);

		for (AboutSection section : listAboutsSection) {
			section.setShortDesc(section.getShortDesc().replaceAll("\\<.*?\\>", "").replace("&nbsp;", ""));
		}
		for(AboutSection aboutSection: listAboutsSection ) {
			csvWriter.write(aboutSection, fieldMapping);
		}
		
		csvWriter.close();
		
	}

}

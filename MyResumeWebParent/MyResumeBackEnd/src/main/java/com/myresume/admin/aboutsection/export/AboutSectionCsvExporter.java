package com.myresume.admin.aboutsection.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.myresume.common.entity.AboutSection;

public class AboutSectionCsvExporter extends AbstractExporter {
	public void export(List<AboutSection> listAboutsSection, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response,  "text/csv",".csv");
				
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"Id","Name","Header","SubHeader","Current Job","Short Description","WebSite","City","Degree","Email","Footer","Enabled"};
		String[] fieldMapping = {"id","name","header","subHeader","currentJob","shortDesc","webSite","city","degree","email","footer","currInd"};
		
		csvWriter.writeHeader(csvHeader);
		
		for(AboutSection aboutSection: listAboutsSection ) {
			csvWriter.write(aboutSection, fieldMapping);
		}
		
		csvWriter.close();
		
	}

}

package com.example.demo_rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestReports {
    public static void main(String[] args) throws JRException, IOException {
            DataBean bean = new DataBean("name", "Tykhovetskyi Yaroslav", LocalDate.now());

            BeanService service = new BeanService();
            service.saveToPdf(service.createJasperPrint(bean));
        }
    }

    class BeanService{
        private static final String TEMPLATE_PATH = "/pdf-reports/half_a4_specific_font.jrxml";
//        private static final String TEMPLATE_PATH = "/pdf-reports/half_a4_needed_font.jrxml";
//        private static final String TEMPLATE_PATH = "/pdf-reports/a4_horizontal_specific_font_template.jrxml";
//        private static final String TEMPLATE_PATH = "/pdf-reports/a4_horizontal_needed_font_template.jrxml";

        private Map<String, Object> getParameters(DataBean bean) throws IOException {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("BEAN", bean);
            return parameters;
        }

        public void saveToPdf(JasperPrint print) throws JRException {
            String exportPath = "C:/Users/yarik/Desktop/demo_rest/src/main/resources/pdf-reports/output/test_output.pdf";
            JasperExportManager.exportReportToPdfFile(print, exportPath);
        }

        public JasperPrint createJasperPrint(DataBean bean) throws JRException, IOException {
            try (InputStream inputStream = new FileInputStream(getRealFilePath(TEMPLATE_PATH))) {
                final JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                return JasperFillManager.fillReport(jasperReport, getParameters(bean), getDataSource(bean));
            }
        }

        private String getRealFilePath(final String path) throws IOException{
            Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
            return resourcePath.toFile().getAbsolutePath();
        }

        private JRDataSource getDataSource(DataBean bean){
            return new JRBeanCollectionDataSource(Collections.singleton(bean));
        }
    }

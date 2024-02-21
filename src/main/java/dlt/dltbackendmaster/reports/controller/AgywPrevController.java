package dlt.dltbackendmaster.reports.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dlt.dltbackendmaster.reports.AgywPrevReport;
import dlt.dltbackendmaster.reports.domain.NewlyEnrolledAgywAndServices;
import dlt.dltbackendmaster.reports.domain.ReportResponse;
import dlt.dltbackendmaster.reports.domain.ResultObject;
import dlt.dltbackendmaster.reports.domain.SummaryNewlyEnrolledAgywAndServices;
import dlt.dltbackendmaster.service.DAOService;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * Controller resposável pela comunicação dos dados do relatório
 * 
 * @author Hamilton Mutaquiha
 *
 */
@RestController
@RequestMapping("/api/agyw-prev")
public class AgywPrevController {
	private static final String REPORTS_HOME = System.getProperty("user.dir") + "/webapps/reports";

	private static final String NEW_ENROLLED_REPORT_TEMPLATE = "/reports/NewEnrolledReportTemplateLandscape.jrxml";
	private static final String NEW_ENROLLED_REPORT_NAME = "DLT2.0_NOVAS_RAMJ_VULNERABILIDADES_E_SERVICOS_POR";
	private static final String NEW_ENROLLED_SUMMARY_REPORT_TEMPLATE = "/reports/SummaryNewEnrolledReportTemplateLandscape.jrxml";
	private static final String NEW_ENROLLED_SUMMARY_REPORT_NAME = "DLT2.0_RESUMO_NOVAS_RAMJ_VULNERABILIDADES_E_SERVICOS_POR";

	private static final String VULNERABILITIES_AND_SERVICES_REPORT_TEMPLATE = "/reports/BeneficiariesVulnerabilitiesAndServicesReportTemplateLandscape.jrxml";
	private static final String VULNERABILITIES_AND_SERVICES_REPORT_NAME = "DLT2.0_BENEFICIARIAS_VULNERABILIDADES_E_SERVICOS_POR";
	private static final String VULNERABILITIES_AND_SERVICES_SUMMARY_REPORT_TEMPLATE = "/reports/SummaryBeneficiariesVulnerabilitiesAndServicesReportTemplateLandscape.jrxml";
	private static final String VULNERABILITIES_AND_SERVICES_SUMMARY_REPORT_NAME = "DLT2.0_BENEFICIARIAS_VULNERABILIDADES_E_SERVICOS_RESUMO_POR";

	private final DAOService service;

	@Autowired
	public AgywPrevController(DAOService service) {
		this.service = service;
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<Map<Integer, Map<String, ResultObject>>> get(
			@RequestParam(name = "districts") Integer[] districts, @RequestParam(name = "startDate") String startDate,
			@RequestParam(name = "endDate") String endDate) {

		AgywPrevReport report = new AgywPrevReport(service);

		try {
			Map<Integer, Map<String, ResultObject>> reportObject = report.getAgywPrevResultObject(districts, startDate,
					endDate);

			return new ResponseEntity<>(reportObject, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(produces = "application/json", path = "/countNewlyEnrolledAgywAndServices")
	public ResponseEntity<List<Object>> countNewlyEnrolledAgywAndServices(
			@RequestParam(name = "districts") Integer[] districts, @RequestParam(name = "startDate") Long startDate,
			@RequestParam(name = "endDate") Long endDate) {

		AgywPrevReport report = new AgywPrevReport(service);

		try {
			List<Object> reportObject = report.countNewlyEnrolledAgywAndServices(districts, new Date(startDate),
					new Date(endDate));

			return new ResponseEntity<>(reportObject, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public static <T> List<List<T>> splitList(List<T> originalList, int chunkSize) {
		List<List<T>> sublists = new ArrayList<>();
		for (int i = 0; i < originalList.size(); i += chunkSize) {
			int end = Math.min(originalList.size(), i + chunkSize);
			sublists.add(originalList.subList(i, end));
		}
		return sublists;
	}

	public static String serializeToJson(List<NewlyEnrolledAgywAndServices> objects) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(objects);

			// Remove trailing commas
			jsonString = removeTrailingComma(jsonString);

			return jsonString;
		} catch (Exception e) {
			e.printStackTrace();
			return "[]"; // Return an empty array on failure
		}
	}

	private static String removeTrailingComma(String jsonString) {
		// Remove trailing comma within arrays
		jsonString = jsonString.replaceAll(",\\s*]", "]");
		// Remove trailing comma within objects
		jsonString = jsonString.replaceAll(",\\s*}", "}");

		return jsonString;
	}

	public void createDirectory(String directoryPath) {
		// Create a Path object for the directory
		Path dirPath = Paths.get(directoryPath);

		try {
			// Create the directory if it does not exist
			if (!Files.exists(dirPath)) {
				Files.createDirectories(dirPath);
				System.out.println("Directory created successfully.");
			} else {
				System.out.println("Directory already exists.");
			}
		} catch (IOException e) {
			System.err.println("Error creating the directory: " + e.getMessage());
		}
	}

	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam(name = "filePath") String filePath) throws IOException {
		File file = new File(filePath);
		Resource resource = new FileSystemResource(file);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=" + file.getName());

		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@GetMapping(produces = "application/json", path = "/getNewlyEnrolledAgywAndServicesSummary")
	public ResponseEntity<String> getNewlyEnrolledAgywAndServicesSummary(
			@RequestParam(name = "province") String province, @RequestParam(name = "districts") Integer[] districts,
			@RequestParam(name = "startDate") Long startDate, @RequestParam(name = "endDate") Long endDate,
			@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "nextIndex") int nextIndex,
			@RequestParam(name = "username") String username) {
		String generatedReportResponse;

		Date initialDate = new Date(startDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedInitialDate = sdf.format(initialDate);

		Date finalDate = new Date(endDate);
		SimpleDateFormat sdfFinal = new SimpleDateFormat("yyyy-MM-dd");
		String formattedFinalDate = sdfFinal.format(finalDate);

		createDirectory(REPORTS_HOME + "/" + username);

		String generatedFilePath = REPORTS_HOME + "/" + username + "/" + NEW_ENROLLED_SUMMARY_REPORT_NAME + "_"
				+ province.toUpperCase() + "_" + formattedInitialDate + "_" + formattedFinalDate + "_" + pageNumber
				+ "_" + ".xlsx";

		List<SummaryNewlyEnrolledAgywAndServices> rows = new ArrayList<>();

		AgywPrevReport report = new AgywPrevReport(service);

		List<Object> reportObjectList = report.getNewlyEnrolledAgywAndServicesSummary(districts, new Date(startDate),
				new Date(endDate));
		Object[][] reportObjectArray = reportObjectList.toArray(new Object[0][0]);

		try {
			for (Object[] obj : reportObjectArray) {
				rows.add(new SummaryNewlyEnrolledAgywAndServices(nextIndex + "",
						String.valueOf(obj[0] != null ? obj[0] : ""), String.valueOf(obj[1] != null ? obj[1] : ""),
						String.valueOf(obj[2] != null ? obj[2] : ""), String.valueOf(obj[3] != null ? obj[3] : ""),
						String.valueOf(obj[4] != null ? obj[4] : ""), String.valueOf(obj[5] != null ? obj[5] : ""),
						String.valueOf(obj[6] != null ? obj[6] : ""), String.valueOf(obj[7] != null ? obj[7] : ""),
						String.valueOf(obj[8] != null ? obj[8] : ""), String.valueOf(obj[9] != null ? obj[9] : ""),
						String.valueOf(obj[10] != null ? obj[10] : ""), String.valueOf(obj[11] != null ? obj[11] : ""),
						String.valueOf(obj[12] != null ? obj[12] : ""), String.valueOf(obj[13] != null ? obj[13] : ""),
						String.valueOf(obj[14] != null ? obj[14] : ""), String.valueOf(obj[15] != null ? obj[15] : ""),
						String.valueOf(obj[16] != null ? obj[16] : ""), String.valueOf(obj[17] != null ? obj[17] : ""),
						String.valueOf(obj[18] != null ? obj[18] : ""), String.valueOf(obj[19] != null ? obj[19] : ""),
						String.valueOf(obj[20] != null ? obj[20] : ""), String.valueOf(obj[21] != null ? obj[21] : ""),
						String.valueOf(obj[22] != null ? obj[22] : ""), String.valueOf(obj[23] != null ? obj[23] : ""),
						String.valueOf(obj[24] != null ? obj[24] : ""), String.valueOf(obj[25] != null ? obj[25] : ""),
						String.valueOf(obj[26] != null ? obj[26] : ""), String.valueOf(obj[27] != null ? obj[27] : ""),
						String.valueOf(obj[28] != null ? obj[28] : ""), String.valueOf(obj[29] != null ? obj[29] : ""),
						String.valueOf(obj[30] != null ? obj[30] : ""), String.valueOf(obj[31] != null ? obj[31] : ""),
						String.valueOf(obj[32] != null ? obj[32] : ""), String.valueOf(obj[33] != null ? obj[33] : ""),
						String.valueOf(obj[34] != null ? obj[34] : ""), String.valueOf(obj[35] != null ? obj[35] : ""),
						String.valueOf(obj[36] != null ? obj[36] : "")));
				nextIndex++;
			}

			// Compile the .jrxml template to a .jasper file
			InputStream jrxmlStream = AgywPrevController.class
					.getResourceAsStream(NEW_ENROLLED_SUMMARY_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

			if (rows.size() > 0) {
				// Convert data to a JRBeanCollectionDataSource
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);

				// Create a Map to store report parameters
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("date_start", formattedInitialDate);
				parameters.put("date_end", formattedFinalDate);
				parameters.put("slab", "Data de Início:");
				parameters.put("elab", "Data de Fim:");

				// Generate the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

				// Apply the configuration to the exporter
				JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();

				// Export the report to XLSX
				JRXlsxExporter exporter = new JRXlsxExporter(jasperReportsContext);
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(generatedFilePath));

				// Set your preferred column width (in pixels)
				exporter.setConfiguration(getXlsxExporterConfiguration());

				exporter.exportReport();
			}

			ObjectMapper objectMapper = new ObjectMapper();
			generatedReportResponse = objectMapper
					.writeValueAsString(new ReportResponse(generatedFilePath, rows.size(), nextIndex));

			System.out.println(generatedFilePath + ": generated and exported to XLSX with borders successfully.");

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(generatedReportResponse, HttpStatus.OK);
	}

	@GetMapping(produces = "application/json", path = "/countBeneficiariesVulnerabilitiesAndServices")
	public ResponseEntity<List<Object>> countBeneficiariesVulnerabilitiesAndServices(
			@RequestParam(name = "districts") Integer[] districts, @RequestParam(name = "startDate") Long startDate,
			@RequestParam(name = "endDate") Long endDate) {

		AgywPrevReport report = new AgywPrevReport(service);

		try {
			List<Object> reportObject = report.countBeneficiariesVulnerabilitiesAndServices(districts,
					new Date(startDate), new Date(endDate));

			return new ResponseEntity<>(reportObject, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/getBeneficiariesVulnerabilitiesAndServices")
	public ResponseEntity<String> getBeneficiariesVulnerabilitiesAndServices(
			@RequestParam(name = "province") String province, @RequestParam(name = "districts") Integer[] districts,
			@RequestParam(name = "startDate") Long startDate, @RequestParam(name = "endDate") Long endDate,
			@RequestParam(name = "pageIndex") int pageIndex, @RequestParam(name = "pageSize") int pageSize,
			@RequestParam(name = "username") String username) throws IOException {

		AgywPrevReport report = new AgywPrevReport(service);

		Date initialDate = new Date(startDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedInitialDate = sdf.format(initialDate);

		Date finalDate = new Date(endDate);
		SimpleDateFormat sdfFinal = new SimpleDateFormat("yyyy-MM-dd");
		String formattedFinalDate = sdfFinal.format(finalDate);

		createDirectory(REPORTS_HOME + "/" + username);

		String generatedFilePath = REPORTS_HOME + "/" + username + "/" + VULNERABILITIES_AND_SERVICES_REPORT_NAME + "_"
				+ province.toUpperCase() + "_" + formattedInitialDate + "_" + formattedFinalDate + "_" + pageIndex + "_"
				+ ".xlsx";

		List<NewlyEnrolledAgywAndServices> rows = new ArrayList<>();

		List<Object> reportObjectList = report.getBeneficiariesVulnerabilitiesAndServices(districts,
				new Date(startDate), new Date(endDate), pageIndex, pageSize);
		Object[][] reportObjectArray = reportObjectList.toArray(new Object[0][0]);

		int i = 1;
		try {
			for (Object[] obj : reportObjectArray) {
				rows.add(new NewlyEnrolledAgywAndServices(i + "", String.valueOf(obj[0]), String.valueOf(obj[1]),
						String.valueOf(obj[2]), String.valueOf(obj[3]), String.valueOf(obj[4]), String.valueOf(obj[5]),
						String.valueOf(obj[6]), String.valueOf(obj[7]), String.valueOf(obj[8] != null ? obj[8] : ""),
						String.valueOf(obj[9] != null ? obj[9] : ""), String.valueOf(obj[10]), String.valueOf(obj[11]),
						String.valueOf(obj[12]), String.valueOf(obj[13]), String.valueOf(obj[14]),
						String.valueOf(obj[15]), String.valueOf(obj[16]), String.valueOf(obj[17]),
						String.valueOf(obj[18]), String.valueOf(obj[19]), String.valueOf(obj[20]),
						String.valueOf(obj[21]), String.valueOf(obj[22] != null ? obj[22] : ""),
						String.valueOf(obj[23] != null ? obj[23] : ""), String.valueOf(obj[24]),
						String.valueOf(obj[25] != null ? obj[25] : ""), String.valueOf(obj[26]),
						String.valueOf(obj[27]), String.valueOf(obj[28]), String.valueOf(obj[29]),
						String.valueOf(obj[30]), String.valueOf(obj[31]), String.valueOf(obj[32]),
						String.valueOf(obj[33]), String.valueOf(obj[34]), String.valueOf(obj[35]),
						String.valueOf(obj[36] != null ? obj[36] : ""), String.valueOf(obj[37] != null ? obj[37] : ""),
						String.valueOf(obj[38] != null ? obj[38] : ""), String.valueOf(obj[39])));
				i++;
			}

			// Compile the .jrxml template to a .jasper file
			InputStream jrxmlStream = AgywPrevController.class
					.getResourceAsStream(VULNERABILITIES_AND_SERVICES_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

			// Convert data to a JRBeanCollectionDataSource
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);

			// Create a Map to store report parameters
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("date_start", formattedInitialDate);
			parameters.put("date_end", formattedFinalDate);
			parameters.put("slab", "Data de Início:");
			parameters.put("elab", "Data de Fim:");

			// Generate the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			// Apply the configuration to the exporter
			JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();

			// Export the report to XLSX
			JRXlsxExporter exporter = new JRXlsxExporter(jasperReportsContext);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(generatedFilePath));

			// Set your preferred column width (in pixels)
			exporter.setConfiguration(getXlsxExporterConfiguration());

			exporter.exportReport();

			System.out.println(generatedFilePath + ": generated and exported to XLSX with borders successfully.");

		} catch (JRException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(generatedFilePath, HttpStatus.OK);
	}

	@GetMapping(produces = "application/json", path = "/getBeneficiariesVulnerabilitiesAndServicesSummary")
	public ResponseEntity<String> getBeneficiariesVulnerabilitiesAndServicesSummary(
			@RequestParam(name = "province") String province, @RequestParam(name = "districts") Integer[] districts,
			@RequestParam(name = "startDate") Long startDate, @RequestParam(name = "endDate") Long endDate,
			@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "nextIndex") int nextIndex,
			@RequestParam(name = "username") String username) {
		String generatedReportResponse;

		Date initialDate = new Date(startDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedInitialDate = sdf.format(initialDate);

		Date finalDate = new Date(endDate);
		SimpleDateFormat sdfFinal = new SimpleDateFormat("yyyy-MM-dd");
		String formattedFinalDate = sdfFinal.format(finalDate);

		createDirectory(REPORTS_HOME + "/" + username);

		String generatedFilePath = REPORTS_HOME + "/" + username + "/"
				+ VULNERABILITIES_AND_SERVICES_SUMMARY_REPORT_NAME + "_" + province.toUpperCase() + "_"
				+ formattedInitialDate + "_" + formattedFinalDate + "_" + pageNumber + "_" + ".xlsx";

		List<SummaryNewlyEnrolledAgywAndServices> rows = new ArrayList<>();

		AgywPrevReport report = new AgywPrevReport(service);

		List<Object> reportObjectList = report.getBeneficiariesVulnerabilitiesAndServicesSummary(districts,
				new Date(startDate), new Date(endDate));
		Object[][] reportObjectArray = reportObjectList.toArray(new Object[0][0]);

		try {
			for (Object[] obj : reportObjectArray) {
				rows.add(new SummaryNewlyEnrolledAgywAndServices(nextIndex + "",
						String.valueOf(obj[0] != null ? obj[0] : ""), String.valueOf(obj[1] != null ? obj[1] : ""),
						String.valueOf(obj[2] != null ? obj[2] : ""), String.valueOf(obj[3] != null ? obj[3] : ""),
						String.valueOf(obj[4] != null ? obj[4] : ""), String.valueOf(obj[5] != null ? obj[5] : ""),
						String.valueOf(obj[6] != null ? obj[6] : ""), String.valueOf(obj[7] != null ? obj[7] : ""),
						String.valueOf(obj[8] != null ? obj[8] : ""), String.valueOf(obj[9] != null ? obj[9] : ""),
						String.valueOf(obj[10] != null ? obj[10] : ""), String.valueOf(obj[11] != null ? obj[11] : ""),
						String.valueOf(obj[12] != null ? obj[12] : ""), String.valueOf(obj[13] != null ? obj[13] : ""),
						String.valueOf(obj[14] != null ? obj[14] : ""), String.valueOf(obj[15] != null ? obj[15] : ""),
						String.valueOf(obj[16] != null ? obj[16] : ""), String.valueOf(obj[17] != null ? obj[17] : ""),
						String.valueOf(obj[18] != null ? obj[18] : ""), String.valueOf(obj[19] != null ? obj[19] : ""),
						String.valueOf(obj[20] != null ? obj[20] : ""), String.valueOf(obj[21] != null ? obj[21] : ""),
						String.valueOf(obj[22] != null ? obj[22] : ""), String.valueOf(obj[23] != null ? obj[23] : ""),
						String.valueOf(obj[24] != null ? obj[24] : ""), String.valueOf(obj[25] != null ? obj[25] : ""),
						String.valueOf(obj[26] != null ? obj[26] : ""), String.valueOf(obj[27] != null ? obj[27] : ""),
						String.valueOf(obj[28] != null ? obj[28] : ""), String.valueOf(obj[29] != null ? obj[29] : ""),
						String.valueOf(obj[30] != null ? obj[30] : ""), String.valueOf(obj[31] != null ? obj[31] : ""),
						String.valueOf(obj[32] != null ? obj[32] : ""), String.valueOf(obj[33] != null ? obj[33] : ""),
						String.valueOf(obj[34] != null ? obj[34] : ""), String.valueOf(obj[35] != null ? obj[35] : ""),
						String.valueOf(obj[36] != null ? obj[36] : "")));
				nextIndex++;
			}

			// Compile the .jrxml template to a .jasper file
			InputStream jrxmlStream = AgywPrevController.class
					.getResourceAsStream(VULNERABILITIES_AND_SERVICES_SUMMARY_REPORT_TEMPLATE);
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

			if (rows.size() > 0) {
				// Convert data to a JRBeanCollectionDataSource
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);

				// Create a Map to store report parameters
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("date_start", formattedInitialDate);
				parameters.put("date_end", formattedFinalDate);
				parameters.put("slab", "Data de Início:");
				parameters.put("elab", "Data de Fim:");

				// Generate the report
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

				// Apply the configuration to the exporter
				JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();

				// Export the report to XLSX
				JRXlsxExporter exporter = new JRXlsxExporter(jasperReportsContext);
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(generatedFilePath));

				// Set your preferred column width (in pixels)
				exporter.setConfiguration(getXlsxExporterConfiguration());

				exporter.exportReport();
			}

			ObjectMapper objectMapper = new ObjectMapper();
			generatedReportResponse = objectMapper
					.writeValueAsString(new ReportResponse(generatedFilePath, rows.size(), nextIndex));

			System.out.println(generatedFilePath + ": generated and exported to XLSX with borders successfully.");

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(generatedReportResponse, HttpStatus.OK);
	}

	private static SimpleXlsxReportConfiguration getXlsxExporterConfiguration() {
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setDetectCellType(true);
		configuration.setAutoFitPageHeight(true);
		configuration.setIgnoreGraphics(false);
		// Set text wrapping
		configuration.setWhitePageBackground(false);
		configuration.setRemoveEmptySpaceBetweenColumns(true);
		configuration.setWrapText(true); // Enable text wrapping

		configuration.setRemoveEmptySpaceBetweenRows(true);
		configuration.setCollapseRowSpan(true);

		// Adjust column width
		configuration.setColumnWidthRatio(5f); // Adjust this ratio based on your preferences

		return configuration;
	}

	@GetMapping(path = "/getNewlyEnrolledAgywAndServices")
	public ResponseEntity<String> getNewlyEnrolledAgywAndServicesV2(@RequestParam(name = "province") String province,
			@RequestParam(name = "districts") Integer[] districts, @RequestParam(name = "startDate") Long startDate,
			@RequestParam(name = "endDate") Long endDate, @RequestParam(name = "pageIndex") int pageIndex,
			@RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "username") String username)
			throws IOException {

		AgywPrevReport report = new AgywPrevReport(service);

		Date initialDate = new Date(startDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedInitialDate = sdf.format(initialDate);

		Date finalDate = new Date(endDate);
		String formattedFinalDate = sdf.format(finalDate); // Using the same formatter for final date

		createDirectory(REPORTS_HOME + "/" + username);

		String generatedFilePath = REPORTS_HOME + "/" + username + "/" + NEW_ENROLLED_REPORT_NAME + "_"
				+ province.toUpperCase() + "_" + formattedInitialDate + "_" + formattedFinalDate + "_" + pageIndex + "_"
				+ ".xlsx";

		long startTime = System.currentTimeMillis();
		try {
			// Set up streaming workbook
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			workbook.setCompressTempFiles(true); // Enable compression of temporary files

			// Create a sheet
			Sheet sheet = workbook.createSheet("Sheet");
			// Create font for bold style
			Font boldFont = workbook.createFont();
			boldFont.setBold(true);

			// Apply bold font style to the cells in the header row
			CellStyle boldCellStyle = workbook.createCellStyle();
			boldCellStyle.setFont(boldFont);

			// Apply bold font style to the cells in the header row
			CellStyle alignCellStyle = workbook.createCellStyle();
			// alignCellStyle.setFont(boldFont);
			alignCellStyle.setAlignment(HorizontalAlignment.CENTER);

			// Define Title
			String titleHeaders = "LISTA DE RAMJ REGISTADAS NO DLT NO PERÍODO EM CONSIDERAÇÃO, SUAS VULNERABILIDADES E SERVIÇOS RECEBIDOS ";
			// Create a header row
			Row titleRow = sheet.createRow(0);
			// Write Title
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue(titleHeaders);
			// titleCell.setCellStyle(boldCellStyle);
			// Merge the cells for the title
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 39));

			// Define Initial Date
			String initialDateHeaders[] = { "Data de Início:", formattedInitialDate };
			// Create a header row
			Row initialHeaderRow = sheet.createRow(1);
			// Write headers
			for (int i = 0; i < initialDateHeaders.length; i++) {
				Cell cell = initialHeaderRow.createCell(i);
				cell.setCellValue(initialDateHeaders[i]);
				// cell.setCellStyle(boldCellStyle);
			}

			// Define Final Date
			String finalDateHeaders[] = { "Data de Fim:", formattedFinalDate };
			// Create a header row
			Row finalHeaderRow = sheet.createRow(2);
			// Write headers
			for (int i = 0; i < finalDateHeaders.length; i++) {
				Cell cell = finalHeaderRow.createCell(i);
				cell.setCellValue(finalDateHeaders[i]);
				cell.setCellStyle(boldCellStyle);
			}

			// Create a header row
			Row sessionRow = sheet.createRow(3);
			// Write Title and Merge cells for session headers

			Cell cell1 = sessionRow.createCell(0);
			cell1.setCellValue("Informação Demográfica");
			cell1.setCellStyle(alignCellStyle);

			Cell cell2 = sessionRow.createCell(17);
			cell2.setCellValue("Vulnerabilidades Gerais");
			cell2.setCellStyle(alignCellStyle);

			Cell cell3 = sessionRow.createCell(30);
			cell3.setCellValue("Serviços e Sub-Serviços");
			cell3.setCellStyle(alignCellStyle);

			// Merge cells for session headers
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 16)); // Merge first 17 columns
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 17, 29)); // Merge next 13 columns
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 30, 39)); // Merge last 10 columns

			// Define headers
			String[] headers = { "Província", "Distrito", "Onde Mora", "Ponto de Entrada", "Organização",
					"Data de Inscrição", "Data de Registo", "Registado Por", "Data da Última Actualização",
					"Actualizado Por", "NUI", "Sexo", "Idade (Registo)", "Idade (Actual)", "Faixa Etária (Registo)",
					"Faixa Etária (Actual)", "Data de Nascimento",
					"Incluida no Indicador AGYW_PREV / Beneficiaria DREAMS ?", "Com Quem Mora", "Sustenta a Casa",
					"É Órfã?", "Vai à escola", "Tem Deficiência", "Tipo de Deficiência", "Já foi casada",
					"Já esteve grávida", "Tem filhos", "Está Grávida ou a Amamentar", "Trabalha", "Já fez teste de HIV",
					"Área de Serviço", "Serviço", "Sub-Serviço", "Pacote de Serviço", "Ponto de Entrada de Serviço",
					"Localização do Serviço", "Data do Serviço", "Provedor do Serviço", "Outras Observações",
					"Status" };

			// Create a header row
			Row headerRow = sheet.createRow(4);
			// Write headers
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				// cell.setCellStyle(boldCellStyle);
			}

			// Insert data rows from the reportObjectList
			List<Object> reportObjectList = report.getNewlyEnrolledAgywAndServices(districts, new Date(startDate),
					new Date(endDate), pageIndex, pageSize);
			int rowCount = 5; // start from row 1 (row 0 is for headers)
			for (Object reportObject : reportObjectList) {
				Row row = sheet.createRow(rowCount++);
				// Write values to cells based on headers
				for (int i = 0; i < headers.length; i++) {
					Object value = getValueAtIndex(reportObject, i); // You need to implement this method
					if (value != null) {
						row.createCell(i).setCellValue(String.valueOf(value));
					}
				}
			}

			// Write the workbook content to a file
			FileOutputStream fileOut = new FileOutputStream(generatedFilePath);
			workbook.write(fileOut);
			fileOut.close();

			// Dispose of temporary files backing this workbook on disk
			workbook.dispose();

			// Close the workbook
			workbook.close();

			System.out.println("Excel file has been created successfully ! - path: " + generatedFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(generatedFilePath, HttpStatus.OK);
	}

	// Method to retrieve value for a specific index from the reportObject
	private static Object getValueAtIndex(Object reportObject, int index) {
		// Assuming reportObject is an array
		if (reportObject instanceof Object[]) {
			Object[] dataArray = (Object[]) reportObject;
			if (index >= 0 && index < dataArray.length) {
				return dataArray[index];
			}
		}
		return null;
	}

}
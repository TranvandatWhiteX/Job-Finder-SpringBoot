package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.domain.enums.JobLevel;
import com.dattran.job_finder_springboot.domain.enums.JobType;
import com.dattran.job_finder_springboot.domain.repositories.BusinessStreamRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImportExcelService {
  BusinessStreamRepository businessStreamRepository;

  public void exportExcel(String filePath) {
    String[] columns = {
      "Title",
      "Job requirement",
      "Job description",
      "Benefit",
      "Job Levels",
      "Job Types",
      "Number requirements",
      "Experience",
      "Business stream",
      "Address",
      "Province",
      "Skills",
      "Salary",
      "Expired date (yyyy-MM-dd)",
      "Note"
    };
    ClassPathResource resource = new ClassPathResource(filePath);
    try (Workbook workbook = new XSSFWorkbook();
        FileOutputStream fileOut = new FileOutputStream(resource.getFile()); ) {
      Sheet sheet = workbook.createSheet("Posts");

      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setFontHeightInPoints((short) 12);
      headerFont.setColor(IndexedColors.BLACK.getIndex());

      CellStyle headerCellStyle = workbook.createCellStyle();
      headerCellStyle.setFont(headerFont);

      Row headerRow = sheet.createRow(0);
      for (int i = 0; i < columns.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(columns[i]);
        cell.setCellStyle(headerCellStyle);
      }
      String[] jobTypes = Arrays.stream(JobType.values()).map(Enum::name).toArray(String[]::new);
      String[] jobLevels = Arrays.stream(JobLevel.values()).map(Enum::name).toArray(String[]::new);
      List<String> businesses = getAllBusinessStreamNames();
      String[] businessStreams = businesses.toArray(new String[0]);
      String[] experiences = {
        "0-Không yêu cầu kinh nghiệm",
        "0.5-Dưới 1 năm kinh nghiệm",
        "1-Từ 1 năm kinh nghiệm",
        "2-Từ 2 năm kinh nghiệm",
        "5-Từ 5 năm kinh nghiệm"
      };
      setupDataValidation(sheet, jobTypes, 1, 10, 5, 5);
      setupDataValidation(sheet, jobLevels, 1, 10, 4, 4);
      setupDataValidation(sheet, businessStreams, 1, 10, 8, 8);
      setupDataValidation(sheet, experiences, 1, 10, 7, 7);
      setupDataValidationWithNamedRange(workbook, sheet, provinceVars, 1, 10, 10, 10);
      setExampleData(sheet, jobLevels, jobTypes, experiences, provinceVars, businessStreams);
      for (int i = 0; i < columns.length; i++) {
        sheet.autoSizeColumn(i);
      }
      workbook.write(fileOut);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void setExampleData(
      Sheet sheet,
      String[] jobLevels,
      String[] jobTypes,
      String[] experiences,
      String[] provinces,
      String[] businessStreams) {
    Row row = sheet.createRow(1);
    row.createCell(0).setCellValue("Lập trình viên Java");
    row.createCell(1).setCellValue("Có kinh nghiệm sử dụng Spring Boot, Kafa, Oracle.");
    row.createCell(2).setCellValue("Lập trình API trong lĩnh vực y tế.");
    row.createCell(3).setCellValue("Lương, thưởng, chính sách tốt.");
    row.createCell(4).setCellValue(jobLevels[0]);
    row.createCell(5).setCellValue(jobTypes[0]);
    row.createCell(6).setCellValue(5);
    row.createCell(7).setCellValue(experiences[0]);
    row.createCell(8).setCellValue(businessStreams[0]);
    row.createCell(9).setCellValue("detail:Số 1 Trần Nguyên Đán;ward:Định Công;district:Hoàng Mai");
    row.createCell(10).setCellValue(provinces[0]);
    row.createCell(11).setCellValue("Java, Kafka, Oracle");
    row.createCell(12)
        .setCellValue("minSalary:10;maxSalary:20;unit:tr;currency:VND;type:MONTHLY_WAGE");
    row.createCell(13).setCellValue("2024-10-21");
    row.createCell(14)
        .setCellValue(
            "Nhập đúng theo định dạng.\nTrong trường hợp lương thỏa thuận thì để other:Thỏa thuận.\nCác giá trị cho type trong lương: MONTHLY_WAGE, HOURLY_WAGE, YEARLY_WAGE.\nCác giá trị cho currency: VND, USD, JPY, GBP, CNY.");
  }

  private List<String> getAllBusinessStreamNames() {
    return businessStreamRepository.findAll().stream()
        .map(businessStream -> businessStream.getCode() + "-" + businessStream.getName())
        .collect(Collectors.toList());
  }

  private void setupDataValidation(
      Sheet sheet, String[] values, int firstRow, int lastRow, int firstCol, int lastCol) {
    DataValidationHelper validationHelper = sheet.getDataValidationHelper();
    DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(values);
    CellRangeAddressList addressList =
        new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
    DataValidation validation = validationHelper.createValidation(constraint, addressList);
    validation.setShowErrorBox(true);
    sheet.addValidationData(validation);
  }

  private void setupDataValidationWithNamedRange(
      Workbook workbook,
      Sheet sheet,
      String[] values,
      int firstRow,
      int lastRow,
      int firstCol,
      int lastCol) {
    // Tạo một sheet ẩn để chứa các giá trị
    Sheet hiddenSheet = workbook.createSheet("Hidden");
    // Đưa các giá trị vào sheet ẩn
    for (int i = 0; i < values.length; i++) {
      hiddenSheet.createRow(i).createCell(0).setCellValue(values[i]);
    }
    // Tạo Named Range tham chiếu đến các giá trị
    Name namedRange = workbook.createName();
    namedRange.setNameName("DropdownValues");
    namedRange.setRefersToFormula("Hidden!$A$1:$A$" + values.length);
    // Tạo Data Validation tham chiếu đến Named Range
    DataValidationHelper validationHelper = sheet.getDataValidationHelper();
    DataValidationConstraint constraint =
        validationHelper.createFormulaListConstraint("DropdownValues");
    CellRangeAddressList addressList =
        new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
    DataValidation validation = validationHelper.createValidation(constraint, addressList);
    validation.setShowErrorBox(true);
    sheet.addValidationData(validation);
    // Ẩn sheet chứa danh sách
    workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
  }

  private final String[] provinceVars = {
    "01-Hà Nội",
    "02-Hà Giang",
    "04-Cao Bằng",
    "06-Bắc Kạn",
    "08-Tuyên Quang",
    "10-Lào Cai",
    "11-Điện Biên",
    "12-Lai Châu",
    "14-Sơn La",
    "15-Yên Bái",
    "17-Hoà Bình",
    "19-Thái Nguyên",
    "20-Lạng Sơn",
    "22-Quảng Ninh",
    "24-Bắc Giang",
    "25-Phú Thọ",
    "26-Vĩnh Phúc",
    "27-Bắc Ninh",
    "30-Hải Dương",
    "31-Hải Phòng",
    "33-Hưng Yên",
    "34-Thái Bình",
    "35-Hà Nam",
    "36-Nam Định",
    "37-Ninh Bình",
    "38-Thanh Hóa",
    "40-Nghệ An",
    "42-Hà Tĩnh",
    "44-Quảng Bình",
    "45-Quảng Trị",
    "46-Thừa Thiên Huế",
    "48-Đà Nẵng",
    "49-Quảng Nam",
    "51-Quảng Ngãi",
    "52-Bình Định",
    "54-Phú Yên",
    "56-Khánh Hòa",
    "58-Ninh Thuận",
    "60-Bình Thuận",
    "62-Kon Tum",
    "64-Gia Lai",
    "66-Đắk Lắk",
    "67-Đắk Nông",
    "68-Lâm Đồng",
    "70-Bình Phước",
    "72-Tây Ninh",
    "74-Bình Dương",
    "75-Đồng Nai",
    "77-Bà Rịa - Vũng Tàu",
    "79-Hồ Chí Minh",
    "80-Long An",
    "82-Tiền Giang",
    "83-Bến Tre",
    "84-Trà Vinh",
    "86-Vĩnh Long",
    "87-Đồng Tháp",
    "89-An Giang",
    "91-Kiên Giang",
    "92-Cần Thơ",
    "93-Hậu Giang",
    "94-Sóc Trăng",
    "95-Bạc Liêu",
    "96-Cà Mau"
  };
}

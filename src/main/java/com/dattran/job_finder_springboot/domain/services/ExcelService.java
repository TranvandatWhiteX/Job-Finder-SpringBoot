package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.app.dtos.JobPostDto;
import com.dattran.job_finder_springboot.domain.entities.Address;
import com.dattran.job_finder_springboot.domain.entities.Salary;
import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.enums.JobLevel;
import com.dattran.job_finder_springboot.domain.enums.JobType;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.repositories.BusinessStreamRepository;
import com.dattran.job_finder_springboot.domain.utils.ProvinceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelService {
  BusinessStreamRepository businessStreamRepository;
  JobPostService jobPostService;
  UserService userService;

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
      setupDataValidationWithNamedRange(workbook, sheet, ProvinceUtil.provinceVars, 1, 10, 10, 10);
      setExampleData(
          sheet, jobLevels, jobTypes, experiences, ProvinceUtil.provinceVars, businessStreams);
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

  public void importExcel(
      MultipartFile file, String userId, HttpServletRequest httpServletRequest) {
    List<JobPostDto> jobPostDtos = createDtoFromExcelFile(file, userId);
    for (JobPostDto jobPostDto : jobPostDtos) {
      jobPostService.postJob(jobPostDto, httpServletRequest);
    }
  }

  private List<JobPostDto> createDtoFromExcelFile(MultipartFile file, String userId) {
    List<JobPostDto> dtos = new ArrayList<>();
    User user = userService.getUserById(userId);
    if (user.getCompanyId().isEmpty()) {
      throw new AppException(ResponseStatus.COMPANY_NOT_FOUND);
    }
    try (InputStream excelFile = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(excelFile)) {
      Sheet sheet = workbook.getSheetAt(0);
      for (Row row : sheet) {
        if (row.getRowNum() == 0) continue;
        String title = row.getCell(0).getStringCellValue();
        String jobRequirement = row.getCell(1).getStringCellValue();
        String jobDescription = row.getCell(2).getStringCellValue();
        String benefit = row.getCell(3).getStringCellValue();
        String jobLevel = row.getCell(4).getStringCellValue();
        String jobType = row.getCell(5).getStringCellValue();
        int numberRequirement = (int) row.getCell(6).getNumericCellValue();
        String experience = row.getCell(7).getStringCellValue();
        String business = row.getCell(8).getStringCellValue();
        String address = row.getCell(9).getStringCellValue();
        String province = row.getCell(10).getStringCellValue();
        String skills = row.getCell(11).getStringCellValue();
        String salary = row.getCell(12).getStringCellValue();
        String expiredDate = row.getCell(13).getStringCellValue();
        JobPostDto dto =
            validateData(
                title,
                jobRequirement,
                jobDescription,
                benefit,
                jobLevel,
                jobType,
                numberRequirement,
                experience,
                business,
                address,
                province,
                skills,
                salary,
                expiredDate,
                user.getCompanyId());
        dtos.add(dto);
      }
    } catch (IOException e) {
      throw new AppException(ResponseStatus.INVALID_EXCEL_FILE);
    }
    return dtos;
  }

  private JobPostDto validateData(
      String title,
      String jobRequirement,
      String jobDescription,
      String benefit,
      String jobLevel,
      String jobType,
      int numberRequirement,
      String experience,
      String business,
      String address,
      String province,
      String skills,
      String salary,
      String expiredDate,
      String companyId) {
    JobPostDto dto = new JobPostDto();
    if (title.isEmpty()
        || jobRequirement.isEmpty()
        || benefit.isEmpty()
        || jobLevel.isEmpty()
        || jobType.isEmpty()
        || jobDescription.isEmpty()
        || experience.isEmpty()
        || business.isEmpty()
        || address.isEmpty()
        || province.isEmpty()
        || skills.isEmpty()
        || expiredDate.isEmpty()
        || companyId.isEmpty()
        || salary.isEmpty()
        || numberRequirement <= 0) {
      throw new AppException(ResponseStatus.DATA_EXCEL_FILE_NULL);
    }
    dto.setJobTitle(title);
    dto.setJobRequirement(jobRequirement);
    dto.setBenefit(benefit);
    dto.setJobDescription(jobDescription);
    dto.setSkills(skills);
    dto.setExpiredDate(LocalDate.parse(expiredDate));
    dto.setCompanyId(companyId);
    dto.setNumberRequirement((long) numberRequirement);
    dto.setJobLevel(JobLevel.valueOf(jobLevel));
    dto.setJobType(JobType.valueOf(jobType));
    dto.setSalary(getSalary(salary));
    dto.setExperience(getCode(experience));
    dto.setAddress(getAddress(address, province));
    dto.setBusinessCode(getCode(business));
    return dto;
  }

  private long getCode(String value) {
    String[] split = value.split("-");
    return Long.parseLong(split[0]);
  }

  private Address getAddress(String address, String province) {
    Address add = new Address();
    long provinceCode = getCode(province);
    add.setProvinceCode(provinceCode);
    Map<String, String> map = toMap(address);
    add.setWard(map.get("ward"));
    add.setDetail(map.get("detail"));
    add.setDistrict(map.get("district"));
    add.setProvince(province.split("-")[1]);
    return add;
  }

  private Salary getSalary(String salary) {
    Salary sal = new Salary();
    if (salary.trim().startsWith("other")) {
      String val = salary.trim().split(":")[1];
      sal.setOther(val);
      return sal;
    }
    Map<String, String> map = toMap(salary);
    sal.setMinSalary(Long.valueOf(map.get("minSalary")));
    sal.setMaxSalary(Long.valueOf(map.get("maxSalary")));
    sal.setUnit(map.get("unit"));
    sal.setType(Salary.Type.valueOf(map.get("type")));
    sal.setCurrency(Salary.CurrencyUnit.valueOf(map.get("currency")));
    return sal;
  }

  private Map<String, String> toMap(String value) {
    Map<String, String> map = new HashMap<>();
    String[] split = value.split(";");
    for (String s : split) {
      String[] keyValue = s.split(":");
      map.put(keyValue[0].trim(), keyValue[1].trim());
    }
    return map;
  }
}

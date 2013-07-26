package util;

import bussiness.SaleDepartmentService;
import dto.SaleDepartMentDto;
import dto.UserInfoDto;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-6
 * Time: 下午4:41
 * 功能描述:
 */
public class ImportUsersUtil {
    private static List<SaleDepartMentDto> saleDepartMentDtoList = null;
    /**
     * 解析excel文件  引入POI
     * @param excelFile
     * @return
     */
    public static List<UserInfoDto> parseUserFromExcel(File excelFile)throws Exception{

        saleDepartMentDtoList = SaleDepartmentService.findAll();
        InputStream is = new FileInputStream(excelFile);
        Workbook workbook = WorkbookFactory.create(is);
        UserInfoDto userInfoDto = null;
        List<UserInfoDto> list = new ArrayList<UserInfoDto>();

        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet hssfSheet = workbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                userInfoDto = new UserInfoDto();
                // 循环列Cell
                // 0用户名 1登入账号 2密码 3联系电话 4 email 5身份证号码  6营业部 7联系地址 8 邮编 9 资金账号
                Cell xh = hssfRow.getCell(0);
                if (xh == null) {
                    continue;
                }
                userInfoDto.name =getStringValue(xh);
                xh = hssfRow.getCell(1);
                if (xh == null) {
                    continue;
                }
                userInfoDto.account=getStringValue(xh);
                xh = hssfRow.getCell(2);
                if (xh == null) {
                    continue;
                }
                userInfoDto.password = getStringValue(xh);
                xh = hssfRow.getCell(3);
                if (xh == null) {
                    continue;
                }
                userInfoDto.phone =getStringValue(xh);
                xh = hssfRow.getCell(4);
                if (xh == null) {
                    continue;
                }
                userInfoDto.email =getStringValue(xh);
                xh = hssfRow.getCell(5);
                if (xh != null) {
                    userInfoDto.idCard = getStringValue(xh);
                }
                xh = hssfRow.getCell(6);
                if (xh != null) {
                    long depId = getDepartmentIdFromString(getStringValue(xh));
                    userInfoDto.saleDept = String.valueOf(depId);
                }
                xh = hssfRow.getCell(7);
                if (xh != null) {
                    userInfoDto.address= getStringValue(xh);
                }
                xh = hssfRow.getCell(8);
                if (xh != null) {
                    userInfoDto.postCode = getStringValue(xh);
                }
                xh = hssfRow.getCell(9);
                if (xh != null) {
                    userInfoDto.capitalAccount = getStringValue(xh);
                }
                list.add(userInfoDto);
            }
        }
        return list;
    }
    private static String getStringValue(Cell xssfCell){
        if(xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN){
            return String.valueOf( xssfCell.getBooleanCellValue());
        }else if(xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC){
            return String.valueOf( (int)(xssfCell.getNumericCellValue()));
        }else{
            return String.valueOf( xssfCell.getStringCellValue());
        }
    }
    public static  long getDepartmentIdFromString(String dpName){
        if(dpName == null || "".equals(dpName.trim())){
            return -1;
        }
        for(SaleDepartMentDto saleDepartMentDto:saleDepartMentDtoList){
            if(saleDepartMentDto.name.trim().equals(dpName.trim())){
                return saleDepartMentDto.id;
            }
        }
        return -1;
    }
}

package cn.net.ssd.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtil {
	/**
    *
    * @Title: creatRow 
    * @Description: 创建字典表
    */
    public static void creatRow(HSSFSheet sheet,String[] infoxArr){
        int max =infoxArr.length;
        for(int i=0;i<max;i++){
            HSSFRow row = sheet.createRow(i+1);
            HSSFCell cell0 = row.createCell(0); 
            if(i<infoxArr.length)
                cell0.setCellValue(infoxArr[i]);
        }
    }
	/**
     * 获取合并单元格的值
     */
    public static String getMergedRegionValue(Sheet sheet ,int row , int column){     
        int sheetMergeCount = sheet.getNumMergedRegions();     
             
        for(int i = 0 ; i < sheetMergeCount ; i++){     
            CellRangeAddress ca = sheet.getMergedRegion(i);     
            int firstColumn = ca.getFirstColumn();     
            int lastColumn = ca.getLastColumn();     
            int firstRow = ca.getFirstRow();     
            int lastRow = ca.getLastRow();     
            if(row >= firstRow && row <= lastRow){     
                 if(column >= firstColumn && column <= lastColumn){     
                    Row fRow = sheet.getRow(firstRow); 
                    Cell fCell = fRow.getCell(firstColumn); 
                    return getCellValue(fCell) ; 
                }     
            }     
        }
        return null ;
    }
	public static String getCellValue(Cell cell){
		if(cell == null) return "";
		if(cell.getCellType() == CellType.STRING){
			return cell.getStringCellValue();
		}else if(cell.getCellType() == CellType.BOOLEAN){
			return String.valueOf(cell.getBooleanCellValue());
		}else if(cell.getCellType() == CellType.FORMULA){
			return cell.getCellFormula() ;
		}else if(cell.getCellType() == CellType.NUMERIC){
			DecimalFormat df = new DecimalFormat("0");
			return df.format(cell.getNumericCellValue());
		}else{
			return cell.getStringCellValue();
		}
		//return "";
	}
    /**   
     * 判断指定的单元格是否是合并单元格
     */ 
	public static boolean isMergedRegion(Sheet sheet,int row ,int column) {   
       int sheetMergeCount = sheet.getNumMergedRegions();   
       for (int i = 0; i < sheetMergeCount; i++) {   
             CellRangeAddress range = sheet.getMergedRegion(i);   
             int firstColumn = range.getFirstColumn(); 
             int lastColumn = range.getLastColumn();   
             int firstRow = range.getFirstRow();   
             int lastRow = range.getLastRow();
             if(row >= firstRow && row <= lastRow){ 
                 if(column >= firstColumn && column <= lastColumn){ 
                     return true;   
                 } 
             }   
       } 
       return false;   
     }
    /**
    *
    * @Title: createName 
    * @Description: 名称管理器中创建名称
    */
    public static HSSFName createName(HSSFWorkbook wb, String name, String expression){
        HSSFName refer = wb.createName();
        refer.setRefersToFormula(expression);
        refer.setNameName(name);
        return refer;
    }
    
    /**
     * @Title: setDataValidation 
     * @Description: 设置数据有效性（通过名称管理器级联相关）
     */
    public static HSSFDataValidation setDataValidation(String name, int firstRow, int endRow, int firstCol, int endCol){
        //加载下拉列表内容
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(name);
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList((short) firstRow, (short) endRow, (short) firstCol, (short) endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
        return data_validation;
    }
    
    /** 
    *
    * @Title: creatRow 
    * @Description: 创建字典表
    */
    public static void creatRow(HSSFSheet sheet, List<Map<String, Object>> list1, List<Map<String, Object>> list2, List<Map<String, Object>> list3, String[] infoxArr){
        int max = Math.max(list1.size(),Math.max(list2.size(), Math.max(list3.size(), infoxArr.length)));
        for(int i=0;i<max;i++){
            HSSFRow row = sheet.createRow(i+1);
            //群组
            HSSFCell cell0 = row.createCell(0); 
            if(i<list2.size())
                cell0.setCellValue(String.format("%s",list2.get(i).get("TYPENAME")));
            //地区
            HSSFCell cell1 = row.createCell(1); 
            if(i<list1.size())
                cell1.setCellValue(String.format("%s",list1.get(i).get("AREANAME")));
            //空白
            HSSFCell cell2 = row.createCell(2); 
            cell2.setCellValue("");
            //手段
            HSSFCell cell3 = row.createCell(3); 
            if(i<list3.size())
                cell3.setCellValue(String.format("%s",list3.get(i).get("METHODNAME")));
            //空白
            HSSFCell cell4 = row.createCell(4); 
            cell4.setCellValue("");
            //运营商
            HSSFCell cell5 = row.createCell(5); 
            if(i<infoxArr.length)
                cell5.setCellValue(infoxArr[i]);
        }
    }


    public static String getInfoxCodeName(String id){
        if(id==null) return "";
        if(id.equals("1")){
            return "移动";
        }else if(id.equals("2")){
            return "联通";
        }else if(id.equals("3")){
            return "电信";
        }
        return id;
    }

    //使用poi，用java实现清除excel工作簿内容之间空行
    public static void delLineNull(Workbook workbook, String Outpath) {
        System.out.println("开始移除空行操作");
        int key = 0;
        int MaxRowNum,MaxCellNum = 0;
        try {
            FileOutputStream out = new FileOutputStream(Outpath);
            Sheet sheet = workbook.getSheetAt(0); //14
            MaxRowNum = 0;
            for (int k = 0; k <= sheet.getLastRowNum(); k++) {
                Row hRow = sheet.getRow(k);
                //System.out.println((k + 1) + "行");
                if (isBlankRow(hRow)) // 找到空行索引
                {
                    int m;
                    for (m = k + 1; m <= sheet.getLastRowNum(); m++) {
                        Row nhRow = sheet.getRow(m);
                        if (!isBlankRow(nhRow)) {
                            sheet.shiftRows(m, sheet.getLastRowNum(), k - m);
                            break;
                        }
                    }
                    if (m > sheet.getLastRowNum())
                        break; // 此工作簿完成
                } else { //非空行
                    MaxRowNum ++;
                    if(MaxCellNum < hRow.getLastCellNum())
                        MaxCellNum = hRow.getLastCellNum();
                }
            }
            workbook.setPrintArea(0, 0, MaxCellNum, 0, MaxRowNum);
            workbook.write(out);
            out.close();
        }catch (Exception e) {
            log.error("ExcelUtil,delLineNull,",e);
        }
    }
    /**
     * 判断excel 空行
     */
    private static boolean isBlankRow(Row row) {
        if (row == null)
            return true;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell hcell = row.getCell(i);
            if (!isBlankCell(hcell))
                return false;
        }
        return true;
    }
    public static boolean isBlankCell(Cell hcell) {
        if (hcell == null)
            return true;

        DataFormatter df = new DataFormatter();
        String content = df.formatCellValue(hcell);
        if (content == null || "".equals(content)) // 找到非空行
        {
            return true;
        }
        return false;
    }
}

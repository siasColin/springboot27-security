package cn.net.ssd.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Cleanup;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * excel读取工具类
 */
public class ImportExeclUtil
{

    private static int totalRows = 0;// 总行数

    private static int totalCells = 0;// 总列数

    /** 无参构造方法 */
    public ImportExeclUtil()
    {
    }

    public static int getTotalRows()
    {
        return totalRows;
    }

    public static int getTotalCells()
    {
        return totalCells;
    }

    /**
     * 根据流读取Excel文件
     */
    public List<List<String>> read(InputStream inputStream, boolean isExcel2003)
            throws IOException
    {
        Workbook wb = isExcel2003 ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
        return readDate(wb);
    }

    /**
     * 读取数据
     */
    private List<List<String>> readDate(Workbook wb)
    {
        List<List<String>> dataLst = new ArrayList<>();
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);

        /** 得到Excel的行数 */
        totalRows = sheet.getPhysicalNumberOfRows();

        /** 得到Excel的列数 */
        if (totalRows >= 1 && sheet.getRow(0) != null)
        {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        /** 循环Excel的行 */
        for (int r = 0; r < totalRows; r++)
        {
            Row row = sheet.getRow(r);
            if (row == null)
            {
                continue;
            }
            List<String> rowLst = new ArrayList<String>();

            /** 循环Excel的列 */
            for (int c = 0; c < getTotalCells(); c++)
            {
                Cell cell = row.getCell(c);
                String cellValue = "";

                if (null != cell)
                {
                    // 以下是判断数据的类型
                    switch (cell.getCellType())
                    {
                        case NUMERIC: // 数字
                            cellValue = cell.getNumericCellValue() + "";
                            break;
                        case STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;
                        case BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case FORMULA: // 公式
                            cellValue = cell.getCellFormula() + "";
                            break;
                        case BLANK: // 空值
                            cellValue = "";
                            break;
                        case ERROR: // 故障
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                            break;
                    }
                }

                rowLst.add(cellValue);
            }

            /** 保存第r行的第c列 */
            dataLst.add(rowLst);
        }

        return dataLst;
    }

    /**
     * 按指定坐标读取实体数据
     * <按顺序放入带有注解的实体成员变量中>
     * @param wb 工作簿
     * @param t 实体
     * @param in 输入流
     * @param integers 指定需要解析的坐标
     */
    @SuppressWarnings("unused")
    public static <T> T readDateT(Workbook wb, T t, InputStream in, Integer[]... integers)
            throws IOException, Exception
    {
        // 获取该工作表中的第一个工作表
        Sheet sheet = wb.getSheetAt(0);
        // 成员变量的值
        Object entityMemberValue = "";

        // 所有成员变量
        Field[] fields = t.getClass().getDeclaredFields();
        // 列开始下标
        int startCell = 0;

        /** 循环出需要的成员 */
        for (int f = 0; f < fields.length; f++)
        {
            fields[f].setAccessible(true);
            String fieldName = fields[f].getName();
            boolean fieldHasAnno = fields[f].isAnnotationPresent(IsNeeded.class);
            // 有注解
            if (fieldHasAnno)
            {
                IsNeeded annotation = fields[f].getAnnotation(IsNeeded.class);
                boolean isNeeded = annotation.isNeeded();

                // Excel需要赋值的列
                if (isNeeded)
                {
                    // 获取行和列
                    int x = integers[startCell][0] - 1;
                    int y = integers[startCell][1] - 1;

                    Row row = sheet.getRow(x);
                    Cell cell = row.getCell(y);

                    if (row == null)
                    {
                        continue;
                    }
                    // Excel中解析的值
                    String cellValue = getCellValue(cell);
                    // 需要赋给成员变量的值
                    entityMemberValue = getEntityMemberValue(entityMemberValue, fields[f], cellValue);
                    // 赋值
                    PropertyUtils.setProperty(t, fieldName, entityMemberValue);
                    // 列的下标加1
                    startCell++;
                }
            }

        }

        return t;
    }
    /**
     * 对于导入的excel 进行反馈信息填写,并存入本地
     */
    public static Map<String,String> feedBackExcel(MultipartFile IFile,List<Map<String,String>> backList, String excelName, int beginLine, int totalcut, String realPath){

        //复制workbook
        Workbook wb;
        //兼容windows,linux分隔符
        String fileSeperator = File.separator;
        Map<String,String> realpathMap = new HashMap<>();
        try {
            wb = chooseWorkbook(IFile.getOriginalFilename(), IFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return realpathMap;
        }
        try{
            wb = operationWb(wb,backList,beginLine,totalcut,true);
            //保存结果excel
            File path = new File(realPath+fileSeperator+"uploadfile"+fileSeperator+"import");
            if(!path.exists()){
                path.mkdirs();
            }
            String datatime = DateUtil.format(LocalDateTime.now(),"yyyyMMddHHmmss");
            String filepath = realPath+fileSeperator+"uploadfile"+fileSeperator+"import"+fileSeperator+excelName+"导入数据结果反馈"+datatime+".xlsx";
            realpathMap.put("filepath",filepath);
            ExcelUtil.delLineNull(wb,filepath);
            wb.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return realpathMap;
    }

    /**
     * 操作wookbook 将成功，失败数据分别保存
     * @param flag  true 表示保存成功数据， false表示保存失败数据
     */
    public static Workbook operationWb(Workbook wb,List<Map<String,String>> backList,int beginLine,int totalcut,boolean flag){

        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        int rows = sheet.getPhysicalNumberOfRows();

        /** 得到excel总列数*/
        int cells = sheet.getRow(0).getPhysicalNumberOfCells();
        // 设置样式1
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.LEFT);
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        // 设置样式2
        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.LEFT);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //给当前sheet创建两列
        sheet.getRow(0).createCell(cells).setCellValue("状态");
        sheet.setColumnWidth(20,8000);
        sheet.getRow(0).createCell(cells+1).setCellValue("信息");
        sheet.setColumnWidth(500,8000);
        //循环sheet的行给每行的最后两列赋与map中的值
        int i = 0;
        for (int r = beginLine - 1; r < rows - totalcut; r++){
            Row row = sheet.getRow(r);
            if(row == null){
                continue;
            }
            if(i >= backList.size()){
                continue;
            }
            Map<String,String> map = backList.get(i);
            Cell cellstatus =  row.createCell(cells);
            cellstatus.setCellValue(map.get("status"));
            if ("成功".equals(map.get("status"))) {
                cellstatus.setCellStyle(cellStyle1);
            } else {
                cellstatus.setCellStyle(cellStyle2);
            }
            Cell cellinfo =  row.createCell(cells+1);
            cellinfo.setCellValue(map.get("msg"));
            i++;
        }
        return wb;
    }

    /**
     * 读取列表数据
     * <按顺序放入带有注解的实体成员变量中>
     * @param wb 工作簿
     * @param t 实体
     * @param beginLine 开始行数
     * @param totalcut 结束行数减去相应行数
     * @return List<T> 实体列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readDateListT(Workbook wb, T t, int beginLine, int totalcut, Object ...colFields)
            throws Exception
    {
        List<T> listt = new ArrayList<T>();
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        totalRows = sheet.getPhysicalNumberOfRows();
        //ceshi
        int a = sheet.getLastRowNum();

        /** 得到Excel的列数 */
        if (totalRows >= 1 && sheet.getRow(0) != null)
        {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        /** 循环Excel的行 */
        for (int r = beginLine - 1; r < totalRows - totalcut; r++)
        {
            Object newInstance = t.getClass().newInstance();
            Row row = sheet.getRow(r);
            if (row == null)
            {
                continue;
            }
            // 成员变量的值
            Object entityMemberValue = "";
            // 所有成员变量
            Field[] fields = t.getClass().getDeclaredFields();
            Map<String,Field> fieldMap = new HashMap<>();
            for(Field field:fields){
                fieldMap.put(field.getName(),field);
            }
            Class superClass =t.getClass().getSuperclass();
            String allClassName = superClass.getName();
            if(!"java.lang.Object".equals(allClassName)){
                Field[] supperFields =superClass.getDeclaredFields();
                for(Field field:supperFields){
                    if(fieldMap.get(field.getName())==null){
                        fieldMap.put(field.getName(),field);
                    }
                }
            }
            // 列开始下标
            int startCell = 0;

            String [] cols = (String[])colFields;
            boolean isAllEmpty = true;
            for (int f = 0; f < cols.length; f++)
            {
                String fieldName = cols[f];
                Field field = fieldMap.get(fieldName);
                Cell cell = row.getCell(startCell);
                String cellValue = getCellValue(cell);
                entityMemberValue = getEntityMemberValue(entityMemberValue, field, cellValue);
                // 赋值
                PropertyUtils.setProperty(newInstance, fieldName, entityMemberValue);
                // 列的下标加1
                startCell++;
                if(!RegexUtils.isNull(cellValue)){
                    isAllEmpty = false;
                }
            }
            if(!isAllEmpty){
                listt.add((T)newInstance);
            }
        }

        return listt;
    }

    /**
     * 根据Excel表格中的数据判断类型得到值
     */
    private static String getCellValue(Cell cell)
    {
        String cellValue = "";
        if (null != cell)
        {
            // 以下是判断数据的类型
            switch (cell.getCellType())
            {
                case NUMERIC: // 数字
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell))
                    {
                        Date theDate = cell.getDateCellValue();
                        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cellValue = dff.format(theDate);
                    }
                    else
                    {
                        DecimalFormat df = new DecimalFormat("#.########");
                        cellValue = df.format(cell.getNumericCellValue());
                    }
                    break;
                case STRING: // 字符串
                    cellValue = cell.getStringCellValue();
                    break;
                case BOOLEAN: // Boolean
                    cellValue = cell.getBooleanCellValue() + "";
                    break;
                case FORMULA: // 公式
                    cellValue = cell.getCellFormula() + "";
                    break;
                case BLANK: // 空值
                    cellValue = "";
                    break;
                case ERROR: // 故障
                    cellValue = "非法字符";
                    break;
                default:
                    cellValue = "未知类型";
                    break;
            }
        }
        return cellValue;
    }

    /**
     * 根据实体成员变量的类型得到成员变量的值
     */
    private static Object getEntityMemberValue(Object realValue, Field f , String cellValue)
    {
        String type = f.getType().getName();
        switch (type)
        {
            case "char":
            case "java.lang.Character":
            case "java.lang.String":
                realValue = cellValue;
                break;
            case "java.util.Date":
                realValue = StrUtil.isBlank(cellValue) ? null : DateUtil.parse(cellValue,"yyyy-MM-dd HH:mm:ss");
                break;
            case "java.lang.Integer":
                realValue = StrUtil.isBlank(cellValue) ? null : Integer.valueOf(cellValue);
                break;
            case "java.lang.Long":
                realValue = StrUtil.isBlank(cellValue) ? null : Long.valueOf(cellValue);
                break;
            case "float":
            case "java.lang.Float":
                realValue = StrUtil.isBlank(cellValue) ? null : Float.parseFloat(cellValue);
                break;
            case "int":
            case "double":
            case "java.lang.Double":
            case "java.lang.Short":
            case "java.math.BigDecimal":
                realValue = StrUtil.isBlank(cellValue) ? null : new BigDecimal(cellValue);
                break;
            default:
                break;
        }
        return realValue;
    }


    /**
     * 根据路径或文件名选择Excel版本
     */
    public static Workbook chooseWorkbook(String filePathOrName, InputStream in)
            throws IOException
    {
        /** 根据版本选择创建Workbook的方式 */
        boolean isExcel2003 = ExcelVersionUtil.isExcel2003(filePathOrName);
        @Cleanup
        InputStream inputStream = in;
        return isExcel2003 ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
    }

    static class ExcelVersionUtil
    {
        public static boolean isExcel2003(String filePath)
        {
            return filePath.matches("^.+\\.(?i)(xls)$");
        }
        public static boolean isExcel2007(String filePath)
        {
            return filePath.matches("^.+\\.(?i)(xlsx)$");
        }
    }

    /**
     * @param xlsxUrl 模板地址
     * @param typeName //导出文件类型名称
     * @param listFieldCodes //列集合
     * @param list   //数据集合
     */
    public void exclImportFile(String xlsxUrl, String typeName, List<String> listFieldCodes, List<Map<String, Object>> list, HttpServletResponse response, OutputStream outputStream) {
        try {
            //服务器上用的
//            InputStream input = this.getClass().getClassLoader().getResourceAsStream(xlsxUrl);
            //本地测试
            String filePath = getClass().getResource(xlsxUrl).getPath();
            FileInputStream input = new FileInputStream(filePath);
            XSSFWorkbook xwb;
            SXSSFWorkbook swb;
            xwb = (XSSFWorkbook) WorkbookFactory.create(input);
            swb = new SXSSFWorkbook(xwb,10000);
            //获取工作表
            SXSSFSheet sxssfSheet = swb.getSheet("Sheet1");
            //创建行
            SXSSFRow xssfRow;
            //创建列，即单元格Cell
            SXSSFCell xssfCell;
            List<String> cols=listFieldCodes;
            //把List里面的数据写到excel中
            for (int i=0;i < list.size();i++) {
                //从第三行开始写入
                xssfRow = sxssfSheet.createRow(i + 3);
                //创建每个单元格Cell，即列的数据
                Map<String, Object> obj =list.get(i);
                if(obj.size()!=cols.size()){
                    System.out.println("实际列数与查询出来的列数不一致！");
                }
                for (int j=0;j<cols.size();j++) {
                    xssfCell = xssfRow.createCell(j);//创建单元格
                    if (null != obj.get(cols.get(j))){
                        if (!obj.get(cols.get(j)).toString().equals("")){
                            xssfCell.setCellValue(obj.get(cols.get(j)).toString()); //设置单元格内容
                        }else{
                            xssfCell.setCellValue(""); //设置单元格内容
                        }
                    }else{
                        xssfCell.setCellValue(""); //设置单元格内容
                        System.out.println(cols.get(j)+"此列名称未找到!");
                    }
                }
            }
            //用输出流写到excel
            // 设置文件名
            String filename = typeName +"("+ getCurrentTime("yyyyMMdd") + ").xlsx";
            filename = new String(filename.getBytes("gbk"), "iso-8859-1");
            response.reset();
            // 设置头信息
            response.setContentType("application/x-xlsx");// 导出excel2007
            response.setHeader("Content-disposition", "attachement;filename=" + filename);
            response.setBufferSize(1024);
            swb.write(outputStream);
            outputStream.flush();
            outputStream.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getCurrentTime(String format) {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(now.getTime());
    }
}
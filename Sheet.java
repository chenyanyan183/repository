package spider;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * 
 * 生成编程书籍的评分排行的专用sheet表
 * @author chen
 *
 */
/**
 * @author chen
 *
 */
public class Sheet {
	private static HSSFWorkbook workbook=new HSSFWorkbook();
	private static HSSFSheet sheet=null;
	private static int nextRowNum=0;
	private static int allRows=0;
	public static HSSFSheet getSheet(){
		synchronized (workbook) {
			if(sheet==null){
				sheet = workbook.createSheet();
				// 3: workbook 封装 数据 ... 表体
				HSSFRow firstRow = sheet.createRow(0);
				firstRow.createCell(0).setCellValue("序号");
				firstRow.createCell(1).setCellValue("书名");
				firstRow.createCell(2).setCellValue("评分");
				firstRow.createCell(3).setCellValue("评价人数");
				firstRow.createCell(4).setCellValue("作者");
				firstRow.createCell(5).setCellValue("出版社");
				firstRow.createCell(6).setCellValue("出版日期");
				firstRow.createCell(7).setCellValue("价格");
			}
		}
		return sheet;
	}
	public static int getNextRowNum() {
		//加锁，防止线程执行产生访问同一页面的问题
		synchronized (sheet) {
			//修改下一行的数量
			nextRowNum+=20;
			return nextRowNum-20;
		}
	}
	public static void setNextRowNum(int nextRowNum) {
		Sheet.nextRowNum = nextRowNum;
	}
	
	public static int getAllRows() {
		return allRows;
	}
	public static void setAllRows(int allRows) {
		Sheet.allRows = allRows;
	}
	/**
	 * 打印excel
	 * @param needRowNum
	 */
	public static void printExcel(int needRowNum){
		//创建打印流，打印
		try {
			//判断是否全部完成
			if(allRows!=needRowNum){
				return;
			}
			FileOutputStream writer=new FileOutputStream("d:/编程书籍评分排行.xls");
			workbook.write(writer);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

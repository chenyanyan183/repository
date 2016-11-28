package spider;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * 
 * ���ɱ���鼮���������е�ר��sheet��
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
				// 3: workbook ��װ ���� ... ����
				HSSFRow firstRow = sheet.createRow(0);
				firstRow.createCell(0).setCellValue("���");
				firstRow.createCell(1).setCellValue("����");
				firstRow.createCell(2).setCellValue("����");
				firstRow.createCell(3).setCellValue("��������");
				firstRow.createCell(4).setCellValue("����");
				firstRow.createCell(5).setCellValue("������");
				firstRow.createCell(6).setCellValue("��������");
				firstRow.createCell(7).setCellValue("�۸�");
			}
		}
		return sheet;
	}
	public static int getNextRowNum() {
		//��������ֹ�߳�ִ�в�������ͬһҳ�������
		synchronized (sheet) {
			//�޸���һ�е�����
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
	 * ��ӡexcel
	 * @param needRowNum
	 */
	public static void printExcel(int needRowNum){
		//������ӡ������ӡ
		try {
			//�ж��Ƿ�ȫ�����
			if(allRows!=needRowNum){
				return;
			}
			FileOutputStream writer=new FileOutputStream("d:/����鼮��������.xls");
			workbook.write(writer);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

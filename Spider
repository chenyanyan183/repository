package spider;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class Spider {
	public static void main(String[] args) {
		//获取当前时间
		long startTime = System.currentTimeMillis();
		
		//定义需要查看的页数
		final int k=5;
		//循环创建线程并运行
		for(int i=0;i<k;i++){
			//创建线程
			Runnable runnable = new Runnable() {
				public void run() {
					HSSFSheet sheet = Sheet.getSheet();
					int nextRowNum = Sheet.getNextRowNum();
					try {
						//得到一页的书籍信息
						String[][] message = GetHtml.getHtml(nextRowNum);
						// 循环 数据，创建行
						for (String[] array : message) {
							// 循环一次 创建一行...
							HSSFRow row = sheet.createRow(nextRowNum += 1);
							row.createCell(0).setCellValue(sheet.getLastRowNum());
							row.createCell(1).setCellValue(array[0]);
							row.createCell(2).setCellValue(array[1]);
							row.createCell(3).setCellValue(array[2]);
							row.createCell(4).setCellValue(array[3]);
							row.createCell(5).setCellValue(array[4]);
							row.createCell(6).setCellValue(array[5]);
							row.createCell(7).setCellValue(array[6]);
						}
						//将sheet表的完成量修改
						Sheet.setAllRows(Sheet.getAllRows()+message.length);
						//打印
						Sheet.printExcel(k*20);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		runnable.run();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间："+(endTime-startTime)+"ms");	
	}
}

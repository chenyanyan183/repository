package spider;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class Spider {
	public static void main(String[] args) {
		//��ȡ��ǰʱ��
		long startTime = System.currentTimeMillis();
		
		//������Ҫ�鿴��ҳ��
		final int k=5;
		//ѭ�������̲߳�����
		for(int i=0;i<k;i++){
			//�����߳�
			Runnable runnable = new Runnable() {
				public void run() {
					HSSFSheet sheet = Sheet.getSheet();
					int nextRowNum = Sheet.getNextRowNum();
					try {
						//�õ�һҳ���鼮��Ϣ
						String[][] message = GetHtml.getHtml(nextRowNum);
						// ѭ�� ���ݣ�������
						for (String[] array : message) {
							// ѭ��һ�� ����һ��...
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
						//��sheet���������޸�
						Sheet.setAllRows(Sheet.getAllRows()+message.length);
						//��ӡ
						Sheet.printExcel(k*20);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		runnable.run();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("��������ʱ�䣺"+(endTime-startTime)+"ms");	
	}
}

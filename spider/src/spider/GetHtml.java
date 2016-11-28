package spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class GetHtml {
	public static Proxy[] proxy=new Proxy[5];
	//ʹ�þ�̬�������ش�������
	static {
		//���ô��������
		proxy[0]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("113.9.122.226", 8080)); 
		proxy[1]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("60.14.174.30", 8080)); 
		proxy[2]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("61.173.224.218", 8080)); 
		proxy[3]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("58.19.12.239", 8080)); 
		proxy[4]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("122.156.129.201", 8080)); 
	}
	@Test
	public static String[][] getHtml(int start) throws Exception {
		URLConnection conn=null;
		//ѭ����ʹ���ڽ������ӵĹ����У���������쳣����ʹ�ô����ٴν�������
		for(int i=6;i>=0;i--){
			//�򿪺ͷ�����֮������ͨ��
			try {
				//����url
				URL url = new URL("https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?type=S&start="+start);
				//ʹ�ñ���ip������
				if(i==6){
					conn = url.openConnection();
					break;
				}else{
					conn = url.openConnection(proxy[i]);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				//�����һ������ip��������ʱ����
				if(i==0){
					throw new Exception("���д���������ã�");
				}
				continue;
			}
		}
		
		//��ȡ��������ȡ�����������ͻ�������
		BufferedReader reader = new BufferedReader( new InputStreamReader(conn.getInputStream(),"utf-8"));
		//��ȡ����
		String line = null;
		//�����鼮��Ϣ��flag��0��ʾ���飬1��ʾ������2��ʾ���֣�3��ʾ����������4��ʾ������Ϣ��
		int flag=0;
		//�����������鼮��Ϣ����һ������Ϊk����ʾ��k���飬�ڶ������꣬��ʾ�鼮��Ϣ��
		String[][] bookMessage=new String[20][7];
		int k=-1;
		//ѭ����ȡ�������ݷ���������
		while( (line = reader.readLine()) != null ){
			//�жϸ����Ƿ�Ϊ�ջ���������Ŀո�
			if(line==null||"".equals(line.trim())){
				continue;
			}else{
				//ȥ���߿ո�
				line=line.trim();
			}
			
			//�ж��Ƿ���������ܣ�
			if(line.contains(",from:'book_subject_search'})\">")){
				//��i��1
				k++;
				//��flag��Ϊ������1��
				flag=1;
				continue;
			}
			
			//�жϱ��������Ƿ�Ϊ����
			if(line.contains("<span class=\"rating_nums\">")){
				//��flag��Ϊ���֣�2��
				flag=2;
			}
			//�жϱ��������Ƿ�Ϊ��������
			if(line.contains("<span class=\"pl\">")){
				//��flag��Ϊ���֣�3��
				flag=3;
			}
			 
			//�жϱ��������Ƿ�Ϊ�鼮������Ϣ
			if(line.contains("<div class=\"pub\">")){
				//��flag��Ϊ���֣�4��
				flag=4;
			}
			
			//���������ж����ж��Ƿ��������������������
			if(flag!=0){
				//�У����ж��Ƿ�Ϊ������ǩ
				if("</a>".equals(line)||"</div>".equals(line)||"</span>".equals(line)){
					//����ǽ�����ǩ���ͽ�flag��Ϊ0
					flag=0;
				}
			}else{
				//û����ִ���¸�ѭ��
				continue;
			}
			Matcher m;
			//���в���
			switch (flag) {
				//��flagΪ1ʱ��ȡ����
				case 1:
					//�ж��Ƿ���ͼƬ������,�Ǿͽ�flag��Ϊ0,����i��һ
					if(line.contains("<img")){
						flag=0;
						k--;
						continue;
					}
					//�ж��Ƿ��а汾��Ϣ
					if(line.contains("</span>")){
						m=Pattern.compile("<span.*?>([\\s\\S]*)</span>").matcher(line);
					    while(m.find()){
					        line=m.group(1);
					    }
					    line=bookMessage[k][0]+line;
					}
					//�洢����
					bookMessage[k][0]=line;
					break;
				
				//��flagΪ2ʱ��ȡ����
				case 2:
					m=Pattern.compile("<span.*?>([\\s\\S]*)</span>").matcher(line);
				    while(m.find()){
				        line=m.group(1);
				    }
				    if(!line.isEmpty()){
						bookMessage[k][1]=line.trim();
					}
					break;	
					//��flagΪ3ʱ��ȡ��������
				case 3:
					//�жϸ����Ƿ�������
					if(!(line=line.trim()).isEmpty()){
						m=Pattern.compile("\\(?(\\d+)[^\\d]+").matcher(line);
					    while(m.find()){
					    	bookMessage[k][2]=m.group(1).trim();
					    }
					}
					break;
					//��flagΪ2ʱ��ȡ���ߣ������磬����ʱ�䣬�۸�
				case 4:
					//�жϸ����Ƿ�������
					if(!(line=line.trim()).isEmpty()){
						m=Pattern.compile("([^/]+)/(([^/]+/)?)([^/]+)/([^/]+)/([^/]+)").matcher(line);
					    while(m.find()){
					    	bookMessage[k][3]=m.group(1).trim();
					    	bookMessage[k][4]=m.group(4).trim();
					    	bookMessage[k][5]=m.group(5).trim();
					    	bookMessage[k][6]=m.group(6).trim();
					    }
					}
					break;
			}
		}
		return bookMessage;
	}
}

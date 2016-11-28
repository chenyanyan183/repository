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
	//使用静态代码块加载代理数组
	static {
		//设置代理的数组
		proxy[0]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("113.9.122.226", 8080)); 
		proxy[1]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("60.14.174.30", 8080)); 
		proxy[2]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("61.173.224.218", 8080)); 
		proxy[3]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("58.19.12.239", 8080)); 
		proxy[4]= new Proxy(Proxy.Type.HTTP, new InetSocketAddress("122.156.129.201", 8080)); 
	}
	@Test
	public static String[][] getHtml(int start) throws Exception {
		URLConnection conn=null;
		//循环，使得在建立连接的过程中，如果出现异常可以使用代理再次建立连接
		for(int i=6;i>=0;i--){
			//打开和服务器之间连接通道
			try {
				//设置url
				URL url = new URL("https://book.douban.com/tag/%E7%BC%96%E7%A8%8B?type=S&start="+start);
				//使用本地ip打开连接
				if(i==6){
					conn = url.openConnection();
					break;
				}else{
					conn = url.openConnection(proxy[i]);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
				//当最后一个代理ip出现问题时报错
				if(i==0){
					throw new Exception("所有代理均不可用！");
				}
				continue;
			}
		}
		
		//获取输入流读取服务器发给客户端数据
		BufferedReader reader = new BufferedReader( new InputStreamReader(conn.getInputStream(),"utf-8"));
		//读取数据
		String line = null;
		//设置书籍信息的flag（0表示无书，1表示书名，2表示评分，3表示评价人数，4表示多项信息）
		int flag=0;
		//定义数组存放书籍信息（第一个坐标为k，表示第k本书，第二个坐标，表示书籍信息）
		String[][] bookMessage=new String[20][7];
		int k=-1;
		//循环读取，将数据放入数组中
		while( (line = reader.readLine()) != null ){
			//判断该行是否为空或者无意义的空格
			if(line==null||"".equals(line.trim())){
				continue;
			}else{
				//去两边空格
				line=line.trim();
			}
			
			//判定是否是新书介绍，
			if(line.contains(",from:'book_subject_search'})\">")){
				//将i加1
				k++;
				//将flag置为书名（1）
				flag=1;
				continue;
			}
			
			//判断本句内容是否为评分
			if(line.contains("<span class=\"rating_nums\">")){
				//将flag置为评分（2）
				flag=2;
			}
			//判断本句内容是否为评价人数
			if(line.contains("<span class=\"pl\">")){
				//将flag置为评分（3）
				flag=3;
			}
			 
			//判断本句内容是否为书籍多项信息
			if(line.contains("<div class=\"pub\">")){
				//将flag置为评分（4）
				flag=4;
			}
			
			//经过内容判定后，判断是否处于有意义的内容区域内
			if(flag!=0){
				//有，则判定是否为结束标签
				if("</a>".equals(line)||"</div>".equals(line)||"</span>".equals(line)){
					//如果是结束标签，就将flag置为0
					flag=0;
				}
			}else{
				//没有则执行下个循环
				continue;
			}
			Matcher m;
			//进行操作
			switch (flag) {
				//当flag为1时提取书名
				case 1:
					//判断是否是图片的内容,是就将flag置为0,并将i减一
					if(line.contains("<img")){
						flag=0;
						k--;
						continue;
					}
					//判断是否含有版本信息
					if(line.contains("</span>")){
						m=Pattern.compile("<span.*?>([\\s\\S]*)</span>").matcher(line);
					    while(m.find()){
					        line=m.group(1);
					    }
					    line=bookMessage[k][0]+line;
					}
					//存储数据
					bookMessage[k][0]=line;
					break;
				
				//当flag为2时提取评分
				case 2:
					m=Pattern.compile("<span.*?>([\\s\\S]*)</span>").matcher(line);
				    while(m.find()){
				        line=m.group(1);
				    }
				    if(!line.isEmpty()){
						bookMessage[k][1]=line.trim();
					}
					break;	
					//当flag为3时提取评价人数
				case 3:
					//判断该行是否有内容
					if(!(line=line.trim()).isEmpty()){
						m=Pattern.compile("\\(?(\\d+)[^\\d]+").matcher(line);
					    while(m.find()){
					    	bookMessage[k][2]=m.group(1).trim();
					    }
					}
					break;
					//当flag为2时提取作者，出版社，出版时间，价格
				case 4:
					//判断该行是否有内容
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

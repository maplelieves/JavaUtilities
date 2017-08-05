import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;


/**
 * @author lieves
 * @category muti-Thread download
 * @version 1.00
 */
public class MultiThreadDownload {
	
	// 定义下载资源的路径
	private String UrlSrc;
	// 定义存储文件的路径
	private String pathDst;
	// 定义下载使用线程数目
	private int threadNum;
	// 定义下载的文件总大小
	private int fileSize;
	// 定义下载的线程对象
	private DownThread[] threads;
	
	
	/**
	 * @constructor
	 */
	private MultiThreadDownload(){};
	public MultiThreadDownload(String UrlSrc, String pathDst, int threadNum)
	{
		this.UrlSrc = UrlSrc;
		this.pathDst = pathDst;
		this.threadNum = threadNum;
		this.threads = new DownThread[threadNum];
	}
	
	/**
	 * @throws Exception 
	 * @method
	 */
	public void download() throws Exception
	{
		URL url = new URL(UrlSrc);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", 
				"image/gif, image/jpeg, image/pjpeg, image/png"
				+ "application/x-shocwave-flash, application/xaml+xml"
				+ "application/vnd.ms-xpsdocument, application/vnd.ms-excel"
				+ "application/vnd.ms-powerpoint, application/msword, */*" );
		connection.setRequestProperty("Accept-Language", "zh-CN");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");
		fileSize = connection.getContentLength();
		connection.disconnect();
		// 部署下载任务
		RandomAccessFile file = new RandomAccessFile(pathDst, "rw");
		file.setLength(fileSize);
		file.close();
		int partionSize = fileSize/threadNum + (fileSize%threadNum>0?1:0);
		if(threadNum <= 1) {
			RandomAccessFile partionfile = new RandomAccessFile(pathDst, "rw");
			threads[0] = new DownThread(0, partionSize, partionfile);	
			threads[0].start();
		}
		else {
			for(int i=0; i<threadNum; i++) 
			{
				int offset = i * partionSize;
				RandomAccessFile partionfile = new RandomAccessFile(pathDst, "rw");
				partionfile.seek(offset);
				threads[i] = new DownThread(offset, 
								i!=threadNum-1? partionSize : fileSize-partionSize*(threadNum-2), 
								partionfile );
				threads[i].start();
			}
		}
	}
	
	/**
	 * 下载完成比率
	 */
	public int getCompleteSize()
	{
		int sumSize = 0;
		
		for(int i=0; i<threadNum; i++)
		{
			sumSize += threads[i].getIndex();
		}
		
		return sumSize;
	}
	/**
	 * 下载完成比率
	 */
	public double getCompleteRate()
	{
		int sumSize = 0;
		double completeRate = 0;
		
		for(int i=0; i<threadNum; i++)
		{
			sumSize += threads[i].getIndex();
		}
		
		try {
			completeRate =  (double)sumSize/fileSize;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return completeRate;
	}
	
	private class DownThread extends Thread
	{
		// 当前块下载大小，文件中偏移量，数据下标
		private int partSize;
		private int offset;
		private int index;
		private RandomAccessFile randomAccessFile;
		
		{
			index = 0;
		}
		
		/**
		 * @constructor
		 */
		private DownThread() {}
		public DownThread(int offset, int partSize, RandomAccessFile randomAccessFile)
		{
			this.offset = offset;
			this.partSize = partSize;
			this.randomAccessFile = randomAccessFile;
		}
		
		/**
		 * getter and setter
		 */
		public int getPartSize()
		{
			return partSize;
		}
		public int getOffset()
		{
			return offset;
		}
		public int getIndex()
		{
			return index;
		}
		
		@Override
		public void run()
		{
			try
			{
				URL url = new URL(UrlSrc);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", 
						"image/gif, image/jpeg, image/pjpeg, image/png"
						+ "application/x-shocwave-flash, application/xaml+xml"
						+ "application/vnd.ms-xpsdocument, application/vnd.ms-excel"
						+ "application/vnd.ms-powerpoint, application/msword, */*" );
				connection.setRequestProperty("Accept-Language", "zh-CN");
				connection.setRequestProperty("Charset", "UTF-8");
				InputStream inputStream = connection.getInputStream();
				inputStream.skip(this.offset);
				byte[] buffer = new byte[1024];
				int rw = 0;
				while(index<partSize && (rw=inputStream.read(buffer))>=0 )
				{
					randomAccessFile.write(buffer, 0, rw);
					index += rw;
				}
				randomAccessFile.close();
				inputStream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}

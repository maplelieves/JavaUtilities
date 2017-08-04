import java.io.RandomAccessFile;




/**
 * @author lieves
 * @category muti-Thread download
 * @version 1.00
 */
public class DownUtil {
	
	// 定义下载资源的路径
	private String pathSrc;
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
	private DownUtil(){};
	public DownUtil(String pathSrc, String pathDst, int threadNum)
	{
		this.pathSrc = pathSrc;
		this.pathDst = pathDst;
		this.threadNum = threadNum;
		this.threads = new DownThread[threadNum];
	}
	
	/**
	 * @method
	 */
	public void download()
	{
		
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
		public DownThread()
		{
			this(0, 0, null);
		}
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
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}



public class Download {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		final MultiThreadDownload multiThreadDownload = new MultiThreadDownload(
				//"https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png",
				//"BaiDu.png",
				"https://cdn.kernel.org/pub/linux/kernel/v3.x/linux-3.18.63.tar.xz",
				"linux-3.18.63.tar.xz",
				4 );
		multiThreadDownload.download();
		class ShowDownloadInfo implements Runnable
		{
			int size = 0;
			@Override
			public void run()
			{
				while(multiThreadDownload.getCompleteRate()<1) {
					
					System.out.printf(	"download:" 
										+ "%5.2f" + "%%"
										+ "--"
										+ "%5.1f" + "KB/s"
										+ "\r\n",
										multiThreadDownload.getCompleteRate()*100,
										(multiThreadDownload.getCompleteSize()-size)/1024.0);
					size = multiThreadDownload.getCompleteSize();
					try {
						Thread.sleep(1000);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		new Thread (new ShowDownloadInfo()).start();
	}

}

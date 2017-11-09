using com.mokylin;

namespace Bleach.Net
{
	public class CGStageReportHandler
	{
		public static void Send(bool isWin,int stars,byte[] report)
		{						

			CGStageReport package = new CGStageReport();
			
			package.isWin = isWin;
			package.stars = stars;
			package.report = report;

			NetHandler.OnSend(703, NetHandler.Serialize(package));

		}
	}
}



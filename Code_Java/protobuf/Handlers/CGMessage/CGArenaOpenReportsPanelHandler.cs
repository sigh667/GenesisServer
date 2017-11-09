using com.mokylin;

namespace Bleach.Net
{
	public class CGArenaOpenReportsPanelHandler
	{
		public static void Send()
		{						

			CGArenaOpenReportsPanel package = new CGArenaOpenReportsPanel();
			

			NetHandler.OnSend(654, NetHandler.Serialize(package));

		}
	}
}



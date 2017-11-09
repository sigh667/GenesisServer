using com.mokylin;

namespace Bleach.Net
{
	public class CGArenaViewAllReportsHandler
	{
		public static void Send()
		{						

			CGArenaViewAllReports package = new CGArenaViewAllReports();
			

			NetHandler.OnSend(655, NetHandler.Serialize(package));

		}
	}
}



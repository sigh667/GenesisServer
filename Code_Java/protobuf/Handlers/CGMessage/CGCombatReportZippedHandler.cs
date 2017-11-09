using com.mokylin;

namespace Bleach.Net
{
	public class CGCombatReportZippedHandler
	{
		public static void Send(CombatReportId reportId)
		{						

			CGCombatReportZipped package = new CGCombatReportZipped();
			
			package.reportId = reportId;

			NetHandler.OnSend(751, NetHandler.Serialize(package));

		}
	}
}



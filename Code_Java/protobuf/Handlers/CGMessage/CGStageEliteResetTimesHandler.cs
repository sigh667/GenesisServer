using com.mokylin;

namespace Bleach.Net
{
	public class CGStageEliteResetTimesHandler
	{
		public static void Send(int stageEliteId)
		{						

			CGStageEliteResetTimes package = new CGStageEliteResetTimes();
			
			package.stageEliteId = stageEliteId;

			NetHandler.OnSend(705, NetHandler.Serialize(package));

		}
	}
}



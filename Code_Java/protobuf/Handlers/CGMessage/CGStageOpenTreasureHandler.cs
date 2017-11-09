using com.mokylin;

namespace Bleach.Net
{
	public class CGStageOpenTreasureHandler
	{
		public static void Send(int stageId)
		{						

			CGStageOpenTreasure package = new CGStageOpenTreasure();
			
			package.stageId = stageId;

			NetHandler.OnSend(706, NetHandler.Serialize(package));

		}
	}
}



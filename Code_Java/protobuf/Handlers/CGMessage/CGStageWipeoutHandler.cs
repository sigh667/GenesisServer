using com.mokylin;

namespace Bleach.Net
{
	public class CGStageWipeoutHandler
	{
		public static void Send(StageType type,int stageId,int times)
		{						

			CGStageWipeout package = new CGStageWipeout();
			
			package.type = type;
			package.stageId = stageId;
			package.times = times;

			NetHandler.OnSend(704, NetHandler.Serialize(package));

		}
	}
}



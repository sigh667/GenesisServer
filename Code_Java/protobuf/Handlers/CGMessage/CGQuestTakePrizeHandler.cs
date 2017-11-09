using com.mokylin;

namespace Bleach.Net
{
	public class CGQuestTakePrizeHandler
	{
		public static void Send(int questId)
		{						

			CGQuestTakePrize package = new CGQuestTakePrize();
			
			package.questId = questId;

			NetHandler.OnSend(803, NetHandler.Serialize(package));

		}
	}
}



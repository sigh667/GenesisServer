using com.mokylin;

namespace Bleach.Net
{
	public class CGQuestCompleteHandler
	{
		public static void Send(int questId)
		{						

			CGQuestComplete package = new CGQuestComplete();
			
			package.questId = questId;

			NetHandler.OnSend(802, NetHandler.Serialize(package));

		}
	}
}



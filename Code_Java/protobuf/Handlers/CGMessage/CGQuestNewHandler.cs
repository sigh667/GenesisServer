using com.mokylin;

namespace Bleach.Net
{
	public class CGQuestNewHandler
	{
		public static void Send(int questId)
		{						

			CGQuestNew package = new CGQuestNew();
			
			package.questId = questId;

			NetHandler.OnSend(801, NetHandler.Serialize(package));

		}
	}
}



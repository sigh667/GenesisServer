using com.mokylin;

namespace Bleach.Net
{
	public class CGItemUseHandler
	{
		public static void Send(int templateId,int amount,long heroId)
		{						

			CGItemUse package = new CGItemUse();
			
			package.templateId = templateId;
			package.amount = amount;
			package.heroId = heroId;

			NetHandler.OnSend(101, NetHandler.Serialize(package));

		}
	}
}



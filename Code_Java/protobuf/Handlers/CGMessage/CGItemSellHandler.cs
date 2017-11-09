using com.mokylin;

namespace Bleach.Net
{
	public class CGItemSellHandler
	{
		public static void Send(int templateId,int amount)
		{						

			CGItemSell package = new CGItemSell();
			
			package.templateId = templateId;
			package.amount = amount;

			NetHandler.OnSend(102, NetHandler.Serialize(package));

		}
	}
}



using com.mokylin;

namespace Bleach.Net
{
	public class CGOpenShopHandler
	{
		public static void Send(int shopTypeId)
		{						

			CGOpenShop package = new CGOpenShop();
			
			package.shopTypeId = shopTypeId;

			NetHandler.OnSend(401, NetHandler.Serialize(package));

		}
	}
}



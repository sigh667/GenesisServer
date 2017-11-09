using com.mokylin;

namespace Bleach.Net
{
	public class CGRefreshGoodsHandler
	{
		public static void Send(int shopTypeId)
		{						

			CGRefreshGoods package = new CGRefreshGoods();
			
			package.shopTypeId = shopTypeId;

			NetHandler.OnSend(402, NetHandler.Serialize(package));

		}
	}
}



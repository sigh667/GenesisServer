using com.mokylin;

namespace Bleach.Net
{
	public class CGBuyGoodHandler
	{
		public static void Send(int shopTypeId,int goodPosition,long curGoodBornTime)
		{						

			CGBuyGood package = new CGBuyGood();
			
			package.shopTypeId = shopTypeId;
			package.goodPosition = goodPosition;
			package.curGoodBornTime = curGoodBornTime;

			NetHandler.OnSend(403, NetHandler.Serialize(package));

		}
	}
}



using com.mokylin;

namespace Bleach.Net
{
	public class CGBuyPointHandler
	{
		public static void Send(int pointType,int counts)
		{						

			CGBuyPoint package = new CGBuyPoint();
			
			package.pointType = pointType;
			package.counts = counts;

			NetHandler.OnSend(501, NetHandler.Serialize(package));

		}
	}
}



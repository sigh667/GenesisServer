using com.mokylin;

namespace Bleach.Net
{
	public class CGTakePrizeHandler
	{
		public static void Send(int mailId)
		{						

			CGTakePrize package = new CGTakePrize();
			
			package.mailId = mailId;

			NetHandler.OnSend(603, NetHandler.Serialize(package));

		}
	}
}



using com.mokylin;

namespace Bleach.Net
{
	public class CGGetAllMailsHandler
	{
		public static void Send()
		{						

			CGGetAllMails package = new CGGetAllMails();
			

			NetHandler.OnSend(601, NetHandler.Serialize(package));

		}
	}
}



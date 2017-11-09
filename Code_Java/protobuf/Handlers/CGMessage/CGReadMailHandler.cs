using com.mokylin;

namespace Bleach.Net
{
	public class CGReadMailHandler
	{
		public static void Send(int mailId)
		{						

			CGReadMail package = new CGReadMail();
			
			package.mailId = mailId;

			NetHandler.OnSend(602, NetHandler.Serialize(package));

		}
	}
}



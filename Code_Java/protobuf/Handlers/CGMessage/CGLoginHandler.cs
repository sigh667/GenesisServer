using com.mokylin;

namespace Bleach.Net
{
	public class CGLoginHandler
	{
		public static void Send(int serverId, string accountId,string channel,string loginKey,long timestamp)
		{						

			CGLogin package = new CGLogin();
			
			package.accountId = accountId;
			package.channel = channel;
			package.loginKey = loginKey;
			package.timestamp = timestamp;

			NetHandler.OnLoginSend(51, serverId, NetHandler.Serialize(package));

		}
	}
}



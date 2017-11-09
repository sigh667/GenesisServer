using com.mokylin;

namespace Bleach.Net
{
	public class CGLoginFenSiHandler
	{
		public static void Send(int serverId, string accountId,string channel,string loginKey,long timestamp,string password,bool isRegister)
		{						

			CGLoginFenSi package = new CGLoginFenSi();
			
			package.accountId = accountId;
			package.channel = channel;
			package.loginKey = loginKey;
			package.timestamp = timestamp;
			package.password = password;
			package.isRegister = isRegister;

			NetHandler.OnLoginSend(54, serverId, NetHandler.Serialize(package));

		}
	}
}



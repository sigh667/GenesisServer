using com.mokylin;

namespace Bleach.Net
{
	public class CGGmCmdMessageHandler
	{
		public static void Send(string cmd,string[] param)
		{						

			CGGmCmdMessage package = new CGGmCmdMessage();
			
			package.cmd = cmd;
			package.param.AddRange(param);

			NetHandler.OnSend(301, NetHandler.Serialize(package));

		}
	}
}



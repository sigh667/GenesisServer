using com.mokylin;

namespace Bleach.Net
{
	public class CGLingYaPhaseUpHandler
	{
		public static void Send(int heroGroupid)
		{						

			CGLingYaPhaseUp package = new CGLingYaPhaseUp();
			
			package.heroGroupid = heroGroupid;

			NetHandler.OnSend(208, NetHandler.Serialize(package));

		}
	}
}



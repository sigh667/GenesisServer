using com.mokylin;

namespace Bleach.Net
{
	public class CGArenaOpenPanelHandler
	{
		public static void Send()
		{						

			CGArenaOpenPanel package = new CGArenaOpenPanel();
			

			NetHandler.OnSend(651, NetHandler.Serialize(package));

		}
	}
}



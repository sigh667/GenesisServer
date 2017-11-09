using com.mokylin;

namespace Bleach.Net
{
	public class CGArenaResetEnemysHandler
	{
		public static void Send()
		{						

			CGArenaResetEnemys package = new CGArenaResetEnemys();
			

			NetHandler.OnSend(653, NetHandler.Serialize(package));

		}
	}
}



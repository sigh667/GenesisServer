using com.mokylin;

namespace Bleach.Net
{
	public class CGChangeHeadFrameHandler
	{
		public static void Send(int headFrameId)
		{						

			CGChangeHeadFrame package = new CGChangeHeadFrame();
			
			package.headFrameId = headFrameId;

			NetHandler.OnSend(503, NetHandler.Serialize(package));

		}
	}
}



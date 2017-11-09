using com.mokylin;

namespace Bleach.Net
{
	public class CGOpenWindowHandler
	{
		public static void Send(int windowTypeId)
		{						

			CGOpenWindow package = new CGOpenWindow();
			
			package.windowTypeId = windowTypeId;

			NetHandler.OnSend(451, NetHandler.Serialize(package));

		}
	}
}



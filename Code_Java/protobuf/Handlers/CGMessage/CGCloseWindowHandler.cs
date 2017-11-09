using com.mokylin;

namespace Bleach.Net
{
	public class CGCloseWindowHandler
	{
		public static void Send(int windowTypeId)
		{						

			CGCloseWindow package = new CGCloseWindow();
			
			package.windowTypeId = windowTypeId;

			NetHandler.OnSend(452, NetHandler.Serialize(package));

		}
	}
}



using com.mokylin;

namespace Bleach.Net
{
	public class CGSelectRoleHandler
	{
		public static void Send(long id)
		{						

			CGSelectRole package = new CGSelectRole();
			
			package.id = id;

			NetHandler.OnSend(53, NetHandler.Serialize(package));

		}
	}
}



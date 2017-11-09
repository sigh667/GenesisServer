using com.mokylin;

namespace Bleach.Net
{
	public class CGHumanRenameHandler
	{
		public static void Send(string name)
		{						

			CGHumanRename package = new CGHumanRename();
			
			package.name = name;

			NetHandler.OnSend(502, NetHandler.Serialize(package));

		}
	}
}



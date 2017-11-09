using com.mokylin;

namespace Bleach.Net
{
	public class CGItemCompoundHandler
	{
		public static void Send(int toTemplateId)
		{						

			CGItemCompound package = new CGItemCompound();
			
			package.toTemplateId = toTemplateId;

			NetHandler.OnSend(103, NetHandler.Serialize(package));

		}
	}
}



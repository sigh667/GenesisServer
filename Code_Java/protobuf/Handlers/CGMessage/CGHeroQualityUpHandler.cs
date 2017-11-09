using com.mokylin;

namespace Bleach.Net
{
	public class CGHeroQualityUpHandler
	{
		public static void Send(long id)
		{						

			CGHeroQualityUp package = new CGHeroQualityUp();
			
			package.id = id;

			NetHandler.OnSend(203, NetHandler.Serialize(package));

		}
	}
}



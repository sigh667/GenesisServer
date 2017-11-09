using com.mokylin;

namespace Bleach.Net
{
	public class CGHeroStarUpHandler
	{
		public static void Send(long id)
		{						

			CGHeroStarUp package = new CGHeroStarUp();
			
			package.id = id;

			NetHandler.OnSend(202, NetHandler.Serialize(package));

		}
	}
}



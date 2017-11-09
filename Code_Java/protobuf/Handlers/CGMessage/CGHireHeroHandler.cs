using com.mokylin;

namespace Bleach.Net
{
	public class CGHireHeroHandler
	{
		public static void Send(int groupId)
		{						

			CGHireHero package = new CGHireHero();
			
			package.groupId = groupId;

			NetHandler.OnSend(201, NetHandler.Serialize(package));

		}
	}
}



using com.mokylin;

namespace Bleach.Net
{
	public class CGChangeAvatarHandler
	{
		public static void Send(long heroUuid)
		{						

			CGChangeAvatar package = new CGChangeAvatar();
			
			package.heroUuid = heroUuid;

			NetHandler.OnSend(504, NetHandler.Serialize(package));

		}
	}
}



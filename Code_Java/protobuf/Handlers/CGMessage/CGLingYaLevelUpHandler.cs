using com.mokylin;

namespace Bleach.Net
{
	public class CGLingYaLevelUpHandler
	{
		public static void Send(int heroGroupid,int templateId)
		{						

			CGLingYaLevelUp package = new CGLingYaLevelUp();
			
			package.heroGroupid = heroGroupid;
			package.templateId = templateId;

			NetHandler.OnSend(207, NetHandler.Serialize(package));

		}
	}
}



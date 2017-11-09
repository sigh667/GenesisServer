using com.mokylin;

namespace Bleach.Net
{
	public class CDKillCDHandler
	{
		public static void Send(com.mokylin.CDType cdType)
		{						

			CDKillCD package = new CDKillCD();
			
			package.cdType = cdType;

			NetHandler.OnSend(506, NetHandler.Serialize(package));

		}
	}
}



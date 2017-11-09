using com.mokylin;

namespace Bleach.Net
{
	public class CGUpdateFormationHandler
	{
		public static void Send(com.mokylin.DBFormation formation)
		{						

			CGUpdateFormation package = new CGUpdateFormation();
			
			package.formation = formation;

			NetHandler.OnSend(505, NetHandler.Serialize(package));

		}
	}
}



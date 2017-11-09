using com.mokylin;

namespace Bleach.Net
{
	public class CGHeroWearEquipHandler
	{
		public static void Send(long id,int position)
		{						

			CGHeroWearEquip package = new CGHeroWearEquip();
			
			package.id = id;
			package.position = position;

			NetHandler.OnSend(205, NetHandler.Serialize(package));

		}
	}
}



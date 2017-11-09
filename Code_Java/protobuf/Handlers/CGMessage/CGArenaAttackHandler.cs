using com.mokylin;

namespace Bleach.Net
{
	public class CGArenaAttackHandler
	{
		public static void Send(int rank,long[] heroUuids,bool isUnconditionalAttack)
		{						

			CGArenaAttack package = new CGArenaAttack();
			
			package.rank = rank;
			package.heroUuids.AddRange(heroUuids);
			package.isUnconditionalAttack = isUnconditionalAttack;

			NetHandler.OnSend(652, NetHandler.Serialize(package));

		}
	}
}



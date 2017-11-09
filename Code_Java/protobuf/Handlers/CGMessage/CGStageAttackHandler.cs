using com.mokylin;

namespace Bleach.Net
{
	public class CGStageAttackHandler
	{
		public static void Send(int stageId,long[] heros)
		{						

			CGStageAttack package = new CGStageAttack();
			
			package.stageId = stageId;
			package.heros.AddRange(heros);

			NetHandler.OnSend(701, NetHandler.Serialize(package));

		}
	}
}



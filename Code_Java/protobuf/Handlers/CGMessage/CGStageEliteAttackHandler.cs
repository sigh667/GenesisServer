using com.mokylin;

namespace Bleach.Net
{
	public class CGStageEliteAttackHandler
	{
		public static void Send(int stageId,long[] heros)
		{						

			CGStageEliteAttack package = new CGStageEliteAttack();
			
			package.stageId = stageId;
			package.heros.AddRange(heros);

			NetHandler.OnSend(702, NetHandler.Serialize(package));

		}
	}
}



using com.mokylin;

namespace Bleach.Net
{
	public class CGGetDailyRewardHandler
	{
		public static void Send(int rewardIndex)
		{						

			CGGetDailyReward package = new CGGetDailyReward();
			
			package.rewardIndex = rewardIndex;

			NetHandler.OnSend(351, NetHandler.Serialize(package));

		}
	}
}



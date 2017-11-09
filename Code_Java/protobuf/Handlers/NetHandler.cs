using Bleach.Util;
using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;

using com.mokylin;


namespace Bleach.Net
{
	public class NetHandler
	{
		[AttributeUsage(AttributeTargets.Method)]
		public class PackageHandlerRegister : Attribute
		{
			internal readonly Type type = null;

			public PackageHandlerRegister(Type type)
			{
				this.type = type;
			}
		}

		public static Action<short, byte[]> OnSend = delegate {};

		public static Action<short, int, byte[]> OnLoginSend = delegate { };

		private static readonly Dictionary<int, Type> packageTypes = new Dictionary<int, Type>()
		{
			
			
			{651, typeof(GCArenaInfo)},
			
			{652, typeof(GCArenaEnemys)},
			
			{653, typeof(GCArenaSelectedEnemyChange)},
			
			{654, typeof(GCArenaCombat)},
			
			{658, typeof(GCArenaIsSendingReward)},
			
			{655, typeof(GCArenaHasNewReport)},
			
			{656, typeof(GCArenaReports)},
			
			{657, typeof(GCArenaReportUpdate)},
			
			{551, typeof(GCGmCmd)},
			
			{752, typeof(GCCombatInitDataMission)},
			
			{753, typeof(GCCombatInitDataExpedition)},
			
			{751, typeof(GCCombatReportZipped)},
			
			{401, typeof(GCDailyRewardInfo)},
			
			{402, typeof(GCGetDailyRewardFailed)},
			
			{403, typeof(GCGetDailyRewardSuccess)},
			
			{501, typeof(GCFunctionOpen)},
			
			{202, typeof(GCHeroAdd)},
			
			{201, typeof(GCChangedProps)},
			
			{205, typeof(GCHeroLingYaCrit)},
			
			{204, typeof(GCHeroLingYaInfos)},
			
			{101, typeof(GCHumanDetailInfo)},
			
			{102, typeof(GCDataError)},
			
			{103, typeof(GCServerException)},
			
			{106, typeof(GCPointRecoverInfo)},
			
			{108, typeof(GCBuyPointCountsReset)},
			
			{109, typeof(GCVipLevelUp)},
			
			{110, typeof(GCHumanRenameFailed)},
			
			{111, typeof(GCHumanRenameSuccess)},
			
			{112, typeof(GCCdStart)},
			
			{113, typeof(GCCdEnd)},
			
			{801, typeof(GCDailyQuestRefresh)},
			
			{802, typeof(GCCronQuestRefresh)},
			
			{803, typeof(GCQuestComplete)},
			
			{804, typeof(GCQuestClose)},
			
			{301, typeof(GCItemUpdate)},
			
			{603, typeof(GCMailUnread)},
			
			{602, typeof(GCAddMails)},
			
			{601, typeof(GCAllMails)},
			
			{701, typeof(GCStageDrop)},
			
			{702, typeof(GCStageWipeoutResult)},
			
			{703, typeof(GCStageEliteReset)},
			
			{51, typeof(GCLoginFail)},
			
			{52, typeof(GCRoleList)},
			
			{451, typeof(GCShopInfo)},
			
			{452, typeof(GCShopManuallyRefreshInfo)},
			
			{455, typeof(GCShopAutoRefreshInfo)},
			
			{453, typeof(GCGoodBuySuccess)},
			
			{454, typeof(GCShopManuallyRefreshCountReset)},
			
			{459, typeof(GCOpenShop)},
			
			{456, typeof(GCShopClose)},
			
			{457, typeof(GCShopPrompt)},
						
		};

		private static readonly Dictionary<Type, Action<object>> handlers = new Dictionary<Type, Action<object>>(packageTypes.Count);
		
		private static readonly ProtoBufSerializer serializer = new ProtoBufSerializer();
		
		public static object Derialize(byte[] data, Type type)
		{
			return serializer.Deserialize(new MemoryStream(data), null, type);
		}
		
		public static byte[] Serialize(object data)
		{
			byte[] buffer;

			using (MemoryStream mem = new MemoryStream())
			{
				serializer.Serialize(mem, data);
				buffer = new byte[mem.Length];
				mem.Seek(0, SeekOrigin.Begin);
				mem.Read(buffer, 0, buffer.Length);
			}
			return buffer;
		}

		public static void RegisterGCHandler(object register)
		{
			Action<object, MethodInfo, PackageHandlerRegister> processor = (i, info, attribute) =>
			{
				Type packageType = attribute.type;
				Action<object> handler = null;
				handlers.TryGetValue(packageType, out handler);
				MethodInfo mi = info;
				if (null == handler)
				{
					handler = (Action<object>)Delegate.CreateDelegate(typeof (Action<object>), register, mi, false);
					handlers.Add(packageType, handler);
				}
				else
				{
					LogUtil.Log("Package type {0} has already registed a handler!!!", packageType.Name);
				}
			};
			ReflectionHelper.ProcessMemberWithCustomAttribute(register, processor, false);
		}

		public static void OnReceive(int packageType, byte[] data)
		{
			Type type = null;

			packageTypes.TryGetValue(packageType, out type);

			if (null != type)
			{
				var packet = Derialize(data, type);

				Action<object> handler = null;
				handlers.TryGetValue(type, out handler);

				if (null != handler)
				{
					handler(packet);
				}
				else
				{
					LogUtil.Log("Can't find handler for message {0}", type.Name);
				}
			}
			else
			{
					LogUtil.Log("Can't find type with packageType {0}", packageType);
			}
		}
	}
}

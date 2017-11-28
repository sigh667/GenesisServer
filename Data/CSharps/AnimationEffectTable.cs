using System;

namespace Bleach.Table
{
	public class AnimationEffectTable : TableLoader
	{
		
		//角色ID
		public readonly int CharacterID;
		
		//延迟时间
		public readonly float DelayTime;
		
		// Constructor	
		public AnimationEffectTable(string[] content,int id):base(content)
		{
			this.CharacterID = Convert.ToInt32(content[currentIndex++]);
			this.DelayTime = Convert.ToSingle(content[currentIndex++]);
				
		}

	}
}



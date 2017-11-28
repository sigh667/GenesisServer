using System;

namespace Bleach.Table
{
	public class BuffTable : TableLoader
	{
		
		//x
		public readonly String EffectID;
		
		//y
		public readonly String TimeSelectEffctID;
		
		//z
		public readonly String EndEffectID;
		
		//${field.comment}
		public readonly bool RoundCheckBeforAttack;
		
		//${field.comment}
		public readonly float EffectCD;
		
		//${field.comment}
		public readonly float BuffRemainTime;
		
		//${field.comment}
		public readonly String YuanSuLeiXing;
		
		// Constructor	
		public BuffTable(string[] content,int id):base(content)
		{
			this.EffectID = ToCachedString(content[currentIndex++]);
			this.TimeSelectEffctID = ToCachedString(content[currentIndex++]);
			this.EndEffectID = ToCachedString(content[currentIndex++]);
			this.RoundCheckBeforAttack = Convert.ToBoolean(content[currentIndex++]);
			this.EffectCD = Convert.ToSingle(content[currentIndex++]);
			this.BuffRemainTime = Convert.ToSingle(content[currentIndex++]);
			this.YuanSuLeiXing = ToCachedString(content[currentIndex++]);
				
		}

	}
}



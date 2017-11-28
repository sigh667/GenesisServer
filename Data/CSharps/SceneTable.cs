using System;

namespace Bleach.Table
{
	public class SceneTable : TableLoader
	{
		
		//${field.comment}
		public readonly String Type;
		
		//${field.comment}
		public readonly String PrefabName;
		
		//${field.comment}
		public readonly String CameraName;
		
		//${field.comment}
		public readonly int FighterCount;
		
		// Constructor	
		public SceneTable(string[] content,int id):base(content)
		{
			this.Type = ToCachedString(content[currentIndex++]);
			this.PrefabName = ToCachedString(content[currentIndex++]);
			this.CameraName = ToCachedString(content[currentIndex++]);
			this.FighterCount = Convert.ToInt32(content[currentIndex++]);
				
		}

	}
}



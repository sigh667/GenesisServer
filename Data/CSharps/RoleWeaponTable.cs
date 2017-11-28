using System;

namespace Bleach.Table
{
	public class RoleWeaponTable : TableLoader
	{
		
		//${field.comment}
		public readonly String WeaponPrefabName;
		
		//${field.comment}
		public readonly String BindingPointName;
		
		//${field.comment}
		public readonly String WeaponPrefabName;
		
		//${field.comment}
		public readonly String BindingPointName;
		
		// Constructor	
		public RoleWeaponTable(string[] content,int id):base(content)
		{
			this.WeaponPrefabName = ToCachedString(content[currentIndex++]);
			this.BindingPointName = ToCachedString(content[currentIndex++]);
			this.WeaponPrefabName = ToCachedString(content[currentIndex++]);
			this.BindingPointName = ToCachedString(content[currentIndex++]);
				
		}

	}
}



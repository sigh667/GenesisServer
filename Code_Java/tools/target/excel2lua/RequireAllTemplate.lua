require "script/tableconfig/AreaTemplate"
require "script/tableconfig/TextsTemplate"
local function checkAllTemplate(  )
	for i,v in ipairs(_G.globalTable) do
		local vCheck = v.check
		vCheck(v)
	end
end

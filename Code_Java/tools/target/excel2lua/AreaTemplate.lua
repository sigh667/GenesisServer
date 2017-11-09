local localText = _G.Lang
local mt = _G.imutableTableMetatable

local function tablecheck(row)
end

local AreaTemplate = {
	check = function(table)
		for k, v in ipairs(table)do
			tablecheck(v)
		end
	end,

	[0] = {id = 0,areaName = localText["未指定"],display = 0,},
	[1000] = {id = 1000,areaName = localText["国内"],display = 0,},
	[1001] = {id = 1001,areaName = localText["北京市"],display = 1,},
	[1002] = {id = 1002,areaName = localText["上海市"],display = 1,},
	[1003] = {id = 1003,areaName = localText["广东省"],display = 1,},
	[1004] = {id = 1004,areaName = localText["天津市"],display = 1,},
	[1005] = {id = 1005,areaName = localText["重庆市"],display = 1,},
	[1006] = {id = 1006,areaName = localText["河北省"],display = 1,},
	[1007] = {id = 1007,areaName = localText["山西省"],display = 1,},
	[1008] = {id = 1008,areaName = localText["内蒙古"],display = 1,},
	[1009] = {id = 1009,areaName = localText["辽宁省"],display = 1,},
	[1010] = {id = 1010,areaName = localText["吉林省"],display = 1,},
	[1011] = {id = 1011,areaName = localText["黑龙江"],display = 1,},
	[1012] = {id = 1012,areaName = localText["江苏省"],display = 1,},
	[1013] = {id = 1013,areaName = localText["浙江省"],display = 1,},
	[1014] = {id = 1014,areaName = localText["安徽省"],display = 1,},
	[1015] = {id = 1015,areaName = localText["福建省"],display = 1,},
	[1016] = {id = 1016,areaName = localText["江西省"],display = 1,},
	[1017] = {id = 1017,areaName = localText["山东省"],display = 1,},
	[1018] = {id = 1018,areaName = localText["河南省"],display = 1,},
	[1019] = {id = 1019,areaName = localText["湖北省"],display = 1,},
	[1020] = {id = 1020,areaName = localText["湖南省"],display = 1,},
	[1021] = {id = 1021,areaName = localText["广西"],display = 1,},
	[1022] = {id = 1022,areaName = localText["海南省"],display = 1,},
	[1023] = {id = 1023,areaName = localText["四川省"],display = 1,},
	[1024] = {id = 1024,areaName = localText["贵州省"],display = 1,},
	[1025] = {id = 1025,areaName = localText["云南省"],display = 1,},
	[1026] = {id = 1026,areaName = localText["西藏"],display = 1,},
	[1027] = {id = 1027,areaName = localText["陕西省"],display = 1,},
	[1028] = {id = 1028,areaName = localText["甘肃省"],display = 1,},
	[1029] = {id = 1029,areaName = localText["青海省"],display = 1,},
	[1030] = {id = 1030,areaName = localText["宁夏"],display = 1,},
	[1031] = {id = 1031,areaName = localText["新疆"],display = 1,},
	[1032] = {id = 1032,areaName = localText["香港"],display = 1,},
	[1033] = {id = 1033,areaName = localText["澳门"],display = 1,},
	[1034] = {id = 1034,areaName = localText["台湾"],display = 1,},
	[2000] = {id = 2000,areaName = localText["海外"],display = 0,},
	[2001] = {id = 2001,areaName = localText["海外"],display = 1,},
	[3000] = {id = 3000,areaName = localText["繁体中文地区"],display = 0,},
	[3001] = {id = 3001,areaName = localText["台湾"],display = 1,},
	[3002] = {id = 3002,areaName = localText["香港"],display = 1,},
	[3003] = {id = 3003,areaName = localText["澳门"],display = 1,},
	[3004] = {id = 3004,areaName = localText["中国"],display = 1,},
	[3005] = {id = 3005,areaName = localText["新加坡"],display = 1,},
	[3006] = {id = 3006,areaName = localText["马来西亚"],display = 1,},
	[3007] = {id = 3007,areaName = localText["印度尼西亚"],display = 1,},
	[3008] = {id = 3008,areaName = localText["泰国"],display = 1,},
	[3009] = {id = 3009,areaName = localText["亚洲其他国家"],display = 1,},
	[3010] = {id = 3010,areaName = localText["美国"],display = 1,},
	[3011] = {id = 3011,areaName = localText["加拿大"],display = 1,},
	[3012] = {id = 3012,areaName = localText["北美洲其他国家"],display = 1,},
	[3013] = {id = 3013,areaName = localText["英国"],display = 1,},
	[3014] = {id = 3014,areaName = localText["法国"],display = 1,},
	[3015] = {id = 3015,areaName = localText["欧洲其他国家"],display = 1,},
	[3016] = {id = 3016,areaName = localText["澳大利亚"],display = 1,},
	[3017] = {id = 3017,areaName = localText["新西兰"],display = 1,},
	[3018] = {id = 3018,areaName = localText["巴西"],display = 1,},
	[3019] = {id = 3019,areaName = localText["南美洲其他国家"],display = 1,},
	[4000] = {id = 4000,areaName = localText["其他国家"],display = 0,},
	[4001] = {id = 4001,areaName = localText["其他国家"],display = 1,},

}
_G.globalTable.AreaTemplate = AreaTemplate

setmetatable(AreaTemplate, mt)
for i, v in ipairs(AreaTemplate) do
	setmetatable(v, mt)
end


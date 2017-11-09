_G.messageType = {
$messageNums
}
_G.messageName = {}

for k, v in pairs(_G.messageType) do
${tab}_G.messageName[v] = k
end

_G.setmetatable(_G.messageType , _G.immutableMetatable)
_G.setmetatable(_G.messageName , _G.immutableMetatable)

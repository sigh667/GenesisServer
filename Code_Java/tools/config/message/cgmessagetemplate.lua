
_G.globalMessageSender.send$messagetype = function ($args)
#if($protomessage != "")
    $protomessage
#end
#if($args != "")
${tab}local buffer = $encodemessage
${tab}assert(nil ~= buffer and "" ~= buffer)
${tab}_G.netManager.sendMessage(_G.messageType.$messagetype, buffer)
#else
${tab}_G.netManager.sendMessage(_G.messageType.$messagetype, "")
#end
${tab}_G.cclog("Send message","$messagename")
end

#foreach( $requireLua in $requireAll)
require "${requireLua.lua}"
#end
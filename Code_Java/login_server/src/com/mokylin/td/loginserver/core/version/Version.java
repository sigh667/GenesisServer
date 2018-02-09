package com.mokylin.td.loginserver.core.version;

import java.util.HashSet;
import java.util.Set;

/**
 * @description: 版本
 * @author: Joey
 * @create: 2018-02-09 16:50
 **/
public enum Version {
    Inst;

    /**
     * 主版本号
     */
    public String mainVersion = "0.1.1";
    /**
     * 副版本号(可能这个字段并不需要)
     */
    public int subVersion = 1;

    private Set<String> AllowedClientVersions = new HashSet<>();

    Version() {
        // 暂时写在代码，后面再放到配置里 TODO
        AllowedClientVersions.add(mainVersion);
    }

    /**
     * 是否允许此版本登陆
     * @param version   版本号
     * @return
     */
    public boolean isAllowed(String version) {
        return AllowedClientVersions.contains(version);
    }
}

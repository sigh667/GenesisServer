package com.genesis.network2client.floodfilter;

/**
 * 洪水攻击的全局控制开关
 * @author Joey
 *
 */
public enum FloodConstants {
    Inst;

    private boolean isOpenFloodFilter;

    public boolean isOpenFloodFilter() {
        return isOpenFloodFilter;
    }

    public void setOpenFloodFilter(boolean isOpenFloodFilter) {
        this.isOpenFloodFilter = isOpenFloodFilter;
    }
}

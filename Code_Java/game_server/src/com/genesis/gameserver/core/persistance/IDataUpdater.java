package com.genesis.gameserver.core.persistance;

import com.genesis.gamedb.persistance.PersistanceWrapper;

public interface IDataUpdater {

    void addUpdate(PersistanceWrapper persistanceWrapper);

    void addDelete(PersistanceWrapper persistanceWrapper);

}

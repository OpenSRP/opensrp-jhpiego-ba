package org.smartgresiter.jhpiego.application;

import org.smartgresiter.jhpiego.BuildConfig;
import org.smartgresiter.jhpiego.util.Utils;
import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;

/**
 * Created by samuelgithengi on 10/19/18.
 */
public class JhpiegoSyncConfiguration extends SyncConfiguration {
    @Override
    public int getSyncMaxRetries() {
        return BuildConfig.MAX_SYNC_RETRIES;
    }

    @Override
    public SyncFilter getSyncFilterParam() {
        return SyncFilter.LOCATION;
    }

    @Override
    public String getSyncFilterValue() {
        String providerId = Utils.context().allSharedPreferences().fetchRegisteredANM();
        return Utils.context().allSharedPreferences().fetchDefaultLocalityId(providerId);
    }

    @Override
    public int getUniqueIdSource() {
        return BuildConfig.OPENMRS_UNIQUE_ID_SOURCE;
    }

    @Override
    public int getUniqueIdBatchSize() {
        return BuildConfig.OPENMRS_UNIQUE_ID_BATCH_SIZE;
    }

    @Override
    public int getUniqueIdInitialBatchSize() {
        return BuildConfig.OPENMRS_UNIQUE_ID_INITIAL_BATCH_SIZE;
    }

    @Override
    public boolean isSyncSettings() {
        return BuildConfig.IS_SYNC_SETTINGS;
    }

    @Override
    public SyncFilter getEncryptionParam() {
        return SyncFilter.TEAM;
    }

    @Override
    public boolean updateClientDetailsTable() {
        return false;
    }
}

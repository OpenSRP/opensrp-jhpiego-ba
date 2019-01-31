package org.smartgresiter.jhpiego.application;

import org.smartgresiter.jhpiego.BuildConfig;
import org.smartgresiter.jhpiego.util.Constants;
import org.smartregister.SyncConfiguration;
import org.smartregister.location.helper.LocationHelper;

/**
 * Created by samuelgithengi on 10/19/18.
 */
public class JhpiegoSyncConfiguration extends SyncConfiguration {
    @Override
    public int getSyncMaxRetries() {
        return BuildConfig.MAX_SYNC_RETRIES;
    }

    @Override
    public String getSyncFilterParam() {
        return Constants.SyncFilters.FILTER_LOCATION_ID;
    }

    @Override
    public String getSyncFilterValue() {
        return LocationHelper.getInstance().locationIdsFromHierarchy();
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
}

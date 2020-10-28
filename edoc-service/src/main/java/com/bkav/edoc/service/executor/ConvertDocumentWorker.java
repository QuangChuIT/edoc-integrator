package com.bkav.edoc.service.executor;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.MapperUtil;

import java.util.concurrent.Callable;

public class ConvertDocumentWorker implements Callable<DocumentCacheEntry> {
    EdocDocument target;

    public ConvertDocumentWorker(EdocDocument target) {
        this.target = target;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public DocumentCacheEntry call() throws Exception {
        return MapperUtil.updateDocument(this.target);
    }

}

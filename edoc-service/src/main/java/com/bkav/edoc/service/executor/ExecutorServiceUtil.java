package com.bkav.edoc.service.executor;

import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.services.EdocDocumentService;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceUtil {

    public static List<DocumentCacheEntry> getDocumentCacheEntries(List<EdocDocument> documents) {
        // create a list to hold the Future object associated with Callable
        List<DocumentCacheEntry> documentCacheEntries = new ArrayList<>();

        // Get ExecutorService from Executors utility class, thread pool size is 50
        ExecutorService executor = Executors.newFixedThreadPool(50);
        Callable<DocumentCacheEntry> callable;
        Future<DocumentCacheEntry> future;

        for (EdocDocument document : documents) {
            callable = new ConvertDocumentWorker(document);
            // submit Callable tasks to be executed by thread pool
            future = executor.submit(callable);
            DocumentCacheEntry documentCacheEntry = null;
            try {
                documentCacheEntry = future.get();
                if (documentCacheEntry != null) {
                    documentCacheEntries.add(documentCacheEntry);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // shut down the executor service now
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
            // Running ...
        }
        return documentCacheEntries;
    }

    public static void main(String[] args) {
        List<EdocDocument> edocDocuments = new EdocDocumentService().findAll();
        getDocumentCacheEntries(edocDocuments);
    }
}

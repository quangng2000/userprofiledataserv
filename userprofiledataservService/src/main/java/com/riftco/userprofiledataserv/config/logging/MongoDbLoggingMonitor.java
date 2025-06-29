package com.riftco.userprofiledataserv.config.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;
import org.springframework.lang.NonNull;

/**
 * MongoDB event listener for logging database operations.
 */
@Configuration
public class MongoDbLoggingMonitor {
    
    private static final Logger log = LoggerFactory.getLogger(MongoDbLoggingMonitor.class);

    /**
     * Creates a MongoDB event listener bean that logs database operations.
     * 
     * @return An event listener for MongoDB operations
     */
    @Bean
    public ApplicationListener<MongoMappingEvent<?>> mongoEventListener() {
        return new AbstractMongoEventListener<Object>() {
            
            @Override
            public void onBeforeConvert(@NonNull BeforeConvertEvent<Object> event) {
                if (log.isDebugEnabled()) {
                    log.debug("MongoDB BeforeConvert: {}", getEntityInfo(event.getSource()));
                }
            }

            @Override
            public void onBeforeSave(@NonNull BeforeSaveEvent<Object> event) {
                if (log.isDebugEnabled()) {
                    log.debug("MongoDB BeforeSave: {} to collection '{}'", 
                            getEntityInfo(event.getSource()), event.getCollectionName());
                }
            }
            
            @Override
            public void onAfterSave(@NonNull AfterSaveEvent<Object> event) {
                log.info("MongoDB AfterSave: {} saved to collection '{}'", 
                        getEntityInfo(event.getSource()), event.getCollectionName());
            }
            
            @Override
            public void onAfterLoad(@NonNull AfterLoadEvent<Object> event) {
                if (log.isDebugEnabled()) {
                    log.debug("MongoDB AfterLoad: Document loaded from collection '{}'", 
                            event.getCollectionName());
                }
            }
            
            @Override
            public void onAfterConvert(@NonNull AfterConvertEvent<Object> event) {
                if (log.isDebugEnabled()) {
                    log.debug("MongoDB AfterConvert: {} from collection '{}'", 
                            getEntityInfo(event.getSource()), event.getCollectionName());
                }
            }
            
            @Override
            public void onBeforeDelete(@NonNull BeforeDeleteEvent<Object> event) {
                log.info("MongoDB BeforeDelete: Document from collection '{}'", 
                        event.getCollectionName());
            }
            
            @Override
            public void onAfterDelete(@NonNull AfterDeleteEvent<Object> event) {
                log.info("MongoDB AfterDelete: Document from collection '{}'", 
                        event.getCollectionName());
            }
            
            private String getEntityInfo(Object source) {
                if (source == null) {
                    return "null";
                }
                try {
                    String className = source.getClass().getSimpleName();
                    // Try to get ID using reflection to avoid exposing full object details
                    return className + "[" + source.toString().replaceAll(".*@([^,]*).*", "$1") + "]";
                } catch (Exception e) {
                    return source.getClass().getSimpleName();
                }
            }
        };
    }
}

package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.util.PropsUtil;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.hikaricp.internal.HikariCPConnectionProvider;

import java.util.Properties;

public class HibernateUtil {

    private static StandardServiceRegistry serviceRegistry;
    private static SessionFactory sessionFactory;

    public static synchronized SessionFactory getSessionFactory() {
        try {
            if (sessionFactory == null) {
                // Create the SessionFactory from hibernate.cfg.xml
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, PropsUtil.get(Environment.DRIVER));
                settings.put(Environment.URL, PropsUtil.get(Environment.URL));
                settings.put(Environment.USER, PropsUtil.get(Environment.USER));
                settings.put(Environment.PASS, PropsUtil.get(Environment.PASS));
                settings.put(Environment.DIALECT, PropsUtil.get(Environment.DIALECT));
                settings.put(Environment.MAX_FETCH_DEPTH, PropsUtil.get(Environment.MAX_FETCH_DEPTH));
                settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, PropsUtil.get(Environment.ENABLE_LAZY_LOAD_NO_TRANS));
                settings.put(Environment.SHOW_SQL, PropsUtil.get(Environment.SHOW_SQL));
                //settings.put(Environment.POOL_SIZE, PropsUtil.get(Environment.POOL_SIZE));
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, PropsUtil.get(Environment.CURRENT_SESSION_CONTEXT_CLASS));
                settings.put(Environment.AUTO_CLOSE_SESSION, PropsUtil.get(Environment.AUTO_CLOSE_SESSION));
                // HikariCP settings
                settings.put("hibernate.connection.provider_class", HikariCPConnectionProvider.class);
                // Maximum waiting time for a connection from the pool
                settings.put("hibernate.hikari.connectionTimeout", "30000");
                // Minimum number of ideal connections in the pool
                settings.put("hibernate.hikari.minimumIdle", "20");
                // Maximum number of actual connection in the pool
                settings.put("hibernate.hikari.maximumPoolSize", "50");
                // Maximum time that a connection is allowed to sit ideal in the pool
                settings.put("hibernate.hikari.idleTimeout", "30000");
                // Enable second level cache (default value is true)
                /*settings.put(Environment.USE_SECOND_LEVEL_CACHE, true);
                // Enable Query cache
                settings.put(Environment.USE_QUERY_CACHE, true);
                settings.put("net.sf.ehcache.configurationResourceName", "./ehcache.xml");
                settings.put(Environment.CACHE_REGION_FACTORY, EhCacheRegionFactory.class);*/

                configuration.setProperties(settings);
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocDocument.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocDocumentDetail.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocTraceHeaderList.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocTrace.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocNotification.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocPriority.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocDocumentType.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocAttachment.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocDynamicContact.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocTraceHeader.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocTraceHeader.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/User.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/Counter.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/EdocDailyCounter.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/ExcelUserHeader.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/ExcelOrganHeader.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/UserRole.hbm.xml"));
                configuration.addInputStream(HibernateUtil.class.getClassLoader().getResourceAsStream("entity/Role.hbm.xml"));
                serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            }
            return sessionFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            LOGGER.error("Initial SessionFactory creation failed." + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        // Close caches and connection pools
        if (serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);
}

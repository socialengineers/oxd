/**
 * All rights reserved -- Copyright 2015 Gluu Inc.
 */
package org.xdi.oxd.server;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.xdi.oxd.common.CoreUtils;
import org.xdi.oxd.server.persistence.PersistenceService;
import org.xdi.oxd.server.service.ConfigurationService;
import org.xdi.oxd.server.service.RpService;

/**
 * Main class to set up and tear down suite.
 *
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 21/08/2013
 */

public class SetUpTest {

    @BeforeSuite
    public static void beforeSuite() {
        System.out.println("Running beforeSuite ...");
        CoreUtils.createExecutor().execute(new Runnable() {
            @Override
            public void run() {
                removeExistingRps();

                ServerLauncher.start();
            }
        });
        // from one side we should give time to start server, from other we can't start in current
        // thread because it will block suite thread, ugly but works...
        CoreUtils.sleep(10);
        System.out.println("Finished beforeSuite!");
    }

    private static void removeExistingRps() {
        try {
            ServerLauncher.getInjector().getInstance(ConfigurationService.class).load();
            ServerLauncher.getInjector().getInstance(PersistenceService.class).create();
            ServerLauncher.getInjector().getInstance(RpService.class).removeAllRps();
            ServerLauncher.getInjector().getInstance(RpService.class).load();
        } catch (Exception e) {
            System.out.println("Failed to removed existing RPs.");
            e.printStackTrace();
        }
        System.out.println("Finished removeExistingRps.");
    }

    @AfterSuite
    public static void afterSuite() {
        ServerLauncher.shutdown();
    }

}

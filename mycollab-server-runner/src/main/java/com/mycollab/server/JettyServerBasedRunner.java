/**
 * This file is part of mycollab-server-runner.
 *
 * mycollab-server-runner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-server-runner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-server-runner.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.server;

import ch.qos.logback.classic.Level;
import com.mycollab.configuration.ApplicationProperties;
import com.mycollab.configuration.DatabaseConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.configuration.logging.LogConfig;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.FileUtils;
import com.mycollab.servlet.*;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.webapp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

/**
 * Generic MyCollab embedded server
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class JettyServerBasedRunner implements IServerRunner {
    private static final Logger LOG = LoggerFactory.getLogger(JettyServerBasedRunner.class);

    private Server server;
    private int port = 8080;

    private InstallationServlet installServlet;
    private ContextHandlerCollection contexts;
    private WebAppContext appContext;
    private ServletContextHandler installationContextHandler;

    public abstract WebAppContext buildContext(String baseDir);

    /**
     * Detect web app folder
     *
     * @return
     */
    private String detectWebApp() {
        File webAppFolder = FileUtils.getDesireFile(FileUtils.getUserFolder(), "webapp", "src/main/webapp");

        if (webAppFolder == null) {
            throw new MyCollabException("Can not detect webapp base folder");
        } else {
            return webAppFolder.getAbsolutePath();
        }
    }

    private ClientConnector clientConnector;

    /**
     * Run web server with arguments
     *
     * @param args
     * @throws Exception
     */
    public void run(String[] args) throws Exception {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);
        ServerInstance.getInstance().registerInstance(this);
        System.setProperty("org.eclipse.jetty.annotations.maxWait", "300");

        for (int i = 0; i < args.length; i++) {
            LOG.info("Argument: " + args[i]);
            if ("--port".equals(args[i])) {
                port = Integer.parseInt(args[++i]);
            } else if ("--cport".equals(args[i])) {
                final int listenPort = Integer.parseInt(args[++i]);
                LOG.info("Detect client port " + listenPort);
                new Thread(() -> {
                    try {
                        clientConnector = new ClientConnector(listenPort);
                    } catch (Exception e) {
                        LOG.error("Can not establish the client socket to port " + listenPort);
                    }
                }).start();
            }
        }

        System.setProperty(ApplicationProperties.MYCOLLAB_PORT, port + "");
        execute();
    }

    private void execute() throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> LOG.error("There is uncaught exception", exception));
        server = new Server();
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setIdleTimeout(360000);
        serverConnector.setPort(port);
        // Set the connectors
        server.addConnector(serverConnector);
        contexts = new ContextHandlerCollection();

        boolean alreadySetup = false;

        if (!checkConfigFileExist()) {
            System.err.println("It seems this is the first time you run MyCollab. For complete installation, you must " +
                    "open the browser and type address http://<your server name>:" + port
                    + " and complete the steps to install MyCollab.");
            installationContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            installationContextHandler.setContextPath("/");

            installServlet = new InstallationServlet();
            installationContextHandler.addServlet(new ServletHolder(installServlet), "/install");
            installationContextHandler.addServlet(new ServletHolder(new DatabaseValidate()), "/validate");
            installationContextHandler.addServlet(new ServletHolder(new EmailValidationServlet()), "/emailValidate");
            installationContextHandler.addServlet(new ServletHolder(new AssetHttpServletRequestHandler()), "/assets/*");
            installationContextHandler.addServlet(new ServletHolder(new SetupServlet()), "/*");
            installationContextHandler.addLifeCycleListener(new ServerLifeCycleListener());

            server.setStopAtShutdown(true);
            contexts.setHandlers(new Handler[]{installationContextHandler});
        } else {
            alreadySetup = true;

            WebAppContext appContext = initWebAppContext();
            ServletContextHandler upgradeContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            upgradeContextHandler.setServer(server);
            upgradeContextHandler.setContextPath("/it");
            upgradeContextHandler.addServlet(new ServletHolder(new UpgradeServlet()), "/upgrade");
            upgradeContextHandler.addServlet(new ServletHolder(new UpgradeStatusServlet()), "/upgrade_status");
            contexts.setHandlers(new Handler[]{upgradeContextHandler, appContext});
            ServerInstance.getInstance().setIsUpgrading(false);
        }

        server.setHandler(contexts);
        server.start();

        if (!alreadySetup) {
            openDefaultWebBrowserForInstallation();
        }

        server.join();
    }

    private void openDefaultWebBrowserForInstallation() {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI("http://localhost:" + port));
            } catch (Exception e) {
                //do nothing, while user can install MyCollab on the remote server
            }
        }
    }

    public void upgrade(File upgradeFile) {
        if (clientConnector != null) {
            clientConnector.reloadRequest(upgradeFile);
        } else {
            throw new MyCollabException("Can not contact host process. Terminate upgrade, you should download MyCollab manually");
        }
    }

    private DataSource buildDataSource() {
        DatabaseConfiguration dbConf = SiteConfiguration.getDatabaseConfiguration();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(dbConf.getDriverClass());
        dataSource.setJdbcUrl(dbConf.getDbUrl());
        dataSource.setUsername(dbConf.getUser());
        dataSource.setPassword(dbConf.getPassword());

        Properties dsProperties = new Properties();
        dsProperties.setProperty("cachePrepStmts", "true");
        dsProperties.setProperty("prepStmtCacheSize", "250");
        dsProperties.setProperty("prepStmtCacheSqlLimit", "2048");
        dsProperties.setProperty("useServerPrepStmts", "true");
        dsProperties.setProperty("maximumPoolSize", "20");
        dsProperties.setProperty("initializationFailFast", "false");
        dataSource.setDataSourceProperties(dsProperties);
        return dataSource;
    }

    private boolean checkConfigFileExist() {
        File confFolder = FileUtils.getDesireFile(FileUtils.getUserFolder(), "conf", "src/main/conf");
        return confFolder != null && new File(confFolder, "mycollab.properties").exists();
    }

    private WebAppContext initWebAppContext() throws IOException {
        SiteConfiguration.loadConfiguration();
        LogConfig.initMyCollabLog();
        String webAppDirLocation = detectWebApp();
        appContext = buildContext(webAppDirLocation);
        appContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        appContext.setServer(server);
        appContext.setConfigurations(new Configuration[]{
                new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(), new PlusConfiguration(),
                new MetaInfConfiguration(), new FragmentConfiguration(),
                new EnvConfiguration()});

        String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        String fileSeparator = System.getProperty("file.separator");
        String osExprClassFolder, osExprJarFile;
        if ("/".equals(fileSeparator)) {
            osExprClassFolder = ".*mycollab-\\S+/target/classes$";
            osExprJarFile = ".*mycollab-\\S+.jar$";
        } else {
            osExprClassFolder = ".+\\\\mycollab-\\S+\\\\target\\\\classes$";
            osExprJarFile = ".+\\\\mycollab-\\S+.jar$";
        }

        for (String classpath : classPaths) {
            if (classpath.matches(osExprClassFolder)) {
                LOG.info("Load folder to classpath " + classpath);
                appContext.getMetaData().addWebInfJar(new PathResource(new File(classpath)));
            } else if (classpath.matches(osExprJarFile)) {
                try {
                    LOG.info("Load jar file in path " + classpath);
                    appContext.getMetaData().addWebInfJar(new PathResource(new File(classpath).toURI().toURL()));
                    appContext.getMetaData().getWebInfClassesDirs().add(new PathResource(new File(classpath).toURI().toURL()));
                } catch (Exception e) {
                    LOG.error("Exception to resolve classpath: " + classpath, e);
                }
            }
        }

        File libFolder = new File(FileUtils.getUserFolder(), "lib");

        if (libFolder.isDirectory()) {
            File[] files = libFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().matches("mycollab-\\S+.jar$")) {
                        LOG.info("Load jar file to classpath " + file.getAbsolutePath());
                        appContext.getMetaData().getWebInfClassesDirs().add(new PathResource(file.toURI()));
                    }
                }
            }
        }

        // Register a mock DataSource scoped to the webapp
        // This must be linked to the webapp via an entry in
        // web.xml:
        // <resource-ref>
        // <res-ref-name>jdbc/mydatasource</res-ref-name>
        // <res-type>javax.sql.DataSource</res-type>
        // <res-auth>Container</res-auth>
        // </resource-ref>
        // At runtime the webapp accesses this as
        // java:comp/env/jdbc/mydatasource
        try {
            LOG.info("Init the datasource");
            org.eclipse.jetty.plus.jndi.Resource mydatasource = new org.eclipse.jetty.plus.jndi.Resource(
                    appContext, "jdbc/mycollabdatasource", buildDataSource());
        } catch (NamingException e) {
            throw new MyCollabException(e);
        }

        return appContext;
    }

    private class ServerLifeCycleListener implements LifeCycle.Listener {

        @Override
        public void lifeCycleStarting(LifeCycle event) {

        }

        @Override
        public void lifeCycleStarted(LifeCycle event) {
            Runnable thread = () -> {
                LOG.debug("Detect root folder webapp");
                File confFolder = FileUtils.getDesireFile(FileUtils.getUserFolder(), "conf", "src/main/conf");

                if (confFolder == null) {
                    throw new MyCollabException("Can not detect webapp base folder");
                } else {
                    File confFile = new File(confFolder, "mycollab.properties");
                    while (!confFile.exists()) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new MyCollabException(e);
                        }
                    }

                    try {
                        appContext = initWebAppContext();
                        appContext.setClassLoader(JettyServerBasedRunner.class.getClassLoader());
                        contexts.addHandler(appContext);

                        ServletContextHandler upgradeContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
                        upgradeContextHandler.setServer(server);
                        upgradeContextHandler.setContextPath("/it");
                        upgradeContextHandler.addServlet(new ServletHolder(new UpgradeServlet()), "/upgrade");
                        upgradeContextHandler.addServlet(new ServletHolder(new UpgradeStatusServlet()), "/upgrade_status");
                        contexts.addHandler(upgradeContextHandler);
                        appContext.start();
                        upgradeContextHandler.start();
                    } catch (Exception e) {
                        LOG.error("Error while starting server", e);
                    }
                    installServlet.setWaitFlag(false);
                    contexts.removeHandler(installationContextHandler);
                }
            };

            new Thread(thread).start();
        }

        @Override
        public void lifeCycleFailure(LifeCycle event, Throwable cause) {

        }

        @Override
        public void lifeCycleStopping(LifeCycle event) {

        }

        @Override
        public void lifeCycleStopped(LifeCycle event) {

        }
    }
}

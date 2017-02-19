/**
 * This file is part of mycollab-scheduler-community.
 *
 * mycollab-scheduler-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.schedule.jobs;

import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.BroadcastMessage;
import com.mycollab.core.Broadcaster;
import com.mycollab.core.Version;
import com.mycollab.core.NewUpdateAvailableNotification;
import com.mycollab.core.utils.JsonDeSerializer;
import com.mycollab.schedule.jobs.GenericQuartzJobBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipFile;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CheckUpdateJob extends GenericQuartzJobBean {
    private static Logger LOG = LoggerFactory.getLogger(CheckUpdateJob.class);

    private static boolean isDownloading = false;
    private static String latestFileDownloadedPath;

    @Override
    public void executeJob(JobExecutionContext context) throws JobExecutionException {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(SiteConfiguration.getApiUrl("checkupdate?version=" + Version.getVersion()), String.class);
        final Properties props = JsonDeSerializer.fromJson(result, Properties.class);
        String version = props.getProperty("version");
        if (Version.isEditionNewer(version)) {
            if (!isDownloading) {
                LOG.info("There is the new version of MyCollab " + version);
                String autoDownloadLink = props.getProperty("autoDownload");
                String manualDownloadLink = props.getProperty("downloadLink");

                if (latestFileDownloadedPath != null) {
                    File installerFile = new File(latestFileDownloadedPath);
                    if (installerFile.exists() && installerFile.getName().startsWith("mycollab" + version.replace('.', '_'))) {
                        Broadcaster.broadcast(new BroadcastMessage(new NewUpdateAvailableNotification(version,
                                autoDownloadLink, manualDownloadLink, latestFileDownloadedPath)));
                        return;
                    }
                }

                if (autoDownloadLink != null) {
                    try {
                        DownloadMyCollabThread downloadMyCollabThread = new DownloadMyCollabThread(version, autoDownloadLink);
                        isDownloading = true;
                        downloadMyCollabThread.start();
                        downloadMyCollabThread.join();
                        File installerFile = downloadMyCollabThread.tmpFile;
                        if (installerFile.exists() && installerFile.isFile() && installerFile.length() > 0 && isValid(installerFile)) {
                            latestFileDownloadedPath = installerFile.getAbsolutePath();
                            Broadcaster.broadcast(new BroadcastMessage(new NewUpdateAvailableNotification(version,
                                    autoDownloadLink, manualDownloadLink, latestFileDownloadedPath)));
                        } else {
                            Broadcaster.broadcast(new BroadcastMessage(new NewUpdateAvailableNotification(version, null,
                                    manualDownloadLink, null)));
                        }
                    } catch (Exception e) {
                        LOG.error("Exception", e);
                    } finally {
                        isDownloading = false;
                    }
                } else {
                    Broadcaster.broadcast(new BroadcastMessage(new NewUpdateAvailableNotification(version, autoDownloadLink,
                            manualDownloadLink, latestFileDownloadedPath)));
                }
            }
        }
    }

    static boolean isValid(final File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static class DownloadMyCollabThread extends Thread {
        private String version;
        private String downloadLink;
        File tmpFile;

        DownloadMyCollabThread(String version, String downloadLink) {
            this.version = version;
            this.downloadLink = downloadLink;
        }

        @Override
        public void run() {
            try {
                tmpFile = File.createTempFile("mycollab" + version.replace('.', '_'), ".zip");
                URL url = new URL(downloadLink);
                LOG.info("Start download the new MyCollab version at " + downloadLink);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();
                InputStream inputStream = null;

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // opens input stream from the HTTP connection
                    inputStream = httpConn.getInputStream();
                } else {
                    if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                            || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                            || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                        // get redirect url from "location" header field
                        String newUrl = httpConn.getHeaderField("Location");

                        // get the cookie if need, for login
                        String cookies = httpConn.getHeaderField("Set-Cookie");

                        // open the new connnection again
                        httpConn = (HttpURLConnection) new URL(newUrl).openConnection();
                        httpConn.setRequestProperty("Cookie", cookies);
                        httpConn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                        httpConn.addRequestProperty("Referer", "google.com");
                        inputStream = httpConn.getInputStream();
                    }
                }
                if (inputStream != null) {
                    int loadedBytes = 0;
                    // opens an output stream to save into file
                    try (FileOutputStream outputStream = new FileOutputStream(tmpFile)) {
                        int bytesRead;
                        byte[] buffer = new byte[4096];
                        while (((bytesRead = inputStream.read(buffer)) != -1)) {
                            outputStream.write(buffer, 0, bytesRead);
                            loadedBytes += bytesRead;
                            LOG.info("  Progress: " + loadedBytes / 1024);
                        }
                        outputStream.close();
                        inputStream.close();
                        httpConn.disconnect();
                        LOG.info("Download MyCollab edition successfully to " + tmpFile.getAbsolutePath());
                    }
                } else {
                    LOG.info("Can not download the new MyCollab. Reason is: " + responseCode);
                }
            } catch (Exception e) {
                LOG.error("Error while download " + downloadLink, e);
            }
        }
    }
}

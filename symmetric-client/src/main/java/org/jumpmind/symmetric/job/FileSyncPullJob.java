/**
 * Licensed to JumpMind Inc under one or more contributor
 * license agreements.  See the NOTICE file distributed
 * with this work for additional information regarding
 * copyright ownership.  JumpMind Inc licenses this file
 * to you under the GNU General Public License, version 3.0 (GPLv3)
 * (the "License"); you may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the GNU General Public License,
 * version 3.0 (GPLv3) along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jumpmind.symmetric.job;

import org.jumpmind.symmetric.ISymmetricEngine;

import static org.jumpmind.symmetric.job.JobDefaults.*;
import org.jumpmind.symmetric.common.ParameterConstants;
import org.jumpmind.symmetric.model.JobDefinition.ScheduleType;
import org.jumpmind.symmetric.model.JobDefinition.StartupType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class FileSyncPullJob extends AbstractJob {

    public FileSyncPullJob(ISymmetricEngine engine, ThreadPoolTaskScheduler taskScheduler) {
        super("job.file.sync.pull", engine, taskScheduler);
    }
    
    @Override
    public JobDefaults getDefaults() {
        boolean fileSyncEnabeld = engine.getParameterService().is(ParameterConstants.FILE_SYNC_ENABLE); 
        
        return new JobDefaults()
                .scheduleType(ScheduleType.PERIODIC)
                .schedule(EVERY_MINUTE)
                .startupType(fileSyncEnabeld ? StartupType.AUTOMATIC : StartupType.DISABLED)
                .description("For FileSync, check for any files to pull down from other nodes.");
    }    

    @Override
    public void doJob(boolean force) throws Exception {
        engine.getFileSyncService().pullFilesFromNodes(force);
    }
    
}

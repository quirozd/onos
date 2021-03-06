/*
 * Copyright 2014-2015 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.net.intent.impl.compiler;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DefaultPath;
import org.onosproject.net.Link;
import org.onosproject.net.Path;
import org.onosproject.net.intent.Intent;
import org.onosproject.net.intent.PathIntent;
import org.onosproject.net.intent.PointToPointIntent;
import org.onosproject.net.provider.ProviderId;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.onosproject.net.DefaultEdgeLink.createEdgeLink;

/**
 * An intent compiler for {@link org.onosproject.net.intent.PointToPointIntent}.
 */
@Component(immediate = true)
public class PointToPointIntentCompiler
        extends ConnectivityIntentCompiler<PointToPointIntent> {

    // TODO: use off-the-shell core provider ID
    private static final ProviderId PID =
            new ProviderId("core", "org.onosproject.core", true);
    // TODO: consider whether the default cost is appropriate or not
    public static final int DEFAULT_COST = 1;

    @Activate
    public void activate() {
        intentManager.registerCompiler(PointToPointIntent.class, this);
    }

    @Deactivate
    public void deactivate() {
        intentManager.unregisterCompiler(PointToPointIntent.class);
    }

    @Override
    public List<Intent> compile(PointToPointIntent intent, List<Intent> installable) {

        ConnectPoint ingressPoint = intent.ingressPoint();
        ConnectPoint egressPoint = intent.egressPoint();

        if (ingressPoint.deviceId().equals(egressPoint.deviceId())) {
            List<Link> links = asList(createEdgeLink(ingressPoint, true), createEdgeLink(egressPoint, false));
            return asList(createPathIntent(new DefaultPath(PID, links, DEFAULT_COST), intent));
        }

        List<Link> links = new ArrayList<>();
        Path path = getPath(intent, ingressPoint.deviceId(),
                egressPoint.deviceId());

        links.add(createEdgeLink(ingressPoint, true));
        links.addAll(path.links());
        links.add(createEdgeLink(egressPoint, false));

        return asList(createPathIntent(new DefaultPath(PID, links, path.cost(),
                                                       path.annotations()), intent));
    }

    /**
     * Creates a path intent from the specified path and original
     * connectivity intent.
     *
     * @param path   path to create an intent for
     * @param intent original intent
     */
    private Intent createPathIntent(Path path,
                                    PointToPointIntent intent) {
        return PathIntent.builder()
                .appId(intent.appId())
                .selector(intent.selector())
                .treatment(intent.treatment())
                .path(path)
                .constraints(intent.constraints())
                .priority(intent.priority())
                .build();
    }

}

/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.compiler.compiler;

import java.io.InputStream;
import java.util.Optional;

import org.kie.api.Service;
import org.kie.internal.builder.ScoreCardConfiguration;

public interface ScoreCardProvider extends Service {

    String loadFromInputStream( InputStream is,
                                ScoreCardConfiguration configuration );

    Optional<String> getGeneratedPMML( InputStream is, ScoreCardConfiguration configuration );
}

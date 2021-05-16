package org.itsallcode.openfasttrace.maven;

/*-
 * #%L
 * OpenFastTrace Maven Plugin
 * %%
 * Copyright (C) 2018 - 2021 itsallcode.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.nio.file.Path;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TraceMojoVerifierTest extends AbstractTraceMojoTest
{

    private static Path PROJECT_WITH_PATH_PATTERN = BASE_TEST_DIR.resolve("project-with-path-pattern");
    private static Path PROJECT_WITHOUT_PATH_PATTERN = BASE_TEST_DIR.resolve("project-without-path-pattern-fails");

    @Test
    public void testTracingWithPathPatternSuccessful() throws Exception
    {

        final Verifier verifier = new Verifier(PROJECT_WITH_PATH_PATTERN.toAbsolutePath().toString());
        verifier.executeGoal("verify");
        verifier.verifyErrorFreeLog();
        assertThat(fileContent(PROJECT_WITH_PATH_PATTERN.resolve("target/tracing-report.txt")))
                .isEqualTo("ok - 6 total\n");
    }

    @Test(expected = VerificationException.class)
    public void testTracingWithPathPatternThrows() throws Exception
    {
        final Verifier verifier = new Verifier(PROJECT_WITHOUT_PATH_PATTERN.toAbsolutePath().toString());
        try {
            verifier.executeGoal("verify");
        } catch (final Exception exception) {
            verifier.verifyTextInLog("Tracing found 1 out of 4 items");
            throw exception;
        }
    }

}

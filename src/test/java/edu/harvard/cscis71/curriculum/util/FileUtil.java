package edu.harvard.cscis71.curriculum.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileUtil {
    public static Map<String, String> load(String pattern) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(pattern);

        return Arrays.stream(resources).map(FileUtil::loadResource)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    private static Optional<Pair<String, String>> loadResource(Resource r) {
        try (InputStream in = r.getInputStream(); InputStreamReader reader = new InputStreamReader(in)) {
            return Optional.of(Pair.of(Objects.requireNonNull(r.getFilename()).substring(0, r.getFilename().lastIndexOf('.')), IOUtils.toString(reader)));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}

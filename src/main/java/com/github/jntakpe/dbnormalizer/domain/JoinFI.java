package com.github.jntakpe.dbnormalizer.domain;

import java.nio.file.Path;

/**
 * Information sur l'état actuel et l'état cible
 *
 * @author jOSS
 */
public class JoinFI {

    private final Path path;

    private final FileInfos currentFI;

    private final FileInfos targetFI;

    public JoinFI(Path path, FileInfos currentFI, FileInfos targetFI) {
        this.path = path;
        this.currentFI = currentFI;
        this.targetFI = targetFI;
    }

    public Path getPath() {
        return path;
    }

    public FileInfos getCurrentFI() {
        return currentFI;
    }

    public FileInfos getTargetFI() {
        return targetFI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinFI joinFI = (JoinFI) o;

        if (!path.equals(joinFI.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return "JoinFI{" +
                "path=" + path +
                '}';
    }
}

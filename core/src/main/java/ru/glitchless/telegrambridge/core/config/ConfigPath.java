package ru.glitchless.telegrambridge.core.config;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ConfigPath {
    @Nullable
    final ConfigPath parent;
    final String name;

    public ConfigPath(String name) {
        this.parent = null;
        this.name = name;
    }

    public ConfigPath(@Nullable ConfigPath root, String name) {
        this.parent = root;
        this.name = name;
        if (getLevel() > 10) {
            throw new RuntimeException("");
        }
    }

    public String getName() {
        return name;
    }

    @Nullable
    public ConfigPath getParent() {
        return parent;
    }

    /**
     * @return list of config paths.
     * Map linked list to array list
     */
    public ArrayList<ConfigPath> flatten() {
        final ArrayList<ConfigPath> flatList = new ArrayList<>();
        ConfigPath currentPath = this;
        while (currentPath != null) {
            flatList.add(currentPath);
            currentPath = currentPath.getParent();
        }
        Collections.reverse(flatList);
        return flatList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigPath)) return false;
        ConfigPath that = (ConfigPath) o;
        return Objects.equals(parent, that.parent) && name.equals(that.name);
    }

    public int getLevel() {
        if (parent == null) {
            return 1;
        }
        return parent.getLevel() + 1;
    }

    @Override
    public int hashCode() {
        try {
            return Objects.hash(parent, name);
        } catch (StackOverflowError error) {
            throw error;
        }
    }

    @Override
    public String toString() {
        if (parent != null) {
            return parent.toString() + "." + name;
        }
        return name;
    }
}

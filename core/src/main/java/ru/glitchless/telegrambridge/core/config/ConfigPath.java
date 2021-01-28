package ru.glitchless.telegrambridge.core.config;

import javax.annotation.Nullable;
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
    }

    public String getName() {
        return name;
    }

    @Nullable
    public ConfigPath getParent() {
        return parent;
    }

    public int getLevel() {
        if (parent == null) {
            return 1;
        }
        return parent.getLevel() + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigPath)) return false;
        ConfigPath that = (ConfigPath) o;
        return Objects.equals(parent, that.parent) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, name);
    }

    @Override
    public String toString() {
        if (parent != null) {
            return parent.toString() + "." + name;
        }
        return name;
    }
}

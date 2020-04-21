package org.trypticon.pdn.dotnet.system;

import org.trypticon.pdn.nrbf.classes.NrbfClassRecord;

import java.util.Map;

/**
 * Stand-in for .NET class {@code System.Version}.
 */
public class Version {
    private final int major;
    private final int minor;
    private final int build;
    private final int revision;

    public Version(NrbfClassRecord record) {
        Map<String, Object> map = record.asMap();
        major = ((Number) map.get("_Major")).intValue();
        minor = ((Number) map.get("_Minor")).intValue();
        build = ((Number) map.get("_Build")).intValue();
        revision = ((Number) map.get("_Revision")).intValue();
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getBuild() {
        return build;
    }

    public int getRevision() {
        return revision;
    }

    @Override
    public String toString() {
        return getMajor() + "." + getMinor() + '.' + getBuild() + '.' + getRevision();
    }
}

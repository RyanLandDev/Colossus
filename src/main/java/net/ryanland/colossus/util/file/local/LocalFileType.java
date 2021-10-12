package net.ryanland.colossus.util.file.local;

public enum LocalFileType {

    UNKNOWN("unk"),

    TEXT("txt"),
    JSON("json", "{}");

    private final String extension;
    private final byte[] defaultContent;

    LocalFileType(String extension, byte[] defaultContent) {
        this.extension = extension;
        this.defaultContent = defaultContent;
    }

    LocalFileType(String extension, String defaultContent) {
        this(extension, defaultContent.getBytes());
    }

    LocalFileType(String extension) {
        this(extension, (byte[]) null);
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getDefaultContent() {
        return defaultContent;
    }

}

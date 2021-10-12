package net.ryanland.colossus.util.file.local;

import java.io.IOException;

public class LocalFileBuilder {

    String fileName;
    LocalFileType fileType = LocalFileType.UNKNOWN;
    byte[] defaultContent = null;
    boolean directory = false;

    public LocalFileBuilder setName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public LocalFileBuilder setFileType(LocalFileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public LocalFileBuilder setDefaultContent(byte[] defaultContent) {
        this.defaultContent = defaultContent;
        return this;
    }

    public LocalFileBuilder setDefaultContent(String defaultContent) {
        return setDefaultContent(defaultContent.getBytes());
    }

    public LocalFileBuilder isDirectory(boolean directory) {
        this.directory = directory;
        return this;
    }

    public LocalFile buildFile() {
        LocalFile file = new LocalFile(fileName + (directory ? "" : "." + fileType.getExtension()));
        try {
            if (!file.exists()) {
                if (directory) {
                    file.mkdir();
                } else {
                    file.createNewFile();
                    if (defaultContent == null) defaultContent = fileType.getDefaultContent();
                    if (defaultContent != null) file.write(defaultContent);
                }

            }

        } catch (IOException err) {
            err.printStackTrace();
        }
        return file;
    }
}

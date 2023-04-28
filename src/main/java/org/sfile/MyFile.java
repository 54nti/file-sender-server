package org.sfile;

public class MyFile {
    private int id;
    private String name;
    private byte[] data;
    private String extension;

    public MyFile(int id, String name, byte[] data, String extension) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.extension = extension;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public String getExtension() {
        return extension;
    }
}

package info.horriblesubs.sher.api.nyaa.model;

public class DataSize {

    private final DataUnit dataUnit;
    private final String size;

    DataSize(String size, DataUnit dataUnit) {
        this.dataUnit = dataUnit;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public DataUnit getUnit() {
        return dataUnit;
    }

    public enum DataUnit {
        Bytes("Byte", "B"),
        KiB("KiloByte", "KB"),
        MiB("MegaByte", "MB"),
        GiB("GigaByte", "GB"),
        TiB("TeraByte", "TB");
        private String f, s;

        DataUnit(String f, String s) {
            this.s = s;
            this.f = f;
        }

        public String getShort() {
            return s;
        }

        public String getFull() {
            return f;
        }
    }
}

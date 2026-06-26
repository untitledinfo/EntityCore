package firepdx.pgc.net.database;

public interface DatabaseProvider {
    void connect();

    void close();
}

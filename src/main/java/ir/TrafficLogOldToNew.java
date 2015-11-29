package ir;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

/**
 * Created by O_Javaheri on 11/29/2015.
 */



public class TrafficLogOldToNew
{
    private static String oldUrl = "jdbc:derby://localhost:1527/datbase/old-kernel;create=true;user=kernel;password=kernel";
    private static String newUrl = "jdbc:derby://localhost:1527/datbase/kernel;create=true;user=kernel;password=kernel";
    private static String tableName = "tb_TrafficLog";
    // jdbc Connection
    private static Connection oldConn = null;
    private static Connection newConn = null;
    private static Statement oldStmt = null;
    private static Statement newStmt = null;

    public static void main(String[] args)
    {
        createConnection();
        insertInNewTable();
    }

    private static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            oldConn = DriverManager.getConnection(oldUrl);
            newConn = DriverManager.getConnection(newUrl);
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }

    private static void insertInNewTable()
    {
        try
        {
            oldStmt = oldConn.createStatement();
            newStmt = newConn.createStatement();
            ResultSet results = oldStmt.executeQuery("select * from " + tableName);

            while(results.next())
            {
                long id = results.getLong(1);
                String deleted = results.getString(2);
                String status = results.getString(3);
                boolean exit = results.getBoolean(4);
                boolean finger = results.getBoolean(5);
                boolean last = results.getBoolean(6);
                boolean offline = results.getBoolean(7);
                String pictures = results.getString(8);
                String trafficDate = results.getString(9);
                String trafficTime = results.getString(10);
                boolean valid = results.getBoolean(11);
                String video = results.getString(12);
                byte[] virdiPicture = results.getBytes(13);
                long cardId = results.getLong(14);
                long gatewayId = results.getLong(15);
                long organId = results.getLong(16);
                long pdpId = results.getLong(17);
                long personId = results.getLong(18);
                long virdiId = results.getLong(19);
                long zoneId = results.getLong(20);

                newStmt.execute("insert into " + tableName + " values (" +
                        id + ",'" + deleted + "','" + status + "','" + exit + "','" + finger + "','"
                        + last + "','" + offline + "','"
                        + pictures + "','" + trafficDate + "','" + trafficTime
                        + "','" + valid + "','" + video
                        + "','" + gatewayId + "','" + organId
                        + "','" + pdpId + "','" + personId
                        + "','" + virdiId + "','" + zoneId
                        + "','" + virdiPicture + "','" + cardId + "')");
            }
            results.close();
            oldStmt.close();
            newStmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }

}
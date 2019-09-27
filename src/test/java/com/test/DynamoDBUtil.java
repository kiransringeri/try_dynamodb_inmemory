package com.test;

import java.io.File;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

/**
 * 
 * @author kiransringeri
 *
 */
public class DynamoDBUtil {
  private static final String port = "8001";
  private static DynamoDBProxyServer server = null;
//  private static final String[] localArgs = {"-inMemory", "-sharedDb", "-port", port};
  private static final String[] localArgs = {"-sharedDb", "-port", port};

  public static void main(String[] args) throws Throwable {
    try {
      System.out.println("User dir="+System.getProperty("user.dir"));
            
      System.out.println("Starting the server");
      startServer(false);
      System.out.println("Server started");
      
      System.out.println("You can open http://localhost:8001/shell/ in your browser");
      System.out.println("Press CTRL+C to stop the server");
    } catch (Throwable th) {
      th.printStackTrace();
    } 
  }

  public static void startServer() throws Throwable {
    startServer(true);
  }
  
  public static void startServer(boolean useExistingDbFile) throws Throwable {
    if(useExistingDbFile) {
      System.out.println("User dir="+System.getProperty("user.dir"));
      
  //    Let us copy the DB file to working directory, so that DynamoDB uses the existing db file
      File dbSrcFile = new File("src/test/resources/shared-local-instance.db");
      File dbFile = new File("shared-local-instance.db");
      if(dbFile.exists()) {
        boolean deleted = dbFile.delete();
        System.out.println("Deleted the existing DB file \"" + dbFile.getAbsolutePath()+"\". Result= " + deleted);
      }
      FileUtils.copyFile(dbSrcFile, dbFile);
      System.out.println("Copied the DB file \"" + dbSrcFile.getAbsolutePath() +"\" to \"" + dbFile.getAbsolutePath()+"\"");
    }
    
    String sqlite4javaLib = "src/test/resources/libs/";
    System.out.println("Setting system property \"sqlite4java.library.path\" to \"" + sqlite4javaLib + "\"");
    System.setProperty("sqlite4java.library.path", sqlite4javaLib);
    
    System.out.println("Starting DynamoDB server (in-memory)...");
    System.out.println("Command line arguments for dynamodb server=" + Arrays.toString(localArgs));
    server = ServerRunner.createServerFromCommandLineArgs(localArgs);
    server.start();
    System.out.println("DynamoDB Server (in-memory) started successfully :-)");
  }

  public static void stopServer() throws Throwable {
    if (server != null)
      server.stop();
  }
}

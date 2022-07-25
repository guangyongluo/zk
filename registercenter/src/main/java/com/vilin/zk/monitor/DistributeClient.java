package com.vilin.zk.monitor;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DistributeClient {

    private ZooKeeper zooKeeper;
    private String connectString = "luoweideMBP:2181";
    private int sessionTimeout = 2000;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        DistributeClient client = new DistributeClient();

        // 1. connected to zookeeper.
        client.getConnect();

        // 2. monitor /servers sub node change.
        client.getServerList();

        // 3. business logic
        client.business();

    }

    private void business() throws InterruptedException {
        TimeUnit.HOURS.sleep(Long.MAX_VALUE);
    }

    private void getServerList() throws InterruptedException, KeeperException {
        final List<String> children = zooKeeper.getChildren("/servers", true);
        List<String> servers = new ArrayList();
        for(String child : children){
            final byte[] data = zooKeeper.getData("/servers/" + child, false, null);
            servers.add(new String(data));
        }
        System.out.println(servers);
    }

    private void getConnect() throws IOException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    getServerList();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (KeeperException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.*;
import org.apache.geode.cache.client.*;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import static org.apache.geode.distributed.ConfigurationProperties.DURABLE_CLIENT_ID;
import static org.apache.geode.distributed.ConfigurationProperties.DURABLE_CLIENT_TIMEOUT;

public class ClientA {

  public static final String CLIENT_NAME = "clientA";
  public static final String POOL_NAME = "pool";

  public static void wait(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
    }
  }

  public static void main(String[] args) {

    // cache creation
    ClientCacheFactory cacheFactory = new ClientCacheFactory();
    cacheFactory.setPdxReadSerialized(true);
    // cacheFactory.setPdxPersistent(true);
    // cacheFactory.addPoolLocator("localhost", 10334);
    cacheFactory.set(DURABLE_CLIENT_ID, CLIENT_NAME);
    cacheFactory.set("log-level", "ALL");
    ClientCache cache = cacheFactory.create();

    // pool creation
    PoolFactory poolFactory = PoolManager.createFactory();
    poolFactory.setSubscriptionEnabled(true);
    poolFactory.addLocator("localhost", 10331);
    poolFactory.create(POOL_NAME);

    // create a local region that matches the server region
    ClientRegionFactory clientRegionFactory =
        cache.createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);

    // setup region factory
    clientRegionFactory.addCacheListener(new ExampleCacheListener(ClientA.CLIENT_NAME));
    // clientRegionFactory.setConcurrencyLevel(2);
    clientRegionFactory.setPoolName(POOL_NAME);

    // create region
    Region region = clientRegionFactory.create("multisiteData");

    // register interest for everything
    region.registerInterestForAllKeys(InterestResultPolicy.KEYS_VALUES, true);

    // inform the server that we are ready to receive events
    cache.readyForEvents();

    System.out.println("[" + CLIENT_NAME + "] " + "Started and ready for events.");
    wait(2000);

    System.out.println("[" + CLIENT_NAME + "] " + "Putting <Integer,String>.");
    Integer key = new Random().nextInt(100);
    String value = "value" + key;
    region.put(key, value);
    wait(2000);

    System.out.println("[" + CLIENT_NAME + "] " + "Putting <Integer,PdxSerializable>.");
    key = new Random().nextInt(100);
    ExamplePdxSerializable valuePdx = new ExamplePdxSerializable();
    valuePdx.setId(key);
    valuePdx.setType("PdxObject");
    valuePdx.setStatus("Ongoing");
    region.put(key, valuePdx);
    wait(2000);

    System.out.println("[" + CLIENT_NAME + "] " + "Waiting before close...");
    wait(10000);

    // end
    cache.close();

  }
}

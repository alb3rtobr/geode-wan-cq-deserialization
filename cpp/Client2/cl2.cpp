
#include <iostream>
#include <thread>

#include <geode/CacheFactory.hpp>
#include <geode/CqAttributesFactory.hpp>
#include <geode/PoolManager.hpp>
#include <geode/QueryService.hpp>
#include <geode/RegionFactory.hpp>
#include <geode/RegionShortcut.hpp>
#include <geode/Struct.hpp>
#include <geode/TypeRegistry.hpp>
#include <geode/CacheListener.hpp>
#include <geode/EntryEvent.hpp>
#include <geode/Region.hpp>

#include "Order.hpp"

using namespace apache::geode::client;
using namespace continuousquery;

class GeodeCacheListener : public CacheListener
{
public:

    CacheListener* getCacheListener()
    {
        return this;
    }

    // Entry events
    void afterUpdate(const EntryEvent& event) override
    {
        if(event.remoteOrigin())
        {
            std::cout << "GeodeCacheListener::afterUpdate from Remote Origin" << std::endl;
        }
        else
        {
            std::cout << "GeodeCacheListener::afterUpdate from Local Origin" << std::endl;
        }

    }

    void afterCreate(const EntryEvent& event) override
    {
        auto region =  event.getRegion();
        std::cout << "GeodeCacheListener::afterCreate  " << region->getName()  << std::endl;
    }

    void afterDestroy(const EntryEvent& event) override
    {
        std::cout << "GeodeCacheListener::afterDestroy" << std::endl;
    }

    void afterInvalidate(const EntryEvent& event) override
    {
        std::cout << "GeodeCacheListener::afterInvalidate" << std::endl;
    }


    // Region events
    void afterRegionClear(const RegionEvent& event) override
    {
        std::cout << "GeodeCacheListener::afterRegionClear" << std::endl;
    }

    void afterRegionDestroy(const RegionEvent& event) override
    {
        std::cout << "GeodeCacheListener::afterRegionDestroy" << std::endl;
    }

    void afterRegionDisconnected(Region& event) override
    {
        std::cout << "GeodeCacheListener::afterRegionDisconnected" << std::endl;
    }

    void afterRegionInvalidate(const RegionEvent& event) override
    {
        std::cout << "GeodeCacheListener::afterRegionInvalidate" << std::endl;
    }

    void afterRegionLive(const RegionEvent& event) override
    {
        std::cout << "GeodeCacheListener::afterRegionLive" << std::endl;
    }

    void close(Region& event) override
    {
        if (closeCallback_)
        {
            closeCallback_();
        }
    }


    void setCloseCallback(std::function<void()>&& callback)
    {
        closeCallback_ = std::move(callback);
    }


private:
    std::function<void()> closeCallback_;
};

int main(int argc, char** argv) {
  auto cacheFactory = CacheFactory();
  std::cout << apache::geode::client::CacheFactory::getVersion() << std::endl;

  cacheFactory.set("log-level", "debug");
  cacheFactory.set("durable-client-id", "clientB");
  cacheFactory.set("durable-timeout", "300s");
  cacheFactory.setPdxReadSerialized(true);
  
  std::shared_ptr<GeodeCacheListener> cacheListener_ = std::make_shared<GeodeCacheListener>();
 

  auto cache = cacheFactory.create();
 
  auto poolFactory = cache.getPoolManager().createFactory();
  auto pool = poolFactory.addLocator("localhost", 10332)
                  .setSubscriptionEnabled(true)
                  .create("pool");

  auto regionFactory = cache.createRegionFactory(RegionShortcut::CACHING_PROXY);
  regionFactory.setCacheListener(cacheListener_);

  auto region = regionFactory.setPoolName("pool").create("multisiteData");
  region->registerAllKeys(true,true,true);

  cache.getTypeRegistry().registerPdxType(Order::createDeserializable);
  cache.readyForEvents();

  std::cout << "Create orders" << std::endl;
  auto order2 = std::make_shared<Order>(2, "product y", 37);


  std::cout << "Putting and changing Order objects in the region" << std::endl;
  region->put("Order2", order2);

  std::this_thread::sleep_for(std::chrono::seconds(120));


  cache.close();
}

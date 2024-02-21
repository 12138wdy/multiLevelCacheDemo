package com.heima.item.canal;


import com.github.benmanes.caffeine.cache.Cache;
import com.heima.item.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;
import com.heima.item.config.redisHandler;


@CanalTable("tb_item")
@Component
public class ItemHandler implements EntryHandler<Item> {

    @Autowired
    private redisHandler redisHandler;
    @Autowired
    private Cache<Long,Item> itemCache;

    @Override
    public void insert(Item item) {
        //修改本地缓存中的数据
        itemCache.put(item.getId(), item);
        //修改redis当中的数据
        redisHandler.saveItem(item);
    }

    @Override
    public void update(Item before, Item after) {
        //修改本地缓存中的数据
        itemCache.put(after.getId(), after);
        //修改redis当中的数据
        redisHandler.saveItem(after);
    }

    @Override
    public void delete(Item item) {
        //删除本地缓存中的数据
        itemCache.invalidate(item.getId());
        //删除redis当中的数据
        redisHandler.deleteItemById(item.getId());
    }
}

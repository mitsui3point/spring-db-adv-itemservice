package hello.itemservice.domain.exception;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;

import java.util.List;

public interface ItemExceptionRepository {
    List<Item> findAll(ItemSearchCond cond);
}

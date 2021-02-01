package net.chrisrichardson.eventstore.examples.customersandorders.ordershistoryviewservice.backend;

import net.chrisrichardson.eventstore.examples.customersandorders.common.domain.Money;
import net.chrisrichardson.eventstore.examples.customersandorders.common.order.OrderState;
import net.chrisrichardson.eventstore.examples.customersandorders.ordershistorycommon.OrderView;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Singleton
public class OrderViewRepository {

  @Inject
  MongoTemplate mongoTemplate;

  public OrderView findOne(String orderId) {
    return mongoTemplate.findOne(new Query(where("id").is(orderId)), OrderView.class);
  }

  public void addOrder(String orderId, Money orderTotal) {
    mongoTemplate.upsert(new Query(where("id").is(orderId)),
            new Update().set("state", OrderState.CREATED).set("orderTotal", orderTotal), OrderView.class);
  }

  public void updateOrderState(String orderId, OrderState state) {
    mongoTemplate.updateFirst(new Query(where("id").is(orderId)),
            new Update().set("state", state), OrderView.class);
  }
}

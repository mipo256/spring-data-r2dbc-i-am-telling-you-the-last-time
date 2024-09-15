package io.mpolivakha.r2dbc.relations;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("orders")
public class Order {

  @Id
  @EqualsAndHashCode.Include
  private Long id;

  private String status;

  @MappedCollection(keyColumn = "order_id", idColumn = "order_id")
  private List<OrderItem> orderItems;
}
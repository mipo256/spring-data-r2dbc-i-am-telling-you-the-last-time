package io.mpolivakha.r2dbc.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("order_item")
public class OrderItem {

  @Id
  @EqualsAndHashCode.Include
  private Long id;

  private String name;

  private double price;
}

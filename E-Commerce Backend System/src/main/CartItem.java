@Entity
public class CartItem {
  @Id @GeneratedValue
  private Long id;

  @ManyToOne
  private Product product;

  private int quantity;

  @ManyToOne
  private Cart cart;
}

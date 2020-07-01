package victor.training.java8.stream.order;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import org.mockito.internal.matchers.Or;
import victor.training.java8.stream.order.dto.OrderDto;
import victor.training.java8.stream.order.entity.Customer;
import victor.training.java8.stream.order.entity.Order;
import victor.training.java8.stream.order.entity.OrderLine;
import victor.training.java8.stream.order.entity.Product;
import victor.training.java8.stream.order.entity.Order.PaymentMethod;

public class TransformStreams {

	public void methodReferencePlay() {

		// f:N->R, f(x) = sqrt(x)  domeniu = Naturale, Codomeniu = Reale

		int i = Integer.parseInt("1"); // dom=String , codom=int : vom nota -  f(String):int
		Function<String, Integer> f1 = Integer::parseInt;

		LocalDateTime dateTime = LocalDateTime.parse("2020-10-01");// f(String): LocalDateTime
		Function<String, LocalDateTime> parseF = LocalDateTime::parse;

		int aaa = Objects.hashCode("aaa");
		Function<Object, Integer> f2 = Objects::hashCode;

		//// pana aici, simplu: referirea fct statice
		// mai jos: referirea functiilor de instanta (ne stattice ) dintr-o instanta existenta

		Order order1 = new Order();

		Long id = order1.getId(); // order.getId(): Long
//		Supplier<Long> f3 = new Supplier<Long>() {
//			@Override
//			public Long get() {
//				return order.getId();
//			}
//		};
		Supplier<Long> f3 = order1::getId; // f(): Long
		BigDecimal bd = BigDecimal.ONE;
		BigDecimal two = bd.add(bd); // bd.add(BD): BD
		Function<BigDecimal, BigDecimal> f4 = bd::add;

		int i1 = bd.compareTo(two);
		Function<BigDecimal, Integer> f5 =  bd::compareTo; //f(BD):Integer

		OrderDto orderDto = this.toDto(order1);
		Function<Order, OrderDto> f6 = this::toDto; // f(Order):OrderDto

		order1.setPaymentMethod(PaymentMethod.CARD);
		Consumer<PaymentMethod> ft = order1::setPaymentMethod; // f(PaymentMethod):void

		// un pic mai greu pana aici. mai jos, horror

		// referirea metodelor de instanta dar fara sa ai vreo instanta in mana.

		Function<Order, Long> f7 = Order::getId; // f(Order):Long
		Long apply = f7.apply(order1); // de ex, cum o chemi


		BiFunction<TransformStreams, Order, OrderDto> f8_DoamneMaIarta = TransformStreams::toDto; // f(TransformStreams ("this"), Order):OrderDto
		OrderDto dto = f8_DoamneMaIarta.apply(this, order1);

		BiFunction<BigDecimal, BigDecimal, Integer> f9_haiCaMerge = BigDecimal::compareTo; // f(BigDecimal (e "this"-ul), BigDecimal ("param")):int

		BiConsumer<Order, PaymentMethod> f10 = Order::setPaymentMethod; // f(Order, PaymentMethod):void
		/// ----------- SFARSIT

		Date date = new Date(); // "new" in cazul asta ce fel de fct este:   new():Date
		Supplier<Date> supDate = Date::new; // soc si groaza prima data cand vezi

		Date date1 = new Date(122132); // "new" in cazul asta ce fel de fct este:   new(Long):Date -->
		Function<Long, Date> overloadCuFourDots = Date::new;
		LongFunction<Date> overloadCuFourDotsLong = Date::new; // refera aceelasi constructor.
		// plangi caci cele doua arata la fel, chiar daca indica spre overloaduri diferite ale constructori


	}


	/**
	 * Transform all entities to DTOs.
	 * Discussion:.. Make it cleanest!
	 */
	public List<OrderDto> p01_toDtos(List<Order> orders) {

		return orders.stream()
//			.map(o -> toDto(o))
//			.map(f6)
			.map(this::toDto)
			.collect(toList()); // o operatie pe stream pana in collect merge lasata oneliner

	}

	private OrderDto toDto(Order order) {
		OrderDto dto = new OrderDto();
		dto.totalPrice = order.getTotalPrice();
		dto.creationDate = order.getCreationDate();
		return dto;
	}

	/**
	 * Note: Order.getPaymentMethod()
	 */
	public Set<PaymentMethod> p02_getUsedPaymentMethods(Customer customer) {
		return null; 
	}
	
	/**
	 * When did the customer created orders ?
	 * Note: Order.getCreationDate()
	 */
	public SortedSet<LocalDate> p03_getOrderDatesAscending(Customer customer) {
		return null; 
	}
	
	
	/**
	 * @return a map order.id -> order
	 */
	public Map<Long, Order> p04_mapOrdersById(Customer customer) {
		return null; 
	}
	
	/** 
	 * Orders grouped by Order.paymentMethod
	 */
	public Map<PaymentMethod, List<Order>> p05_getProductsByPaymentMethod(Customer customer) {
		return null; 
	}
	
	// -------------- MOVIE BREAK :p --------------------
	
	/** 
	 * A hard one !
	 * Get total number of products bought by a customer, across all her orders.
	 * Customer --->* Order --->* OrderLines(.count .product)
	 * The sum of all counts for the same product.
	 * i.e. SELECT PROD_ID, SUM(COUNT) FROM PROD GROUPING BY PROD_ID
	 */
	public Map<Product, Long> p06_getProductCount(Customer customer) {
		
		List<OrderLine> allLines = new ArrayList<>();
		
		for (Order order : customer.getOrders()) {
			allLines.addAll(order.getOrderLines());
		}
		return null; 
		
	}
	
	/**
	 * All the unique products bought by the customer, 
	 * sorted by Product.name.
	 */
	public List<Product> p07_getAllOrderedProducts(Customer customer) {
		return null; 
	}
	
	
	/**
	 * The names of all the products bought by Customer,
	 * sorted and then concatenated by ",".
	 * Example: "Armchair,Chair,Table".
	 * Hint: Reuse the previous function.
	 */
	public String p08_getProductsJoined(Customer customer) {
		return null; 
	}
	
	/**
	 * Sum of all Order.getTotalPrice(), truncated to Long.
	 */
	public Long p09_getApproximateTotalOrdersPrice(Customer customer) {
		return null; 
	}
	
	// ----------- IO ---------------
	
	/**
	 * - Read lines from file as Strings. 
	 * - Where do you close the opened file?
	 * - Parse those to OrderLine using the function bellow
	 * - Validate the created OrderLine. Throw ? :S
	 */
	public List<OrderLine> p10_readOrderFromFile(File file) throws IOException {
		
		Stream<String> lines = null; // ??
		//return lines
		//.map(line -> line.split(";")) // Stream<String[]>
		//.filter(cell -> "LINE".equals(cell[0]))
		//.map(this::parseOrderLine) // Stream<OrderLine>
		//.peek(this::validateOrderLine)
		//.collect(toList());

		// TODO check the number of lines is >= 2

		return null;
		
	}
	
	private OrderLine parseOrderLine(String[] cells) {
		return new OrderLine(new Product(cells[1]), Integer.parseInt(cells[2]));
	}
	
	private void validateOrderLine(OrderLine orderLine) {
		if (orderLine.getCount() < 0) {
			throw new IllegalArgumentException("Negative items");			
		}
	}
	
	
	// TODO print cannonical paths of all files in current directory
	// use Unchecked... stuff
}
